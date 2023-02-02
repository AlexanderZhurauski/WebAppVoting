package dao.database;

import dao.api.IEmailSendingDAO;
import dao.factories.ConnectionSingleton;
import dao.entity.EmailEntity;
import dao.entity.EmailStatus;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmailSendingDBDAO implements IEmailSendingDAO {

    private static final String ADD = "INSERT INTO app.emails(" +
            "recipient, topic, text_message, departures, status)" +
            "VALUES (?, ?, ?, ?, ?);";
    private static final String GET_UNSENT = "SELECT id, recipient, topic, " +
            "text_message, departures, status FROM app.emails " +
            "WHERE departures>0 AND status=? " +
            "ORDER BY id LIMIT ?;";
    private static final String UPDATE = "UPDATE app.emails " +
            "SET recipient=?, topic=?, text_message=?, departures=?, status=? " +
            "WHERE id=?;";
    private static final int NUMBER_EMAILS_TO_SEND = 10;

    @Override
    public void add(EmailEntity email) {
        try (Connection conn = ConnectionSingleton.getInstance().open();
             PreparedStatement stmt = conn.prepareStatement(ADD)) {
            stmt.setString(1, email.getRecipient());
            stmt.setString(2, email.getTopic());
            stmt.setString(3, email.getTextMessage());
            stmt.setInt(4, email.getDepartures());
            stmt.setString(5, email.getStatus().toString());
            stmt.execute();
        } catch (SQLException e) {
            throw new RuntimeException("database query error");
        }
    }

    @Override
    public List<EmailEntity> getUnsent() {
        try (Connection conn = ConnectionSingleton.getInstance().open();
             PreparedStatement stmt = conn.prepareStatement(GET_UNSENT,
                     ResultSet.TYPE_SCROLL_SENSITIVE,
                     ResultSet.CONCUR_UPDATABLE)) {
            stmt.setString(1, EmailStatus.WAITING.toString());
            stmt.setInt(2, NUMBER_EMAILS_TO_SEND);
            List<EmailEntity> emails = new ArrayList<>();
            try (ResultSet resultSet = stmt.executeQuery()) {
                while (resultSet.next()) {
                    emails.add(get(resultSet));
                }
            }
            return emails;
        } catch (SQLException e) {
            throw new RuntimeException("database query error");
        }
    }

    @Override
    public void update(EmailEntity email) {
        try (Connection conn = ConnectionSingleton.getInstance().open();
             PreparedStatement stmt = conn.prepareStatement(UPDATE)) {
            stmt.setString(1, email.getRecipient());
            stmt.setString(2, email.getTopic());
            stmt.setString(3, email.getTextMessage());
            stmt.setInt(4, email.getDepartures());
            stmt.setString(5, email.getStatus().toString());
            stmt.setInt(6, email.getId());
            stmt.execute();
        } catch (SQLException e) {
            throw new RuntimeException("database query error");
        }
    }

    private EmailEntity get(ResultSet email) throws SQLException {
        return new EmailEntity(
                email.getInt("id"),
                email.getString("recipient"),
                email.getString("topic"),
                email.getString("text_message"),
                email.getInt("departures"),
                EmailStatus.getEmailStatus(email.getString("status")
        ));
    }
}
package dao.memory;

import dao.api.IEmailSendingDAO;
import dao.entity.EmailEntity;

import java.util.List;

public class EmailSendingMemoryDAO implements IEmailSendingDAO {


    @Override
    public void add(EmailEntity email) {

    }

    @Override
    public List<EmailEntity> getUnsent() {
        return null;
    }

    @Override
    public void update(EmailEntity email) {

    }
}
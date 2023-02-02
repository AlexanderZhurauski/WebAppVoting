package dao.api;

import dao.entity.EmailEntity;

import java.util.List;

public interface IEmailSendingDAO {

    void add(EmailEntity email);

    List<EmailEntity> getUnsent();

    void update(EmailEntity email);
}
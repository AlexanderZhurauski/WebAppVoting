package dao.database;

import dao.api.IConnection;
import dao.api.IEmailDAO;
import dao.entity.EmailEntity;
import dao.entity.EmailStatus;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class EmailDBDAO implements IEmailDAO {

    private static final int NUMBER_EMAILS_TO_SEND = 10;
    private final IConnection connectionManager;

    public EmailDBDAO(IConnection connectionManager) {
        this.connectionManager = connectionManager;
    }

    @Override
    public void add(EmailEntity email) {
        EntityManager entityManager = connectionManager.open();
        entityManager.getTransaction().begin();

        entityManager.merge(email);

        entityManager.getTransaction().commit();
        entityManager.close();
    }

    @Override
    public List<EmailEntity> getUnsent() {
        List<EmailEntity> emails;
        EntityManager entityManager = connectionManager.open();
        entityManager.getTransaction().begin();

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<EmailEntity> query = criteriaBuilder.createQuery(EmailEntity.class);
        Root<EmailEntity> root = query.from(EmailEntity.class);
        CriteriaQuery<EmailEntity> unsentEmails = query
                .select(root)
                .where(criteriaBuilder.and(
                        criteriaBuilder.gt(root.get("departures"), 0),
                        criteriaBuilder.equal(root.get("status"), EmailStatus.WAITING)
                ))
                .orderBy(criteriaBuilder.asc(root.get("id")));
        TypedQuery<EmailEntity> unsentEmailsQuery = entityManager.createQuery(unsentEmails);
        emails = unsentEmailsQuery
                .setFirstResult(0)
                .setMaxResults(NUMBER_EMAILS_TO_SEND)
                .getResultList();

        entityManager.getTransaction().commit();
        entityManager.close();
        return emails;
    }

    @Override
    public void update(EmailEntity email) {
        EntityManager entityManager = connectionManager.open();
        entityManager.getTransaction().begin();

        EmailEntity emailEntity = entityManager.find(EmailEntity.class, email.getId());
        emailEntity.setVote(email.getVote());
        emailEntity.setRecipient(email.getRecipient());
        emailEntity.setTopic(email.getTopic());
        emailEntity.setTextMessage(email.getTextMessage());
        emailEntity.setDepartures(email.getDepartures());
        emailEntity.setStatus(email.getStatus());

        entityManager.getTransaction().commit();
        entityManager.close();
    }
}

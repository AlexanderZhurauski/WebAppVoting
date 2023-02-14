package dao.database;

import dao.api.IArtistDAO;
import dao.entity.ArtistEntity;
import dao.util.ConnectionManager;

import javax.persistence.EntityManager;
import java.util.List;

public class ArtistDBDAO implements IArtistDAO {

    private final ConnectionManager connectionManager;

    public ArtistDBDAO(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    @Override
    public List<ArtistEntity> getAll() {
        EntityManager entityManager = null;
        List<ArtistEntity> list;

        try {
            entityManager = connectionManager.open();
            entityManager.getTransaction().begin();
            entityManager.createNativeQuery("SET TRANSACTION READ ONLY;").executeUpdate();

            list = entityManager.createQuery("SELECT a FROM ArtistEntity a", ArtistEntity.class)
                    .getResultList();

            entityManager.getTransaction().commit();
        } catch (Exception e) {
            if (entityManager != null && entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw e;
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }

        return list;
    }

    @Override
    public boolean exists(Long id) {
        EntityManager entityManager = null;
        boolean bool = false;

        try {
            entityManager = connectionManager.open();
            entityManager.getTransaction().begin();
            entityManager.createNativeQuery("SET TRANSACTION READ ONLY;").executeUpdate();

            if (entityManager.find(ArtistEntity.class, id) != null) {
                bool = true;
            }

            entityManager.getTransaction().commit();
        } catch (Exception e) {
            if (entityManager != null && entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw e;
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
        return bool;
    }

    @Override
    public ArtistEntity get(Long id) {
        EntityManager entityManager = null;
        ArtistEntity artist;
        try {
            entityManager = connectionManager.open();
            entityManager.getTransaction().begin();
            entityManager.createNativeQuery("SET TRANSACTION READ ONLY;").executeUpdate();

            artist = entityManager.find(ArtistEntity.class, id);

            entityManager.getTransaction().commit();

        } catch (Exception e) {
            if (entityManager != null && entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw e;
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }

        return artist;
    }

    @Override
    public void add(ArtistEntity artist) {
        EntityManager entityManager = null;
        try {
            entityManager = connectionManager.open();
            entityManager.getTransaction().begin();

            entityManager.persist(artist);

            entityManager.getTransaction().commit();
        } catch (Exception e) {
            if (entityManager != null && entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw e;
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
    }

    @Override
    public void update(ArtistEntity artist) {
        EntityManager entityManager = null;
        try {
            entityManager = connectionManager.open();
            entityManager.getTransaction().begin();

            entityManager.merge(artist);

            entityManager.getTransaction().commit();
        } catch (Exception e) {
            if (entityManager != null && entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw e;
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
    }

    @Override
    public void delete(Long id) {
        EntityManager entityManager = null;

        try {
            entityManager = connectionManager.open();
            entityManager.getTransaction().begin();

            ArtistEntity artistEntity = entityManager.find(ArtistEntity.class, id);
            entityManager.remove(artistEntity);

            entityManager.getTransaction().commit();
        } catch (Exception e) {
            if (entityManager != null && entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw e;
        } finally {
            if (entityManager != null) {
                entityManager.close();
            }
        }
    }
}
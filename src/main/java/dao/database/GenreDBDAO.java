package dao.database;

import dao.api.IGenreDAO;
import dao.entity.GenreEntity;
import dao.util.ConnectionManager;

import javax.persistence.EntityManager;
import java.util.List;

public class GenreDBDAO implements IGenreDAO {

    private final ConnectionManager connectionManager;

    public GenreDBDAO(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }
    @Override
    public List<GenreEntity> getAll() {
        EntityManager entityManager = null;
        List<GenreEntity> list;
        try {
            entityManager = connectionManager.open();
            entityManager.getTransaction().begin();

            entityManager.createNativeQuery("SET TRANSACTION READ ONLY;").executeUpdate();
            list = entityManager.createQuery("SELECT g FROM GenreEntity g", GenreEntity.class)
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
        boolean bool = false;
        EntityManager entityManager = null;
        try {
            entityManager = connectionManager.open();
            entityManager.getTransaction().begin();
            entityManager.createNativeQuery("SET TRANSACTION READ ONLY;").executeUpdate();
            if (entityManager.find(GenreEntity.class, id) != null) {
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
    public GenreEntity get(Long id) {
        EntityManager entityManager = null;
        GenreEntity genre;
        try {
            entityManager = connectionManager.open();
            entityManager.getTransaction().begin();
            entityManager.createNativeQuery("SET TRANSACTION READ ONLY;").executeUpdate();

            genre = entityManager.find(GenreEntity.class, id);

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

        return genre;
    }

    @Override
    public void add(GenreEntity genre) {
        EntityManager entityManager = null;
        try {
            entityManager = connectionManager.open();
            entityManager.getTransaction().begin();

            entityManager.persist(genre);

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
    public void update(GenreEntity genre) {
        EntityManager entityManager = null;
        try {
            entityManager = connectionManager.open();
            entityManager.getTransaction().begin();

            entityManager.merge(genre);

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

            GenreEntity genreEntity = entityManager.find(GenreEntity.class, id);
            entityManager.remove(genreEntity);

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
package dao.database;

import dao.api.IGenreDAO;
import dao.entity.GenreEntity;
import dao.factories.ConnectionSingleton;
import dto.GenreDTO;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.stream.Collectors;

public class GenreDBDAO implements IGenreDAO {
    @Override
    public List<GenreDTO> getAll() {
        EntityManager entityManager = null;
        List<GenreDTO> list;
        try {
            entityManager = ConnectionSingleton.getInstance().open();
            entityManager.getTransaction().begin();
            entityManager.createNativeQuery("SET TRANSACTION READ ONLY;").executeUpdate();
            list = entityManager.createQuery("SELECT g FROM GenreEntity g", GenreEntity.class)
                    .getResultStream()
                    .map(GenreDTO::new)
                    .collect(Collectors.toList());
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
    public boolean exists(long id) {
        boolean bool = false;
        EntityManager entityManager = null;
        try {
            entityManager = ConnectionSingleton.getInstance().open();
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
    public GenreDTO get(long id) {
        EntityManager entityManager = null;
        GenreDTO genreDTO;
        try {
            entityManager = ConnectionSingleton.getInstance().open();
            entityManager.getTransaction().begin();
            entityManager.createNativeQuery("SET TRANSACTION READ ONLY;").executeUpdate();

            genreDTO = new GenreDTO(entityManager.find(GenreEntity.class, id));
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

        return genreDTO;
    }

    @Override
    public void add(String genre) {
        EntityManager entityManager = null;
        try {
            entityManager = ConnectionSingleton.getInstance().open();
            entityManager.getTransaction().begin();
            entityManager.persist(new GenreEntity(genre));
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
    public void update(long id, String genre) {
        EntityManager entityManager = null;
        try {
            entityManager = ConnectionSingleton.getInstance().open();
            entityManager.getTransaction().begin();
            GenreEntity genreEntity = entityManager.find(GenreEntity.class, id);
            genreEntity.setGenre(genre);
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
    public void delete(long id) {
        EntityManager entityManager = null;
        try {
            entityManager = ConnectionSingleton.getInstance().open();
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
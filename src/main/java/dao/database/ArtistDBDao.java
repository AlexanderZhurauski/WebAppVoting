package dao.database;

import dao.api.IArtistDAO;
import dao.entity.ArtistEntity;
import dao.factories.ConnectionSingleton;
import dto.ArtistDTO;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.stream.Collectors;

public class ArtistDBDao implements IArtistDAO {

    @Override
    public List<ArtistDTO> getAll() {
        EntityManager entityManager = null;
        List<ArtistDTO> list;

        try {
            entityManager = ConnectionSingleton.getInstance().open();
            entityManager.getTransaction().begin();
            entityManager.createNativeQuery("SET TRANSACTION READ ONLY;").executeUpdate();

            list = entityManager.createQuery("SELECT a FROM ArtistEntity a", ArtistEntity.class)
                    .getResultStream()
                    .map(ArtistDTO::new)
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
        EntityManager entityManager = null;
        boolean bool = false;

        try {
            entityManager = ConnectionSingleton.getInstance().open();
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
    public ArtistDTO get(long id) {
        EntityManager entityManager = null;
        ArtistDTO artistDTO;
        try {
            entityManager = ConnectionSingleton.getInstance().open();
            entityManager.getTransaction().begin();
            entityManager.createNativeQuery("SET TRANSACTION READ ONLY;").executeUpdate();

            ArtistEntity artistEntity = entityManager.find(ArtistEntity.class, id);
            artistDTO = new ArtistDTO(artistEntity);

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

        return artistDTO;
    }

    @Override
    public void add(String artist) {
        EntityManager entityManager = null;
        try {
            entityManager = ConnectionSingleton.getInstance().open();
            entityManager.getTransaction().begin();

            entityManager.persist(new ArtistEntity(artist));

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
    public void update(long id, String artist) {
        EntityManager entityManager = null;
        try {
            entityManager = ConnectionSingleton.getInstance().open();
            entityManager.getTransaction().begin();

            ArtistEntity artistEntity = entityManager.find(ArtistEntity.class, id);
            artistEntity.setArtist(artist);

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
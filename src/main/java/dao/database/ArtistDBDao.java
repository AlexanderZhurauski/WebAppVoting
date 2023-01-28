package dao.database;

import dao.api.IArtistDAO;
import dao.entity.ArtistEntity;
import dao.factories.ConnectionSingleton;
import dto.ArtistDTO;

import javax.persistence.EntityManager;
import javax.persistence.RollbackException;
import java.util.List;

public class ArtistDBDao implements IArtistDAO {

    @Override
    public List<ArtistDTO> getAll() {
        List<ArtistDTO> list;
        EntityManager entityManager = ConnectionSingleton.getInstance().open();
        entityManager.getTransaction().begin();
        list = entityManager.createQuery("from ArtistEntity ", ArtistEntity.class)
                .getResultStream()
                .map(ArtistDTO::new)
                .toList();
        entityManager.getTransaction().commit();
        return list;
    }

    @Override
    public boolean exists(int id) {
        Long l = (long) id;
        boolean bool = false;
        EntityManager entityManager = ConnectionSingleton.getInstance().open();
        entityManager.getTransaction().begin();
        if (entityManager.find(ArtistEntity.class, l) != null) {
            bool = true;
        }
        entityManager.getTransaction().commit();
        return bool;
    }

    @Override
    public ArtistDTO get(int id) {
        Long l = (long) id;
        EntityManager entityManager = ConnectionSingleton.getInstance().open();
        entityManager.getTransaction().begin();
        ArtistDTO artistDTO = entityManager.find(ArtistDTO.class, l);
        entityManager.getTransaction().commit();
        return artistDTO;
    }

    @Override
    public void add(String artist) {
        EntityManager entityManager = ConnectionSingleton.getInstance().open();
        entityManager.getTransaction().begin();
        entityManager.persist(new ArtistEntity(artist));
        entityManager.getTransaction().commit();
    }

    @Override
    public void update(int id, String artist) {
        Long l = (long) id;
        EntityManager entityManager = ConnectionSingleton.getInstance().open();
        entityManager.getTransaction().begin();
        ArtistEntity artistEntity = entityManager.find(ArtistEntity.class, l);
        artistEntity.setArtist(artist);
        entityManager.getTransaction().commit();
    }

    @Override
    public void delete(int id) {
        Long l = (long) id;
        EntityManager entityManager = ConnectionSingleton.getInstance().open();
        entityManager.getTransaction().begin();
        ArtistEntity artistEntity = entityManager.find(ArtistEntity.class, l);
        entityManager.remove(artistEntity);
        entityManager.getTransaction().commit();

    }
}
package dao.database;

import dao.api.IArtistDAO;
import dao.entity.ArtistEntity;
import dao.factories.ConnectionSingleton;
import dto.ArtistDTO;

import javax.persistence.EntityManager;
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
    public boolean exists(long id) {
        boolean bool = false;
        EntityManager entityManager = ConnectionSingleton.getInstance().open();
        entityManager.getTransaction().begin();
        if (entityManager.find(ArtistEntity.class, id) != null) {
            bool = true;
        }
        entityManager.getTransaction().commit();
        return bool;
    }

    @Override
    public ArtistDTO get(long id) {
        EntityManager entityManager = ConnectionSingleton.getInstance().open();
        entityManager.getTransaction().begin();
        ArtistDTO artistDTO = entityManager.find(ArtistDTO.class, id);
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
    public void update(long id, String artist) {
        EntityManager entityManager = ConnectionSingleton.getInstance().open();
        entityManager.getTransaction().begin();
        ArtistEntity artistEntity = entityManager.find(ArtistEntity.class, id);
        artistEntity.setArtist(artist);
        entityManager.getTransaction().commit();
    }

    @Override
    public void delete(long id) {
        EntityManager entityManager = ConnectionSingleton.getInstance().open();
        entityManager.getTransaction().begin();
        ArtistEntity artistEntity = entityManager.find(ArtistEntity.class, id);
        entityManager.remove(artistEntity);
        entityManager.getTransaction().commit();

    }
}
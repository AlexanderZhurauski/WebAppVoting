package dao.database;

import dao.api.IGenreDAO;
import dao.entity.ArtistEntity;
import dao.entity.GenreEntity;
import dao.factories.ConnectionSingleton;
import dto.ArtistDTO;
import dto.GenreDTO;

import javax.persistence.EntityManager;
import java.util.List;

public class GenreDBDAO implements IGenreDAO {
    @Override
    public List<GenreDTO> getAll() {
        List<GenreDTO> list;
        EntityManager entityManager = ConnectionSingleton.getInstance().open();
        entityManager.getTransaction().begin();
        list = entityManager.createQuery("from GenreEntity ", GenreEntity.class)
                .getResultStream()
                .map(GenreDTO::new)
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
        if (entityManager.find(GenreEntity.class, l) != null) {
            bool = true;
        }
        entityManager.getTransaction().commit();
        return bool;
    }

    @Override
    public GenreDTO get(int id) {
        Long l = (long) id;
        EntityManager entityManager = ConnectionSingleton.getInstance().open();
        entityManager.getTransaction().begin();
        GenreDTO genreDTO = new GenreDTO(entityManager.find(GenreEntity.class, l));
        entityManager.getTransaction().commit();
        return genreDTO;
    }

    @Override
    public void add(String genre) {
        EntityManager entityManager = ConnectionSingleton.getInstance().open();
        entityManager.getTransaction().begin();
        entityManager.persist(new GenreEntity(genre));
        entityManager.getTransaction().commit();
    }

    @Override
    public void update(int id, String genre) {
        Long l = (long) id;
        EntityManager entityManager = ConnectionSingleton.getInstance().open();
        entityManager.getTransaction().begin();
        GenreEntity genreEntity = entityManager.find(GenreEntity.class, l);
        genreEntity.setGenre(genre);
        entityManager.getTransaction().commit();
    }

    @Override
    public void delete(int id) {
        Long l = (long) id;
        EntityManager entityManager = ConnectionSingleton.getInstance().open();
        entityManager.getTransaction().begin();
        GenreEntity genreEntity = entityManager.find(GenreEntity.class, l);
        entityManager.remove(genreEntity);
        entityManager.getTransaction().commit();
    }
}
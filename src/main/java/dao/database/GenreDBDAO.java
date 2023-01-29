package dao.database;

import dao.api.IGenreDAO;
import dao.entity.GenreEntity;
import dao.factories.ConnectionSingleton;
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
    public boolean exists(long id) {
        boolean bool = false;
        EntityManager entityManager = ConnectionSingleton.getInstance().open();
        entityManager.getTransaction().begin();
        if (entityManager.find(GenreEntity.class, id) != null) {
            bool = true;
        }
        entityManager.getTransaction().commit();
        return bool;
    }

    @Override
    public GenreDTO get(long id) {
        EntityManager entityManager = ConnectionSingleton.getInstance().open();
        entityManager.getTransaction().begin();
        GenreDTO genreDTO = new GenreDTO(entityManager.find(GenreEntity.class, id));
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
    public void update(long id, String genre) {
        EntityManager entityManager = ConnectionSingleton.getInstance().open();
        entityManager.getTransaction().begin();
        GenreEntity genreEntity = entityManager.find(GenreEntity.class, id);
        genreEntity.setGenre(genre);
        entityManager.getTransaction().commit();
    }

    @Override
    public void delete(long id) {
        EntityManager entityManager = ConnectionSingleton.getInstance().open();
        entityManager.getTransaction().begin();
        GenreEntity genreEntity = entityManager.find(GenreEntity.class, id);
        entityManager.remove(genreEntity);
        entityManager.getTransaction().commit();
    }
}
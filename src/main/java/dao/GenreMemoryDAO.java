package dao;

import dao.api.IGenreDAO;
import dto.ArtistDTO;
import dto.GenreDTO;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class GenreMemoryDAO implements IGenreDAO {

    private final Map<Integer, GenreDTO> genres;
    private final ReadWriteLock lock;
    private final Lock writeLock;
    private final Lock readLock;

    public GenreMemoryDAO() {
        genres = new ConcurrentHashMap<>();
        lock = new ReentrantReadWriteLock();
        writeLock = lock.writeLock();
        readLock = lock.readLock();
        genres.put(1, new GenreDTO(1, "Pop"));
        genres.put(2, new GenreDTO(2, "Rap"));
        genres.put(3, new GenreDTO(3, "Techno"));
        genres.put(4, new GenreDTO(4, "Dubstep"));
        genres.put(5, new GenreDTO(5, "Jazz"));
        genres.put(6, new GenreDTO(6, "Classic Rock"));
        genres.put(7, new GenreDTO(7, "Country"));
        genres.put(8, new GenreDTO(8, "Hard Rock"));
        genres.put(9, new GenreDTO(9, "Blues"));
        genres.put(10, new GenreDTO(10, "Hip Hop"));
    }

    @Override
    public List<GenreDTO> getAll() {
        try {
            readLock.lock();

            return List.copyOf(genres.values());
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public boolean exists(int id) {
        try {
            readLock.lock();

            return genres.containsKey(id);
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public GenreDTO get(int id) {
        try {
            readLock.lock();

            GenreDTO genre = genres.get(id);
            if (genre != null) {
                return genre;
            } else {
                throw new IllegalArgumentException("No genre returned for id "
                        + id);
            }
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public void add(String genre) {
        try {
            writeLock.lock();

            int newId = getNewID();
            genres.put(newId, new GenreDTO(newId, genre));
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public void update(int id, String genre) {
        try {
            writeLock.lock();

            genres.put(id, new GenreDTO(id, genre));
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public void delete(int id) {
        try {
            writeLock.lock();

            genres.remove(id);
        } finally {
            writeLock.unlock();
        }
    }

    private int getNewID() {
        int newID = genres.keySet()
                .stream()
                .max(Integer::compareTo)
                .get() + 1;

        return newID;
    }
}
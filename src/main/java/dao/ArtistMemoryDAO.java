package dao;

import dao.api.IArtistDAO;
import dto.ArtistDTO;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ArtistMemoryDAO implements IArtistDAO {
    private final Map<Integer, ArtistDTO> artists;
    private final ReadWriteLock lock;
    private final Lock writeLock;
    private final Lock readLock;

    public ArtistMemoryDAO() {
        artists = new ConcurrentHashMap<>();
        lock = new ReentrantReadWriteLock();
        writeLock = lock.writeLock();
        readLock = lock.readLock();
        artists.put(1, new ArtistDTO(1, "Taylor Swift"));
        artists.put(2, new ArtistDTO(2, "Prince"));
        artists.put(3, new ArtistDTO(3, "Elvis Presley"));
        artists.put(4, new ArtistDTO(4, "Eminem"));
    }

    @Override
    public List<ArtistDTO> getAll() {
        try {
            readLock.lock();

            return List.copyOf(artists.values());
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public boolean exists(int id) {
        try {
            readLock.lock();

            return artists.containsKey(id);
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public ArtistDTO get(int id) {
        try {
            readLock.lock();

            ArtistDTO artist = artists.get(id);
            if (artist != null) {
                return artist;
            } else {
                throw new IllegalArgumentException("No artist returned for id "
                        + id);
            }
        } finally {
            readLock.unlock();
        }
    }

    @Override
    public void add(String artist) {
        try {
            writeLock.lock();

            int newId = getNewID();
            artists.put(newId, new ArtistDTO(newId, artist));
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public void update(int id, String artist) {
        try {
            writeLock.lock();

            artists.put(id, new ArtistDTO(id, artist));
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public void delete(int id) {
        try {
            writeLock.lock();

            artists.remove(id);
        } finally {
            writeLock.unlock();
        }
    }

    private int getNewID() {
        int newID = artists.keySet()
                .stream()
                .max(Integer::compareTo)
                .get() + 1;

        return newID;
    }
}
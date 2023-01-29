package dao.api;

import dto.ArtistDTO;

import java.util.List;

public interface IArtistDAO {

    List<ArtistDTO> getAll();

    boolean exists(long id);

    ArtistDTO get(long id);

    void add(String artist);

    void update(long id, String artist);

    void delete(long id);
}
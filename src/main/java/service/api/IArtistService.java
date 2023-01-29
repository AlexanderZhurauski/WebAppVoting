package service.api;

import dto.ArtistDTO;

import java.util.List;

public interface IArtistService {

    List<ArtistDTO> getAll();

    boolean exists(long id);

    ArtistDTO get(long id);

    void add(String artist);

    void update(long id, String artist);

    void delete(long id);
}
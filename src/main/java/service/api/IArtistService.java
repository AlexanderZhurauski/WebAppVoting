package service.api;

import dto.ArtistDTO;
import dto.ArtistInputDTO;
import dto.GenreInputDTO;

import java.util.List;

public interface IArtistService {

    List<ArtistDTO> getAll();

    boolean exists(Long id);

    ArtistDTO get(Long id);

    void add(ArtistInputDTO artist);

    void update(Long id, Long version, ArtistInputDTO artist);

    void delete(Long id);
}
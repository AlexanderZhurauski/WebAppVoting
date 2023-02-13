package dao.api;

import dto.ArtistDTO;
import dto.ArtistInputDTO;

import java.util.List;

public interface IArtistDAO {

    List<ArtistDTO> getAll();

    boolean exists(Long id);

    ArtistDTO get(Long id);

    void add(ArtistInputDTO artist);

    void update(Long id, ArtistInputDTO artist);

    void delete(Long id);
}
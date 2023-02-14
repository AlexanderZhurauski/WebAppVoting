package dao.api;

import dao.entity.ArtistEntity;
import dto.ArtistInputDTO;

import java.util.List;

public interface IArtistDAO {

    List<ArtistEntity> getAll();

    boolean exists(Long id);

    ArtistEntity get(Long id);

    void add(ArtistEntity artist);

    void update(ArtistEntity artist);

    void delete(Long id);
}
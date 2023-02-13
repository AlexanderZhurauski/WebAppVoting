package service;

import dao.api.IArtistDAO;
import dto.ArtistDTO;
import dto.ArtistInputDTO;
import service.api.IArtistService;

import java.util.List;
import java.util.Objects;

public class ArtistService implements IArtistService {

    private final IArtistDAO dataSource;

    public ArtistService(IArtistDAO dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<ArtistDTO> getAll() {
        return dataSource.getAll();
    }

    @Override
    public boolean exists(Long id) {
        return dataSource.exists(id);
    }

    @Override
    public ArtistDTO get(Long id) {
        return dataSource.get(id);
    }

    @Override
    public void add(ArtistInputDTO artist) {
        String artistName = artist.getName();
        if (artistName == null || artistName.isBlank()) {
            throw new IllegalArgumentException("No artist name has been provided!");
        }
        dataSource.add(artist);
    }

    @Override
    public void update(Long id, Long version, ArtistInputDTO artist) {
        ArtistDTO artistDTO = dataSource.get(id);
        if (artistDTO == null) {
            throw new IllegalArgumentException("No artist updated for id " + id
                    + " as it doesn't exist.");
        }
        if (!Objects.equals(artistDTO.getVersion(), version)) {
            throw new IllegalArgumentException("No artist updated for id " + id
                    + ". Invalid version provided.");
        }
        String artistName = artistDTO.getName();
        if (artistName == null || artistName.isBlank()) {
            throw new IllegalArgumentException("No artist name has been provided!");
        }
        dataSource.update(id, artist);
    }

    @Override
    public void delete(Long id) {
        if (dataSource.exists(id)) {
            dataSource.delete(id);
        } else {
            throw new IllegalArgumentException("No artist deleted for id " + id);
        }
    }
}
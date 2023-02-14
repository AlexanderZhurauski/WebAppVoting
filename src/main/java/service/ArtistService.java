package service;

import dao.api.IArtistDAO;
import dao.entity.ArtistEntity;
import dto.ArtistDTO;
import dto.ArtistInputDTO;
import service.api.IArtistService;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ArtistService implements IArtistService {

    private final IArtistDAO dataSource;

    public ArtistService(IArtistDAO dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<ArtistDTO> getAll() {
        return convertFromEntityList(dataSource.getAll());
    }

    @Override
    public boolean exists(Long id) {
        return dataSource.exists(id);
    }

    @Override
    public ArtistDTO get(Long id) {
        return convertFromEntity(dataSource.get(id));
    }

    @Override
    public void add(ArtistInputDTO artist) {
        String artistName = artist.getName();
        if (artistName == null || artistName.isBlank()) {
            throw new IllegalArgumentException("No artist name has been provided!");
        }
        dataSource.add(convertToEntity(artist));
    }

    @Override
    public void update(Long id, Long version, ArtistInputDTO artist) {
        ArtistEntity artistEntity = dataSource.get(id);
        if (artistEntity == null) {
            throw new IllegalArgumentException("No artist updated for id " + id
                    + " as it doesn't exist.");
        }
        if (!Objects.equals(artistEntity.getVersion(), version)) {
            throw new IllegalArgumentException("No artist updated for id " + id
                    + ". Invalid version provided.");
        }
        String artistName = artist.getName();
        if (artistName == null || artistName.isBlank()) {
            throw new IllegalArgumentException("No artist name has been provided!");
        }
        artistEntity.setArtist(artist.getName());

        dataSource.update(artistEntity);
    }

    @Override
    public void delete(Long id) {
        if (dataSource.exists(id)) {
            dataSource.delete(id);
        } else {
            throw new IllegalArgumentException("No artist deleted for id " + id);
        }
    }

    private ArtistDTO convertFromEntity(ArtistEntity artist) {
        return new ArtistDTO(artist);
    }

    private List<ArtistDTO> convertFromEntityList(List<ArtistEntity> artists) {
        return artists
                .stream()
                .map(this::convertFromEntity)
                .collect(Collectors.toList());
    }

    private ArtistEntity convertToEntity(ArtistInputDTO artist) {
        return new ArtistEntity(artist.getName());
    }
 }
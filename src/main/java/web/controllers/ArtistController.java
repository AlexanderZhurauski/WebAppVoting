package web.controllers;

import dto.ArtistDTO;
import dto.ArtistInputDTO;
import org.springframework.web.bind.annotation.*;
import service.api.IArtistService;

import java.util.List;

@RestController
public class ArtistController {
    private final IArtistService service;

    public ArtistController(IArtistService service) {
        this.service = service;
    }

    @GetMapping("/artists")
    public List<ArtistDTO> getArtists() {
        return this.service.getAll();
    }

    @PostMapping("/artists")
    public void postArtist(@RequestBody ArtistInputDTO artist) {
        this.service.add(artist);
    }

    @PutMapping("/artists/{id}/version/{versionId}")
    public void updateArtist(@RequestBody ArtistInputDTO artist,
                             @PathVariable Long id,
                             @PathVariable Long versionId) {
        this.service.update(id, versionId, artist);
    }

    @DeleteMapping("/artists/{id}")
    public void deleteArtist(@PathVariable Long id) {
        this.service.delete(id);
    }
}
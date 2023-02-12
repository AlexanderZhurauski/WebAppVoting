package controllers;

import dto.ArtistDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import service.api.IArtistService;

import java.util.List;

@RequestMapping("/artists/*")
@RestController
public class ArtistController {
    private final IArtistService service;

    public ArtistController(IArtistService service) {
        this.service = service;
    }

    @GetMapping
    public List<ArtistDTO> getArtists() {
        return this.service.getAll();
    }

    @PostMapping
    public void postArtist(@RequestBody ArtistDTO artist) {
        this.service.add(artist.getArtist());
    }

    @PatchMapping
    public void updateArtist(@RequestBody ArtistDTO artist) {
        this.service.update(artist.getId(), artist.getArtist());
    }

    @DeleteMapping("/artists/{id}")
    public void deleteArtist(@PathVariable Long id) {
        this.service.delete(id);
    }
}
package web.controllers;

import dto.GenreDTO;
import dto.GenreInputDTO;
import org.springframework.web.bind.annotation.*;
import service.api.IGenreService;

import java.util.List;

@RestController
public class GenreController {
    private final IGenreService service;

    public GenreController(IGenreService service) {
        this.service = service;
    }

    @GetMapping("/genres")
    public List<GenreDTO> getGenres() {
        return this.service.getAll();
    }

    @PostMapping("/genres")
    public void postGenre(@RequestBody GenreInputDTO genre) {
        this.service.add(genre);
    }

    @PutMapping("/genres/{id}/version/{versionId}")
    public void updateGenre(@RequestBody GenreInputDTO genre,
                            @PathVariable Long id,
                            @PathVariable Long versionId) {
        this.service.update(id, versionId, genre);
    }

    @DeleteMapping("/genres/{id}")
    public void deleteGenre(@PathVariable Long id) {
        this.service.delete(id);
    }
}

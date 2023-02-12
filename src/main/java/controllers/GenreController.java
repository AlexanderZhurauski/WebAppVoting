package controllers;

import dto.GenreDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import service.api.IGenreService;

import java.util.List;

@RequestMapping("/genres/*")
@RestController
public class GenreController {
    private final IGenreService service;

    public GenreController(IGenreService service) {
        this.service = service;
    }

    @GetMapping
    public List<GenreDTO> getGenres() {
        return this.service.getAll();
    }

    @PostMapping
    public void postGenre(@RequestBody GenreDTO genre) {
        this.service.add(genre.getGenre());
    }

    @PatchMapping
    public void updateGenre(@RequestBody GenreDTO genre) {
        this.service.update(genre.getId(), genre.getGenre());
    }

    @DeleteMapping("/genres/{id}")
    public void deleteGenre(@PathVariable Long id) {
        this.service.delete(id);
    }
}

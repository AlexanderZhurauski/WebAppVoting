package dto;

public class GenreInputDTO {

    private String name;

    public GenreInputDTO() {
    }

    public GenreInputDTO(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

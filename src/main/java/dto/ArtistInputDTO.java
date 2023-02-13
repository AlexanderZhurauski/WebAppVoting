package dto;

public class ArtistInputDTO {

    private String name;

    public ArtistInputDTO() {
    }

    public ArtistInputDTO(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

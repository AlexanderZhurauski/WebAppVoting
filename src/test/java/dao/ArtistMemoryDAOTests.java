package dao;

import dao.api.IArtistDAO;
import dao.factories.ArtistDAOSingleton;
import dto.ArtistDTO;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ArtistMemoryDAOTests {
    private IArtistDAO dao;
    private List<ArtistDTO> expected;

    @BeforeAll
    public void setup() {
        dao = ArtistDAOSingleton.getInstance();
        expected = List.of(
                new ArtistDTO(1, "Taylor Swift"),
                new ArtistDTO(2, "Prince"),
                new ArtistDTO(3, "Elvis Presley"),
                new ArtistDTO(4, "Eminem")
        );
    }

    @Test
    @DisplayName("Testing if getAll method of ArtistDAO returns the correct list: ")
    public void testGetAll() {

        List<ArtistDTO> actual = dao.getAll();

        assertEquals(expected.size(), actual.size());

        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i), actual.get(i));
        }
    }

    @Test
    @DisplayName("Testing if exists method of ArtistDAO returns correct boolean values: ")
    public void testExists() {
        assertTrue(dao.exists(1));
        assertTrue(dao.exists(4));

        assertFalse(dao.exists(5));
        assertFalse(dao.exists(-10));
    }

    @Test
    @DisplayName("Testing if get method of ArtistDAO returns correct ArtistDTO objects: ")
    public void testGet() {
        assertEquals(expected.get(0), dao.get(1));
        assertEquals(expected.get(3), dao.get(4));

        assertNotEquals(new ArtistDTO(2, "Princess"), dao.get(2));
        assertNotEquals(new ArtistDTO(3, "Elvis Preslay"), dao.get(3));
    }
}

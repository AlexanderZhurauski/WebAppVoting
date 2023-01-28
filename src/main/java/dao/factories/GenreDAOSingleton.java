package dao.factories;

import dao.database.GenreDBDAO;
import dao.api.IGenreDAO;

public class GenreDAOSingleton {

    private static volatile IGenreDAO instance;

    private GenreDAOSingleton() {
    }

    public static IGenreDAO getInstance() {
        if (instance == null) {
            synchronized (GenreDAOSingleton.class) {
                if (instance == null) {
                    instance = new GenreDBDAO();
                }
            }
        }
        return instance;
    }
}
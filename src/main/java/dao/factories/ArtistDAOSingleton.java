package dao.factories;

import dao.database.ArtistDBDao;
import dao.memory.ArtistMemoryDAO;
import dao.api.IArtistDAO;

public class ArtistDAOSingleton {

    private static volatile IArtistDAO instance;

    private ArtistDAOSingleton() {
    }

    public static IArtistDAO getInstance(DAOType type) {
        if (instance == null) {
            synchronized (ArtistDAOSingleton.class) {
                if (instance == null) {
                    switch (type) {
                        case DB: {
                            instance = new ArtistDBDao();
                            break;
                        }
                        case MEMORY: {
                            instance = new ArtistMemoryDAO();
                            break;
                        }
                        default: {
                            throw new IllegalArgumentException("Illegal DAO type provided");
                        }
                    }

                }
            }
        }
        return instance;
    }
}
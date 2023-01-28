package service.factories;

import dao.factories.ArtistDAOSingleton;
import service.ArtistService;
import service.api.IArtistService;

public class ArtistServiceSingleton {

    private volatile static IArtistService instance = null;

    private ArtistServiceSingleton() {
    }

    public static IArtistService getInstance() {
        if (instance == null) {
            synchronized (ArtistServiceSingleton.class) {
                if (instance == null) {
                    instance = new ArtistService(
                            ArtistDAOSingleton.getInstance());
                }
            }
        }
        return instance;
    }
}
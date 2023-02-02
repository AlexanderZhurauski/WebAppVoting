import dto.StatisticsDTO;
import service.api.IStatisticsService;
import service.factories.StatisticsServiceSingleton;

public class Main {

    public static void main(String[] args) {
        IStatisticsService service = StatisticsServiceSingleton.getInstance();
        StatisticsDTO stats = service.getStatistics();
        stats.getBestGenres()
                .forEach((key, value) -> System.out.println(key + ": " + value));
    }
}

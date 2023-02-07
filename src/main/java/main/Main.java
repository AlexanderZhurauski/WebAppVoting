package main;

import dto.SavedVoteDTO;
import dto.VoteDTO;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import service.*;
import service.api.*;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        ApplicationContext xmlContext = new ClassPathXmlApplicationContext("service.xml");

        IArtistService artistService = xmlContext.getBean("ArtistService",
                ArtistService.class);
        IGenreService genreService = xmlContext.getBean("GenreService",
                GenreService.class);
        IVoteService voteService = xmlContext.getBean("VoteService",
                VoteService.class);
        IStatisticsService statisticsService = xmlContext.getBean("StatisticsService",
                StatisticsService.class);
        ISenderService senderService = xmlContext.getBean("SenderService",
                SenderService.class);

        System.out.println("All artists:");
        artistService
                .getAll()
                .forEach(artist -> System.out.println(artist.getId()
                        + ": " + artist.getArtist()));

        System.out.println("----------------");
        System.out.println("All genres:");
        genreService
                .getAll()
                .forEach(genre -> System.out.println(genre.getId()
                        + ": " + genre.getGenre()));

        System.out.println("----------------");
        voteService.save(new SavedVoteDTO(new VoteDTO(1,
                List.of(2L, 3L, 4L), "Bruh", "gandajlfdude@gmail.com")));
        System.out.println("All votes:");
        voteService
                .getAll()
                .forEach(vote -> {
                    String voteText = vote.getCreateDataTime() + ":\n";
                    voteText += vote.getVoteDTO().getArtistId() + "\n";
                    voteText += vote.getVoteDTO().getGenreIds() + "\n";
                    voteText += vote.getVoteDTO().getAbout() + "\n";
                    voteText += vote.getVoteDTO().getEmail();
                    System.out.println(voteText);
                });
    }
}

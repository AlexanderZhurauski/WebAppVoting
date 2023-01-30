package web.servlets;

import service.api.IArtistService;
import service.factories.ArtistServiceSingleton;
import web.util.RequestParamHandler;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "ArtistServlet", urlPatterns = "/artists")
public class ArtistServlet extends ExtendedHttpServlet {

    private final IArtistService service;

    public ArtistServlet() {
        this.service = ArtistServiceSingleton.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=UTF-8");
        PrintWriter writer = resp.getWriter();

        service.getAll()
                .forEach(artist -> writer.append(String.valueOf(artist.getId()))
                        .append(" - ")
                        .append(artist.getArtist())
                        .append("<br>"));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=UTF-8");

        String artist = RequestParamHandler.getRequestParam(req,
                RequestParamHandler.ARTIST_PARAM_NAME);
        service.add(artist);

        PrintWriter writer = resp.getWriter();
        writer.append(artist)
                .append(" added successfully");
    }

    @Override
    public void doPatch(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=UTF-8");

        String id = RequestParamHandler.getRequestParam(req,
                RequestParamHandler.ID_PARAM_NAME);
        long artistId = RequestParamHandler.getID(id);
        String artist = RequestParamHandler.getRequestParam(req,
                RequestParamHandler.ARTIST_PARAM_NAME);
        service.update(artistId, artist);

        PrintWriter writer = resp.getWriter();
        writer.append(artist)
                .append(" update successfully");
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=UTF-8");

        String id = RequestParamHandler.getRequestParam(req,
                RequestParamHandler.ID_PARAM_NAME);
        long artistId = RequestParamHandler.getID(id);
        service.delete(artistId);

        PrintWriter writer = resp.getWriter();
        writer.append("Artist delete successfully");
    }
}
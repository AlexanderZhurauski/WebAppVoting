package web.servlets;

import dto.SavedVoteDTO;
import dto.VoteDTO;
import service.api.ISenderService;
import service.api.IVoteService;
import service.factories.SenderServiceSingleton;
import service.factories.VoteServiceSingleton;
import web.util.RequestParamHandler;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "VoteServlet", urlPatterns = "/vote")
public class VoteServlet extends HttpServlet {

    private final IVoteService service;

    public VoteServlet() {
        this.service = VoteServiceSingleton.getInstance();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=UTF-8");

        String id = RequestParamHandler.getRequestParam(req,
                RequestParamHandler.ARTIST_PARAM_NAME);
        int artistId = RequestParamHandler.getID(id);
        List<Integer> genreIds = RequestParamHandler.getID(req,
                RequestParamHandler.GENRE_PARAM_NAME);
        String about = RequestParamHandler.getRequestParam(req,
                RequestParamHandler.ABOUT_PARAM_NAME);
        String email = RequestParamHandler.getRequestParam(req,
                RequestParamHandler.EMAIL_PARAM_NAME);

        VoteDTO vote = new VoteDTO(artistId, genreIds, about, email);
        service.validate(vote);

        SavedVoteDTO savedVote = new SavedVoteDTO(vote);
        service.save(savedVote);

        String contextPath = req.getContextPath();
        resp.sendRedirect(contextPath + "/results");
    }
}
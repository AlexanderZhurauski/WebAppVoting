package web.servlets;

import service.api.ISendingService;
import service.factories.SenderServiceSingleton;
import web.util.RequestParamHandler;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.UUID;

@WebServlet(name = "EmailVerificationServlet", urlPatterns = "/verification")
public class EmailVerificationServlet extends HttpServlet {
    private static final String CHARACTER_ENCODING = "UTF-8";
    private static final String CONTENT_TYPE = "text/html; charset=UTF-8";
    private static final String VERIFICATION_KEY_ATTRIBUTE = "key";
    private static final String AUTHENTICATION_ATTRIBUTE = "isAuthenticated";
    private final ISendingService senderService;

    public EmailVerificationServlet() {
        senderService = SenderServiceSingleton.getInstance();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding(CHARACTER_ENCODING);
        resp.setContentType(CONTENT_TYPE);

        UUID keyFromServer = UUID.randomUUID();
        req.getSession().setAttribute(VERIFICATION_KEY_ATTRIBUTE, keyFromServer.toString());
        String userEmail = RequestParamHandler.getRequestParam(req, RequestParamHandler.EMAIL_PARAM_NAME);
        String verificationLink = req.getRequestURL().toString() + "?"
                + RequestParamHandler.VERIFICATION_KEY + "=" + keyFromServer;
        senderService.verifyEmail(userEmail, "<a href=" + verificationLink + ">Confirm email!</a>");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding(CHARACTER_ENCODING);
        resp.setContentType(CONTENT_TYPE);

        HttpSession session = req.getSession();
        String keyFromEmail = RequestParamHandler.getRequestParam(req, RequestParamHandler.VERIFICATION_KEY);
        String keyFromServer = (String) session.getAttribute(VERIFICATION_KEY_ATTRIBUTE);

        session.setAttribute(AUTHENTICATION_ATTRIBUTE, keyFromServer.equals(keyFromEmail));
    }
}


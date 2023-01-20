package web.servlets;

import web.util.RequestParamHandler;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

@WebServlet(name = "EmailVerificationServlet", urlPatterns = "/verification")
public class EmailVerificationServlet extends HttpServlet {
    private final UUID verificationKey;

    public EmailVerificationServlet() {
        verificationKey = UUID.randomUUID();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html; charset=UTF-8");

        String userEmail = req.getParameter(RequestParamHandler.EMAIL_PARAM_NAME);

    }
}

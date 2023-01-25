package web.util;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class RequestParamHandler {

    public static final String GENRE_PARAM_NAME = "genre";
    public static final String ARTIST_PARAM_NAME = "artist";
    public static final String ABOUT_PARAM_NAME = "about";
    public static final String ID_PARAM_NAME = "id";
    public static final String EMAIL_PARAM_NAME = "email";
    public static final String VERIFICATION_KEY = "key";

    public static String getRequestParam(HttpServletRequest req, String name) {
        String[] param = req.getParameterValues(name);
        if (param == null || param.length == 0) {
            throw new IllegalArgumentException("The parameter " + name +
                    " was passed incorrectly");
        }
        if (param.length > 1) {
            throw new IllegalArgumentException("More than one parameter " +
                    name + " has been provided");
        }
        if (param[0] == null || param[0].isBlank()) {
            throw new IllegalArgumentException("Invalid " + name +
                    " parameter provided");
        }

        return param[0];
    }

    public static int getID(String id) {
        try {
            return Integer.parseInt(id);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid " + id +
                    " parameter provided");
        }
    }

    public static List<Integer> getID(HttpServletRequest req, String name)
            throws IllegalArgumentException {

        String[] ids = req.getParameterValues(name);
        if (ids == null || ids.length == 0) {
            throw new IllegalArgumentException("User failed to provide ids");
        }

        return Arrays.stream(ids)
                .map(RequestParamHandler::getID)
                .collect(Collectors.toList());
    }
}
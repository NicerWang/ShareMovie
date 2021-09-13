package nk.bigdata.backend.controller.su;

import com.fasterxml.jackson.databind.ObjectMapper;
import nk.bigdata.backend.service.MovieService;
import nk.bigdata.backend.service.impl.MovieServiceImpl;
import nk.bigdata.backend.utils.Status;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "SuDeleteMovieServlet", value = "/su/DeleteMovieServlet")
public class DeleteMovieServlet extends HttpServlet {
    private final MovieService movieService = new MovieServiceImpl();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");
        String json;
        if (id != null && !id.equals("")) {
            int affectRows = movieService.remove(id);
            if (affectRows != 0) {
                json = objectMapper.writeValueAsString(new Status(true));
                response.getWriter().write(json);
                return;
            }
        }
        json = objectMapper.writeValueAsString(new Status(false));
        response.getWriter().write(json);
    }
}

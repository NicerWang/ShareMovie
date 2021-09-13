package nk.bigdata.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import nk.bigdata.backend.domain.Movie;
import nk.bigdata.backend.service.MovieService;
import nk.bigdata.backend.service.impl.MovieServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "GetAllMovieServlet", value = "/GetAllMovieServlet")
public class GetAllMovieServlet extends HttpServlet {
    private final MovieService movieService = new MovieServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String pageStr = request.getParameter("page");
        String pageSizeStr = request.getParameter("pageSize");
        if(pageStr == null || pageSizeStr == null || pageSizeStr.equals("") || pageStr.equals("")){
            List<Movie> movies = movieService.getAll();
            String json = objectMapper.writeValueAsString(movies);
            response.getWriter().write(json);
            return;
        }
        int page = Integer.parseInt(pageStr);
        int pageSize = Integer.parseInt(pageSizeStr);
        List<Movie> movies = movieService.getByPageSize(page,pageSize);
        String json = objectMapper.writeValueAsString(movies);
        response.getWriter().write(json);
    }
}

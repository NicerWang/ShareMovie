package nk.bigdata.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import nk.bigdata.backend.service.MovieRateDataService;
import nk.bigdata.backend.service.MovieService;
import nk.bigdata.backend.service.impl.MovieRateDataServiceImpl;
import nk.bigdata.backend.service.impl.MovieServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "GetMovieInfoServlet", value = "/GetMovieInfoServlet")
public class GetMovieInfoServlet extends HttpServlet {
    private final MovieService movieService = new MovieServiceImpl();
    private final MovieRateDataService movieRateDataService = new MovieRateDataServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String movieId = request.getParameter("movie");
        if (movieId == null || movieId.length() == 0) return;
        List<Object> ret = new ArrayList<>();
        ret.add(movieService.getById(movieId));
        try {
            ret.add(movieRateDataService.getCurrent(movieId));
        } catch (Exception e) {
            ret.add(0.0);
        }
        String json = objectMapper.writeValueAsString(ret);
        response.getWriter().write(json);
    }
}

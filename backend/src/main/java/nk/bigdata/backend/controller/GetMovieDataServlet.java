package nk.bigdata.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import nk.bigdata.backend.service.MovieRateDataService;
import nk.bigdata.backend.service.MovieStarDataService;
import nk.bigdata.backend.service.MovieWordDataService;
import nk.bigdata.backend.service.impl.MovieRateDataServiceImpl;
import nk.bigdata.backend.service.impl.MovieStarDataServiceImpl;
import nk.bigdata.backend.service.impl.MovieWordDataServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "GetMovieDataServlet", value = "/GetMovieDataServlet")
public class GetMovieDataServlet extends HttpServlet {
    MovieRateDataService movieRateDataService = new MovieRateDataServiceImpl();
    MovieWordDataService movieWordDataService = new MovieWordDataServiceImpl();
    MovieStarDataService movieStarDataService = new MovieStarDataServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String movieId = request.getParameter("movie");
        String type = request.getParameter("type");
        if (movieId == null || movieId.length() == 0 || type == null || type.length() == 0) return;
        List<?> ret;
        if (type.equals("word")) {
            ret = movieWordDataService.getAllByMovieId(movieId);
        } else if (type.equals("rate")) {
            ret = movieRateDataService.getAllByMovieId(movieId);
        } else if (type.equals("star")) {
            ret = movieStarDataService.getAllByMovieId(movieId);
        } else return;
        String json = objectMapper.writeValueAsString(ret);
        response.getWriter().write(json);
    }
}

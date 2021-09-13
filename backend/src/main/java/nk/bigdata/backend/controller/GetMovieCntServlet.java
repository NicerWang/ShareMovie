package nk.bigdata.backend.controller;

import nk.bigdata.backend.service.MovieService;
import nk.bigdata.backend.service.impl.MovieServiceImpl;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "GetMovieCntServlet", value = "/GetMovieCntServlet")
public class GetMovieCntServlet extends HttpServlet {
    MovieService movieService = new MovieServiceImpl();
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Integer cnt = movieService.getCnt();
        response.getWriter().write(cnt.toString());
    }
}

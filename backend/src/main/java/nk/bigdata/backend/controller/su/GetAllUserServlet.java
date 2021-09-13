package nk.bigdata.backend.controller.su;

import com.fasterxml.jackson.databind.ObjectMapper;
import nk.bigdata.backend.service.UserService;
import nk.bigdata.backend.service.impl.UserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "SuGetAllUserServlet", value = "/su/GetAllUserServlet")
public class GetAllUserServlet extends HttpServlet {
    private final UserService userService = new UserServiceImpl();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String json = objectMapper.writeValueAsString(userService.getAll());
        response.getWriter().write(json);
    }
}

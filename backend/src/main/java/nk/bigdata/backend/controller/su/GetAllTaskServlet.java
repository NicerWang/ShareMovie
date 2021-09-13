package nk.bigdata.backend.controller.su;

import com.fasterxml.jackson.databind.ObjectMapper;
import nk.bigdata.backend.service.TaskService;
import nk.bigdata.backend.service.impl.TaskServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "SuGetAllTaskServlet", value = "/su/GetAllTaskServlet")
public class GetAllTaskServlet extends HttpServlet {
    private final TaskService taskService = new TaskServiceImpl();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String json = objectMapper.writeValueAsString(taskService.getAll());
        response.getWriter().write(json);
    }
}

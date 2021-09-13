package nk.bigdata.backend.controller.su;

import com.fasterxml.jackson.databind.ObjectMapper;
import nk.bigdata.backend.service.UserService;
import nk.bigdata.backend.service.impl.UserServiceImpl;
import nk.bigdata.backend.utils.Status;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "SuDeleteUserServlet", value = "/su/DeleteUserServlet")
public class DeleteUserServlet extends HttpServlet {
    private final UserService userService = new UserServiceImpl();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String tel = request.getParameter("tel");
        String json;
        if (tel != null && !tel.equals("")) {
            int affectRows = userService.remove(tel);
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

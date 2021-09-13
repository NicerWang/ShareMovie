package nk.bigdata.backend.controller.su;

import com.fasterxml.jackson.databind.ObjectMapper;
import nk.bigdata.backend.service.UserService;
import nk.bigdata.backend.service.impl.UserServiceImpl;
import nk.bigdata.backend.utils.MD5;
import nk.bigdata.backend.utils.Status;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "SuUpdateUserInfoServlet", value = "/su/UpdateUserInfoServlet")
public class UpdateUserInfoServlet extends HttpServlet {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final UserService userService = new UserServiceImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String tel = request.getParameter("tel");
        String newName = request.getParameter("name");
        String pwd = request.getParameter("pwd");
        Map<String, Object> map = new HashMap<>();
        map.put("tel", tel);
        if (newName != null && !newName.equals("")) {
            map.put("newname", newName);
        }
        if (pwd != null && !pwd.equals("")) {
            map.put("pwd", MD5.getResult(pwd + tel));
        }
        String json;
        if (map.size() != 0) {
            int affectRows = userService.update(map);
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

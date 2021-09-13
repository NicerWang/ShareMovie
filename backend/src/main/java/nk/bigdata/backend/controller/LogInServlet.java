package nk.bigdata.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import nk.bigdata.backend.domain.User;
import nk.bigdata.backend.service.UserService;
import nk.bigdata.backend.service.impl.UserServiceImpl;
import nk.bigdata.backend.utils.MD5;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "LogInServlet", value = "/LogInServlet")
public class LogInServlet extends HttpServlet {
    private final UserService userService = new UserServiceImpl();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String tel = request.getParameter("tel");
        String pwd = request.getParameter("pwd");
        List<Object> ret = new ArrayList<>();
        if (pwd != null && tel != null && !tel.equals("") && !pwd.equals("")) {
            User user = userService.getByTel(tel);
            if (user != null && user.getPwd().equals(MD5.getResult(pwd + tel))) {
                if (user.isSu()) {
                    session.setAttribute("su", "true");
                }
                session.setAttribute("user", user);
                ret.add(true);
                ret.add(user.isSu());
                String json = objectMapper.writeValueAsString(ret);
                response.getWriter().write(json);
                return;
            }
        }
        String json = objectMapper.writeValueAsString(false);
        response.getWriter().write(json);
    }
}

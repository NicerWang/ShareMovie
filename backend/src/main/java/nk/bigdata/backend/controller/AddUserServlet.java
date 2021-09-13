package nk.bigdata.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import nk.bigdata.backend.domain.User;
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

@WebServlet(name = "AddUserServlet", value = "/AddUserServlet")
public class AddUserServlet extends HttpServlet {
    UserService userService = new UserServiceImpl();
    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String tel = request.getParameter("tel");
        String name = request.getParameter("name");
        String pwd = request.getParameter("pwd");
        String json;

        if (tel != null && name != null && pwd != null && !tel.equals("") && !pwd.equals("") && !name.equals("")) {
            pwd = pwd + tel;
            pwd = MD5.getResult(pwd);
            int affectRows = 0;
            try {
                affectRows = userService.add(new User(tel, name, pwd));
            } catch (Exception e) {
                e.printStackTrace();
            }
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

package nk.bigdata.backend.controller.su;

import com.fasterxml.jackson.databind.ObjectMapper;
import nk.bigdata.backend.automate.TaskManager;
import nk.bigdata.backend.domain.Task;
import nk.bigdata.backend.service.TaskService;
import nk.bigdata.backend.service.impl.TaskServiceImpl;
import nk.bigdata.backend.utils.Status;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@WebServlet(name = "SuAddTaskServlet", value = "/su/AddTaskServlet")
public class AddTaskServlet extends HttpServlet {
    TaskService taskService = new TaskServiceImpl();
    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");
        String json;
        String type = "include";

        if (id != null && !id.equals("")) {
            List<Task> tasks = taskService.getAll();
            for (Task task : tasks) {
                if (task.getStatus().equals("Loading...")) {
                    json = objectMapper.writeValueAsString(new Status(false));
                    response.getWriter().write(json);
                    return;
                }
            }
            Task task = taskService.getById(id);
            if (task == null) {
                task = new Task(id, "Loading...", "", type, new Date());
                taskService.add(task);
                TaskManager.submitTask(task);
            } else {
                task.setType("update");
                task.setStatus("Loading...");
                TaskManager.submitTask(task);
                taskService.update(task);
            }
            json = objectMapper.writeValueAsString(new Status(true));
            response.getWriter().write(json);
            return;
        }
        json = objectMapper.writeValueAsString(new Status(false));
        response.getWriter().write(json);
    }
}

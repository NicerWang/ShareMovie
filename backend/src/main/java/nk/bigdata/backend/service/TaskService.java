package nk.bigdata.backend.service;

import nk.bigdata.backend.domain.Task;

import java.util.List;

public interface TaskService {
    List<Task> getAll();

    Task getById(String id);

    int add(Task task);

    int update(Task task);

    String getLog(String id);
}

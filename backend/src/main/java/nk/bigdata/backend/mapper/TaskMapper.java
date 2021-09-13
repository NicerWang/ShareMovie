package nk.bigdata.backend.mapper;

import nk.bigdata.backend.domain.Task;

import java.util.List;

public interface TaskMapper {
    List<Task> getAll();

    Task getById(String id);

    int add(Task task);

    int update(Task task);

    int remove(String id);

    String getLog(String id);
}

package nk.bigdata.backend.service.impl;

import nk.bigdata.backend.domain.Task;
import nk.bigdata.backend.mapper.TaskMapper;
import nk.bigdata.backend.service.TaskService;
import nk.bigdata.backend.utils.MyBatisUtils;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

public class TaskServiceImpl implements TaskService {

    @Override
    public List<Task> getAll() {
        SqlSession sqlSession = MyBatisUtils.getSqlSession();
        TaskMapper mapper = sqlSession.getMapper(TaskMapper.class);
        List<Task> tasks = mapper.getAll();
        sqlSession.close();
        return tasks;
    }

    @Override
    public Task getById(String id) {
        SqlSession sqlSession = MyBatisUtils.getSqlSession();
        TaskMapper mapper = sqlSession.getMapper(TaskMapper.class);
        Task task = mapper.getById(id);
        sqlSession.close();
        return task;
    }

    @Override
    public int add(Task task) {
        SqlSession sqlSession = MyBatisUtils.getSqlSession();
        TaskMapper mapper = sqlSession.getMapper(TaskMapper.class);
        int affectRows = mapper.add(task);
        sqlSession.commit();
        sqlSession.close();
        return affectRows;
    }

    @Override
    public int update(Task task) {
        SqlSession sqlSession = MyBatisUtils.getSqlSession();
        TaskMapper mapper = sqlSession.getMapper(TaskMapper.class);
        Task t = new Task();
        t.setStart(task.getStart());
        t.setLog("\"" + task.getLog() + "\"");
        t.setId("\"" + task.getId() + "\"");
        t.setStatus("\"" + task.getStatus() + "\"");
        t.setType("\"" + task.getType() + "\"");
        int affectRows = mapper.update(t);
        sqlSession.commit();
        sqlSession.close();
        return affectRows;
    }

    @Override
    public String getLog(String id) {
        SqlSession sqlSession = MyBatisUtils.getSqlSession();
        TaskMapper mapper = sqlSession.getMapper(TaskMapper.class);
        String tasks = mapper.getLog(id);
        sqlSession.close();
        return tasks;
    }
}

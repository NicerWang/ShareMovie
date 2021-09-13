package nk.bigdata.backend.service.impl;

import nk.bigdata.backend.domain.User;
import nk.bigdata.backend.mapper.UserMapper;
import nk.bigdata.backend.service.UserService;
import nk.bigdata.backend.utils.MyBatisUtils;
import org.apache.ibatis.session.SqlSession;

import java.util.List;
import java.util.Map;

public class UserServiceImpl implements UserService {


    @Override
    public List<User> getAll() {
        SqlSession sqlSession = MyBatisUtils.getSqlSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        List<User> users = mapper.getAll();
        sqlSession.close();
        return users;
    }

    @Override
    public User getByTel(String tel) {
        SqlSession sqlSession = MyBatisUtils.getSqlSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        User user = mapper.getByTel(tel);
        sqlSession.close();
        return user;
    }

    @Override
    public int add(User user) {
        SqlSession sqlSession = MyBatisUtils.getSqlSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        int affectRows = mapper.add(user);
        sqlSession.commit();
        sqlSession.close();
        return affectRows;
    }

    @Override
    public int remove(String tel) {
        SqlSession sqlSession = MyBatisUtils.getSqlSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        int affectRows = mapper.remove(tel);
        sqlSession.commit();
        sqlSession.close();
        return affectRows;
    }

    @Override
    public int update(Map<String, Object> info) {
        SqlSession sqlSession = MyBatisUtils.getSqlSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        info.replaceAll((s, v) -> "\"" + v + "\"");
        int affectRows = mapper.update(info);
        sqlSession.commit();
        sqlSession.close();
        return affectRows;
    }
}

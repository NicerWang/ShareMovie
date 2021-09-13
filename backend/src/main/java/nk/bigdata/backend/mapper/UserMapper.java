package nk.bigdata.backend.mapper;

import nk.bigdata.backend.domain.User;

import java.util.List;
import java.util.Map;

public interface UserMapper {
    List<User> getAll();

    User getByTel(String tel);

    int add(User user);

    int remove(String tel);

    int update(Map<String, Object> info);
}

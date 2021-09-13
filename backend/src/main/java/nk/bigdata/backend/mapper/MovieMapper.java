package nk.bigdata.backend.mapper;

import nk.bigdata.backend.domain.Movie;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MovieMapper {
    List<Movie> getAll();

    List<Movie> getByPageSize(@Param("page") int page,@Param("pageSize") int pageSize);

    Movie getById(String id);

    int remove(String id);

    int add(Movie movie);

    int getCnt();
}

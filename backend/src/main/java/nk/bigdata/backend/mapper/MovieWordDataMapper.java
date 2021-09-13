package nk.bigdata.backend.mapper;

import nk.bigdata.backend.domain.MovieWordData;

import java.util.List;

public interface MovieWordDataMapper {
    List<MovieWordData> getAllByMovieId(String id);

    int removeAllByMovieId(String id);
}

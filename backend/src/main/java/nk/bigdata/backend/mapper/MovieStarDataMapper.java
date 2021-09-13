package nk.bigdata.backend.mapper;

import nk.bigdata.backend.domain.MovieStarData;

import java.util.List;

public interface MovieStarDataMapper {
    List<MovieStarData> getAllByMovieId(String id);

    int removeAllByMovieId(String id);

}

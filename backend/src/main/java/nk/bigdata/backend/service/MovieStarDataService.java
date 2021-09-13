package nk.bigdata.backend.service;

import nk.bigdata.backend.domain.MovieStarData;

import java.util.List;

public interface MovieStarDataService {
    List<MovieStarData> getAllByMovieId(String id);

    int removeAllByMovieId(String id);
}

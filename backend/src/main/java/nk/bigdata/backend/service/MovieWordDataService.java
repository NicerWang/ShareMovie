package nk.bigdata.backend.service;

import nk.bigdata.backend.domain.MovieWordData;

import java.util.List;

public interface MovieWordDataService {
    List<MovieWordData> getAllByMovieId(String id);

    int removeAllByMovieId(String id);
}

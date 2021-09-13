package nk.bigdata.backend.service;

import nk.bigdata.backend.domain.MovieRateData;

import java.util.List;

public interface MovieRateDataService {
    List<MovieRateData> getAllByMovieId(String id);

    int removeAllByMovieId(String id);

    double getCurrent(String id);
}

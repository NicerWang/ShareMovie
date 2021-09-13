package nk.bigdata.backend.mapper;

import nk.bigdata.backend.domain.MovieRateData;

import java.util.List;

public interface MovieRateDataMapper {
    List<MovieRateData> getAllByMovieId(String id);

    int removeAllByMovieId(String id);

    double getCurrent(String id);
}

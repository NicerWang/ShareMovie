package nk.bigdata.backend.service;

import nk.bigdata.backend.domain.Movie;

import java.util.List;

public interface MovieService {
    List<Movie> getAll();

    List<Movie> getByPageSize(int page, int pageSize);

    Movie getById(String id);

    int remove(String id);

    int add(Movie movie);

    int getCnt();
}

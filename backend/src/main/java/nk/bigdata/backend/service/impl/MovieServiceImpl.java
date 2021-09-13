package nk.bigdata.backend.service.impl;

import nk.bigdata.backend.domain.Movie;
import nk.bigdata.backend.mapper.*;
import nk.bigdata.backend.service.MovieService;
import nk.bigdata.backend.utils.MyBatisUtils;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

public class MovieServiceImpl implements MovieService {


    @Override
    public List<Movie> getAll() {
        SqlSession sqlSession = MyBatisUtils.getSqlSession();
        MovieMapper mapper = sqlSession.getMapper(MovieMapper.class);
        List<Movie> movies = mapper.getAll();
        sqlSession.close();
        return movies;
    }

    @Override
    public List<Movie> getByPageSize(int page,int pageSize){
        SqlSession sqlSession = MyBatisUtils.getSqlSession();
        MovieMapper mapper = sqlSession.getMapper(MovieMapper.class);
        List<Movie> movies = mapper.getByPageSize(( page - 1 ) * pageSize, pageSize);
        sqlSession.close();
        return movies;
    }

    @Override
    public Movie getById(String id) {
        SqlSession sqlSession = MyBatisUtils.getSqlSession();
        MovieMapper mapper = sqlSession.getMapper(MovieMapper.class);
        Movie movie = mapper.getById(id);
        sqlSession.close();
        return movie;
    }

    @Override
    public int remove(String id) {
        SqlSession sqlSession = MyBatisUtils.getSqlSession();
        MovieMapper mapper = sqlSession.getMapper(MovieMapper.class);
        MovieRateDataMapper rateMapper = sqlSession.getMapper(MovieRateDataMapper.class);
        MovieWordDataMapper wordMapper = sqlSession.getMapper(MovieWordDataMapper.class);
        MovieStarDataMapper starMapper = sqlSession.getMapper(MovieStarDataMapper.class);
        TaskMapper taskMapper = sqlSession.getMapper(TaskMapper.class);
        int affectRows = mapper.remove(id);
        rateMapper.removeAllByMovieId(id);
        wordMapper.removeAllByMovieId(id);
        starMapper.removeAllByMovieId(id);
        taskMapper.remove(id);
        sqlSession.commit();
        sqlSession.close();
        return affectRows;
    }

    @Override
    public int add(Movie movie) {
        SqlSession sqlSession = MyBatisUtils.getSqlSession();
        MovieMapper mapper = sqlSession.getMapper(MovieMapper.class);
        int affectRows = mapper.add(movie);
        sqlSession.commit();
        sqlSession.close();
        return affectRows;
    }

    @Override
    public int getCnt() {
        SqlSession sqlSession = MyBatisUtils.getSqlSession();
        MovieMapper mapper = sqlSession.getMapper(MovieMapper.class);
        int cnt = mapper.getCnt();
        sqlSession.commit();
        sqlSession.close();
        return cnt;
    }
}

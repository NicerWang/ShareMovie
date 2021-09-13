package nk.bigdata.backend.service.impl;

import nk.bigdata.backend.domain.MovieRateData;
import nk.bigdata.backend.mapper.MovieRateDataMapper;
import nk.bigdata.backend.service.MovieRateDataService;
import nk.bigdata.backend.utils.MyBatisUtils;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

public class MovieRateDataServiceImpl implements MovieRateDataService {

    @Override
    public List<MovieRateData> getAllByMovieId(String id) {
        SqlSession sqlSession = MyBatisUtils.getSqlSession();
        MovieRateDataMapper mapper = sqlSession.getMapper(MovieRateDataMapper.class);
        List<MovieRateData> movieRateDataList = mapper.getAllByMovieId(id);
        sqlSession.close();
        return movieRateDataList;
    }

    @Override
    public int removeAllByMovieId(String id) {
        SqlSession sqlSession = MyBatisUtils.getSqlSession();
        MovieRateDataMapper mapper = sqlSession.getMapper(MovieRateDataMapper.class);
        int affectRows = mapper.removeAllByMovieId(id);
        sqlSession.commit();
        sqlSession.close();
        return affectRows;
    }

    @Override
    public double getCurrent(String id) {
        SqlSession sqlSession = MyBatisUtils.getSqlSession();
        MovieRateDataMapper mapper = sqlSession.getMapper(MovieRateDataMapper.class);
        double rate = mapper.getCurrent(id);
        sqlSession.commit();
        sqlSession.close();
        return rate;
    }
}

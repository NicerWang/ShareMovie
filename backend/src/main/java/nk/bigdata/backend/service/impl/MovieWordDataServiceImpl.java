package nk.bigdata.backend.service.impl;

import nk.bigdata.backend.domain.MovieWordData;
import nk.bigdata.backend.mapper.MovieWordDataMapper;
import nk.bigdata.backend.service.MovieWordDataService;
import nk.bigdata.backend.utils.MyBatisUtils;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

public class MovieWordDataServiceImpl implements MovieWordDataService {

    @Override
    public List<MovieWordData> getAllByMovieId(String id) {
        SqlSession sqlSession = MyBatisUtils.getSqlSession();
        MovieWordDataMapper mapper = sqlSession.getMapper(MovieWordDataMapper.class);
        List<MovieWordData> movieWordDataList = mapper.getAllByMovieId(id);
        sqlSession.close();
        return movieWordDataList;
    }

    @Override
    public int removeAllByMovieId(String id) {
        SqlSession sqlSession = MyBatisUtils.getSqlSession();
        MovieWordDataMapper mapper = sqlSession.getMapper(MovieWordDataMapper.class);
        int affectRows = mapper.removeAllByMovieId(id);
        sqlSession.commit();
        sqlSession.close();
        return affectRows;
    }

}

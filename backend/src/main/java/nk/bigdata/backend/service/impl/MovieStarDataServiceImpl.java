package nk.bigdata.backend.service.impl;

import nk.bigdata.backend.domain.MovieStarData;
import nk.bigdata.backend.mapper.MovieStarDataMapper;
import nk.bigdata.backend.service.MovieStarDataService;
import nk.bigdata.backend.utils.MyBatisUtils;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

public class MovieStarDataServiceImpl implements MovieStarDataService {

    @Override
    public List<MovieStarData> getAllByMovieId(String id) {
        SqlSession sqlSession = MyBatisUtils.getSqlSession();
        MovieStarDataMapper mapper = sqlSession.getMapper(MovieStarDataMapper.class);
        List<MovieStarData> dataList = mapper.getAllByMovieId(id);
        sqlSession.close();
        return dataList;
    }

    @Override
    public int removeAllByMovieId(String id) {
        SqlSession sqlSession = MyBatisUtils.getSqlSession();
        MovieStarDataMapper mapper = sqlSession.getMapper(MovieStarDataMapper.class);
        int affectRows = mapper.removeAllByMovieId(id);
        sqlSession.commit();
        sqlSession.close();
        return affectRows;
    }
}

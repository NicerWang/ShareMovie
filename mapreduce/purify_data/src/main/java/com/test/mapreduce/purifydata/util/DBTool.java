package com.test.mapreduce.purifydata.util;

import com.test.mapreduce.purifydata.entity.PureRatingBean;
import com.test.mapreduce.purifydata.entity.PureStarsBean;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;

public class DBTool {
    private static String movieID;
    private static Connection conn;
    private static PreparedStatement pst;
    private static ResultSet rs;

//    private static Writer writer;
//    static{
//        try {
//            writer = new FileWriter(new File("D:\\test\\ideahadoop\\log\\purifylog\\DBtool.txt"));
//            writer.flush();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }


    //Initialize database links
    public static void init(String id, String ip, String user, String passwd) {
        try {
            movieID = id;
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://" + ip + ":3306/bigdata?serverTimezone=UTC&useSSL=false"
                    , user, passwd);
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void close() {
        try {
            if (rs != null)
                rs.close();
            if (pst != null)
                pst.close();
            if (conn != null)
                conn.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void WriteInfo(PureRatingBean pureRatingBean, PureStarsBean pureStarsBean) {
        //write into movie_star_data
        Map<Integer, Integer> tempMap = pureStarsBean.getCountStars();
        for (int i = 1; i < 6; ++i) {
            int tempStarsNum = tempMap.get(i);
            if (tempStarsNum == 0)
                continue;
            try {
                pst = conn.prepareStatement("select * from movie_star_data where `id`=? and `name` = ?");
                pst.setString(1, movieID);
                pst.setString(2, String.valueOf(i));
                rs = pst.executeQuery();
                if (rs.next()) {
                    tempStarsNum += rs.getInt("value");
                    pst = conn.prepareStatement("update movie_star_data set `value` = ? where `id`=? and `name`=?");
                    pst.setInt(1, tempStarsNum);
                    pst.setString(2, movieID);
                    pst.setString(3, String.valueOf(i));
                } else {
                    pst = conn.prepareStatement("insert into movie_star_data values(?,?,?)");
                    pst.setString(1, movieID);
                    pst.setString(2, String.valueOf(i));
                    pst.setInt(3, tempStarsNum);
                }
                pst.executeUpdate();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        //write into movie_rate_data
        try {
            pst = conn.prepareStatement("insert into movie_rate_data values(?,?,?,?,?,?,?)");
            pst.setString(1, movieID);
            pst.setDate(2, new java.sql.Date(new SimpleDateFormat("yyyy-MM-dd").parse(pureRatingBean.getDate()).getTime()));
            pst.setInt(3, pureRatingBean.getDayCommentsNum());
            pst.setDouble(4, pureRatingBean.getDayAverRating());
            pst.setLong(5, pureRatingBean.getAccumulateCommentsNum());
            pst.setDouble(6, pureRatingBean.getAccumulateAverRating());
            pst.setDouble(7, pureRatingBean.getAccumulateRatingSum());
            pst.executeUpdate();
        } catch (SQLException | ParseException throwables) {
            throwables.printStackTrace();
        }
    }
}

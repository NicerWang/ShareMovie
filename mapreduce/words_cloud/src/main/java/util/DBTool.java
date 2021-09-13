package util;

import java.sql.*;

public class DBTool {
    private static Connection conn;
    private static PreparedStatement pst;
    private static ResultSet rs;


    public static void init(String ip, String user, String passwd) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://" + ip + ":3306/bigdata?serverTimezone=UTC&useSSL=false"
                    , user, passwd);
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
    }

    public static void close() {
        try {
            rs.close();
            pst.close();
            conn.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void WriteInfo(String MovieID, String key, long value) {
        try {
            pst = conn.prepareStatement("select * from movie_word_data where `id`=? and `name`=?");
            pst.setString(1, MovieID);
            pst.setString(2, key);
            rs = pst.executeQuery();

            if (rs.next()) {
                long oldValue = rs.getLong("value");
                long newValue = oldValue + value;
                pst = conn.prepareStatement("update movie_word_data set `value`=? where `id`=? and `name`=?");

                pst.setLong(1, newValue);
                pst.setString(2, MovieID);
                pst.setString(3, key);
                pst.executeUpdate();
            } else {
                pst = conn.prepareStatement("insert into movie_word_data values(?,?,?)");
                pst.setString(1, MovieID);
                pst.setLong(2, value);
                pst.setString(3, key);
                pst.executeUpdate();
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }
}

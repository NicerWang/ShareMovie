package automate;

import com.mysql.jdbc.Driver;
import nk.bigdata.backend.domain.Task;

import java.sql.*;
import java.util.Date;


public class UpdateTaskInfo {
    Connection connection;
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    UpdateTaskInfo() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://ip:3306/bigdata?useSSL=true&useUnicode=true&characterEncoding=UTF-8","user","pwd");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    public void update(Task task){
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("update task set status = ?, log = ?, start = ? where id = ?");
            preparedStatement.setString(1,task.getStatus());
            preparedStatement.setString(2, task.getLog());
            preparedStatement.setTimestamp(3,new Timestamp(new Date().getTime()));
            preparedStatement.setString(4,task.getId());
            System.out.println(preparedStatement);
            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }
}

package com.test.mapreduce.purifydata.entity;

import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.mapreduce.lib.db.DBWritable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class PureRatingBean implements DBWritable, WritableComparable<PureRatingBean> {
    private String movieID;
    private String date;
    private int dayCommentsNum;//当日评论数
    private double dayAverRating;//当日评分平均值
    private long accumulateCommentsNum;//积累评论数
    private double accumulateAverRating;//积累评分平均值
    private double accumulateRatingSum;//积累评分和
    public PureRatingBean() {
    }

    @Override
    public String toString() {
        return "{" + movieID +
                "::" + date +
                "::" + dayCommentsNum +
                "::" + dayAverRating +
                "::" + accumulateCommentsNum +
                "::" + accumulateAverRating +
                "::" + accumulateRatingSum +
                "}";
    }

    public String getMovieID() {
        return movieID;
    }

    public void setMovieID(String movieID) {
        this.movieID = movieID;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getDayCommentsNum() {
        return dayCommentsNum;
    }

    public void setDayCommentsNum(int dayCommentsNum) {
        this.dayCommentsNum = dayCommentsNum;
    }


    public long getAccumulateCommentsNum() {
        return accumulateCommentsNum;
    }

    public void setAccumulateCommentsNum(long accumulateCommentsNum) {
        this.accumulateCommentsNum = accumulateCommentsNum;
    }

    public double getDayAverRating() {
        return dayAverRating;
    }

    public void setDayAverRating(double dayAverRating) {
        this.dayAverRating = dayAverRating;
    }

    public double getAccumulateAverRating() {
        return accumulateAverRating;
    }

    public void setAccumulateAverRating(double accumulateAverRating) {
        this.accumulateAverRating = accumulateAverRating;
    }

    public double getAccumulateRatingSum() {
        return accumulateRatingSum;
    }

    public void setAccumulateRatingSum(double accumulateRatingSum) {
        this.accumulateRatingSum = accumulateRatingSum;
    }


    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeUTF(movieID);
        dataOutput.writeUTF(date);
        dataOutput.writeInt(dayCommentsNum);
        dataOutput.writeDouble(dayAverRating);
        dataOutput.writeLong(accumulateCommentsNum);
        dataOutput.writeDouble(accumulateAverRating);
        dataOutput.writeDouble(accumulateRatingSum);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        movieID = dataInput.readUTF();
        date = dataInput.readUTF();
        dayCommentsNum = dataInput.readInt();
        dayAverRating = dataInput.readDouble();
        accumulateCommentsNum = dataInput.readLong();
        accumulateAverRating = dataInput.readDouble();
        accumulateRatingSum = dataInput.readDouble();
    }

    @Override
    public void write(PreparedStatement preparedStatement) throws SQLException {
        try {
            preparedStatement.setString(1, movieID);
            preparedStatement.setDate(2, new java.sql.Date(new SimpleDateFormat("yyyy-MM-dd").parse(date).getTime()));
            preparedStatement.setInt(3, dayCommentsNum);
            preparedStatement.setDouble(4, dayAverRating);
            preparedStatement.setLong(5, accumulateCommentsNum);
            preparedStatement.setDouble(6, accumulateAverRating);
            preparedStatement.setDouble(7, accumulateRatingSum);
        } catch (ParseException e) {
            e.printStackTrace();
        }
//        preparedStatement.setTimestamp(2,new Timestamp(new SimpleDateFormat("yyyy-MM-dd").parse(date,new ParsePosition(0)).getTime()));

    }

    @Override
    public void readFields(ResultSet resultSet) throws SQLException {
        movieID = resultSet.getString(1);
        date = resultSet.getDate(2).toString();
        dayCommentsNum = resultSet.getInt(3);
        dayAverRating = resultSet.getDouble(4);
        accumulateCommentsNum = resultSet.getLong(5);
        accumulateAverRating = resultSet.getDouble(6);
        accumulateRatingSum = resultSet.getDouble(7);
    }

    @Override
    public int compareTo(PureRatingBean o) {
        return 0;
    }
}

package com.test.mapreduce.purifydata.entity;

import org.apache.hadoop.io.Writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class OriginalRatingBean implements Writable {
    private String date;
    private double rating = 0;
    private int likes;

    public OriginalRatingBean() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    @Override
    public String toString() {
        return "{" + date + "::" + rating + "::" + likes + "}";
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeUTF(date);
        dataOutput.writeDouble(rating);
        dataOutput.writeInt(likes);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        date = dataInput.readUTF();
        rating = dataInput.readDouble();
        likes = dataInput.readInt();
    }
}

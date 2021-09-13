package com.test.mapreduce.purifydata.entity;

import org.apache.hadoop.io.Writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PureStarsBean implements Writable {
    private Map<Integer, Integer> countStars;

    public PureStarsBean() {
        countStars = new HashMap<>();
        countStars.put(1, 0);
        countStars.put(2, 0);
        countStars.put(3, 0);
        countStars.put(4, 0);
        countStars.put(5, 0);
    }

    @Override
    public String toString() {
        return "{" + countStars + "}";
    }

    public Map<Integer, Integer> getCountStars() {
        return countStars;
    }

    public void setCountStars(Map<Integer, Integer> countStars) {
        this.countStars = countStars;
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
    }
}

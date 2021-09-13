package com.test.mapreduce.purifydata;

import com.test.mapreduce.purifydata.entity.OriginalRatingBean;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class PurifyMapper extends Mapper<LongWritable, Text, Text, OriginalRatingBean> {
    private final Text outKey = new Text();
    private final OriginalRatingBean outValue = new OriginalRatingBean();

//    private static Writer writer;
//    static{
//        try {
//            writer = new FileWriter(new File("D:\\test\\ideahadoop\\log\\purifylog\\mapper.txt"));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

        /* * data in value:
         * userName
         * userID
         * rate
         * date
         * likes
         */
        String line = value.toString();
        String[] split = line.split(",");
        if (split.length != 5) {
            return;
        }
        outValue.setRating(Double.parseDouble(split[2]));
        outValue.setDate(split[3].split(" ")[0]);
        outValue.setLikes(Integer.parseInt(split[4]));
        outKey.set(outValue.getDate());
        context.write(outKey, outValue);
    }
}

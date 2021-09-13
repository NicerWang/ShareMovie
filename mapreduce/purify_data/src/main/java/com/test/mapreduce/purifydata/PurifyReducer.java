package com.test.mapreduce.purifydata;

import com.test.mapreduce.purifydata.entity.OriginalRatingBean;
import com.test.mapreduce.purifydata.entity.PureRatingBean;
import com.test.mapreduce.purifydata.entity.PureStarsBean;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class PurifyReducer extends Reducer<Text, OriginalRatingBean, PureRatingBean, PureStarsBean> {

    private static long accumulateCommentsNum = 0;
    private static double accumulateRatingSum = 0;

    private final PureRatingBean outKey = new PureRatingBean();

//    private static Writer writer;
//    static{
//        try {
//            writer = new FileWriter(new File("D:\\test\\ideahadoop\\log\\purifylog\\reducer.txt"));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    @Override
    protected void reduce(Text key, Iterable<OriginalRatingBean> values, Context context) throws IOException, InterruptedException {
        double currentRatingSum = 0;
        int currentCommentsNum = 0;

        //Add additional weight to relevant scores according to the number of likes
        int extraCommentsNum = 0;
        double extraRatingSum = 0;

        PureStarsBean outValue = new PureStarsBean();
        for (OriginalRatingBean value : values) {

            //Add additional weight to relevant scores according to the number of likes
            extraCommentsNum += value.getLikes() / 10;
            extraRatingSum += value.getRating() * (value.getLikes() / 10);

            currentCommentsNum++;
            currentRatingSum += value.getRating();
            int rating = (int) value.getRating();
            outValue.getCountStars().put(rating, outValue.getCountStars().get(rating) + 1);
        }
        accumulateCommentsNum += currentCommentsNum + extraCommentsNum;
        accumulateRatingSum += currentRatingSum + extraRatingSum;
        outKey.setMovieID(context.getConfiguration().get("movieID"));
        outKey.setDate(key.toString());
        outKey.setDayCommentsNum(currentCommentsNum);
        outKey.setDayAverRating((currentRatingSum + extraRatingSum) / (currentCommentsNum + extraCommentsNum));
        outKey.setAccumulateCommentsNum(accumulateCommentsNum);
        outKey.setAccumulateAverRating(accumulateRatingSum / accumulateCommentsNum);
        outKey.setAccumulateRatingSum(accumulateRatingSum);
        context.write(outKey, outValue);
    }
}

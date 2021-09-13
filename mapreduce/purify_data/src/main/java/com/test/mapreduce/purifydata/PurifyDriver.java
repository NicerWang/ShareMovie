package com.test.mapreduce.purifydata;

import com.test.mapreduce.purifydata.entity.OriginalRatingBean;
import com.test.mapreduce.purifydata.entity.PureRatingBean;
import com.test.mapreduce.purifydata.entity.PureStarsBean;
import com.test.mapreduce.purifydata.util.PurifyOutPutFormat;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.db.DBConfiguration;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PurifyDriver {

    static Logger logger = Logger.getLogger(PurifyDriver.class);

    //    static Writer writer;
    public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException, SQLException {
//        writer = new FileWriter(new File("D:\\test\\ideahadoop\\log\\purifylog\\driverlog.txt"));
        String MovieID = "0000000";
        String DBip = "ip";
        String DBUser = "root";
        String DBPasswd = "";
        String UPTimeStampStr = "null";

        switch (args.length) {
            case (int) 5:
                UPTimeStampStr = args[4];
            case (int) 4:
                DBPasswd = args[3];
            case (int) 3:
                DBUser = args[2];
            case (int) 2:
                DBip = args[1];
            case (int) 1:
                MovieID = args[0];
        }

        Configuration conf = new Configuration();
        conf.set("ip", DBip);
        conf.set("user", DBUser);
        conf.set("passwd", DBPasswd);
        conf.set("movieID", MovieID);

        String dbUrl = "jdbc:mysql://" + DBip + ":3306/bigdata?serverTimezone=UTC&useSSL=false";

        logger.warn(dbUrl);

        DBConfiguration.configureDB(conf, "com.mysql.cj.jdbc.Driver", dbUrl,
                DBUser, DBPasswd);

        Job job;

        //Process data in an updated manner
        if (!"null".equals(UPTimeStampStr)) {

            //Query the latest date of last data processing
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://" + DBip + ":3306/bigdata?serverTimezone=UTC&useSSL=false"
                    , DBUser, DBPasswd);
            PreparedStatement pst = conn.prepareStatement("SELECT * FROM bigdata.movie_rate_data where `id`=? order by `date` desc limit 1");
            pst.setString(1, MovieID);
            ResultSet rs = pst.executeQuery();
            rs.next();
            String oldDate = rs.getDate("date").toString();
            int dayCommentsNum = rs.getInt("day_comments_num");
            double dayAveRating = rs.getDouble("day_aver_rating");
            long accumulateCommentsNum = rs.getLong("accumulate_comments_num");
            double accumulateRatingSum = rs.getDouble("accumulate_rating_sum");
            pst.close();
            String newDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date(Long.parseLong(UPTimeStampStr)));

            /* *updated from the latest date of last processing.   parameters:
             * movieID
             * newDate
             * accumulateRatingsSum
             * accumulateCommentsNum
             * dayAveRating
             * dayCommentsNum
             *
             * and Delete old data
             */
            if (oldDate.equals(newDate)) {
                //set jobName && pass in parameters
                conf.set("newDate", newDate);
                conf.set("accumulateRatingSum", String.valueOf(accumulateRatingSum));
                conf.set("accumulateCommentsNum", String.valueOf(accumulateCommentsNum));
                conf.set("dayAveRating", String.valueOf(dayAveRating));
                conf.set("dayCommentsNum", String.valueOf(dayCommentsNum));
                job = Job.getInstance(conf, MovieID + " update ratings and starts from old date");
                //delete related old data
                PreparedStatement pr = conn.prepareStatement("delete from bigdata.movie_rate_data where `id`=? and `date`=?");
                pr.setString(1, MovieID);
                pr.setString(2, newDate);
                pr.executeUpdate();
                pr.close();
            }
            /* *Update from new date.   parameters::
             * movieID
             * accumulateRatingsSum
             * accumulateCommentsNum
             */
            else {
                conf.set("accumulateRatingSum", String.valueOf(accumulateRatingSum));
                conf.set("accumulateCommentsNum", String.valueOf(accumulateCommentsNum));
                job = Job.getInstance(conf, MovieID + " update ratings and starts from new date");
            }
            job.setReducerClass(PurifyReducer4Update.class);
            rs.close();
            conn.close();
        }
        //Process data in a fully processed manner
        else {
            job = Job.getInstance(conf, MovieID);
            job.setReducerClass(PurifyReducer.class);
        }

        job.setJarByClass(PurifyDriver.class);
        job.setMapperClass(PurifyMapper.class);

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(OriginalRatingBean.class);
        job.setOutputKeyClass(PureRatingBean.class);
        job.setOutputValueClass(PureStarsBean.class);

        job.setOutputFormatClass(PurifyOutPutFormat.class);

        String p = "/input/" + MovieID + "/*.csv";
        FileInputFormat.setInputPaths(job, new Path(p));
        FileOutputFormat.setOutputPath(job, new Path("/output/" + MovieID + "/rate_success_mark"));

        //for test
//        FileInputFormat.setInputPaths(job,new Path("D:\\test\\ideahadoop\\input\\"+MovieID+".txt"));
//        FileOutputFormat.setOutputPath(job,new Path("D:\\test\\ideahadoop\\output\\"+MovieID+"\\rate_success_mark"));

        boolean result = job.waitForCompletion(true);
        System.exit(result ? 0 : 1);
    }
}

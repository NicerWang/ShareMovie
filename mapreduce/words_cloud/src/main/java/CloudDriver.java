import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import util.CloudOutPutFormat;

import java.io.IOException;


/**
 * args:  movieID,ip,user,passwd
 */
public class CloudDriver {
    public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {

        String MovieID = "7777777";
        String DBip = "";
        String DBUser = "root";
        String DBPasswd = "";
        switch (args.length) {
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
        Job job = Job.getInstance(conf, MovieID + "::" + DBip + "::" + DBUser + "::" + DBPasswd + "::_c");

        job.setJarByClass(CloudDriver.class);
        job.setMapperClass(CloudMapper.class);
        job.setReducerClass(CloudReducer.class);

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(LongWritable.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(LongWritable.class);
        job.setOutputFormatClass(CloudOutPutFormat.class);

        FileInputFormat.setInputPaths(job, new Path("/input/" + MovieID + "_content"));
        FileOutputFormat.setOutputPath(job, new Path("/output/" + MovieID + "/content_success_mark"));

        //forWindowsTest
//        FileInputFormat.setInputPaths(job,new Path("D:\\test\\ideahadoop\\input\\"+MovieID+"_content"+"\\*"));
//        FileOutputFormat.setOutputPath(job,new Path("D:\\test\\ideahadoop\\output\\"+MovieID+"\\content_success_mark"));

        boolean result = job.waitForCompletion(true);
        System.exit(result ? 0 : 1);
    }
}

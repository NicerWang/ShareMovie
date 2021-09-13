package util;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.RecordWriter;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

public class CloudOutPutFormat extends FileOutputFormat<Text, LongWritable> {
    @Override
    public RecordWriter<Text, LongWritable> getRecordWriter(TaskAttemptContext taskAttemptContext) throws IOException, InterruptedException {
        CloudRecordWriter crw = new CloudRecordWriter(taskAttemptContext);
        return crw;
    }
}

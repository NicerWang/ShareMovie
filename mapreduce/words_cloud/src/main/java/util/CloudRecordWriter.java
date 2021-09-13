package util;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.RecordWriter;
import org.apache.hadoop.mapreduce.TaskAttemptContext;

import java.io.IOException;

public class CloudRecordWriter extends RecordWriter<Text, LongWritable> {

    private final DBTool dbTool = new DBTool();
    private final String MovieID;

    public CloudRecordWriter(TaskAttemptContext taskAttemptContext) {
        String[] args = taskAttemptContext.getJobName().split("::");
        MovieID = args[0];
        DBTool.init(args[1], args[2], args[3]);
    }

    @Override
    public void write(Text text, LongWritable longWritable) throws IOException, InterruptedException {
        dbTool.WriteInfo(MovieID, text.toString(), longWritable.get());
    }

    @Override
    public void close(TaskAttemptContext taskAttemptContext) throws IOException, InterruptedException {
        DBTool.close();
    }
}

package com.test.mapreduce.purifydata.util;

import com.test.mapreduce.purifydata.entity.PureRatingBean;
import com.test.mapreduce.purifydata.entity.PureStarsBean;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.mapreduce.RecordWriter;
import org.apache.hadoop.mapreduce.TaskAttemptContext;

public class PurifyRecordWriter extends RecordWriter<PureRatingBean, PureStarsBean> {

    private final DBTool dbTool = new DBTool();

    public PurifyRecordWriter(TaskAttemptContext context) {
        Configuration conf = context.getConfiguration();
        DBTool.init(conf.get("movieID"), conf.get("ip"), conf.get("user"), conf.get("passwd"));
    }

    @Override
    public void write(PureRatingBean pureRatingBean, PureStarsBean pureStarsBean) {
        dbTool.WriteInfo(pureRatingBean, pureStarsBean);
    }

    @Override
    public void close(TaskAttemptContext taskAttemptContext) {
        DBTool.close();
    }
}

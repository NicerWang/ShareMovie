package com.test.mapreduce.purifydata.util;

import com.test.mapreduce.purifydata.entity.PureRatingBean;
import com.test.mapreduce.purifydata.entity.PureStarsBean;
import org.apache.hadoop.mapreduce.RecordWriter;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class PurifyOutPutFormat extends FileOutputFormat<PureRatingBean, PureStarsBean> {
    @Override
    public RecordWriter<PureRatingBean, PureStarsBean> getRecordWriter(TaskAttemptContext context) {
        return new PurifyRecordWriter(context);
    }
}

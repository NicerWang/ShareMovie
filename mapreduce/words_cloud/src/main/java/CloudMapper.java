import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.common.Term;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.util.List;

public class CloudMapper extends Mapper<LongWritable, Text, Text, LongWritable> {
    private final Text val = new Text();
    private final LongWritable one = new LongWritable(1);

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String content = value.toString();
        List<Term> keywordList = HanLP.segment(content);
        for (Term term : keywordList) {
            String type = term.nature.toString();
            if (!type.startsWith("w") && !type.startsWith("m") && !type.startsWith("q") && !type.startsWith("p") && !type.startsWith("u") && !type.startsWith("r") && !type.startsWith("c") && !type.startsWith("e") && !type.startsWith("y") && !type.startsWith("d") && !type.startsWith("nx")) {
                if (term.word.length() > 1 && !term.word.contains("电影") && !term.word.contains("喜欢")) {
                    val.set(term.word);
                    context.write(val, one);
                }
            }
        }
    }
}

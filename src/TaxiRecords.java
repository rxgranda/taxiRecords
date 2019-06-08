import java.io.IOException;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.TreeSet;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.chain.ChainMapper;
import org.apache.hadoop.mapreduce.lib.chain.ChainReducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.KeyValueTextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

public class TaxiRecords {

    /* --- subsection 1.1 and 1.2 ------------------------------------------ */
    public class TaxiDriverMapper
            extends Mapper<LongWritable, Text, Text, IntWritable> {
        private static final int MISSING = 9999;
        private static final String EMPTY_STATUS = "'E'";
        private static final String CLIENT_STATUS = "'M'";

        @Override
        public void map(LongWritable key, Text value, Context context)
                throws IOException, InterruptedException {

            String[] info = value.toString().split("\t");
            //706,'2010-02-28 23:46:08',37.66721,-122.41029,'E','2010-03-01 04:02:28',37.6634,-122.43123,'E'
            if (info.length>=9) {
                String statusStart = info[1];
                String statusEnd = info[info.length - 1];
                if(statusStart.equals(EMPTY_STATUS)&& statusEnd.equals(EMPTY_STATUS)){
                    System.out.println("[Skip empty track]");
                }

                int id = Integer.parseInt(info[0]);
                String dateStart = info[1];
                String latStart = info[2];
                String longStart = info[3];

                String dateEnd = info[info.length - 4];
                String latEnd = info[info.length - 3];
                String longEnd = info[info.length - 2];

            }else{
                System.out.println("Invalid size fields: "+value.toString());
            }


//            String year = line.substring(15, 19);
//            int airTemperature;
//            if (line.charAt(87) == '+') { // parseInt doesn't like leading plus signs
//                airTemperature = Integer.parseInt(line.substring(88, 92));
//            } else {
//                airTemperature = Integer.parseInt(line.substring(87, 92));
//            }
//            String quality = line.substring(92, 93);
//            if (airTemperature != MISSING && quality.matches("[01459]")) {
//                context.write(new Text(year), new IntWritable(airTemperature));
//            }
        }
    }

    public class TaxiDriverReducer
            extends Reducer<Text, IntWritable, Text, IntWritable> {

        @Override
        public void reduce(Text key, Iterable<IntWritable> values, Context context)
                throws IOException, InterruptedException {

            int maxValue = Integer.MIN_VALUE;
            for (IntWritable value : values) {
                maxValue = Math.max(maxValue, value.get());
            }
            context.write(key, new IntWritable(maxValue));
        }
    }

    public static void main(String[] args) throws Exception {

        if (args.length != 2) {
            System.err.println("Usage: We need <input path> <output path>");
            System.exit(-1);
        }

        Path input = new Path(args[0]);
        Path output1 = new Path(args[1], "pass1");

        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "word count");
        job.setJarByClass(TaxiRecords.class);

        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        job.setMapperClass(TaxiDriverMapper.class);
        job.setReducerClass(TaxiDriverReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        System.exit(job.waitForCompletion(true) ? 0 : 1);


        // subsection 1.1 - first map reduce job
//        Job wordcountJob = runWordCount(input, output1);
//        if (!wordcountJob.waitForCompletion(true)) {
//            System.exit(1);
//        }


            }
}

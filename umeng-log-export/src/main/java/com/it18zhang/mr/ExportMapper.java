package com.it18zhang.mr;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class ExportMapper extends Mapper<LongWritable , Text, ExportDBWritable, NullWritable>{

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

        String[] arr = value.toString().split(",");
        String appid = arr[0];
        String statRes = arr[1];

        ExportDBWritable edb = new ExportDBWritable();
        edb.setAppid(appid);
        edb.setStatRes(statRes);

        context.write(edb,NullWritable.get());


    }
}

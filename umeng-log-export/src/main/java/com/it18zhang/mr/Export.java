package com.it18zhang.mr;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.db.DBConfiguration;
import org.apache.hadoop.mapreduce.lib.db.DBInputFormat;
import org.apache.hadoop.mapreduce.lib.db.DBOutputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Export {
    public static void main(String[] args) throws Exception {

        Job job = Job.getInstance();
        Configuration conf = job.getConfiguration();

        // mapreduce.job.jar
        job.setJarByClass(Export.class);

        // mapreduce.job.name
        job.setJobName("Export");

        String table = prepareTable("com.mysql.jdbc.Driver", "jdbc:mysql://localhost:3306/qst", "root", "root");

        // 设置输入输出路径
        DBConfiguration.configureDB(conf, "com.mysql.jdbc.Driver", "jdbc:mysql://localhost:3306/qst", "root", "root");
        FileInputFormat.addInputPath(job, new Path(args[0]));

        DBInputFormat.setInput(job, ExportDBWritable.class, "select * from text", "select count(*) from text");
        DBOutputFormat.setOutput(job, table, args[1], args[2]);

        FileSystem fs = FileSystem.get(conf);
        if (fs.exists(new Path(args[0]))) {
            fs.delete(new Path(args[0]), true);
        }

        // 设置输出kv
        // mapreduce.job.output.key.class
        // mapreduce.job.output.value.class
        job.setMapOutputKeyClass(ExportDBWritable.class);
        job.setMapOutputValueClass(NullWritable.class);


        // 指定map和reduce的类
        // mapreduce.job.map.class
        // mapreduce.job.reduce.class
        job.setMapperClass(ExportMapper.class);

        job.waitForCompletion(true);
    }

    private static String prepareTable(String driver,String url, String username, String password) {
        try {
            //池化数据源
            ComboPooledDataSource ds = new ComboPooledDataSource();
            ds.setDriverClass(driver);
            ds.setJdbcUrl(url);
            ds.setUser(username);
            ds.setPassword(password);
            Connection conn = ds.getConnection() ;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd") ;
            String str = sdf.format(new Date()) ;
            String table = "stat_res_" + str ;
            String sql = "create table if not exists " + table + "(appid varchar(20) primary key , res varchar(20))" ;
            conn.createStatement().execute(sql);
            conn.close();
            return table ;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null ;
    }

}

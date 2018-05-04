package com.it18zhang.mr;

import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.mapreduce.lib.db.DBWritable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ExportDBWritable implements DBWritable,WritableComparable<ExportDBWritable> {

    private String appid;
    private String statRes;

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getStatRes() {
        return statRes;
    }

    public void setStatRes(String statRes) {
        this.statRes = statRes;
    }

    public int compareTo(ExportDBWritable o) {
        return 0;
    }

    public void write(DataOutput out) throws IOException {
        out.writeUTF(appid);
        out.writeUTF(statRes);
    }

    public void readFields(DataInput in) throws IOException {
        this.appid = in.readUTF();
        this.statRes = in.readUTF();

    }

    public void write(PreparedStatement statement) throws SQLException {
        statement.setString(1,appid);
        statement.setString(2,statRes);

    }

    public void readFields(ResultSet resultSet) throws SQLException {
        appid = resultSet.getString(1);
        statRes = resultSet.getString(2);
    }
}

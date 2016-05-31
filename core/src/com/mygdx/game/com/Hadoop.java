package com.mygdx.game.com;

import com.badlogic.gdx.Gdx;
import com.mygdx.game.data.Data;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hdfs.DFSClient;
import org.apache.hadoop.util.Progressable;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


/**
 * Created by nicolas on 18/05/2016.
 */
public class Hadoop {

    public static String TAG = "HADOOP";
    public static String GENETIC_DIRECTORY = "/genetique/";
    public static String HDFS_PATH = "hdfs://pc11.bigdata:9000";

    public static  String HIVE = "jdbc:hive://pc11.bigdata:9000/hive";
    public static String driverName = "org.apache.hive.jdbc.HiveDriver";
    public static String GENETIC_TABLE_NAME = "GENETIC";
    public static String HADOOP_USER_NAME = "hduser";
    public static String HADOOP_USER_PASSWORD = "setup";

    public static String readFile(String fileName){
        String content = "";
        try{
            Path pt=new Path(HDFS_PATH+GENETIC_DIRECTORY+fileName);
            FileSystem fs = FileSystem.get(new Configuration());
            BufferedReader br=new BufferedReader(new InputStreamReader(fs.open(pt)));
            String line;
            line=br.readLine();
            while (line != null){
                content+= line;
                System.out.println(line);
                line=br.readLine();
            }

        }catch(Exception e){
            Gdx.app.log(TAG, "Error while reading the file "+fileName);
        }
        return content;
    }

    public static void saveFile(String fileName, String content) throws URISyntaxException, IOException {
        Configuration configuration = new Configuration();
        FileSystem hdfs = FileSystem.get(new URI(HDFS_PATH), configuration);
        Path file = new Path(HDFS_PATH+GENETIC_DIRECTORY+fileName);
        if(hdfs.exists(file))
            hdfs.delete(file, true);
        OutputStream os = hdfs.create(file,
                new Progressable() {
                    @Override
                    public void progress() {
                        Gdx.app.log(TAG, "...upload in progress ");
                    }
                });
        BufferedWriter br = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
        br.write(content);
        br.close();
        hdfs.close();
    }

    public static void saveFile(Path src, String fileName) throws IOException, URISyntaxException {
        if(Data.HADOOP) {
            Gdx.app.log(TAG, "Save file : [" + src.getName() + "] on Hadoop [" + fileName + "]");
            Configuration conf = new Configuration();
            conf.set("fs.defaultFS", HDFS_PATH);
            FileSystem fs = FileSystem.get(conf);
            Path dst = new Path(HDFS_PATH + GENETIC_DIRECTORY + fileName);
            fs.copyFromLocalFile(src, dst);
        }else{
            Gdx.app.log(TAG, "Can't save file on Hadoop -> Data.hadoop = false");
        }
    }

    public static void createGeneticTable() throws ClassNotFoundException, SQLException {
        Class.forName(driverName);
        System.out.println(TAG+": Connect to HIVE database : "+HIVE+" with user "+HADOOP_USER_NAME+", password "+HADOOP_USER_PASSWORD);
        Connection con = HADOOP_USER_NAME.isEmpty() ? DriverManager.getConnection(HIVE) : DriverManager.getConnection(HIVE, HADOOP_USER_NAME, HADOOP_USER_PASSWORD);
        Statement stmt = con.createStatement();
        System.out.println(TAG+": Connection successful-------------------------------");
        String query = "CREATE TABLE IF NOT EXISTS "+GENETIC_TABLE_NAME
                +" (filename String, name String," +
                "generation String, dateG String," +
                "scoreG int, scoreA int," +
                "scoreH int, scoreP int) " +
                "COMMENT 'Genetic AI details' " +
                "ROW FORMAT DELIMITED " +
                "FIELDS TERMINATED BY '\\t' " +
                "LINES TERMINATED BY '\\n' " +
                "STORED AS TEXTFILE";
        System.out.println(TAG+": Execute query ["+query+"]");
        stmt.executeQuery(query);
        System.out.println(TAG + ": Table " + GENETIC_TABLE_NAME + " created");
        con.close();
    }

    public static void saveGeneticDataOnHive(String name, String generation, String date, int scoreG, int scoreA, int scoreH, int scoreP) throws SQLException {
        //create the file with data for hive
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter("genetic_tmp.txt", false));
            out.write(name+generation+"\\t"+name+"\\t"+generation+"\\t"+date+"\\"+scoreG+"\\t"+scoreA+"\\t"+scoreH+"\\t"+scoreP+"\\n");
            Gdx.app.log(TAG, "Write the tmp file");
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            Class.forName(driverName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Connection con = HADOOP_USER_NAME.isEmpty() ? DriverManager.getConnection(HIVE) : DriverManager.getConnection(HIVE, HADOOP_USER_NAME, HADOOP_USER_PASSWORD);
        Statement stmt = con.createStatement();

        String query = "LOAD DATA LOCAL INPATH 'genetic_tmp.txt' OVERWRITE INTO TABLE "+GENETIC_TABLE_NAME+";";
        stmt.executeQuery(query);
        Gdx.app.log(TAG, "Load data from tmp file into "+GENETIC_TABLE_NAME+" successful");
        con.close();
    }
}

package com.mygdx.game.com;

import com.badlogic.gdx.Gdx;
import com.mygdx.game.data.Data;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hdfs.DFSClient;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.util.Progressable;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


/**
 * Created by nicolas on 18/05/2016.
 */
public class Hadoop {

    private static final String WEB_TABLE_NAME = "WEB";
    public static final String HADOOP_LOCAL_DIR = "/tmp/hadoop_tmp_local";
    private static final String LAST_TABLE_NAME = "last";
    public static String HADOOP_CONFIG_DIRECTORY = "/hadoop/hadoop-2.7.1/etc/hadoop/";
    public static String TAG = "HADOOP";
    public static String GENETIC_DIRECTORY = "genetique/";
    public static String GENETIC_TESTED = "pool_tester/";
    public static String HDFS_PATH = "hdfs://pc11.bigdata:9000/";
    public static String NAME_NODE = "pc11.bigdata";

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
    public static void copyFile(String src, String fileName) throws IOException, URISyntaxException {
        if(Data.HADOOP) {
            Gdx.app.log(TAG, "Save file : [" + src+ "] on Hadoop [" + fileName + "]");

            Configuration conf = new Configuration();
            conf.addResource(HADOOP_CONFIG_DIRECTORY+"core-site.xml");
            conf.addResource(HADOOP_CONFIG_DIRECTORY + "hdfs-site.xml");
            conf.addResource(HADOOP_CONFIG_DIRECTORY+"mapred-site.xml");
            Gdx.app.log(TAG, "Connectif to ---"+conf.get("fs.default.name"));

            InputStream in = new BufferedInputStream(new FileInputStream(src));

            FileSystem fs = FileSystem.get(URI.create(fileName), conf);
            OutputStream out = fs.create(new Path(fileName));

            IOUtils.copyBytes(in, out, 4096, true);

            Gdx.app.log(TAG, fileName+" copied to HDFS");
        }else{
            Gdx.app.log(TAG, "Can't save file on Hadoop -> Data.hadoop = false");
        }
    }
    public static void copyFile2(String srcPath, String fileName) throws IOException, URISyntaxException {
        if(Data.HADOOP) {
            Path src = new Path(srcPath);
            Gdx.app.log(TAG, "Save file : [" + src+ "] on Hadoop [" + fileName + "]");
            Configuration conf = new Configuration();
            //conf.set("fs.defaultFS", HDFS_PATH);
            conf.addResource(HADOOP_CONFIG_DIRECTORY+"core-site.xml");
            conf.addResource(HADOOP_CONFIG_DIRECTORY + "hdfs-site.xml");
            conf.addResource(HADOOP_CONFIG_DIRECTORY + "mapred-site.xml");
            //conf.set("fs.default.name", HDFS_PATH);
            FileSystem fs = FileSystem.get(conf);
            Path dst = new Path(fileName);
            if(!(fs.exists(dst.getParent())))
            {
                Gdx.app.error(TAG, "No Such destination exists :"+dst.getParent());
            }
            //fs.copyFromLocalFile(src, dst);
            fs.copyFromLocalFile(false, src, dst);
        }else{
            Gdx.app.log(TAG, "Can't save file on Hadoop -> Data.hadoop = false");
        }
    }

    public static void createGeneticTable() throws ClassNotFoundException, SQLException {
        if(!Data.HADOOP)
            return;
        Class.forName(driverName);
        System.out.println(TAG+": Connect to HIVE database : "+HIVE+" with user "+HADOOP_USER_NAME+", password "+HADOOP_USER_PASSWORD);
        Connection con = HADOOP_USER_NAME.isEmpty() ? DriverManager.getConnection(HIVE) : DriverManager.getConnection(HIVE, HADOOP_USER_NAME, HADOOP_USER_PASSWORD);
        Statement stmt = con.createStatement();
        System.out.println(TAG+": Connection successful-------------------------------");
        String query = "CREATE TABLE IF NOT EXISTS "+GENETIC_TABLE_NAME
                +" (filename String, name String," +
                "generation String, dateG DATE," +
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
        if(!Data.HADOOP)
            return;
        //create the file with data for hive
        String filePath = "/tmp/genetic_tmp.txt";
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(filePath, false));
            out.write(name+generation+"\t"+name+"\t"+generation+"\t"+date+"\t"+scoreG+"\t"+scoreA+"\t"+scoreH+"\t"+scoreP+"\n");
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

        String query = "LOAD DATA LOCAL INPATH '"+filePath+"'  INTO TABLE "+GENETIC_TABLE_NAME + "";
        stmt.execute(query);
        Gdx.app.log(TAG, "Load data from tmp file " + filePath + " into " + GENETIC_TABLE_NAME + " successful");
        con.close();
    }

    /**
     * Copy a file from hdfs
     * @param source
     * @param dest
     * @throws IOException
     */
    public void copyFromHdfs (String source, String dest) throws IOException {

        Configuration conf = new Configuration();
        conf.set("fs.defaultFS", HDFS_PATH);


        FileSystem fileSystem = FileSystem.get(conf);
        Path srcPath = new Path(source);

        Path dstPath = new Path(dest);
// Check if the file already exists
        if (!(fileSystem.exists(dstPath))) {
            System.out.println("No such destination " + dstPath);
            return;
        }

// Get the filename out of the file path
        String filename = source.substring(source.lastIndexOf('/') + 1, source.length());

        try{
            fileSystem.copyToLocalFile(srcPath, dstPath);
            System.out.println("File " + filename + "copied to " + dest);
        }catch(Exception e){
            System.err.println("Exception caught! :" + e);
        }finally{
            fileSystem.close();
        }
    }

    public static void saveGeneticDataOnHive(List<String> hiveList) throws SQLException {
        if(!Data.HADOOP)
            return;
        long begin = System.currentTimeMillis();
        //create the file with data for hive
        String filePath = "/tmp/genetic_tmp.txt";
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(filePath, false));
            for(String s : hiveList)
                    if(!s.isEmpty() || s != null)
                        out.write(s);
            //out.write(name+generation+"\t"+name+"\t"+generation+"\t"+date+"\t"+scoreG+"\t"+scoreA+"\t"+scoreH+"\t"+scoreP+"\n");
            Gdx.app.log(TAG, "Write the tmp file");
            out.close();

            Long time = System.currentTimeMillis();
            Process p = Runtime.getRuntime().exec("scp "+filePath+" "+HADOOP_USER_NAME+"@"+NAME_NODE+":"+filePath);
            p.waitFor();
            Gdx.app.log(TAG, "Copy file " + filePath + " on hiveserver in " + (System.currentTimeMillis()-time) + "ms");
            //scp file on pc11.bigdata
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            Class.forName(driverName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Connection con = HADOOP_USER_NAME.isEmpty() ? DriverManager.getConnection(HIVE) : DriverManager.getConnection(HIVE, HADOOP_USER_NAME, HADOOP_USER_PASSWORD);
        Statement stmt = con.createStatement();

        String query = "LOAD DATA LOCAL INPATH '"+filePath+"' INTO TABLE "+GENETIC_TABLE_NAME+"";
        stmt.execute(query);
        Gdx.app.log(TAG, "Load data from tmp file " + filePath + " into " + GENETIC_TABLE_NAME + " successful in "+(System.currentTimeMillis()-begin)+" ms");
        con.close();
    }

    public static void hadoopCopyFromLocal(String src, String dest) {
        if(!Data.HADOOP)
            return;
        long begin = System.currentTimeMillis();
        String com = "hadoop fs -copyFromLocal "+src+" "+dest;
        Gdx.app.log(TAG, "Save file : [" + src + "] on Hadoop [" + dest + "] with the comande line [" + com + "]");
        try {
            Process exe = Runtime.getRuntime().exec("hadoop fs -copyFromLocal "+src+" "+dest);
            exe.waitFor();
            Scanner sc = new Scanner(exe.getInputStream());
            while(sc.hasNext())
                Gdx.app.log(TAG, sc.nextLine());

            Gdx.app.log(TAG, "File copied in "+(System.currentTimeMillis()-begin)+" ms");
        } catch (IOException e) {
            Gdx.app.error(TAG, "hadoop fs failed");
        } catch (InterruptedException e) {
            Gdx.app.error(TAG, "hadoop fs failed");
        }

    }

    public static void hadoopPut(String src, String dest) {
        if(!Data.HADOOP)
            return;
        long begin = System.currentTimeMillis();
        String com = "hadoop fs -put "+src+" "+dest;
        Gdx.app.log(TAG, "Save file : [" + src + "] on Hadoop [" + dest + "] with the command line put["+com+"]" );
        try {
            Process exe = Runtime.getRuntime().exec(com);
            exe.waitFor();
            Gdx.app.log(TAG, "File(s) copied successfully in "+(System.currentTimeMillis()-begin)+" ms");
        } catch (Exception e) {
            Gdx.app.error(TAG, "hadoop fs -put failed ["+e.getMessage()+"]");
        }

    }
    public static void createWebTable()throws ClassNotFoundException, SQLException {
        Class.forName(driverName);
        System.out.println(TAG + ": Connect to HIVE database : " + HIVE + " with user " + HADOOP_USER_NAME + ", password " + HADOOP_USER_PASSWORD);
        Connection con = HADOOP_USER_NAME.isEmpty() ? DriverManager.getConnection(HIVE) : DriverManager.getConnection(HIVE, HADOOP_USER_NAME, HADOOP_USER_PASSWORD);
        Statement stmt = con.createStatement();
        System.out.println(TAG + ": Connection successful-------------------------------");
        String query = "CREATE TABLE IF NOT EXISTS " + WEB_TABLE_NAME + " (generation String, avgScore int, maxScore int, minScore int) " + "COMMENT 'Web info' " + "ROW FORMAT DELIMITED " + "FIELDS TERMINATED BY '\\t' " + "LINES TERMINATED BY '\\n' "
                + "STORED AS TEXTFILE";
        System.out.println(TAG + ": Execute query [" + query + "]");
        stmt.executeQuery(query);
        System.out.println(TAG + ": Table " + WEB_TABLE_NAME + " created");
        con.close();
    }
    public static void saveWebDataOnHive()throws SQLException{
        if(!Data.HADOOP)
            return;
        long begin = System.currentTimeMillis();
        try {
            Class.forName(driverName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Connection con = HADOOP_USER_NAME.isEmpty() ? DriverManager.getConnection(HIVE) : DriverManager.getConnection(HIVE, HADOOP_USER_NAME, HADOOP_USER_PASSWORD);
        Statement stmt = con.createStatement();

        String query = "INSERT INTO TABLE "+WEB_TABLE_NAME+" Select generation, avg(scoreg), max(scoreg), min(scoreg) from "+GENETIC_TABLE_NAME+" group by generation order by generation asc";
        Gdx.app.log(TAG, "Execute query ["+query+"]");
        stmt.execute(query);
        System.out.println(TAG + "Load data from " + GENETIC_TABLE_NAME + " into " + WEB_TABLE_NAME + " successful in " + (System.currentTimeMillis() - begin) + "ms");
        con.close();
    }

    public static void copyFromLocal(String s, String s1) {

    }

    public static void createLastTable() throws ClassNotFoundException, SQLException {
        if(!Data.HADOOP)
            return;
        Class.forName(driverName);
        System.out.println(TAG+": Connect to HIVE database : "+HIVE+" with user "+HADOOP_USER_NAME+", password "+HADOOP_USER_PASSWORD);
        Connection con = HADOOP_USER_NAME.isEmpty() ? DriverManager.getConnection(HIVE) : DriverManager.getConnection(HIVE, HADOOP_USER_NAME, HADOOP_USER_PASSWORD);
        Statement stmt = con.createStatement();
        System.out.println(TAG+": Connection successful-------------------------------");
        String query = "CREATE TABLE IF NOT EXISTS "+LAST_TABLE_NAME
                +" (filename String, name String," +
                "generation String, dateG DATE," +
                "scoreG int, scoreA int," +
                "scoreH int, scoreP int) " +
                "COMMENT 'Genetic AI details' " +
                "ROW FORMAT DELIMITED " +
                "FIELDS TERMINATED BY '\\t' " +
                "LINES TERMINATED BY '\\n' " +
                "STORED AS TEXTFILE";
        System.out.println(TAG+": Execute query ["+query+"]");
        stmt.executeQuery(query);
        System.out.println(TAG + ": Table " + LAST_TABLE_NAME + " created");
        con.close();
    }

    public static void saveLastDataOnHive()throws SQLException{
        if(!Data.HADOOP)
            return;
        long begin = System.currentTimeMillis();
        try {
            Class.forName(driverName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Connection con = HADOOP_USER_NAME.isEmpty() ? DriverManager.getConnection(HIVE) : DriverManager.getConnection(HIVE, HADOOP_USER_NAME, HADOOP_USER_PASSWORD);
        Statement stmt = con.createStatement();

        String query = "INSERT OVERWRITE TABLE "+LAST_TABLE_NAME+" Select distinct filename, name, generation, dateg, scoreg, scorea, scoreh, scorep from (select max(generation) as maxg from genetic) maxt join "+GENETIC_TABLE_NAME+" g on (g.generation = maxt.maxg)";
        Gdx.app.log(TAG, "Execute query ["+query+"]");
        stmt.execute(query);
        System.out.println(TAG + "Load data from " + GENETIC_TABLE_NAME + " into " + LAST_TABLE_NAME + " successful in " + (System.currentTimeMillis() - begin) + "ms");
        con.close();
    }

    public static ArrayList<String> getLastGenerationFilename() throws ClassNotFoundException, SQLException {
        Class.forName(driverName);
        System.out.println(TAG + ": Connect to HIVE database : " + HIVE + " with user " + HADOOP_USER_NAME + ", password " + HADOOP_USER_PASSWORD);
        Connection con = HADOOP_USER_NAME.isEmpty() ? DriverManager.getConnection(HIVE) : DriverManager.getConnection(HIVE, HADOOP_USER_NAME, HADOOP_USER_PASSWORD);
        Statement stmt = con.createStatement();
        System.out.println(TAG + ": Connection successful-------------------------------");
        String query = "select filename from " + LAST_TABLE_NAME +" order by scoreg desc";
        System.out.println(TAG + ": Execute query [" + query + "]");
        stmt.executeQuery(query);
        ResultSet set = stmt.getResultSet();
        ArrayList<String> array = new ArrayList<>();
        while(set.next())
        {
            array.add(set.getString(0));
        }
        con.close();
        return array;
    }

}

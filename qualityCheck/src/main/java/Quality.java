import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.ContentSummary;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.spark.ContextCleaner;
import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.storage.StorageLevel;
import scala.Tuple2;
import java.io.*;
import org.apache.spark.api.java.function.*;
import java.lang.String.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class Quality {
	private SparkConf sparkConf; 
	private JavaSparkContext ctx;									
        private Configuration conf;
	private JavaRDD<Tuple2<String, String>> dataRDD;
	private long blocksize;
	private String source;
	public Quality(String[] args) {	
		source = args[0];
		this.initQuality();
	}
	public void initQuality() {
		if (this.ctx == null) {
			this.sparkConf = new SparkConf().setAppName("Spark_Quality_Check");
			this.ctx = new JavaSparkContext(this.sparkConf);
		}
		else {
			this.sparkConf = this.ctx.getConf();
		}
		this.conf = this.ctx.hadoopConfiguration();
		this.blocksize = this.conf.getLong("dfs.blocksize", 134217728);
	}
	
	public void runCheck() {
		JavaRDD<String> rdd1_1 =ctx.textFile(this.source);
		JavaRDD<String> rdd1_2 = rdd1_1.filter(new map1());
		JavaPairRDD<String,String> prdd1_1 = rdd1_2.mapToPair(new map2());
		JavaPairRDD prdd1_2=prdd1_1.reduceByKey(new map3()).sortByKey();
		JavaRDD rdd_sum = prdd1_2.keys();
		JavaPairRDD prdd_sum = rdd_sum.mapToPair(new map4());		
		
		JavaRDD rdd2_1 = ctx.textFile(this.source+"/pTophat.sCufflinks.gene_exp.diff");
		JavaPairRDD prdd2_1 = rdd2_1.mapToPair(new map2_1());
		JavaPairRDD<String,Tuple2<String,String>> prdd2_2 = prdd_sum.leftOuterJoin(prdd2_1).sortByKey();

		JavaRDD rdd3_1 = ctx.textFile(this.source+"/sparkTophat.gene_exp.diff");
		JavaPairRDD prdd3_1 = rdd3_1.mapToPair(new map2_2());
		JavaPairRDD prdd3_2 = prdd2_2.leftOuterJoin(prdd3_1).sortByKey();

		JavaRDD rdd4_1 = ctx.textFile(this.source+"/serial.gene_exp.diff");
		JavaPairRDD prdd4_1 = rdd4_1.mapToPair(new map2_3());
		JavaPairRDD prdd4_2 = prdd3_2.leftOuterJoin(prdd4_1).sortByKey();
		
		prdd4_2.repartition(1).saveAsTextFile("file:///home/hadoop/prdd_sum");
		
		
	}
}


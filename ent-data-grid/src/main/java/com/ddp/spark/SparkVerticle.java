package com.ddp.spark;

import com.ddp.util.Runner;
import io.vertx.core.AbstractVerticle;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;

/**
 * Created by cloudera on 2/8/17.
 */
public class SparkVerticle extends AbstractVerticle{

    private JavaSparkContext javaSparkContext;

    public static void main(String argv[]){
        Runner.runExample(SparkVerticle.class);
    }

    public void start(){
        SparkConf conf = new SparkConf().setAppName("vert").setMaster("local[2]");
        JavaSparkContext sc = new JavaSparkContext(conf);
    }
}

package com.ddp.spark;

import com.ddp.access.UserClassParameter;
import com.ddp.access.UserClassRunner;
import com.ddp.cpybook.CopybookIngestion;
import com.ddp.access.CopybookIngestionParameter;
import com.ddp.userclass.RunUserClass;
import com.ddp.util.CustomMessage;
import com.ddp.util.CustomMessageCodec;
import com.ddp.util.Runner;
import com.google.gson.Gson;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SQLContext;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import parquet.Log;

import java.util.concurrent.TimeUnit;

/**
 * Created by cloudera on 2/8/17.
 */
public class SparkVerticle extends AbstractVerticle{

    private Logger LOG = LoggerFactory.getLogger("SimpleREST");

    private SparkSession spark;

    private Gson gson = new Gson();

    private Object createSparkSession() {
        SparkConf conf = new SparkConf();
        LOG.info("------ Create new SparkContext {} -------", config().getString("spark.master"));
        String execUri = System.getenv("SPARK_EXECUTOR_URI");
        conf.setAppName(config().getString("spark.appname"));

        if (execUri != null) {
            conf.set("spark.executor.uri", execUri);
        }

        if (System.getenv("SPARK_HOME") != null) {
            conf.setSparkHome(System.getenv("SPARK_HOME"));
        }

        conf.set("spark.scheduler.mode", "FAIR");
        conf.setMaster(config().getString("spark.master"));
            conf.set("master", "yarn");
            conf.set("spark.submit.deployMode", "client");
            conf.set("spark.executor.uri", "local:/usr/lib/spark2/spark-archive.zip");

        SparkSession sparkSession = SparkSession
                .builder()
                .config(conf)
                .getOrCreate();

        return sparkSession;
    }

    private JavaSparkContext createSparkContext(){
        SparkConf sparkConf = new SparkConf();

        // Only for tests, really
       sparkConf.setMaster(config().getString("spark.master"));
       sparkConf.setSparkHome("/usr/lib/spark2");
        // Only for tests, really
       sparkConf.setAppName(config().getString("spark.appname"));
       sparkConf.set("spark.yarn.dist.jars", "/usr/lib/spark2/jars/hadoop-yarn-common-2.6.4.jar,/usr/lib/spark2/jars/hadoop-yarn-server-common-2.6.4.jar,/usr/lib/spark2/jars/hadoop-yarn-server-web-proxy-2.6.4.jar,/usr/lib/spark2/jars/hadoop-yarn-client-2.6.4.jar,/usr/lib/spark2/jars/hadoop-yarn-api-2.6.4.jar");

        sparkConf.setIfMissing(
                "spark.streaming.gracefulStopTimeout",
                Long.toString(TimeUnit.MILLISECONDS.convert(60, TimeUnit.SECONDS)));
        sparkConf.setIfMissing("spark.cleaner.ttl", Integer.toString(20 * 60));


        long generationIntervalMS =
                TimeUnit.MILLISECONDS.convert(60, TimeUnit.SECONDS);

        JavaSparkContext jsc = JavaSparkContext.fromSparkContext(SparkContext.getOrCreate(sparkConf));
        return jsc;
        //return new JavaStreamingContext(jsc, new Duration(generationIntervalMS));
    }

    public static void main(String argv[]){
        Runner.runExample(SparkVerticle.class);
    }

    public void start(){
        //SparkConf conf = new SparkConf().setMaster(config().getString("spark.master")).setAppName(config().getString("spark.appname"));

        SparkSession s = (SparkSession) createSparkSession();
        JavaSparkContext jsc = createSparkContext();

        spark = SparkSession
                .builder()
                .master(config().getString("spark.master"))
                .appName(config().getString("spark.appname"))
                .enableHiveSupport()
                .getOrCreate();

        spark.conf().set(
                "spark.streaming.gracefulStopTimeout",
                Long.toString(TimeUnit.MILLISECONDS.convert(60, TimeUnit.SECONDS)));
        spark.conf().set("spark.cleaner.ttl", Integer.toString(20 * 60));

        EventBus eventBus = getVertx().eventBus();

        eventBus.registerDefaultCodec(CustomMessage.class, new CustomMessageCodec());

        eventBus.consumer(config().getString("eventbus.spark"), message -> {
            CustomMessage customMessage = (CustomMessage) message.body();

            System.out.println("Custom message received: "+customMessage.getSummary());
            handleEvent(customMessage);
            // Replying is same as publishing
            CustomMessage replyMessage = new CustomMessage(200, "a00000002", "Message sent from cluster receiver!");
            message.reply(replyMessage);
        });

    }

    private void handleEvent(CustomMessage msg){

        switch (msg.getStatusCode())
        {
            case 1: {
                CopybookIngestionParameter a = gson.fromJson(msg.getSummary(), CopybookIngestionParameter.class);

                vertx.executeBlocking(future -> {
                    // Call some blocking API that takes a significant amount of time to return
                    Object result = CopybookIngestion.apply(config().getString("hdfs.conf"), spark.sqlContext(), a).run();
                    future.complete(result);
                }, res -> {
                    System.out.println("The result is: " + res.result());
                });
                break;
            }

            case 2: {
                UserClassParameter a = gson.fromJson(msg.getSummary(), UserClassParameter.class);

                vertx.executeBlocking(future -> {
                    // Call some blocking API that takes a significant amount of time to return
                    //Object result = RunUserClass.apply(jclFactory, jcl, sqlContext, a).run();
                    //future.complete(result);
                }, res -> {
                    System.out.println("The result is: " + res.result());
                });
                break;
            }

        }


    }
}

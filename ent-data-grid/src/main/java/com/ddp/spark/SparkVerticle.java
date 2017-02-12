package com.ddp.spark;

import com.ddp.cpybook.CopybookIngestion;
import com.ddp.cpybook.CopybookIngestionParameter;
import com.ddp.util.CustomMessage;
import com.ddp.util.CustomMessageCodec;
import com.ddp.util.Runner;
import com.google.gson.Gson;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SQLContext;
import org.glassfish.jersey.internal.inject.Custom;

/**
 * Created by cloudera on 2/8/17.
 */
public class SparkVerticle extends AbstractVerticle{

    private SparkContext sparkContext;
    private SQLContext sqlContext;
    private Gson gson = new Gson();

    public static void main(String argv[]){
        Runner.runExample(SparkVerticle.class);
    }

    public void start(){
        SparkConf conf = new SparkConf().setMaster(config().getString("spark.master")).setAppName(config().getString("spark.appname"));
        sparkContext = new SparkContext(conf);
        sqlContext = new SQLContext(sparkContext);
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

        CopybookIngestionParameter a = gson.fromJson(msg.getSummary(), CopybookIngestionParameter.class);

        vertx.executeBlocking(future -> {
            // Call some blocking API that takes a significant amount of time to return
            Object result = CopybookIngestion.apply(config().getString("hdfs.conf"), sqlContext, a).run();
            future.complete(result);
        }, res -> {
            System.out.println("The result is: " + res.result());
        });
    }
}

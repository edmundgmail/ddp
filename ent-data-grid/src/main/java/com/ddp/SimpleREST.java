/*
 * Copyright 2014 Red Hat, Inc.
 *
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  and Apache License v2.0 which accompanies this distribution.
 *
 *  The Eclipse Public License is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 *  The Apache License v2.0 is available at
 *  http://www.opensource.org/licenses/apache2.0.php
 *
 *  You may elect to redistribute this code under either of these licenses.
 */

package com.ddp;

import com.ddp.metadata.DataBrowse;
import com.ddp.metadata.IDataBrowse;
import com.ddp.pojos.DataSourceDetail;
import com.ddp.pojos.RequestParam;
import com.google.gson.Gson;
import com.hazelcast.spi.impl.operationservice.impl.responses.Response;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.asyncsql.AsyncSQLClient;
import io.vertx.ext.asyncsql.MySQLClient;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.SQLConnection;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

import com.ddp.util.*;
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.ext.web.handler.StaticHandler;


/**
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class SimpleREST extends AbstractVerticle {

  // Convenience method so you can run it in your IDE
  public static void main(String[] args) {
    Runner.runExample(SimpleREST.class);
  }

  private Map<String, JsonObject> products = new HashMap<>();

  private IDataBrowse dataBrowse;
  private Logger LOGGER = LoggerFactory.getLogger("SimpleREST");
  JDBCClient client;

    @Override
  public void start() {

    setUpInitialData();


    JsonObject config = new JsonObject()
            .put("url", "jdbc:mysql://localhost:3306/metadata_ddp?user=root&password=password")
            .put("driver_class", "com.mysql.jdbc.Driver");

    client = JDBCClient.createShared(vertx, config);

      Router router = Router.router(vertx);
      router.route().handler(
                CorsHandler.create("*")
                        .allowedMethod(HttpMethod.GET)
                        .allowedMethod(HttpMethod.POST)
                        .allowedMethod(HttpMethod.OPTIONS)
                        .allowedHeader("X-PINGARUNER")
                        .allowedHeader("Content-Type")
              );
      //router.route().handler(BodyHandler.create());
      router.get("/hierarchy").handler(this::handleListHierarchy);
      router.get("/hierarchy/:sourceID").handler(this::handleListHierarchy);
      //router.route("/*").handler(StaticHandler.create());

      vertx.createHttpServer().requestHandler(router::accept).listen(9001);
  }

  private void handleListHierarchy(RoutingContext routingContext) {
    HttpServerResponse response = routingContext.response();

      Consumer<Integer> errorHandler = i-> response.setStatusCode(i).end();
      Consumer<String> responseHandler = s-> response.putHeader("content-type", "application/json").end(s);

      client.getConnection( res-> {
         if(res.succeeded()){
             String sourceID = routingContext.request().getParam("sourceID");
             LOGGER.info("sourceID=" + sourceID);

             if(sourceID==null)
                dataBrowse.listDataSourceDetails(res.result(), errorHandler, responseHandler);
             else
                 dataBrowse.listDataSourceDetails(res.result(), errorHandler, responseHandler);

         }
      });
  }

  private void setUpInitialData() {
      dataBrowse = new DataBrowse();
  }


}

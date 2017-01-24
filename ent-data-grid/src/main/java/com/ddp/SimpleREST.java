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
import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
  private Gson gson;
  private Logger LOGGER = LoggerFactory.getLogger("SimpleREST");

  @Override
  public void start() {

    setUpInitialData();
      gson = new Gson();

    final JDBCClient client = JDBCClient.createShared(vertx, new JsonObject()
              .put("url", "jdbc:mysql://localhost:3306/ddp_metastore?profileSQL=true")
              .put("user","root")
              .put("password","cloudera")
              .put("driver_class", "com.mysql.jdbc.Driver")
              .put("max_pool_size", 30));

    dataBrowse = new DataBrowse(client);

    Router router = Router.router(vertx);

    router.route().handler(CorsHandler.create("*")
            .allowedMethod(HttpMethod.GET)
            .allowedMethod(HttpMethod.POST)
            .allowedMethod(HttpMethod.OPTIONS)
            .allowedHeader("X-PINGARUNER")
            .allowedHeader("Content-Type"));

    //router.route().handler(BodyHandler.create());
    router.get("/products/:productID").handler(this::handleGetProduct);
    router.put("/products/:productID").handler(this::handleAddProduct);
    router.get("/products").handler(this::handleListProducts);


    router.get("/hierarchy").handler(this::handleListHierarchy);
      //router.route("/*").handler(StaticHandler.create());

    vertx.createHttpServer().requestHandler(router::accept).listen(9001);
  }

  private void handleListHierarchy(RoutingContext routingContext) {

      RequestParam param = new RequestParam(routingContext);
      LOGGER.info("param=" + param);

      JsonArray arr = new JsonArray();
      List<DataSourceDetail> details = dataBrowse.listDataSourceDetails(param);

      details.forEach( d->arr.add(new JsonObject(gson.toJson(d))));
      routingContext.response().putHeader("content-type", "application/json").end(arr.encodePrettily());
  }

  private void handleGetProduct(RoutingContext routingContext) {
    String productID = routingContext.request().getParam("productID");
    HttpServerResponse response = routingContext.response();
    if (productID == null) {
      sendError(400, response);
    } else {
      JsonObject product = products.get(productID);
      if (product == null) {
        sendError(404, response);
      } else {
        response.putHeader("content-type", "application/json").end(product.encodePrettily());
      }
    }
  }

  private void handleAddProduct(RoutingContext routingContext) {
    String productID = routingContext.request().getParam("productID");
    HttpServerResponse response = routingContext.response();
    if (productID == null) {
      sendError(400, response);
    } else {
      JsonObject product = routingContext.getBodyAsJson();
      if (product == null) {
        sendError(400, response);
      } else {
        products.put(productID, product);
        response.end();
      }
    }
  }

  private void handleListProducts(RoutingContext routingContext) {
    JsonArray arr = new JsonArray();
    products.forEach((k, v) -> arr.add(v));
    routingContext.response().putHeader("content-type", "application/json").end(arr.encodePrettily());
  }

  private void sendError(int statusCode, HttpServerResponse response) {
    response.setStatusCode(statusCode).end();
  }

  private void setUpInitialData() {
    addProduct(new JsonObject().put("id", "prod3568").put("name", "Egg Whisk").put("price", 3.99).put("weight", 150));
    addProduct(new JsonObject().put("id", "prod7340").put("name", "Tea Cosy").put("price", 5.99).put("weight", 100));
    addProduct(new JsonObject().put("id", "prod8643").put("name", "Spatula").put("price", 1.00).put("weight", 80));
  }

  private void addProduct(JsonObject product) {
    products.put(product.getString("id"), product);
  }
}

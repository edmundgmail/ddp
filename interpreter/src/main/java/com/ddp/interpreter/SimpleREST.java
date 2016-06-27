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

package com.ddp.interpreter;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

import java.util.HashMap;
import java.util.Map;

import com.ddp.interpreter.ScalaInterpreter;
import com.ddp.util.Runner;


/**
 * @author <a href="http://tfox.org">Tim Fox</a>
 */
public class SimpleREST extends AbstractVerticle {

  // Convenience method so you can run it in your IDE
  public static void main(String[] args) {
    Runner.runExample(SimpleREST.class);
  }

  private Map<String, JsonObject> products = new HashMap<>();
  private ScalaInterpreter scalaInterpreter = new ScalaInterpreter();
  

  @Override
  public void start() {

    setUpInitialData();
    compileScripts();
    
    Router router = Router.router(vertx);

    router.route().handler(BodyHandler.create());
    router.get("/products/:productID").handler(this::handleGetProduct);
    router.put("/products/:productID").handler(this::handleAddProduct);
    router.get("/products").handler(this::handleListProducts);
    router.get("/exec/:funcName").handler(this::handleExec);
    router.get("/test/:funcName").handler(this::handleTest);
    vertx.createHttpServer().requestHandler(router::accept).listen(8081);
  }
  
  private void handleTest(RoutingContext routingContext){
      com.my.ScalaTest1 t1 = new com.my.ScalaTest1();
      routingContext.response().putHeader("content-type", "application/json").end(t1.doIt());
          
  }
  
  private void handleExec(RoutingContext routingContext){
    String args[] = routingContext.request().getParam("funcName").split("&&");
    System.out.println("funcName=" + args[0]);
    System.out.println("param=" + (args.length>1? args[1] : "{}"));
    String json = (args.length>1) ? scalaInterpreter.runIt(args[0], args[1]) : scalaInterpreter.runIt(args[0]);
    routingContext.response().putHeader("content-type", "application/json").end(json);
    
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

  private void compileScripts(){
    //ScalaInterpreterExample.compile();
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

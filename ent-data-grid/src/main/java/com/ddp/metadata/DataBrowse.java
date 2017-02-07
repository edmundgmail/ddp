package com.ddp.metadata;

import com.ddp.pojos.DataSourceDetail;
import com.ddp.pojos.RequestParam;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.ResultSet;
import io.vertx.ext.sql.SQLConnection;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * Created by cloudera on 1/24/17.
 */
public class DataBrowse implements IDataBrowse{

    Logger LOGGER = LoggerFactory.getLogger(DataBrowse.class);

    public void listDataSourceDetails(SQLConnection conn, Consumer<Integer> errorHandler, Consumer<String> responseHandler){
                        conn.query("SELECT datasource_name FROM datasource", query -> {
                            if (query.failed()) {
                                errorHandler.accept(500);
                            } else {
                                if (query.result().getNumRows() == 0) {
                                    errorHandler.accept(403);
                                } else {

                                    //String s = query.result().getRows().stream().map(r->r.encode()).reduce("", (a,b)->a+b);

                                    responseHandler.accept(new JsonArray(query.result().getRows()).encode());
                                }
                            }
                        });
    }
}

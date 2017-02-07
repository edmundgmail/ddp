package com.ddp.metadata;

import com.ddp.pojos.DataSourceDetail;
import com.ddp.pojos.RequestParam;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.ResultSet;
import io.vertx.ext.sql.SQLConnection;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by cloudera on 1/24/17.
 */
public class DataBrowse implements IDataBrowse{

    Logger LOGGER = LoggerFactory.getLogger(DataBrowse.class);

    public void listDataSourceDetails(SQLConnection conn, HttpServerResponse response){
                        conn.query("SELECT datasource_name FROM datasource", query -> {
                            if (query.failed()) {
                                response.setStatusCode(500).end();
                            } else {
                                if (query.result().getNumRows() == 0) {
                                    response.setStatusCode(403).end();
                                } else {
                                    response.putHeader("content-type", "application/json").end(query.result().getRows().get(0).encode());
                                }
                            }
                        });
    }
}

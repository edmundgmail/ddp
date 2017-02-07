package com.ddp.metadata;

import com.ddp.pojos.DataSourceDetail;
import com.ddp.pojos.RequestParam;
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
    private JDBCClient jdbcClient;

    Logger LOGGER = LoggerFactory.getLogger(DataBrowse.class);

    public DataBrowse(JDBCClient client){
        this.jdbcClient = client;
    }

    public Optional<List<JsonObject>> listDataSourceDetails(RequestParam requestParam){
        try {
            jdbcClient.getConnection(res -> {
                if (res.succeeded()) {

                    SQLConnection connection = res.result();

                    connection.query("SELECT datasource_name FROM datasource", res2 -> {
                        if (res2.succeeded()) {
                            ResultSet rs = res2.result();
                             rs.getRows();
                        }
                        else
                            Optional.empty();
                    });
                } else {
                    // Failed to get connection - deal with it
                    LOGGER.info("can't get connecton");
                    Optional.empty();
                }
            });
        }
        catch(Exception e){
            e.printStackTrace();
            return Optional.empty();
        }
        //return Optional.empty();
    }
}

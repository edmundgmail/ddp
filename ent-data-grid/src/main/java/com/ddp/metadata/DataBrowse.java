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

/**
 * Created by cloudera on 1/24/17.
 */
public class DataBrowse implements IDataBrowse{
    private JDBCClient jdbcClient;

    Logger LOGGER = LoggerFactory.getLogger(DataBrowse.class);

    public DataBrowse(JDBCClient client){
        this.jdbcClient = client;
    }

    public List<DataSourceDetail> listDataSourceDetails(RequestParam requestParam){

        try {
            jdbcClient.getConnection(res -> {
                if (res.succeeded()) {

                    SQLConnection connection = res.result();

                    connection.query("SELECT * FROM view_hierarchy", res2 -> {
                        if (res2.succeeded()) {

                            ResultSet rs = res2.result();
                            // Do something with results
                        }
                    });
                } else {
                    // Failed to get connection - deal with it
                    LOGGER.info("can't get connecton");

                }
            });
        }
        catch(Exception e){
            e.printStackTrace();
        }
        DataSourceDetail detail = new DataSourceDetail();
        detail.setSourceName("test1");

        List<DataSourceDetail> l = new ArrayList<>();
        l.add(detail);

        return l;
    }
}

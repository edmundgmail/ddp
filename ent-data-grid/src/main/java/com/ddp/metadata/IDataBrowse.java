package com.ddp.metadata;

import com.ddp.pojos.DataSourceDetail;
import com.ddp.pojos.RequestParam;
import com.hazelcast.spi.impl.operationservice.impl.responses.Response;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.sql.SQLConnection;

import java.util.List;
import java.util.Optional;

/**
 * Created by cloudera on 1/24/17.
 */
public interface IDataBrowse {
    void listDataSourceDetails(SQLConnection conn, HttpServerResponse Response) ;
}

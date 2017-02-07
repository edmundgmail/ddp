package com.ddp.metadata;

import com.ddp.pojos.DataSourceDetail;
import com.ddp.pojos.RequestParam;
import io.vertx.core.json.JsonObject;

import java.util.List;
import java.util.Optional;

/**
 * Created by cloudera on 1/24/17.
 */
public interface IDataBrowse {
    Optional<List<JsonObject>> listDataSourceDetails(RequestParam requestParam) ;
}

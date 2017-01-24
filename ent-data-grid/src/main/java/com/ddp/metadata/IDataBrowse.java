package com.ddp.metadata;

import com.ddp.pojos.DataSourceDetail;
import com.ddp.pojos.RequestParam;

import java.util.List;

/**
 * Created by cloudera on 1/24/17.
 */
public interface IDataBrowse {
    List<DataSourceDetail> listDataSourceDetails(RequestParam requestParam) ;
}

package com.ddp.pojos;

import java.util.List;

/**
 * Created by cloudera on 1/24/17.
 */
public class DataSourceDetail {
    String sourceName;
    List<DataEntityDetail> entities;

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public List<DataEntityDetail> getEntities() {
        return entities;
    }

    public void setEntities(List<DataEntityDetail> entities) {
        this.entities = entities;
    }

    @Override
    public String toString() {
        return "DataSourceDetail{" +
                "sourceName='" + sourceName + '\'' +
                ", entities=" + entities +
                '}';
    }
}

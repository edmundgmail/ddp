package com.ddp.pojos;

import java.util.List;

/**
 * Created by cloudera on 1/24/17.
 */
public class DataEntityDetail {
    String entityName;
    List<DataFieldDetail> fieldDetails;

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public List<DataFieldDetail> getFieldDetails() {
        return fieldDetails;
    }

    public void setFieldDetails(List<DataFieldDetail> fieldDetails) {
        this.fieldDetails = fieldDetails;
    }

    @Override
    public String toString() {
        return "DataEntityDetail{" +
                "entityName='" + entityName + '\'' +
                ", fieldDetails=" + fieldDetails +
                '}';
    }
}

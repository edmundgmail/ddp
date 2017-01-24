package com.ddp.pojos;

import io.vertx.ext.web.RoutingContext;

/**
 * Created by cloudera on 1/24/17.
 */
public class RequestParam {
    int pageSize;
    int pageNum;

    public RequestParam(RoutingContext routingContext)
    {
        String pageNum = routingContext.request().getParam("pageNum");
        String pageSize = routingContext.request().getParam("pageSize");

        if(pageSize == null){
            pageSize = "50";
        }

        if(pageNum==null){
            pageNum = "0";
        }

        this.pageSize = Integer.parseInt(pageSize);
        this.pageNum = Integer.parseInt(pageNum);

    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    @Override
    public String toString() {
        return "requestBoby{" +
                "pageSize=" + pageSize +
                ", pageNum=" + pageNum +
                '}';
    }
}

package com.corp.detective.webservice;

public interface AirWebServiceHandler {
    void onDataArrived(Object result, boolean ok, long timestamp);
}

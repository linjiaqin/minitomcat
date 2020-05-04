package com.ljq.http;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MyHttpSession {
    protected Map<String, Object> attributes = new ConcurrentHashMap<>();

    public MyHttpSession(){ }

    public MyHttpSession(String session){
        this.session = session;
        this.Id = session;
    }

    public void setSession(String session){
        this.Id = this.session = session;
    }

    private String session;

    private String Id;

    public String getId(){
        return Id;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(String name, Object object) {
        attributes.put(name, object);
    }

    public Object getAttribute(String name){
        return attributes.get(name);
    }
}

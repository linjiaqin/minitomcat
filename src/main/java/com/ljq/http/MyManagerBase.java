package com.ljq.http;

import sun.management.VMManagement;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MyManagerBase {
    public static ConcurrentHashMap<String, MyHttpSession> sessions = new ConcurrentHashMap<>();

    public static ThreadLocal<String> session= new ThreadLocal<>();

    public static MyHttpSession createSession(){
        StringBuilder res = new StringBuilder("");
        double d = Math.random();
        final int rand = (int)(d*10000);

        final long time = System.currentTimeMillis();

        int jvmid = jvmPid();

        res.append(rand+"_"+time+"_"+jvmid);
        MyHttpSession httpSession = new MyHttpSession(res.toString());
        sessions.put(res.toString().trim(), httpSession);
        session.set(res.toString().trim());
        return httpSession;
    }

    public static MyHttpSession getSession(){
        String sessionId = session.get();
        if (session.get() == null || !sessions.containsKey(session.get())){
            createSession();
        }
        sessionId = session.get();
        //System.out.println(sessions.get(sessionId).getId());
        return sessions.get(sessionId);
    }

    public static final int jvmPid() {
        try {
            RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();
            Field jvm = runtime.getClass().getDeclaredField("jvm");
            jvm.setAccessible(true);
            VMManagement mgmt = (VMManagement) jvm.get(runtime);
            Method pidMethod = mgmt.getClass().getDeclaredMethod("getProcessId");
            pidMethod.setAccessible(true);
            int pid = (Integer) pidMethod.invoke(mgmt);
            return pid;
        } catch (Exception e) {
            return -1;
        }
    }

}

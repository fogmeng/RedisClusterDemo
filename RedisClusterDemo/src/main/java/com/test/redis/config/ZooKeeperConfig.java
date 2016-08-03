package com.test.redis.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

public class ZooKeeperConfig {

    private String path;

    private String nameSpace;

    private int sleepTimes;

    private int maxRetries;

    private String connectString;

    private String scheme;

    private String auth;

    private static Map<String, String> props = new HashMap<String, String>();

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getNameSpace() {
        return nameSpace;
    }

    public void setNameSpace(String nameSpace) {
        this.nameSpace = nameSpace;
    }

    public int getSleepTimes() {
        return sleepTimes;
    }

    public void setSleepTimes(int sleepTimes) {
        this.sleepTimes = sleepTimes;
    }

    public int getMaxRetries() {
        return maxRetries;
    }

    public void setMaxRetries(int maxRetries) {
        this.maxRetries = maxRetries;
    }

    public String getConnectString() {
        return connectString;
    }

    public void setConnectString(String connectString) {
        this.connectString = connectString;
    }

    public static Map<String, String> getProps() {
        return props;
    }

    public static String getProperty(String key) {
        return props.get(key);
    }

    public String getScheme() {
        return scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    public void init() throws Exception {
        ZooKeeper zk = new ZooKeeper(connectString, 2000, new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                System.out.println("Connecting to ZK.");
            }
        });
        zk.addAuthInfo(scheme, auth.getBytes());

        // Wait till connection is established.
        while (zk.getState() != ZooKeeper.States.CONNECTED) {
            Thread.sleep(30);
        }
        String str = "/" + nameSpace + path;
        List<String> list = zk.getChildren("/" + nameSpace + path, null);
        for (String p : list) {
            String value = new String(zk.getData(str + "/" + p, null, null));
            System.out.println(value);
            props.put(p, value);
        }
    }

    

}

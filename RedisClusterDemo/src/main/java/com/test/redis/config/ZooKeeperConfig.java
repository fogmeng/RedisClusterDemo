package com.test.redis.config;

import java.io.LineNumberReader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.data.Stat;

public class ZooKeeperConfig {

	private String path;

	private String nameSpace;

	private int sleepTimes;

	private int maxRetries;

	private String connectString;

	private static Map<String, String> props = new HashMap<String, String>();

	private CuratorFramework client;

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

	public CuratorFramework getClient() {
		return client;
	}

	public void init() throws Exception {
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(sleepTimes, maxRetries);
		client = CuratorFrameworkFactory.builder().connectString(connectString).retryPolicy(retryPolicy)
		        .namespace(nameSpace).build();
		client.start();

		Stat stat = client.checkExists().forPath(path);
		if (stat == null) {
			throw new RuntimeException("Path " + path + " does not exists.");
		}
		String cfg = new String(client.getData().forPath(path), "UTF-8");
		try (LineNumberReader reader = new LineNumberReader(new StringReader(cfg))) {
			String cfgLine = null;
			do {
				cfgLine = reader.readLine();
				if (StringUtils.isNotBlank(cfgLine) && !cfgLine.trim().startsWith("#")) {
					String[] cfgItem = StringUtils.split(cfgLine, "=");
					props.put(cfgItem[0], cfgItem[1]);
				} else {
					continue;
				}
			} while (cfgLine != null);
		}

	}

}

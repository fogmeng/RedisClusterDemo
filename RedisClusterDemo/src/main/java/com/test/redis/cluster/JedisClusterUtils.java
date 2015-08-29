package com.test.redis.cluster;

import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

public class JedisClusterUtils {

	private JedisCluster jc = null;

	private String hostAndPort = null;

	private int commandTimeout = 0;

	private int maxRedirections = 0;

	private JedisPoolConfig jedisPoolConfig = null;

	private JedisClusterUtils() {
	}

	public JedisCluster getJedisCluster() {
		return jc;
	}

	public void init() {
		Set<HostAndPort> hpSet = new HashSet<HostAndPort>();
		StringTokenizer st = new StringTokenizer(hostAndPort, ",");
		while (st.hasMoreElements()) {
			String hostAndPort = st.nextToken();
			int idx = hostAndPort.indexOf(":");
			String host = hostAndPort.substring(0, idx);
			int port = Integer.parseInt(hostAndPort.substring(idx + 1));
			HostAndPort hp = new HostAndPort(host, port);
			hpSet.add(hp);
		}
		jc = new JedisCluster(hpSet, commandTimeout, maxRedirections, jedisPoolConfig);
	}

	public String getHostAndPort() {
		return hostAndPort;
	}

	public void setHostAndPort(String hostAndPort) {
		this.hostAndPort = hostAndPort;
	}

	public int getCommandTimeout() {
		return commandTimeout;
	}

	public void setCommandTimeout(int commandTimeout) {
		this.commandTimeout = commandTimeout;
	}

	public int getMaxRedirections() {
		return maxRedirections;
	}

	public void setMaxRedirections(int maxRedirections) {
		this.maxRedirections = maxRedirections;
	}

	public String hset(String key, String value) {
		return jc.set(key, value);
	}

	public JedisPoolConfig getJedisPoolConfig() {
		return jedisPoolConfig;
	}

	public void setJedisPoolConfig(JedisPoolConfig jedisPoolConfig) {
		this.jedisPoolConfig = jedisPoolConfig;
	}

	public static void main(String[] args) {
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
		JedisClusterUtils utils = (JedisClusterUtils) ctx.getBean("jedisClusterUtils");
		JedisCluster jc = utils.getJedisCluster();

		for (int i = 0; i < 100; i++) {
			jc.set("key" + i, "value" + i);
		}

		for (int i = 0; i < 100; i++) {
			System.out.println(jc.get("key" + i));
		}
		ctx.close();
	}

}

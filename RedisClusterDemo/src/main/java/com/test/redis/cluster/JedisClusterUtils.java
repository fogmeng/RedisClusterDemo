package com.test.redis.cluster;

import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.zookeeper.server.auth.DigestAuthenticationProvider;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.test.redis.config.ZooKeeperConfig;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

public class JedisClusterUtils {

	private JedisCluster jc = null;

	public Long lpush(String key, String... string) {
		return jc.lpush(key, string);
	}

	public Long rpush(String key, String... string) {
		return jc.rpush(key, string);
	}

	public Long hset(String key, String field, String value) {
		return jc.hset(key, field, value);
	}

	public String hget(String key, String field) {
		return jc.hget(key, field);
	}

	public Map<String, String> hgetAll(String key) {
		return jc.hgetAll(key);
	}

	public List<String> blpop(int timeout, String key) {
		return jc.blpop(timeout, key);
	}

	public List<String> brpop(int timeout, String key) {
		return jc.brpop(timeout, key);
	}

	public String set(String key, String value) {
		return jc.set(key, value);
	}

	public String get(String key) {
		return jc.get(key);
	}

	public void init() {
		Set<HostAndPort> hpSet = new HashSet<HostAndPort>();
		String hostsAndPorts = ZooKeeperConfig.getProperty("redis.hostsAndPorts");
		StringTokenizer st = new StringTokenizer(hostsAndPorts, ",");
		while (st.hasMoreElements()) {
			String hostAndPort = st.nextToken();
			int idx = hostAndPort.indexOf(":");
			String host = hostAndPort.substring(0, idx);
			int port = Integer.parseInt(hostAndPort.substring(idx + 1));
			HostAndPort hp = new HostAndPort(host, port);
			hpSet.add(hp);
		}
		JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
		jedisPoolConfig.setMaxTotal(Integer.parseInt(ZooKeeperConfig.getProperty("redis.pool.maxTotal")));
		jedisPoolConfig.setMaxIdle(Integer.parseInt(ZooKeeperConfig.getProperty("redis.pool.maxIdle")));
		jedisPoolConfig.setMinIdle(Integer.parseInt(ZooKeeperConfig.getProperty("redis.pool.minIdle")));
		jedisPoolConfig.setTestOnBorrow(Boolean.parseBoolean(ZooKeeperConfig.getProperty("redis.pool.testOnBorrow")));
		jedisPoolConfig.setTestOnReturn(Boolean.parseBoolean(ZooKeeperConfig.getProperty("redis.pool.testOnReturn")));
		jedisPoolConfig.setTestWhileIdle(Boolean.parseBoolean(ZooKeeperConfig.getProperty("redis.pool.testWhileIdle")));
		jedisPoolConfig.setNumTestsPerEvictionRun(
		        Integer.parseInt(ZooKeeperConfig.getProperty("redis.pool.numTestsPerEvictionRun")));
		jedisPoolConfig.setTimeBetweenEvictionRunsMillis(
		        Long.parseLong(ZooKeeperConfig.getProperty("redis.pool.timeBetweenEvictionRunsMillis")));
		jedisPoolConfig.setMaxWaitMillis(Long.parseLong(ZooKeeperConfig.getProperty("redis.pool.maxWaitMillis")));

		int maxRedirections = Integer.parseInt(ZooKeeperConfig.getProperty("redis.default.max.redirections"));
		int commandTimeout = Integer.parseInt(ZooKeeperConfig.getProperty("redis.default.commandtimeout"));
		jc = new JedisCluster(hpSet, commandTimeout, maxRedirections, jedisPoolConfig);
	}

	public static void main(String[] args) throws Exception {
		test0();
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
		JedisClusterUtils utils = ctx.getBean(JedisClusterUtils.class);
		
		utils.test1();
		// utils.test2();
		// utils.test3();
		ctx.close();
	}
	
	public static void test0() throws NoSuchAlgorithmException {
		System.out.println(DigestAuthenticationProvider.generateDigest("admin:manager"));
	}

	public void test1() {
		for (int i = 0; i < 500; i++) {
			jc.set("key" + i, "value" + i);
		}

		for (int i = 0; i < 500; i++) {
			System.out.println(jc.get("key" + i));
		}
	}

	public void test2() {
		rpush("mylist", new String[] { "hello", "world" });
		List<String> list = brpop(30, "mylist");
		for (String s : list) {
			System.out.println(s);
		}
	}

	public void test3() {
		for (int i = 0; i < 20; i++) {
			hset("hsetkey", "field" + i, "value" + i);
		}
	}

}

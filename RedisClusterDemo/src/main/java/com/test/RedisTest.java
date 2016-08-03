package com.test;

import java.util.Date;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.google.gson.Gson;

public class RedisTest {

    public static void main(String[] args) {
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        StringRedisTemplate template = ctx.getBean(StringRedisTemplate.class);
        Integer cnt = args == null || args.length == 0 ? 100000 : Integer.parseInt(args[0]);
        template.setKeySerializer(null);
        System.out.println(template.type("key0").code());
        System.out.println(template.boundSetOps("key1").pop());
        System.out.println(new Date());
        testSet(template, cnt);
        testGet(template, cnt);
        testHSet(template, cnt);
        testHGet(template, cnt);
        System.out.println(new Date());
        ctx.close();

    }

    public static void testHSet(StringRedisTemplate template, int exeCnt) {
        System.out.println("method: hset");
        RedisObject ro = new RedisObject();
        ro.setGroupType("0");
        ro.setId(0L);
        ro.setName("aaaaaaaaaaaaaaaaaaaaaaaavvvvvvvvvvvvvvvvv");
        Gson gson = new Gson();
        String value = gson.toJson(ro);

        long min = 10;
        long max = 0;
        long sum = 0;
        for (int i = 0; i < exeCnt; i++) {
            long start = System.currentTimeMillis();
            template.boundHashOps("hkey").put(ro.getName(), value);
            long end = System.currentTimeMillis();
            System.out.println(i);
            long exeTime = end - start;
            if (exeTime > max) {
                max = exeTime;
            }
            if (exeTime < min) {
                min = exeTime;
            }
            sum += exeTime;
        }

        System.out.println("exceute counts: " + exeCnt);
        System.out.println("min: " + min + "ms, max: " + max + ", avg: " + sum * 1.0 / exeCnt + ", sum: " + sum + "ms");
    }

    public static void testSet(StringRedisTemplate template, int exeCnt) {
        System.out.println("method: set");
        long min = 10;
        long max = 0;
        long sum = 0;
        for (int i = 0; i < exeCnt; i++) {
            long start = System.currentTimeMillis();
            template.boundSetOps("key" + i).add("value" + i);
            long end = System.currentTimeMillis();
            System.out.println(i);
            long exeTime = end - start;
            if (exeTime > max) {
                max = exeTime;
            }
            if (exeTime < min) {
                min = exeTime;
            }
            sum += exeTime;
        }

        System.out.println("exceute counts: " + exeCnt);
        System.out.println("min: " + min + "ms, max: " + max + ", avg: " + sum * 1.0 / exeCnt + ", sum: " + sum + "ms");
    }

    public static void testHGet(StringRedisTemplate template, int exeCnt) {
        System.out.println("method: hget");
        RedisObject ro = new RedisObject();
        ro.setGroupType("0");
        ro.setId(0L);
        ro.setName("aaaaaaaaaaaaaaaaaaaaaaaavvvvvvvvvvvvvvvvv");

        long min = 10;
        long max = 0;
        long sum = 0;
        for (int i = 0; i < exeCnt; i++) {
            long start = System.currentTimeMillis();
            template.boundHashOps("hkey").get(ro.getName());
            long end = System.currentTimeMillis();
            System.out.println(i);
            long exeTime = end - start;
            if (exeTime > max) {
                max = exeTime;
            }
            if (exeTime < min) {
                min = exeTime;
            }
            sum += exeTime;
        }

        System.out.println("exceute counts: " + exeCnt);
        System.out.println("min: " + min + "ms, max: " + max + ", avg: " + sum * 1.0 / exeCnt + ", sum: " + sum + "ms");
    }

    public static void testGet(StringRedisTemplate template, int exeCnt) {
        System.out.println("method: get");
        long min = 10;
        long max = 0;
        long sum = 0;
        for (int i = 0; i < exeCnt; i++) {
            long start = System.currentTimeMillis();
            template.boundSetOps("key" + i).pop();
            long end = System.currentTimeMillis();
            System.out.println(i);
            long exeTime = end - start;
            if (exeTime > max) {
                max = exeTime;
            }
            if (exeTime < min) {
                min = exeTime;
            }
            sum += exeTime;
        }

        System.out.println("exceute counts: " + exeCnt);
        System.out
                .println("min: " + min + "ms, max: " + max + ", avg: " + sum * 1.0 / exeCnt + "ms, sum: " + sum + "ms");
    }

    public static class RedisObject {

        private Long id;

        private String name;

        private String groupType;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getGroupType() {
            return groupType;
        }

        public void setGroupType(String groupType) {
            this.groupType = groupType;
        }

    }

}

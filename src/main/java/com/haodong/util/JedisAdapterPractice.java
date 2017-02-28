package com.haodong.util;

import redis.clients.jedis.Jedis;

/**
 * Created by torch on 17-2-28.
 */
public class JedisAdapterPractice {
    public static void print(int index, Object obj) {
        System.out.println(String.format("%d,  %s", index, obj));
    }


    public static void main(String[] args) {
        //什么都不填，则链接6379端口
        Jedis jedis = new Jedis("redis://localhost:6379/9");
        jedis.flushDB();
        jedis.set("hello", "world");
        print(1, jedis.get("hello"));
        jedis.rename("hello", "newHello");
        print(2, jedis.get("newHello"));
        jedis.set("pv", "100");
        jedis.incr("pv");
        print(3, jedis.get("pv"));
        jedis.incrBy("pv", 5);
        jedis.decrBy("pv", 2);
        //练习list的使用
        String listName = "list";
        for (int i = 0; i < 10; i++) {
            jedis.lpush(listName, "a" + String.valueOf(i));
        }
        print(4, jedis.lrange(listName, 0, 3));//全都是闭区间
    }
}

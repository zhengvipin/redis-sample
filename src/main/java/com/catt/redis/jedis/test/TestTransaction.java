package com.catt.redis.jedis.test;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.List;

/**
 * @author zwp
 * @since 2019-12-29 13:19
 */
public class TestTransaction {
    public boolean transMethod() throws InterruptedException {
        Jedis jedis = new Jedis("192.168.211.211", 6379);

        System.out.println("********************数据初始化*******************");
        jedis.set("balance", "100");
        jedis.set("debt", "0");
        System.out.println("初始化完成.");

        // 可用余额
        int balance;
        // 欠额
        int debt;
        // 实刷额度
        int cost = 10;

        jedis.watch("balance");

        System.out.println("********************网络延迟*******************");
        Thread.sleep(7000);
        // 模拟加塞：此时在redis客户端运行：set balance 5
        System.out.println("网络恢复.");

        balance = Integer.parseInt(jedis.get("balance"));
        if (balance < cost) {
            jedis.unwatch();
            System.out.println("余额不足.");
            return false;
        } else {
            System.out.println("********************开启事务.*******************");
            Transaction transaction = jedis.multi();
            transaction.decrBy("balance", cost);
            transaction.incrBy("debt", cost);
            List<Object> exec = transaction.exec();
            if (exec == null) {
                System.out.println("发生了加塞操作，回滚事务.");
            }
            balance = Integer.parseInt(jedis.get("balance"));
            debt = Integer.parseInt(jedis.get("debt"));

            System.out.println("当前余额：" + balance + ".");
            System.out.println("负债：" + debt + ".");
            return true;
        }
    }

    /**
     * 通俗点讲，watch命令就是标记一个键，如果标记了一个键， 在提交事务前如果该键被别人修改过，那事务就会失败，这种情况通常可以在程序中
     * 重新再尝试一次。
     * 首先标记了键balance，然后检查余额是否足够，不足就取消标记，并不做扣减； 足够的话，就启动事务进行更新操作，
     * 如果在此期间键balance被其它人修改， 那在提交事务（执行exec）时就会报错， 程序中通常可以捕获这类错误再重新执行一次，直到成功。
     */
    public static void main(String[] args) throws InterruptedException {
        TestTransaction test = new TestTransaction();
        boolean result = test.transMethod();
        System.out.println("回调结果: " + result + ".");
    }
}

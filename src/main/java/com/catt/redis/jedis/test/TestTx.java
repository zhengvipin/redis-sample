package com.catt.redis.jedis.test;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.List;

/**
 * @author zwp
 * @since 2019-12-29 13:19
 */
public class TestTx {
    public boolean transMethod() throws InterruptedException {
        Jedis jedis = new Jedis("192.168.211.211", 6379);

        System.out.println("********************init*******************");
        jedis.set("balance", "100");
        jedis.set("debt", "0");
        System.out.println("init finish.");

        int balance;// 可用余额
        int debt;// 欠额
        int amtToSubtract = 10;// 实刷额度

        jedis.watch("balance");

        System.out.println("********************internet delay*******************");
        Thread.sleep(7000);
        // 模拟加塞：在redis客户端运行：set balance 5
        System.out.println("internet ok.");

        balance = Integer.parseInt(jedis.get("balance"));
        if (balance < amtToSubtract) {
            jedis.unwatch();
            System.out.println("modify");
            return false;
        } else {
            System.out.println("********************transaction*******************");
            Transaction transaction = jedis.multi();
            transaction.decrBy("balance", amtToSubtract);
            transaction.incrBy("debt", amtToSubtract);
            List<Object> exec = transaction.exec();
            if (exec == null) {
                System.out.println("发生了加塞操作，回滚事务.");
            }
            balance = Integer.parseInt(jedis.get("balance"));
            debt = Integer.parseInt(jedis.get("debt"));

            System.out.println("balance：" + balance);
            System.out.println("debt：" + debt);
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
        TestTx test = new TestTx();
        boolean retValue = test.transMethod();
        System.out.println("main retValue-------: " + retValue);
    }
}

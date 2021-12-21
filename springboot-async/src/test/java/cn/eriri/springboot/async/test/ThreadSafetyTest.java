package cn.eriri.springboot.async.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @description:
 * @author: tangshijie
 * @date: 2021/12/20
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ThreadSafetyTest {

    private final Logger logger = LoggerFactory.getLogger(ThreadSafetyTest.class);

    /**
     * 测试缓存导致的可见性问题
     * 1.期望的结果:20000；
     * 2.实际的结果:在10000-20000之间
     */
    @Test
    @SuppressWarnings("java:S2699")
    public void Test1() {
        try {
            long calc = Example.calc();
            logger.info(String.valueOf(calc));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 测试编译优化带来的有序性问题例子：利用双重检查创建单列对象
     * 1.期望的结果：能正常的创建单例模式
     * 2.可能发生的结果：空指针异常
     * 3.原因 jvm编译优化后的执行路径：1.分配内存地址M 2.将M的地址赋值给instance变量 3.最后在M上初始化Singleton对象
     *
     * 假设有两个线程 A，B 同时调用 getInstance 方法，线程 A 执行完步骤 2 后，发生了线程切换，切换到了线程 B ，
     * 如果此时线程 B 也执行 getInstance 方法，那么在线程那么线程 B 在执行第一个判断时会发现 instance != null ，所以直接返回 instance，
     * 而此时的 instance 是没有初始化过的，如果我们这个时候访问 instance 的成员变量就可能触发空指针异常。
     */
    @Test
    @SuppressWarnings("java:S2699")
    public void Test2() {
        Thread th1 = new Thread(Singleton::getInstance);
        Thread th2 = new Thread(Singleton::getInstance);
    }
}

class Example {

    private long count = 0;

    private void add10K() {
        int idx = 0;
        while (idx++ < 10000) {
            count += 1;
        }
    }

    public static long calc() throws InterruptedException {
        final Example example = new Example();
        // 创建两个线程，执行 add() 操作
        Thread th1 = new Thread(example::add10K);
        Thread th2 = new Thread(example::add10K);
        // 启动两个线程
        th1.start();
        th2.start();
        // 等待两个线程执行结束
        th1.join();
        th2.join();
        return example.count;
    }

}

class Singleton {

    static Singleton instance;

    static Singleton getInstance() {
        if (instance == null) {
            synchronized (Singleton.class) {
                if (instance == null)
                    instance = new Singleton();
            }
        }
        return instance;
    }
}
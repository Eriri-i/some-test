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
public class ThreadSafeTyTest {

    private final Logger logger = LoggerFactory.getLogger(ThreadSafeTyTest.class);

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

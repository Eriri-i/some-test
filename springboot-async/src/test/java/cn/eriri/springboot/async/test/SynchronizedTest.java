package cn.eriri.springboot.async.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @description:
 * @author: tangshijie
 * @date: 2021/12/21
 */
@SuppressWarnings({"java:S2187"})
public class SynchronizedTest {

    private final Logger log = LoggerFactory.getLogger(SynchronizedTest.class);
    volatile int a = 1;
    volatile int b = 1;

    public static void main(String[] args) {
        SynchronizedTest synchronizedTest = new SynchronizedTest();
        new Thread(synchronizedTest::add).start();
        new Thread(synchronizedTest::compare).start();
    }

    public synchronized void add() {
        log.info("add start");
        for (int i = 0; i < 10000; i++) {
            a++;
            b++;
        }
        log.info("add done");
    }

    public void compare() {
        log.info("compare start");
        for (int i = 0; i < 10000; i++) {
            //a始终等于b吗？
            if (a < b) {
                log.info("a:{},b:{},{}", a, b, a > b);
                //最后的a>b应该始终是false吗？
            }
        }
        log.info("compare done");
    }
}

package cn.eriri.springboot.async;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @description: 启动类
 * @author: tangshijie
 * @date: 2021/12/20
 */
@SpringBootApplication
@Configurable
public class AsyncApplication {

    public static void main(String[] args) {
        SpringApplication.run(AsyncApplication.class);
    }
}

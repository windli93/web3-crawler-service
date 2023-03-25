package cn.com.web3.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @Author hongjian.li
 * @Description
 * @Date 2023/3/25 13:53
 **/
@SpringBootApplication(
        scanBasePackages = {"cn.com.web3"},
        exclude = {RedisAutoConfiguration.class, RedisRepositoriesAutoConfiguration.class})
@EntityScan(basePackages = {"cn.com.web3"})
@EnableJpaRepositories(basePackages = {"cn.com.web3"})
public class Web3MainClass {
    public static void main(String[] args) {
        SpringApplication.run(Web3MainClass.class, args);
    }
}

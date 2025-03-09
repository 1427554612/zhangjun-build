package com.zhangjun_study.build;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class BuildApplication {

    public static void main(String[] args) {
        SpringApplication.run(BuildApplication.class,args);
    }

}

package com.zhangjun_study.build;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import java.util.Date;
import java.util.Iterator;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@ComponentScan(basePackages = "com.zhangjun_study")
public class BuildApplication {

    // {"name":"zhangjun","age":10},{"name":"zhangjun","age":"20"}
    public static void main(String[] args) {
        SpringApplication.run(BuildApplication.class,args);
    }

    @Bean
    public Date getDate() throws JsonProcessingException {
        Date date = new Date();
        ObjectMapper objectMapper = new ObjectMapper();
        System.out.println("data = " + date);
        return new Date();
    }

}

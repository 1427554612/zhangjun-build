package com.zhangjun_study.build.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@RequestMapping("/api/build")
@RestController
public class BuildController {
    private static Logger logger  = LoggerFactory.getLogger(BuildController.class);

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    ApplicationContext applicationContext;


    @GetMapping(value = "/save")
    public String testBlock(){
        for (String s : applicationContext.getBeanNamesForType(RedisTemplate.class)) {
            System.out.println("容器中的date对象为：" + s);
        }

        redisTemplate.opsForValue().set("zj_key_"+System.currentTimeMillis(),"zj_value_"+System.currentTimeMillis());
        return "ok";
    }

    /**
     * Jacoco测试
     * @return
     */
    @GetMapping(value = "/testJacoco/{i}/{j}")
    public boolean testJacoco(@PathVariable  Integer i ,@PathVariable Integer j){
        boolean flag = false;
        ObjectMapper objectMapper = new ObjectMapper();
        if (i!=0){
            if (i<10){
                i+=10;
            }
            else {
                i+=1;
            }
        }
        if (j==0){
            j+=10;
        }else {
            j+=1;
        }
        if (i+j == 20){
            return true;
        }
        return flag;
    }

    @GetMapping(value = "/jmeter/{index}")
    public String testJmeter(@PathVariable Integer index, HttpServletRequest request) throws Exception {
        if(index==1)logger.info(request.getLocalAddr() + request.getLocalPort() + request.getRequestURI() + "调用成功");
        else if (index == 2){
            return "error";
        }else {
            throw new Exception("参数不能为1~2之外的值");
        }
        return "ok";
    }
}

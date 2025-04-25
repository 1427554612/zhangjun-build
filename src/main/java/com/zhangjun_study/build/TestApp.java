package com.zhangjun_study.build;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Iterator;

public  class TestApp {

    public int test(){
	int a = 10;
	int b = a/1;
	return b;
    }

    public  String testNull(){
        String s = null;
        return s.replace("-","");
    }

    public  int chongfu(int i,int j){
        System.out.println(i+j);
        return i+j;
    }

    public  int chongfu2(int i,int j){
        return i+j;
    }

    public static void main(String[] args) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String data1 = "{\"name\":\"zhangjun\",\"age\":10}";
        String data2 = "{\"name\":\"zhangjun\",\"age\":\"20\"}";
        JsonNode jsonNode1 = objectMapper.readTree(data1);
        JsonNode jsonNode2 = objectMapper.readTree(data2);
        Iterator<JsonNode> elements = jsonNode1.elements();
        while (elements.hasNext()){
            System.out.println(elements.next());
        }
    }

}

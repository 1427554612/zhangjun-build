package com.zhangjun_study.build.utlis;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class EncodeTest {

    public static void main(String[] args) throws UnsupportedEncodingException {
        String data = "{\"name\":\"张军\",\"age\":18}";
        System.out.println(URLEncoder.encode(data));
        byte[] bytes = data.getBytes(StandardCharsets.UTF_8);
        String s = new String(bytes, Charset.defaultCharset());
        System.out.println(s);
    }
}

package zw.itman.controller;

import org.apache.commons.lang.RandomStringUtils;

public class sendSms {

    public String sendSms(){
        String randomNumeric = RandomStringUtils.randomNumeric(6);
        System.out.println("您的验证码为"+randomNumeric);
        return randomNumeric;
    }
}

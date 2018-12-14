package zw.itman.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import javax.servlet.http.HttpServletRequest;

import java.util.concurrent.TimeUnit;

@Controller
public class SendController {
    //访问次数最大等于3
    private static final int limit = 3;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    //private static String sms;
    @RequestMapping("/send/{mobile}")
    public String send(@PathVariable String mobile, Model model) {
        System.out.println("=========="+mobile);
        String msg="";
        if (stringRedisTemplate.opsForList().size("value") < limit) {
          String  sms= new sendSms().sendSms();
            stringRedisTemplate.opsForList().leftPush("value", sms);
            stringRedisTemplate.expire("value",30, TimeUnit.SECONDS);
            System.out.println(sms);
            msg="已向手机号:"+mobile+"发送验证码！";
            model.addAttribute("mobile",mobile);
            model.addAttribute("sms",sms);
            return "index";
        } else {
            msg="请求次数过多，请10秒后重试";

        }
        model.addAttribute("msg",msg);
        return "index";

    }
    @RequestMapping("/login")
    public String login(HttpServletRequest request){

        String sms1 = request.getParameter("sms");
        System.out.println("==sms1=="+sms1);
        String value=stringRedisTemplate.opsForList().index("value",stringRedisTemplate.opsForList().size("value")-1);
        System.out.println("value==="+value);
        if(sms1.equals(value)){
            return "success";

        }else{
            return "index";
        }
    }


}

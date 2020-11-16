package com.yunchuang.artco.controoler.regist;

import com.yunchuang.artco.dao.CheackPhoneMapper;
import com.yunchuang.artco.domain.AjaxRes;
import com.yunchuang.artco.domain.CheackPhone;
import com.yunchuang.artco.domain.User;
import com.yunchuang.artco.service.teacher.ITeacherService;
import com.yunchuang.artco.service.teacher.TeacherServiceImp;
import com.yunchuang.artco.util.ResponseResult;
import com.yunchuang.artco.util.SmsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.SimpleFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
@ApiIgnore()
public class RegistController {
    @Autowired
    private ITeacherService teacherService;
    @Autowired
    private CheackPhoneMapper cheackPhoneMapper;
    @RequestMapping("/regist")
    @ResponseBody

    public ResponseResult regist(@RequestParam String username,
                          @RequestParam String password,
                          @RequestParam String ResPassword,
                          @RequestParam String code,
                                        HttpServletRequest request
                          ) {
        if(code==null||"".equals(code)){
            return ResponseResult.error("验证码不能为空");
        }
        //查询code是否正确
        CheackPhone cheackPhone = cheackPhoneMapper.selectByPhone(code);
        //取出cookie中的电话
        Cookie[] cookies = request.getCookies();
        Cookie telCookie = null;
        outter: if(null!=cookies){
            for(Cookie cookie : cookies){
                if("tel".equals(cookie.getName())){
                    telCookie=cookie;
                    break outter;
                }
            }
            return ResponseResult.error("请先发送验证码");
        }
        //查询数据库是否有该条验证码存在
        if("".equals(username.replace(" ",""))) {
            return ResponseResult.error("用户名不能为空");
        }
        else if ("".equals(password.replace(" ",""))||
                "".equals(ResPassword.replace(" ",""))){
            return ResponseResult.error("密码不能为空");
        }
        else if (!password.equals(ResPassword)) {
            return ResponseResult.error("两次密码输入不一致");
        }
        else if(!passwordIsTrue(password)){
            return ResponseResult.error("请按要求输入密码");
        }
        //若code正确
        else if(cheackPhone!=null){
            //存放一天时间内注册的手机号
            ArrayList<CheackPhone> oneDayRegist = new ArrayList<>();
            //查询当前手机号注册记录
            CheackPhone[] cheackPhones= cheackPhoneMapper.selectTels(cheackPhone.getTel());
            //一天注册的个数
            int count=0;
           for(CheackPhone cheackPhone1:cheackPhones){
               Date date = new Date();
               long nowTime = date.getTime();
               //一天时间的手机注册信息
               if(nowTime-cheackPhone1.getTime().getTime()<=24*60*60*1000){
                   oneDayRegist.add(cheackPhone1);
                   count++;
               }
           }
            cheackPhoneMapper.setNullByCode(cheackPhone.getCode());
           //如果一天注册超过8次 提醒明天再注册
           if(count>8){
               return ResponseResult.error("你当前号码在一天时间内注册超过三次,请明天再来");
           }
           else{
               return teacherService.addTeacher(username, password, telCookie.getValue());
           }
        }
        else {
            return ResponseResult.error("请重新发送短信验证");
        }
    }

    //接受电话并返回短信
    @GetMapping("/getMSg")
    @ResponseBody
    public ResponseResult getCode(@RequestParam String tel, HttpServletResponse response,
                                  HttpServletRequest request) throws UnknownHostException {
        //取出cookie中的电话
        Cookie[] cookies = request.getCookies();
        if(null!=cookies) {
            for (Cookie cookie : cookies) {
                if ("tel".equals(cookie.getName())) {
                    if(telIsTrue(tel)&&cookie.getValue().equals(tel))
                    return ResponseResult.error("请勿再60s内重新发送");
                }
            }
        }
         if(telIsTrue(tel)){
            //保存电话60s
            Cookie telCookie = new Cookie("tel",tel);
            telCookie.setMaxAge(60);
            response.addCookie(telCookie);
            //存入实体类
            String code = SmsUtils.sendMessage(tel);
            String suffix=code;
             for (int i = 0; i <10 ; i++) {
                 suffix+=(int)(Math.random()*10);
             }
            Date date = new Date();
             System.out.println(date);
            InetAddress ip4 = Inet4Address.getLocalHost();
            String ip=ip4.toString();
            CheackPhone cheackPhone = new CheackPhone(null,tel,code,ip,date,suffix);
            cheackPhoneMapper.insert(cheackPhone);
            return ResponseResult.success("发送成功");
        }
        else {
            return ResponseResult.success("电话号码有误");
        }
    }
    //验证电话号码是否有误
    public boolean telIsTrue(String tel){
        String reg ="^1[3456789]\\d{9}$";
        Pattern p = Pattern.compile(reg);
        Matcher m = p.matcher(tel);
        return m.matches();
    }
    public boolean passwordIsTrue(String password){
        String reg="^(?=.*?[0-9])(?=.*?[a-z])[0-9a-z]{8,}$";
        Pattern p = Pattern.compile(reg);
        Matcher m = p.matcher(password);
        return m.matches();
    }
}

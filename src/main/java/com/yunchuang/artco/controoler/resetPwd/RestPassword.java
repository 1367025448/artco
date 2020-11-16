package com.yunchuang.artco.controoler.resetPwd;

import com.yunchuang.artco.dao.CheackPhoneMapper;
import com.yunchuang.artco.dao.UserMapper;
import com.yunchuang.artco.domain.CheackPhone;
import com.yunchuang.artco.domain.User;
import com.yunchuang.artco.util.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
@ApiIgnore()
public class RestPassword {
    @Autowired
    private CheackPhoneMapper cheackPhoneMapper;
    @Autowired
    private UserMapper userMapper;
    @RequestMapping("/resetPwd")
    @ResponseBody
    public ResponseResult resetPwd(@RequestParam String code,
                                   @RequestParam String tel,
                                   HttpServletRequest request){
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
        if(cheackPhone!=null){
            User user = userMapper.selectTel(tel);
            cheackPhoneMapper.setNullByCode(code);
           return user==null?ResponseResult.error("你当前手机号暂未注册"):ResponseResult.success("验证成功",tel);
        }
        return ResponseResult.error("验证码不正确");
    }



    @RequestMapping("/newPwd")
    @ResponseBody
    public ResponseResult newPwd(@RequestParam String password ,
                                 @RequestParam String ResPwd,
                                 @RequestParam String tel
                                 ){

        if("".equals(password)||"".equals(ResPwd)){
           return ResponseResult.error("密码不能为空");
        }
        else if(!password.equals(ResPwd)){
           return ResponseResult.error("两次密码输入不一致");
        }
        else{
            String reg="^(?=.*?[0-9])(?=.*?[a-z])[0-9a-z]{8,}$";
            Pattern p = Pattern.compile(reg);
            Matcher m = p.matcher(password);
            if(m.matches()){
                userMapper.updatePwd(tel,password);
               return ResponseResult.success("修改成功");
            }
          return   ResponseResult.error("密码由6位以上数字和字母组成");
        }

    }
}

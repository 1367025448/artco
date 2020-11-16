package com.yunchuang.artco.controoler.login;

import org.springframework.ui.Model;
import com.yunchuang.artco.dao.MenuMapper;
import com.yunchuang.artco.domain.AjaxRes;
import com.yunchuang.artco.domain.Menu;
import com.yunchuang.artco.domain.User;
import com.yunchuang.artco.service.teacher.ITeacherService;
//import com.yunchuang.artco.util.SmsUtils;
import com.yunchuang.artco.util.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
@SessionAttributes(value = {"user","navigationBars"})
@Controller
@ApiIgnore()
@SuppressWarnings("all")
public class LoginController {
    @Autowired
    private ITeacherService teacherService;
    @Autowired
    private MenuMapper menuMapper;
    @RequestMapping("/login")
    @ResponseBody

    public ResponseResult login(
            @RequestParam String username,
            @RequestParam String password,
            Model model,
            HttpServletRequest request
    ){
      if("".equals(username.replace(" ",""))){
          return ResponseResult.error("账号不能为空");
      }
      else if ("".equals(password.replace(" ",""))){
          return ResponseResult.error("密码不能为空");
      }
      else{
          User user = new User(null,null,password,username,null);
          User user1 = teacherService.loginTeacher(user);
          if(user1!=null){
              model.addAttribute("user", user1);
              List<Menu> menusList = menuMapper.getMenusList();
              model.addAttribute("navigationBars",menusList);
              request.getSession();
              return ResponseResult.success("ddd");
          }else {
              return ResponseResult.error("账号或密码错误");
          }
      }
    }
}

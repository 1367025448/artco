package com.yunchuang.artco.service.teacher;

import com.yunchuang.artco.domain.AjaxRes;
import com.yunchuang.artco.domain.User;
import com.yunchuang.artco.util.ResponseResult;
import org.springframework.stereotype.Service;


public interface ITeacherService {
    //登录
     User loginTeacher(User user);

     ResponseResult addTeacher(String username, String password,String telCookie);

}

package com.yunchuang.artco.service.teacher;

import com.yunchuang.artco.dao.UserMapper;
import com.yunchuang.artco.domain.AjaxRes;
import com.yunchuang.artco.domain.User;
import com.yunchuang.artco.util.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TeacherServiceImp implements ITeacherService{
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private User user;
    @Override
    public User loginTeacher(User user) {
        User user1 = userMapper.loginSelect(user.getPassword(), user.getTel());
        return user1;
    }

    public ResponseResult addTeacher(String username,String password,String telCookie){
        User user1 = userMapper.selectByName(username);
        if(user1!=null){
           return ResponseResult.error("已存在该用户");
        }else {
            User user2=userMapper.selectTel(telCookie);
            if(user2!=null){
                return ResponseResult.error("该电话已注册");
            }else {
                user.setTel(telCookie);
                user.setUsername(username);
                user.setPassword(password);
                userMapper.insert(user);
                return ResponseResult.success("注册成功");
            }
        }
    }


}

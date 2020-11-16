package com.yunchuang.artco.dao;

import com.yunchuang.artco.domain.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    User selectByPrimaryKey(Integer id);

    List<User> selectAll();

    int updateByPrimaryKey(User record);

    User loginSelect(@Param("password") String password,@Param("tel") String tel);

    User selectTel(String telCookie);

    User selectByName(String username);

    void updatePwd(@Param("tel") String tel ,@Param("password") String password);
}
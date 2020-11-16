package com.yunchuang.artco.dao;

import com.yunchuang.artco.domain.CheackPhone;
import java.util.List;

public interface CheackPhoneMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CheackPhone record);

    CheackPhone selectByPrimaryKey(Integer id);

    List<CheackPhone> selectAll();

    int updateByPrimaryKey(CheackPhone record);

    CheackPhone selectByPhone(String code);

    void setNullByCode(String code);

    CheackPhone[] selectTels(String tel);
}
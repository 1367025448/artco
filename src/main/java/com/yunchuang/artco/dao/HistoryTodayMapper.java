package com.yunchuang.artco.dao;

import com.yunchuang.artco.domain.HistoryToday;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public interface HistoryTodayMapper {
    int deleteByPrimaryKey(Long id);

    int insert(HistoryToday record);

    HistoryToday selectByPrimaryKey(Long id);

    List<HistoryToday> selectAll();

    int updateByPrimaryKey(HistoryToday record);

    ArrayList<HistoryToday> selectByDate(@Param("date") String date,@Param("page") Integer page, @Param("limit") Integer limit);

    ArrayList<HistoryToday> GuessLike(@Param("text")String text,@Param("page") Integer page, @Param("limit") Integer limit);

    ArrayList<HistoryToday> getOneMonth(int i);
}
package com.yunchuang.artco.domain;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PageList {
  //查询结果总条数
    private Long totals;
    //一页多少条数据
    public final static Integer ROW =10;
//查询第几页
   private Integer page;

   //返回一页的数据
    private List<?>  getOneLists = new ArrayList<>();
}

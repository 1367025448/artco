package com.yunchuang.artco.domain;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Menu {
    private Long id;

    private String menuName;

    private String menuUrl;

    private Long parentId;

    private String fontClass;

    private List<Menu> children =new ArrayList<>();

}
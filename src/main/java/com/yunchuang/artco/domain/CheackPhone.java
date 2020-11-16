package com.yunchuang.artco.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CheackPhone {
    private Integer id;

    private String tel;

    private String code;

    private String ipAddr;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" ,timezone="GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date time;

    private String suffix;
}
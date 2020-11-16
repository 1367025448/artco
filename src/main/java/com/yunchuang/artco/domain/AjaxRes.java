package com.yunchuang.artco.domain;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class AjaxRes {
    private String msg;
    private Boolean success;
}

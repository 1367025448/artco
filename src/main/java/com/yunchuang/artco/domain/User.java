package com.yunchuang.artco.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.persistence.Id;
import javax.persistence.Table;

@Component
@Data
@NoArgsConstructor
@AllArgsConstructor

public class User {

    private Integer id;

    private String username;

    private String password;

    private String tel;

    private String guessHobby;
}
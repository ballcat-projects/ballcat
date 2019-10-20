package com.hccake.ballcat.api.modules.api.controller;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;

/**
 * @author Hccake
 * @version 1.0
 * @date 2019/10/17 23:12
 */
@Data
public class TestObj {

    private Integer id;
    private String str;
    private String blankStr;
    private ArrayList<String> list;
    private Map<String, String> map;
    private LocalDateTime localDateTime;
    private LocalDate localDate;
}
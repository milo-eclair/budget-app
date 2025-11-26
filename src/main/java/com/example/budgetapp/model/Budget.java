package com.example.budgetapp.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Budget {
    private int budgetId;             
    private LocalDate budgetMonth;       // date型（年月)
    private Integer budgetAmount;        // int
    private int userId;               // ユーザーID
    private Boolean deleteFlg;           // 論理削除
    private String createUser;
    private LocalDateTime createDate;
    private String updateUser;
    private LocalDateTime updateDate;
}
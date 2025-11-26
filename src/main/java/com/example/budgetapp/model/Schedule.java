package com.example.budgetapp.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Schedule {

    private int scheduleId;           
    private String scheduleTitle;        // VARCHAR(255)
    private LocalDate scheduleStartDate; // DATE
    private LocalDate scheduleEndDate;   // DATE
    private Integer scheduleAmount;      // INT
    private String scheduleMemo;         // VARCHAR(255)
    private int userId;               
    private Boolean deleteFlg;           // BOOLEAN
    private String createUser;           // 作成者
    private LocalDateTime createDate;    // 作成日時
    private String updateUser;           // 更新者
    private LocalDateTime updateDate;    // 更新日時
}

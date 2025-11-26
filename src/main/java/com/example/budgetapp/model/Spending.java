package com.example.budgetapp.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Spending {
    private int spendingId;
    private Integer amount;
    private LocalDate spendingDate;
    private String memo;
    private int userId;
    private int categoryId;
    private Boolean deleteFlg;
    private String createUser;
    private LocalDateTime createDate;
    private String updateUser;
    private LocalDateTime updateDate;

    private String categoryName;             // 既存のカテゴリ名表示用
    private String customCategoryName;       // 追加：その他カテゴリ手入力用
}

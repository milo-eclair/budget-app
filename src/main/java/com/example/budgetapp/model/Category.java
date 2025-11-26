package com.example.budgetapp.model;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Category {
    private int categoryId;       
    private String categoryName;     // カテゴリ名
    private int userId;           // ユーザーID（NULL可、デフォルトカテゴリはNULL）
    private Boolean deleteFlg;       // 論理削除
    private String createUser;
    private LocalDateTime createDate;
    private String updateUser;
    private LocalDateTime updateDate;
}

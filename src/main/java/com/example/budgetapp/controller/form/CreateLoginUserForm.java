package com.example.budgetapp.controller.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.Data;

/**
 * ログインユーザーを作成するためのフォーム
 */
@Data
public class CreateLoginUserForm {

    /** ユーザー名 */
    @NotBlank(message = "ユーザー名は必須です")
    @Size(min = 4, message = "ユーザー名は最小4文字必要です")
    private String userName;

    /** パスワード */
    @NotBlank(message = "パスワードは必須です")
    @Size(min = 8, message = "パスワードは最小8文字必要です")
    private String password;

    /** Emailアドレス（任意） */
    private String userEmail;

    /** 電話番号 */
    private String userPhoneNumber;
}

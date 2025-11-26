package com.example.budgetapp.model;

import java.time.LocalDateTime;

import lombok.Value;

/**
 * ログインユーザーを表すモデル
 */
@Value
public class LoginUser {

    /** ユーザーID（主キー） */
    private final String userId;

    /** ユーザー名称 */
    private final String userName;

    /** ユーザーパスワード */
    private final String userPassword;

    /** ユーザーEmailアドレス */
    private final String userEmail;

    /** ユーザー電話番号 */
    private final String userPhoneNumber;

    /** 削除フラグ */
    private final boolean deleteFlg;

    /** 作成者 */
    private final String createUser;

    /** 作成日 */
    private final LocalDateTime createDate;

    /** 更新者 */
    private final String updateUser;

    /** 更新日 */
    private final LocalDateTime updateDate;
}

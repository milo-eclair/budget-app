package com.example.budgetapp.repository;

import java.util.Optional;

import com.example.budgetapp.model.LoginUser;

/**
 * ログインユーザーを管理するリポジトリ
 */
public interface LoginUserRepository {

    /**
     * ユーザーIDからログインユーザーを検索する。
     *
     * @param userId ユーザーID
     * @return ログインユーザー（存在しない場合は空のOptional）
     */
    Optional<LoginUser> findByUserId(String userId);

    /**
     * ユーザー名からログインユーザーを検索する。
     *
     * @param userName ユーザー名
     * @return ログインユーザー（存在しない場合は空のOptional）
     */
    Optional<LoginUser> findByUserName(String userName);

    /**
     * ログインユーザーを登録する。
     *
     * @param loginUser ログインユーザー
     */
    void register(LoginUser loginUser);
}

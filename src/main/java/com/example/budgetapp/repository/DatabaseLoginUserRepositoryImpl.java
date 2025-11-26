package com.example.budgetapp.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.example.budgetapp.model.LoginUser;
import com.example.budgetapp.repository.mybatis.LoginUserMapper;

/**
 * ログインユーザーをデータベースで管理するリポジトリ
 */
@Repository
public class DatabaseLoginUserRepositoryImpl implements LoginUserRepository {

    /** ログインユーザーのマッパー */
    private final LoginUserMapper loginUserMapper;

    /** コンストラクタでマッパーを注入 */
    public DatabaseLoginUserRepositoryImpl(LoginUserMapper loginUserMapper) {
        this.loginUserMapper = loginUserMapper;
    }

    /** ユーザーIDからログインユーザーを検索 */
    @Override
    public Optional<LoginUser> findByUserId(String userId) {
        return loginUserMapper.findByUserId(userId);
    }

    /** ユーザー名からログインユーザーを検索 */
    @Override
    public Optional<LoginUser> findByUserName(String userName) {
        return loginUserMapper.findByUserName(userName);
    }

    /** ログインユーザーをデータベースに登録 */
    @Override
    public void register(LoginUser loginUser) {
        loginUserMapper.register(loginUser);
    }
}

package com.example.budgetapp.repository.mybatis;

import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.example.budgetapp.model.LoginUser;

/**
 * ログインユーザーのマッパー
 */
@Mapper
public interface LoginUserMapper {

    /**
     * ユーザーIDからログインユーザーを検索する。
     *
     * @param userId ユーザーID
     * @return ログインユーザー
     */
    Optional<LoginUser> findByUserId(String userId);

    /**
     * ユーザー名からログインユーザーを検索する。
     *
     * @param userName ユーザー名
     * @return ログインユーザー
     */
    @Select("SELECT * FROM tb_mst_user_login WHERE user_name = #{userName}")
    Optional<LoginUser> findByUserName(String userName);

    /**
     * ログインユーザーを登録する。
     *
     * @param loginUser ログインユーザー
     */
    void register(LoginUser loginUser);
}

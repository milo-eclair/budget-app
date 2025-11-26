package com.example.budgetapp.repository.mybatis;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.budgetapp.model.Category;

@Mapper
public interface CategoryMapper {

    // delete_flg = false のみ取得
    List<Category> findAll();

    // IDでカテゴリ取得
    Category findById(@Param("categoryId") int categoryId);

    // カテゴリ登録
    void insert(Category category);

    // カテゴリ更新
    void update(Category category);

    // 論理削除
    void logicalDelete(@Param("categoryId") int categoryId,
                       @Param("updateUser") String updateUser);

    // ユーザーIDでカテゴリ取得（デフォルトは userId = null）
    List<Category> findByUserId(@Param("userId") int userId);

    // DailyCalendar用：ユーザーID別カテゴリ + Systemカテゴリ
    List<Category> findAllByUserIdOrGlobal(@Param("userId") int userId);

    // カテゴリ管理用：自分のカテゴリのみ（Systemカテゴリを除く）
    List<Category> findByUserIdExcludingSystem(@Param("userId") int userId);
}

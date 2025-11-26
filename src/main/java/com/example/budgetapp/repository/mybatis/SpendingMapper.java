package com.example.budgetapp.repository.mybatis;

import java.time.LocalDate;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.budgetapp.model.Category;
import com.example.budgetapp.model.Spending;

@Mapper
public interface SpendingMapper {

    // 支出IDで1件取得
    Spending findById(@Param("spendingId") int spendingId);

    // ユーザーと日付で支出一覧を取得（日表示などに使用）
    List<Spending> findByUserIdAndDate(@Param("userId") int userId,
                                       @Param("spendingDate") LocalDate spendingDate);

    // 支出を新規登録
    void insert(Spending spending);

    // 支出を更新
    void update(Spending spending);

    // 論理削除（削除フラグを立てる方式）
    void logicalDelete(@Param("spendingId") int spendingId,
                       @Param("updateUser") String updateUser);

    // 追加: ユーザー用カテゴリ取得（共通カテゴリ → 個人カテゴリ → その他）
    List<Category> findCategoriesForUser(@Param("userId") int userId);

    // 🧩 追加: 指定月の支出合計金額を取得（支出グラフ用）
    Integer findMonthlyTotal(@Param("userId") int userId,
                             @Param("startDate") LocalDate startDate,
                             @Param("endDate") LocalDate endDate);

    // 🧩 追加: 指定月の予算金額を取得（支出グラフ用）
    Integer findMonthlyBudget(@Param("userId") int userId,
                              @Param("year") int year,
                              @Param("month") int month);
}

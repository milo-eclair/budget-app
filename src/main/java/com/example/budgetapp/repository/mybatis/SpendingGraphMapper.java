package com.example.budgetapp.repository.mybatis;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.budgetapp.model.SpendingGraph;

@Mapper
public interface SpendingGraphMapper {

    /**
     * 指定ユーザーの月別カテゴリ支出合計を取得
     * @param userId ログイン中のユーザーID
     * @param startOfMonth 月の開始日
     * @param endOfMonth 月の終了日
     * @return カテゴリ別の支出合計リスト
     */
    List<SpendingGraph> findSpendingByCategory(
        @Param("userId") int userId,
        @Param("startOfMonth") java.time.LocalDate startOfMonth,
        @Param("endOfMonth") java.time.LocalDate endOfMonth
    );
}

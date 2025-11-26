package com.example.budgetapp.repository.mybatis;

import java.time.LocalDate;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.budgetapp.model.Budget;

@Mapper
public interface BudgetMapper {

    List<Budget> findAll(); // delete_flg = false のみ取得

    Budget findById(@Param("budgetId") String budgetId);

    void insert(Budget budget);

    void update(Budget budget);

    void logicalDelete(@Param("budgetId") int budgetId, @Param("updateUser") int updateUser);

    // 年月とユーザーで予算を取得
    Budget findByUserIdAndMonth(@Param("userId") int userId,
                                @Param("budgetMonth") LocalDate budgetMonth);
}

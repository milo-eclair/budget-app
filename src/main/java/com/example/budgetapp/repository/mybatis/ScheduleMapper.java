package com.example.budgetapp.repository.mybatis;

import java.time.LocalDate;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.budgetapp.model.Schedule;

@Mapper
public interface ScheduleMapper {

    // すべての有効な予定を取得（削除フラグ false のみ）
    List<Schedule> findAll();

    // IDで予定を取得
    Schedule findById(@Param("scheduleId") int scheduleId);

    // 予定を新規登録
    void insert(Schedule schedule);

    // 予定を更新
    void update(Schedule schedule);

    // 論理削除
    void logicalDelete(@Param("scheduleId") int scheduleId, @Param("updateUser") int updateUser);

    // ユーザーと日付で予定を取得（例：日表示カレンダー用）
    List<Schedule> findByUserIdAndDate(
        @Param("userId") int userId,
        @Param("targetDate") LocalDate targetDate
    );

    // ユーザーと期間で予定を取得（月表示カレンダー用）
    List<Schedule> findByUserIdAndDateRange(
        @Param("userId") int userId,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate
    );
}

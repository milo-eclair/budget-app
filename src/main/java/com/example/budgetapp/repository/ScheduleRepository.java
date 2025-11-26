package com.example.budgetapp.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.example.budgetapp.model.Schedule;
import com.example.budgetapp.repository.mybatis.ScheduleMapper;

/**
 * 予定を管理するリポジトリクラス。
 * <p>
 * データベース上の tb_trn_schedule テーブルを操作する。
 * Mapper（MyBatis）を利用して、予定の登録・取得・更新・削除を行う。
 * </p>
 */
@Repository
public class ScheduleRepository {

    /** 予定管理マッパー */
    private final ScheduleMapper scheduleMapper;

    /**
     * コンストラクタ
     *
     * @param scheduleMapper 予定管理マッパー
     */
    public ScheduleRepository(ScheduleMapper scheduleMapper) {
        this.scheduleMapper = scheduleMapper;
    }

    /**
     * 指定したユーザーと日付に紐づく予定を取得する。
     *
     * @param userId ユーザーID
     * @param targetDate 対象日
     * @return 該当する予定のリスト
     */
    public List<Schedule> findByUserIdAndDate(int userId, LocalDate targetDate) {
        return scheduleMapper.findByUserIdAndDate(userId, targetDate);
    }

    /**
     * 予定IDを指定して1件取得する。
     *
     * @param scheduleId 予定ID
     * @return 該当する予定（存在しない場合は空）
     */
    public Optional<Schedule> findById(int scheduleId) {
        return Optional.ofNullable(scheduleMapper.findById(scheduleId));
    }

    /**
     * 新しい予定を登録する。
     *
     * @param schedule 登録対象の予定
     */
    public void insert(Schedule schedule) {
        scheduleMapper.insert(schedule);
    }

    /**
     * 既存の予定を更新する。
     *
     * @param schedule 更新対象の予定
     */
    public void update(Schedule schedule) {
        scheduleMapper.update(schedule);
    }

    /**
     * 予定を論理削除する。
     *
     * @param scheduleId 削除対象の予定ID
     * @param updateUser 更新者
     */
    public void logicalDelete(int scheduleId, int updateUser) {
        scheduleMapper.logicalDelete(scheduleId, updateUser);
    }
}

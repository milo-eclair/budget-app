package com.example.budgetapp.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.budgetapp.model.Schedule;
import com.example.budgetapp.repository.ScheduleRepository;

/**
 * 予定管理を行うサービスクラス
 * <p>
 * ScheduleRepository を利用して予定の登録・取得・更新・削除を行う。
 * </p>
 */
@Service
public class ScheduleService {

    /** 予定管理リポジトリ */
    private final ScheduleRepository scheduleRepository;

    /**
     * 予定管理サービスのコンストラクタ
     *
     * @param scheduleRepository 予定管理リポジトリ
     */
    public ScheduleService(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    /**
     * 指定ユーザーと日付に紐づく予定を取得する。
     *
     * @param userId ユーザーID
     * @param date 対象日
     * @return 該当する予定リスト
     */
    public List<Schedule> getSchedulesByUserAndDate(int userId, LocalDate date) {
        return scheduleRepository.findByUserIdAndDate(userId, date);
    }

    /**
     * 新しい予定を登録する。
     *
     * @param schedule 登録対象の予定
     */
    public void registerSchedule(Schedule schedule) {
        // deleteFlg 初期化
        schedule.setDeleteFlg(false);

        // TODO: createUser と updateUser はログインユーザー名を設定すること
        scheduleRepository.insert(schedule);
    }

    /**
     * 既存の予定を更新する。
     *
     * @param schedule 更新対象の予定
     */
    public void updateSchedule(Schedule schedule) {
        // TODO: updateUser はログインユーザー名を設定すること
        scheduleRepository.update(schedule);
    }

    /**
     * 予定を論理削除する。
     *
     * @param scheduleId 削除対象の予定ID
     * @param updateUser 更新者
     */
    public void deleteSchedule(int scheduleId, int updateUser) {
        scheduleRepository.logicalDelete(scheduleId, updateUser);
    }

    /**
     * scheduleId で予定を取得する
     *
     * @param scheduleId 予定ID
     * @return 該当予定（存在しない場合は Optional.empty()）
     */
    public Optional<Schedule> getScheduleById(int scheduleId) {
        return scheduleRepository.findById(scheduleId);
    }
}

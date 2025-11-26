package com.example.budgetapp.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Optional;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.budgetapp.model.LoginUser;
import com.example.budgetapp.model.Schedule;
import com.example.budgetapp.repository.LoginUserRepository;
import com.example.budgetapp.service.ScheduleService;

/**
 * 予定管理専用のコントローラー
 */
@Controller
public class ScheduleController {

    private final ScheduleService scheduleService;
    private final LoginUserRepository loginUserRepository;

    public ScheduleController(ScheduleService scheduleService, LoginUserRepository loginUserRepository) {
        this.scheduleService = scheduleService;
        this.loginUserRepository = loginUserRepository;
    }

    /**
     * 予定登録
     */
    @PostMapping("/schedule/register")
    public String registerSchedule(Schedule schedule,
                                   @RequestParam(name = "date", required = false)
                                   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                   Principal principal) {

        Optional<LoginUser> loginUserOpt = loginUserRepository.findByUserName(principal.getName());
        if (loginUserOpt.isEmpty()) {
            throw new IllegalStateException("ログインユーザーが存在しません: " + principal.getName());
        }
        int userId = Integer.parseInt(loginUserOpt.get().getUserId());
        schedule.setUserId(userId);

        schedule.setDeleteFlg(false);
        schedule.setCreateUser(Integer.toString(userId));
        schedule.setUpdateUser(Integer.toString(userId));

        scheduleService.registerSchedule(schedule);

        LocalDate redirectDate = (date != null) ? date : schedule.getScheduleStartDate();
        return "redirect:/daily_calendar?date=" + redirectDate;
    }

    /**
     * 予定更新
     */
    @PostMapping("/schedule/update")
    public String updateSchedule(Schedule schedule,
                                 @RequestParam(name = "date", required = false)
                                 @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                 Principal principal) {

        Optional<LoginUser> loginUserOpt = loginUserRepository.findByUserName(principal.getName());
        if (loginUserOpt.isEmpty()) {
            throw new IllegalStateException("ログインユーザーが存在しません: " + principal.getName());
        }
        String userId = loginUserOpt.get().getUserId();

        schedule.setUpdateUser(userId);
        scheduleService.updateSchedule(schedule);

        LocalDate redirectDate = (date != null) ? date : schedule.getScheduleStartDate();
        return "redirect:/daily_calendar?date=" + redirectDate;
    }

    /**
     * 予定削除（論理削除）
     */
    @PostMapping("/schedule/delete")
    public String deleteSchedule(@RequestParam int scheduleId,
                                 @RequestParam(name = "date", required = false)
                                 @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                 Principal principal) {

        Optional<LoginUser> loginUserOpt = loginUserRepository.findByUserName(principal.getName());
        if (loginUserOpt.isEmpty()) {
            throw new IllegalStateException("ログインユーザーが存在しません: " + principal.getName());
        }
        int userId = Integer.parseInt(loginUserOpt.get().getUserId());

        scheduleService.deleteSchedule(scheduleId, userId);

        LocalDate redirectDate = (date != null) ? date : LocalDate.now();
        return "redirect:/daily_calendar?date=" + redirectDate;
    }
}

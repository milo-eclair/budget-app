package com.example.budgetapp.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.budgetapp.model.Category;
import com.example.budgetapp.model.LoginUser;
import com.example.budgetapp.model.Schedule;
import com.example.budgetapp.model.Spending;
import com.example.budgetapp.repository.LoginUserRepository;
import com.example.budgetapp.repository.mybatis.CategoryMapper;
import com.example.budgetapp.repository.mybatis.SpendingMapper;
import com.example.budgetapp.service.ScheduleService;

/**
 * コントローラー
 * 日表示カレンダー画面を表示する為のコントローラー
 */
@Controller
public class DailyCalendarController {

    private final SpendingMapper spendingMapper;
    private final LoginUserRepository loginUserRepository;
    private final CategoryMapper categoryMapper;
    private final ScheduleService scheduleService;

    public DailyCalendarController(SpendingMapper spendingMapper,
                                   LoginUserRepository loginUserRepository,
                                   CategoryMapper categoryMapper,
                                   ScheduleService scheduleService) {
        this.spendingMapper = spendingMapper;
        this.loginUserRepository = loginUserRepository;
        this.categoryMapper = categoryMapper;
        this.scheduleService = scheduleService;
    }

    /**
     * 日表示カレンダー画面を表示する為のメソッド
     */
    @GetMapping("/daily_calendar")
    public String showDailyCalendar(
            @RequestParam(name = "date", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            Model model,
            Principal principal) {

        // ログインユーザー取得
        Optional<LoginUser> loginUserOpt = loginUserRepository.findByUserName(principal.getName());
        if (loginUserOpt.isEmpty()) {
            throw new IllegalArgumentException("指定されたユーザーが存在しません: " + principal.getName());
        }
        int userId = Integer.parseInt(loginUserOpt.get().getUserId());

        // 支出取得
        LocalDate selectedDate = (date != null) ? date : LocalDate.now();
        List<Spending> expenses = spendingMapper.findByUserIdAndDate(userId, selectedDate);

        // ユーザー登録カテゴリ取得（食費・生活雑費も含む）
        List<Category> categoryList = categoryMapper.findAllByUserIdOrGlobal(userId);

        // 支出にカテゴリ名をセット
        for (Spending s : expenses) {
            categoryList.stream()
                    .filter(c -> c.getCategoryId() == s.getCategoryId())
                    .findFirst()
                    .ifPresentOrElse(
                            c -> s.setCategoryName(c.getCategoryName()),
                            () -> s.setCategoryName("未分類")
                    );
        }

        // サービスを経由して予定を取得
        List<Schedule> schedules = scheduleService.getSchedulesByUserAndDate(userId, selectedDate);

        // Modelに格納
        model.addAttribute("selectedDate", selectedDate);
        model.addAttribute("expenses", expenses);
        model.addAttribute("categoryList", categoryList);
        model.addAttribute("loginUserName", principal.getName());
        model.addAttribute("schedules", schedules);

        // 支出合計
        Integer totalSpending = expenses.stream()
                .mapToInt(Spending::getAmount)
                .sum();
        model.addAttribute("totalSpending", totalSpending);

        // ★ 追加部分：戻るボタンで適切な年月に戻れるようにする
        model.addAttribute("year", selectedDate.getYear());
        model.addAttribute("month", selectedDate.getMonthValue());

        return "daily_calendar/index";
    }

}

package com.example.budgetapp.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.budgetapp.model.Budget;
import com.example.budgetapp.model.LoginUser;
import com.example.budgetapp.model.Schedule;
import com.example.budgetapp.model.Spending;
import com.example.budgetapp.repository.LoginUserRepository;
import com.example.budgetapp.repository.mybatis.BudgetMapper;
import com.example.budgetapp.repository.mybatis.ScheduleMapper;
import com.example.budgetapp.repository.mybatis.SpendingMapper;
import com.example.budgetapp.util.Calendar;
import com.example.budgetapp.util.Calendar.Day;

@Controller
public class MonthlyCalendarController {

    private final BudgetMapper budgetMapper;
    private final LoginUserRepository loginUserRepository;
    private final ScheduleMapper scheduleMapper;
    private final SpendingMapper spendingMapper;

    public MonthlyCalendarController(BudgetMapper budgetMapper,
                                     LoginUserRepository loginUserRepository,
                                     ScheduleMapper scheduleMapper,
                                     SpendingMapper spendingMapper) {
        this.budgetMapper = budgetMapper;
        this.loginUserRepository = loginUserRepository;
        this.scheduleMapper = scheduleMapper;
        this.spendingMapper = spendingMapper;
    }

    @GetMapping({"/monthly_calendar", "/calendar/{year}/{month}"})
    public String monthlyCalendar(@PathVariable(required = false) Integer year,
                                  @PathVariable(required = false) Integer month,
                                  Model model,
                                  Principal principal) {

        LocalDate today = LocalDate.now();
        if (year == null || month == null) {
            year = today.getYear();
            month = today.getMonthValue();
        }

        Integer budgetAmount = null;
        Integer totalSpending = 0; // 月合計
        Integer remainingBudget = null;
        Budget budget = null;
        Integer userId = null;

        // ユーザー情報を取得
        if (principal != null) {
            String userName = principal.getName();
            Optional<LoginUser> loginUserOpt = loginUserRepository.findByUserName(userName);
            if (loginUserOpt.isPresent()) {
                userId = Integer.parseInt(loginUserOpt.get().getUserId());
                budget = budgetMapper.findByUserIdAndMonth(userId, LocalDate.of(year, month, 1));
                if (budget != null) {
                    budgetAmount = budget.getBudgetAmount();
                }
            }
        }

        // カレンダー生成
        List<List<Day>> calendarWeeks = Calendar.generateCalendar(year, month);

        // 日ごとの総合計金額を保持する Map（日 -> 合計金額）
        Map<Integer, Integer> dayTotalAmountMap = new HashMap<>();

        if (userId != null) {

            // ✅ ここで「すでに計上済みの予定」を管理（同じ予定IDを重複計上しない）
            Set<Integer> countedScheduleIds = new HashSet<>();

            for (List<Day> week : calendarWeeks) {
                for (Day day : week) {
                    if (day != null) {
                        LocalDate targetDate = LocalDate.of(year, month, day.date);

                        // --- 予定 ---
                        List<Schedule> schedules = scheduleMapper.findByUserIdAndDate(userId, targetDate);

                        day.schedules = schedules.stream()
                                .map(s -> {
                                    Calendar.Schedule cs = new Calendar.Schedule();
                                    cs.scheduleTitle = s.getScheduleTitle();
                                    cs.scheduleAmount = (s.getScheduleAmount() != null) ? s.getScheduleAmount() : 0;
                                    return cs;
                                })
                                .collect(Collectors.toList());

                        // ✅ 金額重複防止ロジック
                        int scheduleTotal = 0;
                        for (Schedule s : schedules) {
                            if (!countedScheduleIds.contains(s.getScheduleId())) {
                                countedScheduleIds.add(s.getScheduleId());
                                if (s.getScheduleAmount() != null) {
                                    scheduleTotal += s.getScheduleAmount();
                                }
                            }
                        }

                        // --- 支出 ---
                        List<Spending> spendings = spendingMapper.findByUserIdAndDate(userId, targetDate);
                        int spendingTotal = spendings.stream()
                                .mapToInt(s -> (s.getAmount() != null) ? s.getAmount() : 0)
                                .sum();
                        day.spendingTotal = spendingTotal;

                        // --- 日ごとの合計 ---
                        int totalAmount = scheduleTotal + spendingTotal;
                        dayTotalAmountMap.put(day.date, totalAmount);

                        // --- 月合計に反映 ---
                        totalSpending += totalAmount;
                    }
                }
            }
        }

        // 残り予算計算
        remainingBudget = (budgetAmount != null) ? budgetAmount - totalSpending : null;

        // 前月・翌月情報
        YearMonth currentMonth = YearMonth.of(year, month);
        YearMonth prevMonth = currentMonth.minusMonths(1);
        YearMonth nextMonth = currentMonth.plusMonths(1);

        // Viewへの値設定
        model.addAttribute("year", year);
        model.addAttribute("month", month);
        model.addAttribute("budgetAmount", budgetAmount);
        model.addAttribute("totalSpending", totalSpending);
        model.addAttribute("remainingBudget", remainingBudget);
        model.addAttribute("calendarWeeks", calendarWeeks);
        model.addAttribute("dayTotalAmountMap", dayTotalAmountMap);
        model.addAttribute("prevYear", prevMonth.getYear());
        model.addAttribute("prevMonth", prevMonth.getMonthValue());
        model.addAttribute("nextYear", nextMonth.getYear());
        model.addAttribute("nextMonth", nextMonth.getMonthValue());

        boolean existingBudget = (budget != null);
        model.addAttribute("existingBudget", existingBudget);
        if (existingBudget) {
            model.addAttribute("existingBudgetId", budget.getBudgetId());
            model.addAttribute("existingBudgetMonth",
                    budget.getBudgetMonth() != null
                            ? budget.getBudgetMonth().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM"))
                            : null);
        } else {
            model.addAttribute("existingBudgetId", null);
            model.addAttribute("existingBudgetMonth", String.format("%04d-%02d", year, month));
        }

        return "monthly_calendar/index";
    }
}

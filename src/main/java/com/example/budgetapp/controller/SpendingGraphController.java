package com.example.budgetapp.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.budgetapp.model.LoginUser;
import com.example.budgetapp.model.SpendingGraph;
import com.example.budgetapp.repository.LoginUserRepository;
import com.example.budgetapp.repository.mybatis.SpendingGraphMapper;
import com.example.budgetapp.repository.mybatis.SpendingMapper;

@Controller
public class SpendingGraphController {

    private final SpendingGraphMapper spendingGraphMapper;
    private final SpendingMapper spendingMapper; // ← 支出合計取得用
    private final LoginUserRepository loginUserRepository;

    public SpendingGraphController(
            SpendingGraphMapper spendingGraphMapper,
            SpendingMapper spendingMapper,
            LoginUserRepository loginUserRepository) {
        this.spendingGraphMapper = spendingGraphMapper;
        this.spendingMapper = spendingMapper;
        this.loginUserRepository = loginUserRepository;
    }

    // 指定年月の支出グラフ表示
    @GetMapping("/graph/{year}/{month}")
    public String showSpendingGraph(@PathVariable int year,
                                    @PathVariable int month,
                                    Model model,
                                    Principal principal) {

        // ログインユーザー取得
        Optional<LoginUser> loginUserOpt = loginUserRepository.findByUserName(principal.getName());
        if (loginUserOpt.isEmpty()) {
            throw new IllegalArgumentException("指定されたユーザーが存在しません: " + principal.getName());
        }
        int userId = Integer.parseInt(loginUserOpt.get().getUserId());

        // 対象年月の開始日と終了日
        YearMonth targetMonth = YearMonth.of(year, month);
        LocalDate startOfMonth = targetMonth.atDay(1);
        LocalDate endOfMonth = targetMonth.atEndOfMonth();

        // Mapper を使ってカテゴリ別支出合計を取得
        List<SpendingGraph> graphData = spendingGraphMapper.findSpendingByCategory(userId, startOfMonth, endOfMonth);

        // ★ 月の支出合計を取得（新規追加）
        Integer monthlyTotal = spendingMapper.findMonthlyTotal(userId, startOfMonth, endOfMonth);
        if (monthlyTotal == null) {
            monthlyTotal = 0;
        }

        // ★ 予算取得（例：予算テーブルから）
        Integer budgetAmount = spendingMapper.findMonthlyBudget(userId, year, month);
        if (budgetAmount == null) {
            budgetAmount = 0;
        }

        // ★ 残り予算を計算
        int remainingBudget = budgetAmount - monthlyTotal;

        // モデルにセット
        model.addAttribute("graphData", graphData);
        model.addAttribute("year", year);
        model.addAttribute("month", month);
        model.addAttribute("monthlyTotal", monthlyTotal);
        model.addAttribute("budgetAmount", budgetAmount);
        model.addAttribute("remainingBudget", remainingBudget);

        // 表示するHTML
        return "spendingGraph/index";
    }
}

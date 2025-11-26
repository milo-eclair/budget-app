package com.example.budgetapp.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.budgetapp.model.Budget;
import com.example.budgetapp.model.LoginUser;
import com.example.budgetapp.repository.LoginUserRepository;
import com.example.budgetapp.repository.mybatis.BudgetMapper;

@Controller
public class BudgetController {

    private final BudgetMapper budgetMapper;
    private final LoginUserRepository loginUserRepository;

    public BudgetController(BudgetMapper budgetMapper, LoginUserRepository loginUserRepository) {
        this.budgetMapper = budgetMapper;
        this.loginUserRepository = loginUserRepository;
    }

    // 予算登録処理（カレンダー用）
    @PostMapping("/budget/register")
    public String register(@RequestParam String budgetMonth,
                           @RequestParam Integer budgetAmount,
                           Principal principal) {

        Optional<LoginUser> loginUserOpt = loginUserRepository.findByUserName(principal.getName());
        if (loginUserOpt.isEmpty()) {
            throw new IllegalArgumentException("指定されたユーザーが存在しません: " + principal.getName());
        }
        int userId = Integer.parseInt(loginUserOpt.get().getUserId());

        LocalDate monthDate = LocalDate.parse(budgetMonth + "-01");

        Budget existing = budgetMapper.findByUserIdAndMonth(userId, monthDate);
        if (existing != null) {
            existing.setBudgetAmount(budgetAmount);
            existing.setBudgetMonth(monthDate);
            existing.setUpdateUser(Integer.toString(userId));
            budgetMapper.update(existing);

            int year = existing.getBudgetMonth().getYear();
            int month = existing.getBudgetMonth().getMonthValue();
            return "redirect:/calendar/" + year + "/" + month;
        }

        Budget budget = new Budget();
        budget.setBudgetMonth(monthDate);
        budget.setBudgetAmount(budgetAmount);
        budget.setUserId(userId);
        budget.setCreateUser(Integer.toString(userId));
        budget.setUpdateUser(Integer.toString(userId));
        budgetMapper.insert(budget);

        int year = budget.getBudgetMonth().getYear();
        int month = budget.getBudgetMonth().getMonthValue();
        return "redirect:/calendar/" + year + "/" + month;
    }

    // 予算更新
    @PostMapping("/budget/update")
    public String update(@RequestParam String budgetId,
                         @RequestParam String budgetMonth,
                         @RequestParam Integer budgetAmount,
                         Principal principal) {

        Optional<LoginUser> loginUserOpt = loginUserRepository.findByUserName(principal.getName());
        if (loginUserOpt.isEmpty()) {
            throw new IllegalArgumentException("指定されたユーザーが存在しません: " + principal.getName());
        }
        int userId = Integer.parseInt(loginUserOpt.get().getUserId());

        LocalDate monthDate = LocalDate.parse(budgetMonth + "-01");

        Budget budget = new Budget();
        budget.setBudgetId(Integer.parseInt(budgetId));
        budget.setBudgetMonth(monthDate);
        budget.setBudgetAmount(budgetAmount);
        budget.setUpdateUser(Integer.toString(userId));

        budgetMapper.update(budget);

        int year = monthDate.getYear();
        int month = monthDate.getMonthValue();
        return "redirect:/calendar/" + year + "/" + month;
    }

    // 予算削除（論理削除）
    @PostMapping("/budget/delete")
    public String delete(@RequestParam int budgetId,
                         @RequestParam String budgetMonth,
                         Principal principal) {

        Optional<LoginUser> loginUserOpt = loginUserRepository.findByUserName(principal.getName());
        if (loginUserOpt.isEmpty()) {
            throw new IllegalArgumentException("指定されたユーザーが存在しません: " + principal.getName());
        }
        int userId = Integer.parseInt(loginUserOpt.get().getUserId());

        budgetMapper.logicalDelete(budgetId, userId);

        LocalDate monthDate = LocalDate.parse(budgetMonth + "-01");
        int year = monthDate.getYear();
        int month = monthDate.getMonthValue();
        return "redirect:/calendar/" + year + "/" + month;
    }
}

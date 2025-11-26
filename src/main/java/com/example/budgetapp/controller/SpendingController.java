package com.example.budgetapp.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.budgetapp.model.LoginUser;
import com.example.budgetapp.model.Spending;
import com.example.budgetapp.repository.LoginUserRepository;
import com.example.budgetapp.repository.mybatis.SpendingMapper;

@Controller
public class SpendingController {

    private final SpendingMapper spendingMapper;
    private final LoginUserRepository loginUserRepository;

    public SpendingController(SpendingMapper spendingMapper,
                              LoginUserRepository loginUserRepository) {
        this.spendingMapper = spendingMapper;
        this.loginUserRepository = loginUserRepository;
    }

    /**
     * 支出登録
     */
    @Transactional
    @PostMapping("/spending/register")
    public String registerSpending(Spending spending,
                                   @RequestParam(name = "date", required = false)
                                   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                   @RequestParam(name = "otherCategory", required = false) String otherCategory,
                                   Principal principal) {

        LoginUser loginUser = loginUserRepository.findByUserName(principal.getName())
                .orElseThrow(() -> new IllegalStateException("ユーザーが存在しません"));

        int userId = Integer.parseInt(loginUser.getUserId());
        spending.setUserId(userId);
        spending.setDeleteFlg(false);
        spending.setCreateUser(loginUser.getUserId());
        spending.setCreateDate(LocalDateTime.now());
        spending.setUpdateUser(loginUser.getUserId());
        spending.setUpdateDate(LocalDateTime.now());

        // category_id が 3 の場合は otherCategory を customCategoryName にセット
        if (spending.getCategoryId() == 3 && otherCategory != null && !otherCategory.isEmpty()) {
            spending.setCustomCategoryName(otherCategory);
        }

        spendingMapper.insert(spending);

        LocalDate redirectDate = (date != null) ? date : spending.getSpendingDate();
        return "redirect:/daily_calendar?date=" + redirectDate;
    }

    /**
     * 支出更新
     */
    @Transactional
    @PostMapping("/spending/update")
    public String updateSpending(Spending spending,
                                 @RequestParam(name = "date", required = false)
                                 @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                 @RequestParam(name = "otherCategory", required = false) String otherCategory,
                                 Principal principal) {

        LoginUser loginUser = loginUserRepository.findByUserName(principal.getName())
                .orElseThrow(() -> new IllegalStateException("ユーザーが存在しません"));

        spending.setUpdateUser(loginUser.getUserId());
        spending.setUpdateDate(LocalDateTime.now());

        // category_id が 3 の場合は otherCategory を customCategoryName にセット
        if (spending.getCategoryId() == 3 && otherCategory != null && !otherCategory.isEmpty()) {
            spending.setCustomCategoryName(otherCategory);
        }

        spendingMapper.update(spending);

        LocalDate redirectDate = (date != null) ? date : spending.getSpendingDate();
        return "redirect:/daily_calendar?date=" + redirectDate;
    }

    /**
     * 支出削除（論理削除）
     */
    @Transactional
    @PostMapping("/spending/delete")
    public String deleteSpending(@RequestParam int spendingId,
                                 @RequestParam(name = "date", required = false)
                                 @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                 Principal principal) {

        LoginUser loginUser = loginUserRepository.findByUserName(principal.getName())
                .orElseThrow(() -> new IllegalStateException("ユーザーが存在しません"));

        // 削除前にリダイレクト日付を決定
        LocalDate redirectDate = date;
        if (redirectDate == null) {
            Spending spending = spendingMapper.findById(spendingId);
            if (spending != null) {
                redirectDate = spending.getSpendingDate();
            } else {
                redirectDate = LocalDate.now();
            }
        }

        // 論理削除
        spendingMapper.logicalDelete(spendingId, loginUser.getUserId());

        return "redirect:/daily_calendar?date=" + redirectDate;
    }
}

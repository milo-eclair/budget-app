package com.example.budgetapp.controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.budgetapp.model.Category;
import com.example.budgetapp.model.LoginUser;
import com.example.budgetapp.repository.LoginUserRepository;
import com.example.budgetapp.repository.mybatis.CategoryMapper;

/**
 * カテゴリ管理用コントローラー
 * - 登録・削除・一覧表示を管理
 */
@Controller
public class CategoryController {

    private final CategoryMapper categoryMapper;
    private final LoginUserRepository loginUserRepository;

    public CategoryController(CategoryMapper categoryMapper, LoginUserRepository loginUserRepository) {
        this.categoryMapper = categoryMapper;
        this.loginUserRepository = loginUserRepository;
    }

    /**
     * カテゴリ登録処理
     */
    @Transactional
    @PostMapping("/category/register")
    public String register(@RequestParam String categoryName,
                           @RequestParam String date,
                           Principal principal) {

        LoginUser loginUser = loginUserRepository.findByUserName(principal.getName())
                .orElseThrow(() -> new IllegalArgumentException("指定されたユーザーが存在しません"));

        Category category = new Category();
        category.setCategoryName(categoryName);
        category.setUserId(Integer.parseInt(loginUser.getUserId()));
        category.setDeleteFlg(false);
        category.setCreateUser(loginUser.getUserId());
        category.setCreateDate(LocalDateTime.now());
        category.setUpdateUser(loginUser.getUserId());
        category.setUpdateDate(LocalDateTime.now());

        categoryMapper.insert(category);

        return "redirect:/daily_calendar?date=" + date;
    }

    /**
     * 登録済カテゴリを取得するメソッド（カテゴリ管理画面用）
     * Systemカテゴリは除外
     */
    public List<Category> getCategoryList(Principal principal) {
        LoginUser loginUser = loginUserRepository.findByUserName(principal.getName())
                .orElseThrow(() -> new IllegalArgumentException("指定されたユーザーが存在しません"));
        return categoryMapper.findByUserIdExcludingSystem(Integer.parseInt(loginUser.getUserId()));
    }

    /**
     * カテゴリ削除処理（論理削除）
     * Systemカテゴリは削除不可
     */
    @Transactional
    @PostMapping("/category/delete")
    public String delete(@RequestParam int categoryId,
                         @RequestParam String date,
                         Principal principal) {

        LoginUser loginUser = loginUserRepository.findByUserName(principal.getName())
                .orElseThrow(() -> new IllegalArgumentException("指定されたユーザーが存在しません"));

        Category category = categoryMapper.findById(categoryId);
        if (category == null) {
            throw new IllegalArgumentException("指定されたカテゴリが存在しません: " + categoryId);
        }

        // Systemカテゴリは削除不可
        if (category.getUserId() == 1) {
            throw new IllegalArgumentException("Systemカテゴリは削除できません");
        }

        category.setDeleteFlg(true);
        category.setUpdateUser(loginUser.getUserId());
        category.setUpdateDate(LocalDateTime.now());

        categoryMapper.update(category);

        return "redirect:/daily_calendar?date=" + date;
    }
}

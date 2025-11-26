package com.example.budgetapp.controller;

import java.time.LocalDateTime;

import jakarta.validation.Valid;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.budgetapp.controller.form.CreateLoginUserForm;
import com.example.budgetapp.model.LoginUser;
import com.example.budgetapp.repository.LoginUserRepository;

/**
 * ホーム画面を管理するコントローラー
 */
@Controller
public class HomeController {

    private final LoginUserRepository loginUserRepository;
    private final PasswordEncoder passwordEncoder;

    public HomeController(LoginUserRepository loginUserRepository, PasswordEncoder passwordEncoder) {
        this.loginUserRepository = loginUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /** ホーム画面 */
    @GetMapping("/")
    public String index() {
        return "home/index";
    }

    /** サインイン画面表示 */
    @GetMapping("/signin")
    public String showSignInForm(Model model) {
        model.addAttribute("createLoginUserForm", new CreateLoginUserForm());
        return "home/signin";
    }

    /** ユーザー登録 */
    @PostMapping("/signin")
    public String registerUser(@ModelAttribute @Valid CreateLoginUserForm createLoginUserForm,
                               BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "home/signin";
        }

        // パスワードをエンコード
        String encodedPassword = passwordEncoder.encode(createLoginUserForm.getPassword());

        // LoginUserを作成（全フィールド順番通りに指定）
        LoginUser loginUser = new LoginUser(
                null,                                  // userId
                createLoginUserForm.getUserName(),        // userName
                encodedPassword,                          // userPassword
                createLoginUserForm.getUserEmail(),       // userEmail
                createLoginUserForm.getUserPhoneNumber(), // userPhoneNumber
                false,                                    // deleteFlg
                "system",                                 // createUser
                LocalDateTime.now(),                      // createDate
                "system",                                 // updateUser
                LocalDateTime.now()                       // updateDate
        );

        // リポジトリに登録
        loginUserRepository.register(loginUser);

        return "redirect:/login";
    }

    /** ログイン画面表示 */
    @GetMapping("/login")
    public String showLoginForm() {
        return "home/login";
    }
}

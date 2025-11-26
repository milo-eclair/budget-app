package com.example.budgetapp.service;

import java.util.Optional;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.budgetapp.model.LoginUser;
import com.example.budgetapp.repository.LoginUserRepository;

/**
 * ログインユーザーの詳細を管理するサービス
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final LoginUserRepository loginUserRepository;

    public CustomUserDetailsService(LoginUserRepository loginUserRepository) {
        this.loginUserRepository = loginUserRepository;
    }

    /**
     * ユーザー名からログインユーザーの詳細を取得するように変更
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // userId ではなく userName で検索するよう変更
        Optional<LoginUser> loginUser = loginUserRepository.findByUserName(username);

        if (loginUser.isEmpty()) {
            throw new UsernameNotFoundException("ログインユーザーが見つかりませんでした");
        }

        LoginUser user = loginUser.get();

        return User
                .withUsername(user.getUserName())           // ユーザー名でログイン
                .password(user.getUserPassword())           // パスワードはそのまま
                .disabled(user.isDeleteFlg())               // deleteFlg を disabled に設定
                .roles("USER")                              // 権限は USER 固定
                .build();
    }
}

package com.example.budgetapp.controller.form;

import java.time.LocalDate;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.Data;

/**
 * 予定登録・更新用のフォーム
 */
@Data
public class ScheduleForm {

    /**
     * 予定タイトル
     */
    @NotBlank(message = "タイトルは必須です")
    @Size(min = 3, max = 255, message = "タイトルは3文字以上255文字以内で入力してください")
    private String title;

    /**
     * 予定開始日
     */
    @NotNull(message = "開始日は必須です")
    private LocalDate startDate;

    /**
     * 予定終了日
     */
    @NotNull(message = "終了日は必須です")
    private LocalDate endDate;

    /**
     * 予定金額
     */
    @NotNull(message = "金額は必須です")
    @Min(value = 0, message = "金額は0以上で入力してください")
    private Integer amount;

    /**
     * 予定詳細
     */
    @Size(max = 255, message = "詳細は255文字以内で入力してください")
    private String detail;

}

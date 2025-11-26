package com.example.budgetapp.model;

public class SpendingGraph {
    private String categoryName;  // カテゴリ名（例: 食費, 生活雑費）
    private int totalAmount;      // そのカテゴリの支出合計

    // --- Getter / Setter ---
    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }
}

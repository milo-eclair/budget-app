/*
============================================================
ファイル名    : createTable.sql
作成日        : 2025-10-05
作成者        : 矢野陸斗
用途          : データベース「budget_app」のテーブル作成用SQL
対象環境      : PostgreSQL 17 / 開発環境・AWS環境
更新履歴      :
  2025-10-05  tb_mst_user_login 作成
  2025-10-05  tb_mst_category 作成（user_id 外部キー、NULL可）
============================================================
注意事項      :
- 本ファイルを直接アプリから実行するものではなく、開発・初期構築用。
- 変更履歴は必ずコメント欄に記載すること。
- 実行前に必ずバックアップを取得すること。
============================================================
*/

-- カテゴリテーブル
CREATE TABLE tb_mst_category (
    category_id     SERIAL   PRIMARY KEY,             -- カテゴリID（主キー）
    category_name   VARCHAR(30)   NOT NULL,                -- カテゴリ名
    user_id         INTEGER,                            -- ユーザーID（外部キー、NULL可）
    delete_flg      BOOLEAN       DEFAULT FALSE,           -- 削除フラグ
    create_user     VARCHAR(10)   NOT NULL,                -- 作成者
    create_date     TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP, -- 作成日
    update_user     VARCHAR(10)   NOT NULL,                -- 更新者
    update_date     TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP, -- 更新日
    CONSTRAINT fk_user_category FOREIGN KEY(user_id) REFERENCES tb_mst_user_login(user_id)
);

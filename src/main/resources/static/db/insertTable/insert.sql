INSERT INTO tb_mst_category (
    category_id,
    category_name,
    user_id,
    delete_flg,
    create_user,
    create_date,
    update_user,
    update_date
) VALUES
('c01', '食費', NULL, false, 'system', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP),
('c02', '生活雑費', NULL, false, 'system', CURRENT_TIMESTAMP, 'system', CURRENT_TIMESTAMP);

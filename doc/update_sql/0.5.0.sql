INSERT INTO `sys_dict` (`code`, `title`, `remarks`, `editable`, `value_type`, `hash_code`, `deleted`, `create_time`, `update_time`)
VALUES ('user_status', '用户状态', NULL, 0, 1, '9527', 0, NOW(), NOW());

INSERT INTO `sys_dict_item` (`dict_code`, `value`, `name`, `attributes`, `sort`, `remarks`, `deleted`, `create_time`, `update_time`)
VALUES ('user_status', '0', '关闭', '{
    \"tagColor\": \"#d9d9d9\",
    \"languages\": {
        \"en-US\": \"Closure\",
        \"zh-CN\": \"关闭\"
    },
    \"textColor\": \"#d9d9d9\",
    \"badgeStatus\": \"default\"
}', 2, NULL, 0, NOW(), NULL)
     , ('user_status', '1', '正常', '{
    \"tagColor\": \"blue\",
    \"languages\": {
        \"en-US\": \"Normal\",
        \"zh-CN\": \"正常\"
    },
    \"textColor\": \"#5b8ff9\",
    \"badgeStatus\": \"processing\"
}', 1, NULL, 0, NOW(), NULL);

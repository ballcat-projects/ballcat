-- 修改测试客户端的 scope, 用于跳过验证码和密码解密
update oauth_client_details set scope = 'skip_captcha,skip_password_decode' where client_id = 'test'
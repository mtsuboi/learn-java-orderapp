INSERT INTO users(user_id,user_name,password,role)
SELECT 'odrappadmin','受注アプリ（管理者）','p@ssw0rd','ADMIN'
FROM dual WHERE NOT EXISTS (SELECT 1 FROM users WHERE user_id = 'odrappadmin');

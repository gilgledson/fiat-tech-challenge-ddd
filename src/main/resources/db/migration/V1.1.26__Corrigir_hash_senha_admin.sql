-- O hash seedado em V1.1.5 não correspondia de fato à senha documentada (admin123),
-- fazendo o login do admin padrão sempre falhar com 401. Nunca editamos uma migration
-- já aplicada, então corrigimos aqui com um UPDATE.
UPDATE USUARIO
SET senha_hash = '$2a$10$eOb3BYV4Q1GpADCdV4Kwn.2wyQWj2E/j77IgVHnYdFiMARlGmlQYq'
WHERE email = 'admin@oficina.com.br';

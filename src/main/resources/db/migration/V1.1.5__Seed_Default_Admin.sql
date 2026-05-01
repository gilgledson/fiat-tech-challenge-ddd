-- Inserção de um usuário administrador padrão para acesso inicial e testes
-- Email: admin@oficina.com.br
-- Senha: admin123
INSERT INTO USUARIO (id, email, senha_hash, perfil, ativo)
VALUES (
    'e0281660-5826-4c1f-8cab-238cdb1ac328', 
    'admin@oficina.com.br', 
    '$2a$10$GRLdNijSQMUvl/au9ShLqurqhXGAsvH.L0G6itv9x/U6S6Iuy2u5y', 
    'ADMIN', 
    true
) ON CONFLICT (email) DO NOTHING;

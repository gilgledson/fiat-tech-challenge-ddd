CREATE TABLE CLIENTE (
    id UUID PRIMARY KEY,
    usuario_id UUID UNIQUE,
    nome VARCHAR(255) NOT NULL,
    cpf_cnpj VARCHAR(20) NOT NULL UNIQUE,
    telefone VARCHAR(20),
    cep VARCHAR(9),
    logradouro VARCHAR(255),
    numero VARCHAR(20),
    complemento VARCHAR(100),
    bairro VARCHAR(100),
    cidade VARCHAR(100),
    uf CHAR(2),
    CONSTRAINT fk_cliente_usuario FOREIGN KEY (usuario_id) REFERENCES USUARIO(id)
);

CREATE TABLE VEICULO (
    id UUID PRIMARY KEY,
    cliente_id UUID NOT NULL,
    placa VARCHAR(10) NOT NULL UNIQUE,
    marca VARCHAR(100) NOT NULL,
    modelo VARCHAR(100) NOT NULL,
    ano INTEGER NOT NULL,
    CONSTRAINT fk_veiculo_cliente FOREIGN KEY (cliente_id) REFERENCES CLIENTE(id)
);
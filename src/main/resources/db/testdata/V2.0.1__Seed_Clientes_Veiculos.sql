-- Seed de 10 Clientes
INSERT INTO CLIENTE (id, nome, cpf_cnpj, email, telefone, cep, logradouro, numero, bairro, cidade, uf) VALUES 
('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'Ana Souza', '11122233344', 'ana@email.com', '11911111111', '01001000', 'Rua A', '10', 'Bairro 1', 'São Paulo', 'SP'),
('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12', 'Bruno Lima', '22233344455', 'bruno@email.com', '11922222222', '01001001', 'Rua B', '20', 'Bairro 2', 'São Paulo', 'SP'),
('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a13', 'Carla Dias', '33344455566', 'carla@email.com', '11933333333', '09001000', 'Rua C', '30', 'Bairro 3', 'Santo André', 'SP'),
('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a14', 'Daniel Alves', '44455566677', 'daniel@email.com', '11944444444', '09701000', 'Rua D', '40', 'Bairro 4', 'São Bernardo', 'SP'),
('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a15', 'Elaine Costa', '55566677788', 'elaine@email.com', '11955555555', '09501000', 'Rua E', '50', 'Bairro 5', 'São Caetano', 'SP'),
('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a16', 'Fábio Jonas', '66677788899', 'fabio@email.com', '11966666666', '07001000', 'Rua F', '60', 'Bairro 6', 'Guarulhos', 'SP'),
('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a17', 'Gisele B', '77788899900', 'gisele@email.com', '11977777777', '06001000', 'Rua G', '70', 'Bairro 7', 'Osasco', 'SP'),
('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a18', 'Hugo Gomes', '88899900011', 'hugo@email.com', '11988888888', '06401000', 'Rua H', '80', 'Bairro 8', 'Barueri', 'SP'),
('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a19', 'Iara Silva', '99900011122', 'iara@email.com', '11999999999', '08701000', 'Rua I', '90', 'Bairro 9', 'Mogi', 'SP'),
('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a20', 'Jonas Melo', '00011122233', 'jonas@email.com', '11900000000', '13001000', 'Rua J', '100', 'Bairro 10', 'Campinas', 'SP');

-- Seed de 10 Veículos (um para cada cliente)
INSERT INTO VEICULO (id, cliente_id, marca, modelo, ano, placa) VALUES 
('b0eebc99-9c0b-4ef8-bb6d-6bb9bd380b11', 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'Fiat', 'Palio', 2015, 'AAA1111'),
('b0eebc99-9c0b-4ef8-bb6d-6bb9bd380b12', 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12', 'Chevrolet', 'Onix', 2018, 'BBB2222'),
('b0eebc99-9c0b-4ef8-bb6d-6bb9bd380b13', 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a13', 'Ford', 'Ka', 2017, 'CCC3333'),
('b0eebc99-9c0b-4ef8-bb6d-6bb9bd380b14', 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a14', 'Hyundai', 'HB20', 2020, 'DDD4444'),
('b0eebc99-9c0b-4ef8-bb6d-6bb9bd380b15', 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a15', 'Renault', 'Kwid', 2021, 'EEE5555'),
('b0eebc99-9c0b-4ef8-bb6d-6bb9bd380b16', 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a16', 'Jeep', 'Renegade', 2019, 'FFF6666'),
('b0eebc99-9c0b-4ef8-bb6d-6bb9bd380b17', 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a17', 'Honda', 'Civic', 2022, 'GGG7777'),
('b0eebc99-9c0b-4ef8-bb6d-6bb9bd380b18', 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a18', 'Toyota', 'Yaris', 2021, 'HHH8888'),
('b0eebc99-9c0b-4ef8-bb6d-6bb9bd380b19', 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a19', 'Fiat', 'Uno', 2010, 'III9999'),
('b0eebc99-9c0b-4ef8-bb6d-6bb9bd380b20', 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a20', 'Volkswagen', 'Polo', 2023, 'JJJ0000');
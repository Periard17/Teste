CREATE TABLE Operadoras (
    Registro_ANS INT NOT NULL,
    CNPJ VARCHAR(14) NOT NULL,
    Razao_Social VARCHAR(255),
    Nome_Fantasia VARCHAR(255),
    Modalidade VARCHAR(50),
    Logradouro VARCHAR(255),
    Numero VARCHAR(50),
    Complemento VARCHAR(255),
    Bairro VARCHAR(100),
    Cidade VARCHAR(100),
    UF CHAR(2),
    CEP VARCHAR(8),
    DDD CHAR(2),
    Telefone VARCHAR(15),
    Fax VARCHAR(15),
    Endereco_eletronico VARCHAR(255),
    Representante VARCHAR(255),
    Cargo_Representante VARCHAR(100),
    Regiao_de_Comercializacao INT,
    Data_Registro_ANS DATE,
    PRIMARY KEY (Registro_ANS)
);
LOAD DATA INFILE 'C:\Users\administrativo\Desktop\The Mountain\Teste\Est3\Relatorio_cadop (1).csv'
INTO TABLE Operadoras
FIELDS TERMINATED BY ';'
ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 ROWS
(
    Registro_ANS,
    CNPJ,
    Razao_Social,
    Nome_Fantasia,
    Modalidade,
    Logradouro,
    Numero,
    Complemento,
    Bairro,
    Cidade,
    UF,
    CEP,
    DDD,
    Telefone,
    Fax,
    Endereco_eletronico,
    Representante,
    Cargo_Representante,
    Regiao_de_Comercializacao,
    Data_Registro_ANS
);


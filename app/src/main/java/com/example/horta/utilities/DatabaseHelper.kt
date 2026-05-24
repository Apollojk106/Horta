package com.example.horta.utilities

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "projeto_horta.db"
        private const val DATABASE_VERSION = 6
        private const val TAG = "DatabaseHelper"
    }

    override fun onCreate(db: SQLiteDatabase) {

        //Sessao de login local
        db.execSQL("""
            CREATE TABLE IF NOT EXISTS sessao (
                id_sessao INTEGER PRIMARY KEY AUTOINCREMENT,
                id_cliente INTEGER NOT NULL,
                token_sessao TEXT NOT NULL,
                data_login TEXT DEFAULT CURRENT_TIMESTAMP,
                data_expiracao TEXT,
                ativo INTEGER DEFAULT 1,
                FOREIGN KEY (id_cliente) REFERENCES cliente(id_cliente)
            )
        """)

        //sessao manual para poder testar o sistema
        db.execSQL("""
            INSERT OR IGNORE INTO sessao (id_sessao, id_cliente, token_sessao, data_login, data_expiracao, ativo) 
            VALUES (1, 1, 'token_sessao_padrao', datetime('now'), datetime('now', '+7 days'), 1);
        """)

        // Token precisaria ser web
        db.execSQL("""
            CREATE TABLE IF NOT EXISTS token_recuperacao (
                id_token INTEGER PRIMARY KEY AUTOINCREMENT,
                id_cliente INTEGER NOT NULL,
                token TEXT NOT NULL UNIQUE,
                data_criacao TEXT DEFAULT CURRENT_TIMESTAMP,
                data_expiracao TEXT NOT NULL,
                usado INTEGER DEFAULT 0,
                FOREIGN KEY (id_cliente) REFERENCES cliente(id_cliente)
            )
        """)

        //Carrinho local
        db.execSQL("""
            CREATE TABLE IF NOT EXISTS carrinho (
                id_carrinho INTEGER PRIMARY KEY AUTOINCREMENT,
                id_produto INTEGER NOT NULL,
                nome_produto TEXT NOT NULL,
                quantidade INTEGER NOT NULL,
                data_adicao TEXT DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY (id_produto) REFERENCES produto(id_produto)
            )
        """)



        // 1. Tabela FORNECEDOR
        db.execSQL("""
            CREATE TABLE IF NOT EXISTS fornecedor (
                id_fornecedor INTEGER PRIMARY KEY,
                produto TEXT,
                empresa TEXT,
                contato TEXT,
                endereco TEXT,
                cidade TEXT,
                cep TEXT,
                pais TEXT,
                data_inicio TEXT
            )
        """)

        // 2. Tabela CONTATO
        db.execSQL("""
            CREATE TABLE IF NOT EXISTS contato (
                id_contato INTEGER PRIMARY KEY AUTOINCREMENT,
                nome TEXT,
                email TEXT,
                assunto TEXT,
                mensagem TEXT
            )
        """)

        // 3. Tabela EVENTO
        db.execSQL("""
            CREATE TABLE IF NOT EXISTS evento (
                id_evento INTEGER PRIMARY KEY,
                descricao TEXT,
                dataEvento TEXT,
                preco_arrecadado REAL
            )
        """)

        // 4. Tabela DOACAO
        db.execSQL("""
            CREATE TABLE IF NOT EXISTS doacao (
                id_doacao INTEGER PRIMARY KEY AUTOINCREMENT,
                nome TEXT,
                email TEXT,
                mensagem TEXT,
                valor REAL,
                cpf TEXT NOT NULL,
                dataDoacao TEXT DEFAULT CURRENT_TIMESTAMP
            )
        """)

        // 5. Tabela VOLUNTARIO
        db.execSQL("""
            CREATE TABLE IF NOT EXISTS voluntario (
                id_voluntario INTEGER PRIMARY KEY AUTOINCREMENT,
                nome TEXT,
                telefone TEXT,
                email TEXT,
                senha TEXT,
                motivo TEXT,
                cep TEXT,
                dataCad TEXT DEFAULT CURRENT_TIMESTAMP
            )
        """)

        // 6. Tabela CLIENTE
        db.execSQL("""
            CREATE TABLE IF NOT EXISTS cliente (
                id_cliente INTEGER PRIMARY KEY AUTOINCREMENT,
                nome TEXT,
                email TEXT UNIQUE,
                senha TEXT,
                telefone TEXT,
                cep TEXT,
                dataCad TEXT DEFAULT CURRENT_TIMESTAMP
            )
        """)

        // 7. Tabela HORTA
        db.execSQL("""
            CREATE TABLE IF NOT EXISTS horta (
                id_horta INTEGER PRIMARY KEY,
                nome TEXT,
                endereco TEXT,
                id_fornecedor INTEGER,
                FOREIGN KEY (id_fornecedor) REFERENCES fornecedor(id_fornecedor)
            )
        """)

        // 8. Tabela TIPO_PRODUTO
        db.execSQL("""
            CREATE TABLE IF NOT EXISTS tipo_produto (
                id_tipo INTEGER PRIMARY KEY,
                nome_tipo TEXT
            )
        """)

        // 9. Tabela PRODUTO
        db.execSQL("""
            CREATE TABLE IF NOT EXISTS produto (
                id_produto INTEGER PRIMARY KEY,
                nome TEXT,
                quantidade INTEGER,
                data_colheita TEXT,
                preco REAL DEFAULT 0,
                id_horta INTEGER,
                id_tipo INTEGER,
                FOREIGN KEY (id_horta) REFERENCES horta(id_horta),
                FOREIGN KEY (id_tipo) REFERENCES tipo_produto(id_tipo)
            )
        """)

        // 10. Tabela CONTATO_PERFIL
        db.execSQL("""
            CREATE TABLE IF NOT EXISTS contato_perfil (
                id_contato INTEGER PRIMARY KEY,
                mensagem TEXT,
                telefone INTEGER,
                nome TEXT,
                id_cliente INTEGER,
                id_voluntario INTEGER,
                FOREIGN KEY (id_cliente) REFERENCES cliente(id_cliente),
                FOREIGN KEY (id_voluntario) REFERENCES voluntario(id_voluntario)
            )
        """)

        // 11. Tabela PEDIDO
        db.execSQL("""
            CREATE TABLE IF NOT EXISTS pedido (
                id_pedido INTEGER PRIMARY KEY,
                dataPedido TEXT DEFAULT CURRENT_TIMESTAMP,
                dataEntrega TEXT,
                id_cliente INTEGER,
                FOREIGN KEY (id_cliente) REFERENCES cliente(id_cliente)
            )
        """)

        // 12. Tabela PEDIDO_PRODUTO
        db.execSQL("""
            CREATE TABLE IF NOT EXISTS pedido_produto (
                quantidade INTEGER,
                id_pedido INTEGER,
                id_produto INTEGER,
                preco REAL,
                FOREIGN KEY (id_pedido) REFERENCES pedido(id_pedido),
                FOREIGN KEY (id_produto) REFERENCES produto(id_produto)
            )
        """)

        // 13. Tabela HORTA_EVENTO
        db.execSQL("""
            CREATE TABLE IF NOT EXISTS horta_evento (
                id_evento INTEGER,
                id_horta INTEGER,
                FOREIGN KEY (id_evento) REFERENCES evento(id_evento),
                FOREIGN KEY (id_horta) REFERENCES horta(id_horta)
            )
        """)

        // 14. Tabela HORTA_VOLUNTARIO
        db.execSQL("""
            CREATE TABLE IF NOT EXISTS horta_voluntario (
                id_voluntario INTEGER,
                id_horta INTEGER,
                data_serv TEXT,
                FOREIGN KEY (id_voluntario) REFERENCES voluntario(id_voluntario),
                FOREIGN KEY (id_horta) REFERENCES horta(id_horta)
            )
        """)

        // Inserir dados iniciais
        Log.d(TAG, "Tabelas criadas!")
        inserirDadosIniciais(db)
    }

    private fun inserirDadosIniciais(db: SQLiteDatabase) {

        // ========== 1. FORNECEDOR ==========
        db.execSQL("""
        INSERT OR IGNORE INTO fornecedor (id_fornecedor, produto, empresa, contato, endereco, cidade, cep, pais, data_inicio) VALUES
        (1, 'Adubo Orgânico', 'Verde Vida Ltda', 'Carlos Menezes', 'Rua das Flores 120', 'São Paulo', '01010000', 'Brasil', '2021-01-10'),
        (2, 'Ferramentas', 'Agro Forte', 'Fernanda Lima', 'Av Brasil 450', 'Campinas', '13010000', 'Brasil', '2020-05-15'),
        (3, 'Sementes', 'BioSeeds', 'Ricardo Alves', 'Rua Central 88', 'Curitiba', '80010000', 'Brasil', '2019-03-20'),
        (4, 'Irrigação', 'HidroCampo', 'Patrícia Souza', 'Rua Azul 77', 'Belo Horizonte', '30110000', 'Brasil', '2022-02-14')
    """)

        // ========== 2. CONTATO ==========
        db.execSQL("""
        INSERT OR IGNORE INTO contato (id_contato, nome, email, assunto, mensagem) VALUES
        (1, 'João Silva', 'joao@gmail.com', 'Dúvida', 'Gostaria de saber sobre os produtos'),
        (2, 'Maria Souza', 'maria@hotmail.com', 'Parceria', 'Tenho interesse em parceria'),
        (3, 'Pedro Alves', 'pedro@yahoo.com', 'Entrega', 'Qual o prazo de entrega?'),
        (4, 'Ana Lima', 'ana@gmail.com', 'Doação', 'Quero fazer uma doação')
    """)

        // ========== 3. EVENTO ==========
        db.execSQL("""
        INSERT OR IGNORE INTO evento (id_evento, descricao, dataEvento, preco_arrecadado) VALUES
        (1, 'Feira Orgânica de Verão', '2025-01-15', 1500.00),
        (2, 'Mutirão Comunitário', '2025-02-10', 800.00),
        (3, 'Workshop de Compostagem', '2025-03-12', 1200.00),
        (4, 'Festival Verde', '2025-04-18', 3000.00)
    """)

        // ========== 4. DOACAO ==========
        db.execSQL("""
        INSERT OR IGNORE INTO doacao (id_doacao, nome, email, mensagem, valor, cpf, dataDoacao) VALUES
        (1, 'João Silva', 'joao@gmail.com', 'Parabéns pelo projeto', 100.00, '12345678901', '2025-01-10'),
        (2, 'Maria Souza', 'maria@gmail.com', 'Continuem assim', 200.00, '12345678902', '2025-01-11'),
        (3, 'Pedro Alves', 'pedro@gmail.com', 'Apoio total', 150.00, '12345678903', '2025-01-12'),
        (4, 'Ana Lima', 'ana@gmail.com', 'Projeto incrível', 80.00, '12345678904', '2025-01-13')
    """)

        // ========== 5. VOLUNTARIO ==========
        db.execSQL("""
        INSERT OR IGNORE INTO voluntario (id_voluntario, nome, telefone, email, senha, motivo, cep, dataCad) VALUES
        (1, 'João Pedro', '11999990001', 'joao@teste.com', '123', 'Ajudar comunidade', '01001000', '2025-01-10'),
        (2, 'Maria Clara', '11999990002', 'maria@teste.com', '123', 'Aprender agricultura', '01002000', '2025-01-11'),
        (3, 'Lucas Lima', '11999990003', 'lucas@teste.com', '123', 'Trabalho social', '01003000', '2025-01-12'),
        (4, 'Ana Beatriz', '11999990004', 'ana@teste.com', '123', 'Contato com natureza', '01004000', '2025-01-13')
    """)

        // ========== 6. CLIENTE ==========
        db.execSQL("""
        INSERT OR IGNORE INTO cliente (id_cliente, nome, email, senha, telefone, cep, dataCad) VALUES
        (1, 'Bruno Silva', 'bruno@gmail.com', '123', '11988880001', '02001000', '2025-01-10'),
        (2, 'Camila Souza', 'camila@gmail.com', '123', '11988880002', '02002000', '2025-01-11'),
        (3, 'Diego Lima', 'diego@gmail.com', '123', '11988880003', '02003000', '2025-01-12'),
        (4, 'Elaine Costa', 'elaine@gmail.com', '123', '11988880004', '02004000', '2025-01-13')
    """)

        // ========== 7. HORTA ==========
        db.execSQL("""
        INSERT OR IGNORE INTO horta (id_horta, nome, endereco, id_fornecedor) VALUES
        (1, 'Horta Central', 'Rua Verde 10', 1),
        (2, 'Horta Norte', 'Rua Azul 20', 2),
        (3, 'Horta Sul', 'Rua Amarela 30', 3),
        (4, 'Horta Leste', 'Rua Branca 40', 4)
    """)

        // ========== 8. TIPO_PRODUTO ==========
        db.execSQL("""
        INSERT OR IGNORE INTO tipo_produto (id_tipo, nome_tipo) VALUES
        (1, 'Verdura'),
        (2, 'Legume'),
        (3, 'Fruta'),
        (4, 'Tempero')
    """)

        // ========== 9. PRODUTO ==========
        db.execSQL("""
        INSERT OR IGNORE INTO produto (id_produto, nome, quantidade, data_colheita, preco, id_horta, id_tipo) VALUES
        (1, 'Alface', 100, '2025-01-10', 5.50, 1, 1),
        (2, 'Tomate', 200, '2025-01-12', 8.00, 2, 2),
        (3, 'Cenoura', 150, '2025-01-15', 4.00, 3, 2),
        (4, 'Couve', 120, '2025-01-18', 3.50, 4, 1)
    """)

        // ========== 10. CONTATO_PERFIL ==========
        db.execSQL("""
        INSERT OR IGNORE INTO contato_perfil (id_contato, mensagem, telefone, nome, id_cliente, id_voluntario) VALUES
        (1, 'Contato sobre pedido', 119999111, 'Bruno Silva', 1, NULL),
        (2, 'Dúvida sobre entrega', 119999112, 'Camila Souza', 2, NULL),
        (3, 'Quero ser voluntário', 119999113, 'João Pedro', NULL, 1),
        (4, 'Ajuda na horta', 119999114, 'Maria Clara', NULL, 2)
    """)

        // ========== 11. PEDIDO ==========
        db.execSQL("""
        INSERT OR IGNORE INTO pedido (id_pedido, dataPedido, dataEntrega, id_cliente) VALUES
        (1, '2025-01-10', '2025-01-15', 1),
        (2, '2025-01-11', '2025-01-16', 2),
        (3, '2025-01-12', '2025-01-17', 3),
        (4, '2025-01-13', '2025-01-18', 4)
    """)

        // ========== 12. PEDIDO_PRODUTO ==========
        db.execSQL("""
        INSERT OR IGNORE INTO pedido_produto (quantidade, id_pedido, id_produto, preco) VALUES
        (2, 1, 1, 11.00),
        (5, 2, 2, 40.00),
        (3, 3, 3, 12.00),
        (4, 4, 4, 14.00)
    """)

        // ========== 13. HORTA_EVENTO ==========
        db.execSQL("""
        INSERT OR IGNORE INTO horta_evento (id_evento, id_horta) VALUES
        (1, 1),
        (2, 2),
        (3, 3),
        (4, 4)
    """)

        // ========== 14. HORTA_VOLUNTARIO ==========
        db.execSQL("""
        INSERT OR IGNORE INTO horta_voluntario (id_voluntario, id_horta, data_serv) VALUES
        (1, 1, '2025-01-05'),
        (2, 2, '2025-01-06'),
        (3, 3, '2025-01-07'),
        (4, 4, '2025-01-08')
    """)
        Log.d(TAG, "Tabelas preenchidas!")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS fornecedor")// tabelas fixas
        db.execSQL("DROP TABLE IF EXISTS contato")
        db.execSQL("DROP TABLE IF EXISTS evento")
        db.execSQL("DROP TABLE IF EXISTS doacao")
        db.execSQL("DROP TABLE IF EXISTS voluntario")
        db.execSQL("DROP TABLE IF EXISTS cliente")
        db.execSQL("DROP TABLE IF EXISTS horta")
        db.execSQL("DROP TABLE IF EXISTS tipo_produto")
        db.execSQL("DROP TABLE IF EXISTS produto")
        db.execSQL("DROP TABLE IF EXISTS contato_perfil")
        db.execSQL("DROP TABLE IF EXISTS pedido")
        db.execSQL("DROP TABLE IF EXISTS pedido_produto")
        db.execSQL("DROP TABLE IF EXISTS horta_evento")
        db.execSQL("DROP TABLE IF EXISTS horta_voluntario")

        db.execSQL("DROP TABLE IF EXISTS token_recuperacao")//utilidades necessarias
        db.execSQL("DROP TABLE IF EXISTS sessao")

        Log.d(TAG, "Tabelas derrubadas!")
        onCreate(db)
    }
}
package com.example.horta.utilities

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "projeto_horta.db"
        private const val DATABASE_VERSION = 11
        private const val TAG = "DatabaseHelper"
    }

    override fun onCreate(db: SQLiteDatabase) {

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

        db.execSQL("""
            CREATE TABLE IF NOT EXISTS contato (
                id_contato INTEGER PRIMARY KEY AUTOINCREMENT,
                nome TEXT,
                email TEXT,
                assunto TEXT,
                mensagem TEXT
            )
        """)

        db.execSQL("""
            CREATE TABLE IF NOT EXISTS evento (
                id_evento INTEGER PRIMARY KEY,
                descricao TEXT,
                dataEvento TEXT,
                preco_arrecadado REAL
            )
        """)

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

        db.execSQL("""
            CREATE TABLE IF NOT EXISTS tipo_produto (
                id_tipo INTEGER PRIMARY KEY,
                nome_tipo TEXT
            )
        """)

        // ========== 2. TABELAS COM DEPENDÊNCIAS ==========

        db.execSQL("""
            CREATE TABLE IF NOT EXISTS horta (
                id_horta INTEGER PRIMARY KEY,
                nome TEXT,
                endereco TEXT,
                id_fornecedor INTEGER,
                FOREIGN KEY (id_fornecedor) REFERENCES fornecedor(id_fornecedor)
            )
        """)

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

        db.execSQL("""
            CREATE TABLE IF NOT EXISTS pedido (
                id_pedido INTEGER PRIMARY KEY,
                dataPedido TEXT DEFAULT CURRENT_TIMESTAMP,
                dataEntrega TEXT,
                id_cliente INTEGER,
                FOREIGN KEY (id_cliente) REFERENCES cliente(id_cliente)
            )
        """)

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

        db.execSQL("""
            CREATE TABLE IF NOT EXISTS horta_evento (
                id_evento INTEGER,
                id_horta INTEGER,
                FOREIGN KEY (id_evento) REFERENCES evento(id_evento),
                FOREIGN KEY (id_horta) REFERENCES horta(id_horta)
            )
        """)

        db.execSQL("""
            CREATE TABLE IF NOT EXISTS horta_voluntario (
                id_voluntario INTEGER,
                id_horta INTEGER,
                data_serv TEXT,
                FOREIGN KEY (id_voluntario) REFERENCES voluntario(id_voluntario),
                FOREIGN KEY (id_horta) REFERENCES horta(id_horta)
            )
        """)

        // ========== 3. TABELAS EXTRAS (dependem de cliente e produto) ==========

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

        db.execSQL("""
            CREATE TABLE IF NOT EXISTS endereco (
                id_endereco INTEGER PRIMARY KEY AUTOINCREMENT,
                id_cliente INTEGER NOT NULL,
                rua TEXT,
                numero TEXT,
                bairro TEXT,
                cidade TEXT,
                estado TEXT,
                cep TEXT,
                complemento TEXT,
                data_cadastro TEXT DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY (id_cliente) REFERENCES cliente(id_cliente)
            )
        """)

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


        // Inserir fornecedores
        db.execSQL("""
            INSERT OR IGNORE INTO fornecedor (id_fornecedor, produto, empresa, contato, endereco, cidade, cep, pais, data_inicio) VALUES
            (1, 'Adubo Orgânico', 'Verde Vida Ltda', 'Carlos Menezes', 'Rua das Flores 120', 'São Paulo', '01010000', 'Brasil', '2021-01-10'),
            (2, 'Ferramentas', 'Agro Forte', 'Fernanda Lima', 'Av Brasil 450', 'Campinas', '13010000', 'Brasil', '2020-05-15'),
            (3, 'Sementes', 'BioSeeds', 'Ricardo Alves', 'Rua Central 88', 'Curitiba', '80010000', 'Brasil', '2019-03-20'),
            (4, 'Irrigação', 'HidroCampo', 'Patrícia Souza', 'Rua Azul 77', 'Belo Horizonte', '30110000', 'Brasil', '2022-02-14')
        """)

        // Inserir clientes
        db.execSQL("""
            INSERT OR IGNORE INTO cliente (id_cliente, nome, email, senha, telefone, cep) VALUES
            (1, 'Bruno Silva', 'bruno@gmail.com', '123', '11988880001', '02001000'),
            (2, 'Camila Souza', 'camila@gmail.com', '123', '11988880002', '02002000')
        """)

        // Inserir tipos de produto
        db.execSQL("""
            INSERT OR IGNORE INTO tipo_produto (id_tipo, nome_tipo) VALUES
            (1, 'Verdura'),
            (2, 'Legume'),
            (3, 'Fruta'),
            (4, 'Tempero')
        """)

        // Inserir hortas
        db.execSQL("""
            INSERT OR IGNORE INTO horta (id_horta, nome, endereco, id_fornecedor) VALUES
            (1, 'Horta Central', 'Rua Verde 10', 1),
            (2, 'Horta Norte', 'Rua Azul 20', 2)
        """)

        // Inserir produtos
        db.execSQL("""
            INSERT OR IGNORE INTO produto (id_produto, nome, quantidade, data_colheita, preco, id_horta, id_tipo) VALUES
            (1, 'Alface', 100, '2025-01-10', 5.50, 1, 1),
            (2, 'Tomate', 200, '2025-01-12', 8.00, 2, 2),
            (3, 'Cenoura', 150, '2025-01-15', 4.00, 1, 2),
            (4, 'Couve', 120, '2025-01-18', 3.50, 2, 1)
        """)

        // Sessão manual para teste
        db.execSQL("""
            INSERT OR IGNORE INTO sessao (id_sessao, id_cliente, token_sessao, data_login, data_expiracao, ativo) 
            VALUES (1, 1, 'token_sessao_padrao', datetime('now'), datetime('now', '+7 days'), 1)
        """)

        Log.d(TAG, "Tabelas criadas e dados inseridos com sucesso!")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS carrinho")
        db.execSQL("DROP TABLE IF EXISTS token_recuperacao")
        db.execSQL("DROP TABLE IF EXISTS sessao")
        db.execSQL("DROP TABLE IF EXISTS endereco")
        db.execSQL("DROP TABLE IF EXISTS pedido_produto")
        db.execSQL("DROP TABLE IF EXISTS pedido")
        db.execSQL("DROP TABLE IF EXISTS horta_evento")
        db.execSQL("DROP TABLE IF EXISTS horta_voluntario")
        db.execSQL("DROP TABLE IF EXISTS contato_perfil")
        db.execSQL("DROP TABLE IF EXISTS produto")
        db.execSQL("DROP TABLE IF EXISTS horta")
        db.execSQL("DROP TABLE IF EXISTS tipo_produto")
        db.execSQL("DROP TABLE IF EXISTS cliente")
        db.execSQL("DROP TABLE IF EXISTS voluntario")
        db.execSQL("DROP TABLE IF EXISTS doacao")
        db.execSQL("DROP TABLE IF EXISTS evento")
        db.execSQL("DROP TABLE IF EXISTS contato")
        db.execSQL("DROP TABLE IF EXISTS fornecedor")

        Log.d(TAG, "Tabelas antigas removidas, recriando...")
        onCreate(db)
    }
}
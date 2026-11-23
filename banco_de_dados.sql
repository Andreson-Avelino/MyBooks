-- MyBooks v1.0.0
-- Script de criacao da tabela de livros no SQLite.
-- A estrutura abaixo corresponde ao esquema descrito para o aplicativo.
-- O aplicativo cria o banco automaticamente por meio do DatabaseHelper.

CREATE TABLE IF NOT EXISTS livros (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    titulo TEXT NOT NULL,
    autor TEXT NOT NULL,
    sinopse TEXT NOT NULL,
    favorito INTEGER NOT NULL DEFAULT 0
);

CREATE TABLE servico (
    id TEXT PRIMARY KEY UNIQUE NOT NULL,
    descricao TEXT NOT NULL,
    preco decimal NOT NULL,
    user_id   TEXT                    NOT NULL
        constraint fk_servico_users
            references users (id)
);
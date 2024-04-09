CREATE TABLE pessoa (
    id TEXT PRIMARY KEY UNIQUE NOT NULL,
    nome TEXT NOT NULL,
    numero TEXT NOT NULL,
    user_id   TEXT                    NOT NULL
        constraint fk_pessoa_users
            references users (id)
);
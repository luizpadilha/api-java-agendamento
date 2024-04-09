CREATE TABLE notificacao
(
    id        TEXT PRIMARY KEY UNIQUE NOT NULL,
    titulo    TEXT                    NOT NULL,
    descricao TEXT                    NOT NULL,
    user_id   TEXT                    NOT NULL
        constraint fk_notificacao_users
            references users (id)
);
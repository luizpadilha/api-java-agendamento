CREATE TABLE if not exists agenda
(
    id         TEXT PRIMARY KEY UNIQUE NOT NULL,
    horario    timestamp               NOT NULL,
    pessoa_id  TEXT                    NOT NULL
    constraint fk_agenda_pessoa
    references pessoa (id),
    servico_id TEXT                    NOT NULL
    constraint fk_agenda_servico
    references servico (id),
    user_id    TEXT                    NOT NULL
    constraint fk_agenda_users
    references users (id)
    );
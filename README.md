# API de Agendamento de Serviço

Esta é uma API Spring que fornece endpoints para gerenciar agendamentos de serviço, com segurança de autenticação por JWT.

## Autenticação

### login
Endpoint para autenticar usuários e obter token JWT.

- **URL:** `/api/auth/login`
- **Método HTTP:** POST
- **Corpo da Solicitação (JSON):**
  ```json
  {
      "username": "usuario",
      "password": "senha"
  }
- **Código de Status:** 200 OK
- **Corpo da Resposta (JSON):**
   ```json
   {
    "token": "token_jwt"
    "id": "bc6a87a8-807a-430b-9f3a-2fb692"
   }

## Funcionalidades

- Pessoas: Os usuários podem adicionar/remover/editar informações sobre clientes, como nome, telefone.
- Serviços: Os usuários podem adicionar/remover/editar informações sobre os serviços oferecidos, como descrição, preço, etc.
- Horários: Os usuários podem adicionar/remover/editar horários para os serviços, associando-os a clientes específicos e serviços selecionados.

## Pré-requisitos

- java SDK: 17
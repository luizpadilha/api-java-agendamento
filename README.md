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
## Testes Automatizados

## JUnit5

O JUnit é um framework de testes automatizados para a linguagem Java, e nesse projeto estamos utilizando o JUnit 5.
Ele fornece uma série de anotações, classes e métodos auxiliares para facilitar a criação e execução de testes.
A documentação oficial do JUnit pode ser encontrada [aqui](https://junit.org/junit5/docs/current/user-guide/).

## Geração de Relatório de Cobertura com JaCoCo

O JaCoCo é uma ferramenta de análise de cobertura de testes para a plataforma Java. Ele permite medir a porcentagem de código coberta pelos testes automatizados, identificando as partes do código que não foram testadas.

### Passo a Passo para Gerar o Relatório de Cobertura

1. Execute os testes automatizados [maven > lifecycle > verify].

2. Acesse o relatório de cobertura. Abra o arquivo `index.html` localizado no diretório `target/site/jacoco` do projeto em um navegador web.
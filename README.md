# SGAEM - Sistema de Gestão Empresarial

## Sobre o Projeto

O SGAEM (Sistema de Gestão Empresarial) é uma aplicação desenvolvida com o objetivo de otimizar e centralizar processos administrativos e operacionais, oferecendo uma solução eficiente para controle, análise e integração de dados corporativos.

O sistema permite o gerenciamento de informações em tempo real, contribuindo para maior produtividade, organização e suporte à tomada de decisão.

---

## Funcionalidades

- Gestão de produtos  
- Controle de movimentações (entrada, saída e transferência)  
- Gestão de usuários e permissões  
- Integração com APIs externas  
- Geração de relatórios e indicadores  
- Controle de unidades/filiais  
- Busca de dados  

---

## Tecnologias Utilizadas

### Backend
- Node.js / Java (ajustar conforme o projeto)  
- API REST  
- Integração via HTTP/JSON  

### Frontend
- Angular  
- TypeScript  
- HTML5 e CSS3  

### Banco de Dados
- PostgreSQL  

### Outros
- Git / GitLab  
- Docker (se aplicável)  
- CI/CD (se aplicável)  

---

## Estrutura do Projeto

    SGAEM/
    ├── backend/
    │   ├── src/
    │   ├── controllers/
    │   ├── services/
    │   └── models/
    ├── frontend/
    │   ├── src/
    │   ├── app/
    │   └── assets/
    ├── database/
    │   └── scripts/
    ├── docs/
    └── README.md

---

## Como Executar o Projeto

### Pré-requisitos

- Node.js  
- PostgreSQL  
- Angular CLI  
- Git  

---

### Backend

    cd backend
    npm install
    npm run start

---

### Frontend

    cd frontend
    npm install
    ng serve

A aplicação estará disponível em:

    http://localhost:4200

---

### Banco de Dados

1. Criar o banco no PostgreSQL  
2. Executar os scripts localizados em:

    /database/scripts

---

## Variáveis de Ambiente

Crie um arquivo `.env` no backend com as seguintes variáveis:

    DB_HOST=localhost
    DB_PORT=5432
    DB_NAME=sgaem
    DB_USER=postgres
    DB_PASS=senha
    PORT=3000

---

## Integrações

O sistema possui integração com APIs externas para sincronização de dados, consultas e automação de processos.

---

## Testes

    npm run test

---

## Deploy

O deploy pode ser realizado em ambiente Linux, via containers Docker ou em provedores de nuvem como AWS e Azure.

---

## Autor

Nacles Bernardino Pirajá Gomes Junior  
Email: naclesjunior@gmail.com  
GitHub: https://github.com/nacles  
LinkedIn: https://linkedin.com/in/nacles  

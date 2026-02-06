# Rockstars App - Sistema de Gerenciamento de Artistas e Álbuns

Sistema fullstack para gerenciamento de artistas e álbuns musicais, com suporte a upload de capas, notificações em tempo real e categorização regional.

---

## Informações do Candidato

| Campo | Informação |
|-------|------------|
| **Número de Inscrição** | 16346 |
| **Nome** | Diego Lucio da Cunha Lobo |
| **Cargo** | Analista de Tecnologia da Informação (Engenheiro de Computação Sênior) |

---

## Demonstração

| Ambiente | URL |
|----------|-----|
| **Frontend** | [https://rockstars.dots.dev.br](https://rockstars.dots.dev.br) |
| **API Backend** | [https://rockstars-api.dots.dev.br](https://rockstars-api.dots.dev.br) |
| **Swagger UI** | [https://rockstars-api.dots.dev.br/swagger-ui](https://rockstars-api.dots.dev.br/swagger-ui) |

### Credenciais de Teste

```
Usuário: rockstar
Senha: MyWay0rTh3H1ghw@y
```

---

## Funcionalidades

### Backend (API REST)

- ✅ **Autenticação JWT** com refresh token automático
- ✅ **CRUD de Artistas** com paginação, busca e ordenação
- ✅ **CRUD de Álbuns** com associação N:N com artistas
- ✅ **Upload de Capas** via MinIO (S3-compatible)
- ✅ **WebSocket** para notificações em tempo real
- ✅ **Rate Limiting** (10 requisições/minuto)
- ✅ **Documentação Swagger/OpenAPI**
- ✅ **Sincronização de Regionais** via API externa
- ✅ **Soft Delete** para artistas e álbuns
- ✅ **Controle de Acesso** baseado em roles (ADMIN/USER)

### Frontend (SPA React)

- ✅ **Autenticação JWT** com renovação automática
- ✅ **Listagem de Artistas** com busca, ordenação e paginação
- ✅ **Detalhamento de Artistas** com álbuns associados
- ✅ **Cadastro/Edição** de artistas e álbuns
- ✅ **Upload de Capas** com preview
- ✅ **Notificações em Tempo Real** via WebSocket
- ✅ **Tema Claro/Escuro**
- ✅ **Layout Responsivo** (mobile-first)
- ✅ **Lazy Loading** de rotas

---

## Stack Tecnológica

### Backend

| Tecnologia | Versão | Descrição |
|------------|--------|-----------|
| **Quarkus** | 3.31.1 | Framework Java supersônico |
| **Java** | 21 | Linguagem de programação |
| **PostgreSQL** | 16 | Banco de dados relacional |
| **Hibernate/Panache** | - | ORM e padrão Active Record |
| **SmallRye JWT** | - | Autenticação JWT |
| **MinIO** | Latest | Object Storage (S3-compatible) |
| **Flyway** | - | Migrações de banco de dados |
| **Bucket4j** | 1.0.7 | Rate Limiting |
| **BCrypt** | 0.4 | Hash de senhas |

### Frontend

| Tecnologia | Versão | Descrição |
|------------|--------|-----------|
| **React** | 19.2.0 | Biblioteca UI |
| **TypeScript** | 5.9.3 | Tipagem estática |
| **Vite** | 7.2.4 | Build tool |
| **Tailwind CSS** | 4.1.18 | Framework CSS |
| **React Router** | 7.13.0 | Roteamento SPA |
| **Axios** | 1.9.0 | Cliente HTTP |
| **Vitest** | 3.2.4 | Framework de testes |

### Infraestrutura

| Tecnologia | Descrição |
|------------|-----------|
| **Docker** | Containerização |
| **Docker Compose** | Orquestração de containers |
| **Nginx** | Servidor web para frontend |
| **Netlify** | Deploy do frontend |

---

## Arquitetura

Para detalhes completos da arquitetura, consulte [ARCHITECTURE.md](./ARCHITECTURE.md).

### Visão Geral

```
┌─────────────────────────────────────────────────────────────────┐
│                         FRONTEND                                 │
│  React 19 + TypeScript + Vite + Tailwind CSS                    │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐              │
│  │   Pages     │  │  Components │  │  Contexts   │              │
│  └──────┬──────┘  └──────┬──────┘  └──────┬──────┘              │
│         └────────────────┼────────────────┘                      │
│                    ┌─────┴─────┐                                 │
│                    │  Facades  │                                 │
│                    └─────┬─────┘                                 │
│                    ┌─────┴─────┐                                 │
│                    │ Services  │                                 │
│                    └─────┬─────┘                                 │
└──────────────────────────┼──────────────────────────────────────┘
                           │ HTTP/WebSocket
┌──────────────────────────┼──────────────────────────────────────┐
│                     BACKEND API                                  │
│  Quarkus 3.31 + Java 21 + JWT + WebSocket                       │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐              │
│  │  Resources  │  │  Services   │  │ Repositories│              │
│  │  (REST API) │  │  (Business) │  │   (Data)    │              │
│  └──────┬──────┘  └──────┬──────┘  └──────┬──────┘              │
└─────────┼────────────────┼────────────────┼─────────────────────┘
          │                │                │
┌─────────┼────────────────┼────────────────┼─────────────────────┐
│         │           INFRAESTRUTURA        │                      │
│  ┌──────┴──────┐  ┌──────┴──────┐  ┌──────┴──────┐              │
│  │ PostgreSQL  │  │    MinIO    │  │  External   │              │
│  │   (Data)    │  │  (Storage)  │  │    APIs     │              │
│  └─────────────┘  └─────────────┘  └─────────────┘              │
└─────────────────────────────────────────────────────────────────┘
```

---

## Estrutura do Projeto

```
rockstars-app/
├── back-end/                    # API Backend (Quarkus)
│   ├── src/main/java/          # Código fonte Java
│   │   └── br/com/rockstars/
│   │       ├── client/         # Clientes REST externos
│   │       ├── config/         # Configurações e inicialização
│   │       ├── domain/         # DTOs, Entities, Enums
│   │       ├── exception/      # Tratamento de exceções
│   │       ├── repository/     # Repositórios de dados
│   │       ├── resource/v1/    # Endpoints REST
│   │       ├── service/        # Lógica de negócio
│   │       └── websocket/      # WebSocket handlers
│   ├── src/main/resources/
│   │   ├── db/migration/       # Migrações Flyway
│   │   └── application.properties
│   ├── Dockerfile
│   └── build.gradle
│
├── front-end/                   # Frontend (React)
│   ├── src/
│   │   ├── components/         # Componentes React
│   │   │   ├── ui/             # Componentes de UI reutilizáveis
│   │   │   ├── Header/
│   │   │   ├── Footer/
│   │   │   └── Layout/
│   │   ├── contexts/           # AuthContext, ThemeContext
│   │   ├── facades/            # Camada de abstração
│   │   ├── hooks/              # Custom hooks
│   │   ├── pages/              # Páginas da aplicação
│   │   ├── services/           # Serviços HTTP
│   │   ├── types/              # Tipos TypeScript
│   │   └── test/               # Configuração de testes
│   ├── Dockerfile
│   ├── nginx.conf
│   └── package.json
│
├── docker-compose.yaml          # Orquestração de containers
├── .env.example                 # Template de variáveis de ambiente
├── README.md                    # Este arquivo
└── ARCHITECTURE.md              # Documentação de arquitetura
```

---

## Instalação e Execução

### Pré-requisitos

- Docker e Docker Compose
- Node.js 22+ (para desenvolvimento frontend)
- Java 21+ e Gradle (para desenvolvimento backend)

### Execução com Docker Compose

```bash
# 1. Clone o repositório
git clone https://github.com/diegolobo/diegoluciodacunhalobo002256.git
cd rockstars-app

# 2. Configure as variáveis de ambiente
cp .env.example .env
# Edite o arquivo .env com suas configurações

# 3. Inicie todos os serviços
docker-compose up -d --build

# 4. Acesse a aplicação
# Frontend: http://localhost:8080
# Backend API: http://localhost:3000
# Swagger UI: http://localhost:3000/swagger-ui
```

### Desenvolvimento Local

#### Backend

```bash
cd back-end

# Modo desenvolvimento com live reload
./gradlew quarkusDev

# Build para produção
./gradlew build

# Executar testes
./gradlew test
```

#### Frontend

```bash
cd front-end

# Instalar dependências
npm install

# Modo desenvolvimento
npm run dev

# Build para produção
npm run build

# Executar testes
npm run test:run

# Linting
npm run lint
```

---

## API Endpoints

### Autenticação

| Método | Endpoint | Descrição | Auth |
|--------|----------|-----------|------|
| POST | `/api/v1/auth/login` | Login do usuário | Não |
| POST | `/api/v1/auth/register` | Registro de usuário | Não |
| POST | `/api/v1/auth/refresh` | Renovar token | Sim |

### Artistas

| Método | Endpoint | Descrição | Auth |
|--------|----------|-----------|------|
| GET | `/api/v1/artists` | Listar artistas | Sim |
| GET | `/api/v1/artists/{id}` | Buscar artista | Sim |
| GET | `/api/v1/artists/{id}/albums` | Álbuns do artista | Sim |
| POST | `/api/v1/artists` | Criar artista | ADMIN |
| PUT | `/api/v1/artists/{id}` | Atualizar artista | ADMIN |
| DELETE | `/api/v1/artists/{id}` | Desativar artista | ADMIN |

### Álbuns

| Método | Endpoint | Descrição | Auth |
|--------|----------|-----------|------|
| GET | `/api/v1/albums` | Listar álbuns | Sim |
| GET | `/api/v1/albums/{id}` | Buscar álbum | Sim |
| POST | `/api/v1/albums` | Criar álbum | ADMIN |
| PUT | `/api/v1/albums/{id}` | Atualizar álbum | ADMIN |
| DELETE | `/api/v1/albums/{id}` | Desativar álbum | ADMIN |
| POST | `/api/v1/albums/{id}/artists/{artistId}` | Vincular artista | ADMIN |
| DELETE | `/api/v1/albums/{id}/artists/{artistId}` | Desvincular artista | ADMIN |

### Capas de Álbum

| Método | Endpoint | Descrição | Auth |
|--------|----------|-----------|------|
| GET | `/api/v1/albums/{id}/covers` | Listar capas | Sim |
| POST | `/api/v1/albums/{id}/covers` | Upload de capa | ADMIN |
| DELETE | `/api/v1/albums/{id}/covers/{coverId}` | Remover capa | ADMIN |

---

## Testes

### Backend (JUnit 5 + REST Assured)

```bash
cd back-end
./gradlew test
```

### Frontend (Vitest + React Testing Library)

```bash
cd front-end

# Executar testes
npm run test:run

# Testes com coverage
npm run test:coverage
```

**Cobertura de Testes Frontend:**
- Serviços de autenticação
- Facades de autenticação
- Componentes de Login
- Página Home (listagem)
- PrivateRoute (proteção de rotas)

---

## Deploy

### Frontend (Netlify)

1. Conecte o repositório no Netlify
2. Configure:
   - **Base directory:** `front-end`
   - **Build command:** `npm run build`
   - **Publish directory:** `front-end/dist`
3. Adicione variável de ambiente:
   - `VITE_API_URL=https://rockstars-api.dots.dev.br`

### Backend (Docker)

```bash
# Build da imagem
docker build -t rockstars-api ./back-end

# Executar
docker run -p 3000:8080 rockstars-api
```

---

## Variáveis de Ambiente

Consulte o arquivo [.env.example](./.env.example) para a lista completa de variáveis.

| Variável | Descrição | Exemplo |
|----------|-----------|---------|
| `POSTGRES_USER` | Usuário do banco | `sa_rockstar` |
| `POSTGRES_PASSWORD` | Senha do banco | `*****` |
| `MINIO_ACCESS_KEY` | Chave de acesso MinIO | `*****` |
| `MINIO_SECRET_KEY` | Chave secreta MinIO | `*****` |
| `ADMIN_USERNAME` | Usuário admin inicial | `admin` |
| `ADMIN_PASSWORD` | Senha admin inicial | `*****` |
| `JWT_ISSUER` | Emissor do JWT | `https://rockstars.com.br` |
| `VITE_API_URL` | URL da API para o frontend | `http://localhost:3000` |

---

## Licença

Este projeto foi desenvolvido como parte de processo seletivo e é de uso exclusivo para avaliação técnica.

---

## Autor

**Diego Lucio da Cunha Lobo**

- GitHub: [@diegolobo](https://github.com/diegolobo)

# Rockstars App - Arquitetura do Sistema

Este documento descreve a arquitetura técnica completa do sistema Rockstars App, incluindo backend (API REST) e frontend (SPA React).

---

## Visão Geral

O Rockstars App é um sistema fullstack para gerenciamento de artistas e álbuns musicais, composto por:

- **Backend**: API REST desenvolvida em Quarkus (Java 21)
- **Frontend**: Single Page Application (SPA) em React 19 + TypeScript
- **Infraestrutura**: PostgreSQL, MinIO (Object Storage), Docker

### Diagrama de Arquitetura

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                              FRONTEND (SPA)                                  │
│                    React 19 + TypeScript + Vite + Tailwind                  │
│  ┌─────────────┐   ┌─────────────┐   ┌─────────────┐   ┌─────────────┐      │
│  │    Pages    │   │  Components │   │   Contexts  │   │    Hooks    │      │
│  │  - Login    │   │  - Header   │   │ - AuthCtx   │   │- useWebSock │      │
│  │  - Home     │   │  - Footer   │   │ - ThemeCtx  │   │             │      │
│  │  - Artist*  │   │  - Layout   │   │             │   │             │      │
│  │  - Album*   │   │  - UI/*     │   │             │   │             │      │
│  └──────┬──────┘   └──────┬──────┘   └──────┬──────┘   └──────┬──────┘      │
│         └──────────────────┼─────────────────┴─────────────────┘            │
│                      ┌─────┴─────┐                                          │
│                      │  Facades  │ ← Camada de abstração                    │
│                      │ - auth    │                                          │
│                      │ - artist  │                                          │
│                      │ - album   │                                          │
│                      └─────┬─────┘                                          │
│                      ┌─────┴─────┐                                          │
│                      │ Services  │ ← Comunicação HTTP                       │
│                      │ - api     │                                          │
│                      │ - auth    │                                          │
│                      │ - artist  │                                          │
│                      │ - album   │                                          │
│                      │ - websock │                                          │
│                      └─────┬─────┘                                          │
└────────────────────────────┼────────────────────────────────────────────────┘
                             │ HTTP/REST + WebSocket
                             │
┌────────────────────────────┼────────────────────────────────────────────────┐
│                       BACKEND API                                            │
│              Quarkus 3.31 + Java 21 + JWT + WebSocket                       │
│                      ┌─────┴─────┐                                          │
│                      │ Resources │ ← REST Endpoints (JAX-RS)                │
│                      │ /api/v1/* │                                          │
│                      └─────┬─────┘                                          │
│                      ┌─────┴─────┐                                          │
│                      │ Services  │ ← Lógica de Negócio                      │
│                      └─────┬─────┘                                          │
│                      ┌─────┴─────┐                                          │
│                      │Repository │ ← Panache (Active Record)                │
│                      └─────┬─────┘                                          │
│  ┌─────────────┐   ┌──────┴──────┐   ┌─────────────┐   ┌─────────────┐      │
│  │   Domain    │   │   Config    │   │  Exception  │   │  WebSocket  │      │
│  │ - Entities  │   │ - MinIO     │   │ - Global    │   │ - Notific.  │      │
│  │ - DTOs      │   │ - Health    │   │   Handler   │   │             │      │
│  │ - Enums     │   │ - Admin     │   │             │   │             │      │
│  └─────────────┘   └─────────────┘   └─────────────┘   └─────────────┘      │
└─────────┬────────────────┬────────────────┬─────────────────────────────────┘
          │                │                │
┌─────────┴──────┐  ┌──────┴──────┐  ┌──────┴──────┐
│   PostgreSQL   │  │    MinIO    │  │  External   │
│   (Database)   │  │  (Storage)  │  │    APIs     │
│                │  │             │  │ (Regionais) │
└────────────────┘  └─────────────┘  └─────────────┘
```

---

## Backend (API REST)

### Stack Tecnológica

| Componente | Tecnologia | Versão |
|------------|------------|--------|
| Framework | Quarkus | 3.31.1 |
| Linguagem | Java | 21 (LTS) |
| Banco de Dados | PostgreSQL | 16 |
| ORM | Hibernate + Panache | - |
| Migrações | Flyway | - |
| Autenticação | SmallRye JWT | - |
| Object Storage | MinIO (S3-compat) | Latest |
| Rate Limiting | Bucket4j | 1.0.7 |
| Documentação | OpenAPI/Swagger | - |

### Estrutura de Pacotes

```
br.com.rockstars/
├── client/                 # Clientes REST externos
│   └── RegionalClient.java # Cliente para API de regionais
├── config/                 # Configurações e inicialização
│   ├── AdminUserInitializer.java
│   ├── DatabaseHealthCheck.java
│   ├── MinioConfig.java
│   └── MinioHealthCheck.java
├── domain/                 # Camada de domínio
│   ├── dto/               # Data Transfer Objects
│   │   ├── AlbumDTO.java
│   │   ├── AlbumRequestDTO.java
│   │   ├── ArtistDTO.java
│   │   ├── ArtistRequestDTO.java
│   │   ├── LoginRequestDTO.java
│   │   ├── TokenResponseDTO.java
│   │   └── ...
│   ├── entity/            # Entidades JPA
│   │   ├── Artist.java
│   │   ├── Album.java
│   │   ├── AlbumCover.java
│   │   ├── User.java
│   │   ├── Regional.java
│   │   └── BaseEntity.java
│   └── enums/             # Enumerações
│       └── ArtistType.java
├── exception/              # Tratamento de exceções
│   ├── BusinessException.java
│   ├── NotFoundException.java
│   └── GlobalExceptionHandler.java
├── repository/             # Repositórios Panache
│   ├── ArtistRepository.java
│   ├── AlbumRepository.java
│   ├── AlbumCoverRepository.java
│   ├── UserRepository.java
│   └── RegionalRepository.java
├── resource/v1/            # Endpoints REST
│   ├── AuthResource.java
│   ├── ArtistResource.java
│   ├── AlbumResource.java
│   ├── AlbumCoverResource.java
│   ├── EnumResource.java
│   └── RegionalResource.java
├── service/                # Lógica de negócio
│   ├── AuthService.java
│   ├── ArtistService.java
│   ├── AlbumService.java
│   ├── AlbumCoverService.java
│   ├── StorageService.java
│   └── RegionalSyncService.java
└── websocket/              # WebSocket handlers
    └── AlbumNotificationSocket.java
```

### Modelo de Dados

```
┌──────────────────┐       ┌──────────────────┐       ┌──────────────────┐
│      USER        │       │     ARTIST       │       │     REGIONAL     │
├──────────────────┤       ├──────────────────┤       ├──────────────────┤
│ id: BIGINT PK    │       │ id: BIGINT PK    │       │ id: BIGINT PK    │
│ username: VARCHAR│       │ name: VARCHAR    │       │ name: VARCHAR    │
│ password: VARCHAR│       │ type: ENUM       │       │ external_id: INT │
│ role: VARCHAR    │       │ active: BOOLEAN  │◄──────│                  │
│ created_at: TS   │       │ regional_id: FK  │       │                  │
│ updated_at: TS   │       │ created_at: TS   │       └──────────────────┘
└──────────────────┘       │ updated_at: TS   │
                           └────────┬─────────┘
                                    │ N:N
                           ┌────────┴─────────┐
                           │  ARTIST_ALBUM    │
                           ├──────────────────┤
                           │ artist_id: FK    │
                           │ album_id: FK     │
                           └────────┬─────────┘
                                    │
                           ┌────────┴─────────┐       ┌──────────────────┐
                           │      ALBUM       │       │   ALBUM_COVER    │
                           ├──────────────────┤       ├──────────────────┤
                           │ id: BIGINT PK    │       │ id: BIGINT PK    │
                           │ title: VARCHAR   │──────►│ album_id: FK     │
                           │ release_year: INT│  1:N  │ url: VARCHAR     │
                           │ active: BOOLEAN  │       │ file_name: VARCHAR│
                           │ created_at: TS   │       │ created_at: TS   │
                           │ updated_at: TS   │       └──────────────────┘
                           └──────────────────┘
```

### Fluxo de Autenticação

```
┌────────┐     ┌────────────┐     ┌─────────────┐     ┌──────────────┐
│ Client │     │ AuthResource│    │ AuthService │     │UserRepository│
└───┬────┘     └──────┬─────┘     └──────┬──────┘     └──────┬───────┘
    │                 │                   │                   │
    │ POST /login     │                   │                   │
    │────────────────►│                   │                   │
    │                 │ authenticate()    │                   │
    │                 │──────────────────►│                   │
    │                 │                   │ findByUsername()  │
    │                 │                   │──────────────────►│
    │                 │                   │◄──────────────────│
    │                 │                   │                   │
    │                 │                   │ verify BCrypt     │
    │                 │                   │ generate JWT      │
    │                 │◄──────────────────│                   │
    │ TokenResponse   │                   │                   │
    │◄────────────────│                   │                   │
    │                 │                   │                   │
```

### Endpoints da API

#### Autenticação (`/api/v1/auth`)

| Método | Endpoint | Descrição | Auth |
|--------|----------|-----------|------|
| POST | `/login` | Login do usuário | Não |
| POST | `/register` | Registrar usuário | Não |
| POST | `/refresh` | Renovar token JWT | Sim |

#### Artistas (`/api/v1/artists`)

| Método | Endpoint | Descrição | Auth |
|--------|----------|-----------|------|
| GET | `/` | Listar artistas (paginado) | USER |
| GET | `/{id}` | Buscar artista por ID | USER |
| GET | `/{id}/albums` | Álbuns do artista | USER |
| POST | `/` | Criar artista | ADMIN |
| PUT | `/{id}` | Atualizar artista | ADMIN |
| DELETE | `/{id}` | Desativar artista (soft delete) | ADMIN |

#### Álbuns (`/api/v1/albums`)

| Método | Endpoint | Descrição | Auth |
|--------|----------|-----------|------|
| GET | `/` | Listar álbuns (paginado) | USER |
| GET | `/{id}` | Buscar álbum por ID | USER |
| POST | `/` | Criar álbum | ADMIN |
| PUT | `/{id}` | Atualizar álbum | ADMIN |
| DELETE | `/{id}` | Desativar álbum (soft delete) | ADMIN |
| POST | `/{id}/artists/{artistId}` | Vincular artista | ADMIN |
| DELETE | `/{id}/artists/{artistId}` | Desvincular artista | ADMIN |

#### Capas de Álbum (`/api/v1/albums/{id}/covers`)

| Método | Endpoint | Descrição | Auth |
|--------|----------|-----------|------|
| GET | `/` | Listar capas do álbum | USER |
| POST | `/` | Upload de capa | ADMIN |
| DELETE | `/{coverId}` | Remover capa | ADMIN |

### Segurança

- **JWT (JSON Web Token)**: Autenticação stateless com tokens de curta duração (5 minutos)
- **Refresh Token**: Renovação automática de tokens sem re-autenticação
- **BCrypt**: Hash de senhas com salt automático
- **RBAC**: Controle de acesso baseado em roles (ADMIN/USER)
- **Rate Limiting**: 10 requisições por minuto para proteção contra DDoS
- **CORS**: Configurável via variáveis de ambiente

---

## Frontend (SPA React)

### Stack Tecnológica

| Componente | Tecnologia | Versão |
|------------|------------|--------|
| Framework | React | 19.2.0 |
| Linguagem | TypeScript | 5.9.3 |
| Build Tool | Vite | 7.2.4 |
| Estilização | Tailwind CSS | 4.1.18 |
| Roteamento | React Router | 7.13.0 |
| HTTP Client | Axios | 1.9.0 |
| Testes | Vitest + Testing Library | 3.2.4 |

### Estrutura de Diretórios

```
front-end/
├── public/                 # Arquivos estáticos
├── src/
│   ├── components/        # Componentes React
│   │   ├── ui/           # Componentes de UI reutilizáveis
│   │   │   ├── ArtistCard.tsx
│   │   │   ├── AlbumCard.tsx
│   │   │   ├── ConfirmModal.tsx
│   │   │   ├── FormField.tsx
│   │   │   ├── NotificationToast.tsx
│   │   │   ├── Pagination.tsx
│   │   │   └── SearchInput.tsx
│   │   ├── Header/
│   │   ├── Footer/
│   │   ├── Layout/
│   │   └── PrivateRoute.tsx
│   ├── contexts/          # React Context API
│   │   ├── AuthContext.tsx
│   │   └── ThemeContext.tsx
│   ├── facades/           # Camada de abstração
│   │   ├── auth.facade.ts
│   │   ├── artist.facade.ts
│   │   └── album.facade.ts
│   ├── hooks/             # Custom Hooks
│   │   └── useWebSocket.ts
│   ├── pages/             # Páginas da aplicação
│   │   ├── Login/
│   │   ├── Register/
│   │   ├── Home/
│   │   ├── ArtistDetail/
│   │   ├── ArtistForm/
│   │   └── AlbumForm/
│   ├── services/          # Serviços HTTP
│   │   ├── api.ts
│   │   ├── auth.service.ts
│   │   ├── artist.service.ts
│   │   ├── album.service.ts
│   │   └── websocket.service.ts
│   ├── types/             # Tipos TypeScript
│   │   ├── auth.types.ts
│   │   ├── artist.types.ts
│   │   ├── album.types.ts
│   │   └── common.types.ts
│   ├── test/              # Configuração de testes
│   ├── App.tsx
│   ├── routes.tsx
│   └── main.tsx
├── Dockerfile
├── nginx.conf
├── package.json
├── tailwind.config.js
├── tsconfig.json
└── vite.config.ts
```

### Padrões de Design

#### Facade Pattern

A camada de facades abstrai a complexidade dos serviços HTTP:

```typescript
// facades/auth.facade.ts
export const authFacade = {
  async login(credentials: LoginCredentials): Promise<User> {
    const response = await authService.login(credentials);
    authService.setToken(response.token);
    return authService.getUserFromToken();
  },

  async logout(): Promise<void> {
    authService.logout();
  }
};
```

#### Context API

Gerenciamento de estado global com React Context:

```typescript
// contexts/AuthContext.tsx
const AuthContext = createContext<AuthContextType | null>(null);

export function AuthProvider({ children }: { children: ReactNode }) {
  const [user, setUser] = useState<User | null>(null);
  const [isLoading, setIsLoading] = useState(true);

  // ... lógica de autenticação

  return (
    <AuthContext.Provider value={{ user, login, logout, isLoading }}>
      {children}
    </AuthContext.Provider>
  );
}
```

#### Lazy Loading

Carregamento sob demanda de páginas para otimização:

```typescript
// routes.tsx
const Login = lazy(() => import('./pages/Login'));
const Home = lazy(() => import('./pages/Home'));
const ArtistDetail = lazy(() => import('./pages/ArtistDetail'));
```

### Fluxo de Dados

```
┌─────────────────────────────────────────────────────────────────┐
│                         User Interface                           │
│  ┌──────────┐   ┌──────────┐   ┌──────────┐   ┌──────────┐     │
│  │   Page   │   │Components│   │  Forms   │   │  Modals  │     │
│  └────┬─────┘   └────┬─────┘   └────┬─────┘   └────┬─────┘     │
│       │              │              │              │            │
│       └──────────────┴──────────────┴──────────────┘            │
│                              │                                   │
│                         ┌────┴────┐                             │
│                         │ Contexts│ ← Estado Global             │
│                         │Auth/Theme                             │
│                         └────┬────┘                             │
│                              │                                   │
│                         ┌────┴────┐                             │
│                         │ Facades │ ← Abstração                 │
│                         └────┬────┘                             │
│                              │                                   │
│                         ┌────┴────┐                             │
│                         │Services │ ← HTTP/WebSocket            │
│                         │  + API  │                             │
│                         └────┬────┘                             │
└──────────────────────────────┼──────────────────────────────────┘
                               │
                          ┌────┴────┐
                          │ Backend │
                          │   API   │
                          └─────────┘
```

### Rotas da Aplicação

| Rota | Componente | Proteção | Descrição |
|------|------------|----------|-----------|
| `/login` | Login | Pública | Página de login |
| `/cadastro` | Register | Pública | Registro de usuário |
| `/` | Home | Privada | Listagem de artistas |
| `/artistas/:id` | ArtistDetail | Privada | Detalhes do artista |
| `/artistas/novo` | ArtistForm | Privada | Criar artista |
| `/artistas/:id/editar` | ArtistForm | Privada | Editar artista |
| `/albuns/novo` | AlbumForm | Privada | Criar álbum |
| `/albuns/:id/editar` | AlbumForm | Privada | Editar álbum |

### Sistema de Temas

O sistema suporta tema claro e escuro com persistência em localStorage:

```typescript
// contexts/ThemeContext.tsx
export function ThemeProvider({ children }: { children: ReactNode }) {
  const [theme, setTheme] = useState<'light' | 'dark'>(() => {
    const saved = localStorage.getItem('theme');
    return saved === 'dark' ? 'dark' : 'light';
  });

  useEffect(() => {
    document.documentElement.classList.toggle('dark', theme === 'dark');
    localStorage.setItem('theme', theme);
  }, [theme]);
}
```

### Tailwind CSS v4

Configuração de cores customizadas:

```css
/* index.css */
@import "tailwindcss";

@theme {
  --color-cream-50: #fefcf7;
  --color-cream-100: #fdf8ed;
  --color-cream-200: #faefd6;
  --color-dark-500: #3a3a3a;
  --color-dark-600: #2d2d2d;
  --color-dark-700: #1f1f1f;
  --color-dark-800: #171717;
  --color-dark-900: #0f0f0f;
  --color-accent-gold: #d4af37;
  --color-accent-coral: #ff6b6b;
}

@custom-variant dark (&:where(.dark, .dark *));
```

---

## Infraestrutura

### Docker Compose

O sistema é orquestrado via Docker Compose com 4 serviços:

```yaml
services:
  postgres:      # Banco de dados PostgreSQL 16
  minio:         # Object Storage (S3-compatible)
  api:           # Backend Quarkus
  frontend:      # Frontend React (Nginx)
```

### Fluxo de Deploy

```
┌─────────────┐     ┌─────────────┐     ┌─────────────┐
│   GitHub    │────►│  Build CI   │────►│   Deploy    │
│ Repository  │     │ (opcional)  │     │  Container  │
└─────────────┘     └─────────────┘     └─────────────┘
                           │
              ┌────────────┼────────────┐
              │            │            │
        ┌─────┴─────┐ ┌────┴────┐ ┌─────┴─────┐
        │  Backend  │ │Frontend │ │   MinIO   │
        │  Docker   │ │ Docker  │ │  Docker   │
        └─────┬─────┘ └────┬────┘ └─────┬─────┘
              │            │            │
        ┌─────┴────────────┴────────────┴─────┐
        │         Docker Network              │
        │        (rockstars-network)          │
        └─────────────────────────────────────┘
```

### Multi-Stage Build (Frontend)

```dockerfile
# Estágio 1: Build
FROM node:22-alpine AS builder
WORKDIR /app
COPY package*.json ./
RUN npm ci
COPY . .
ARG VITE_API_URL
ENV VITE_API_URL=$VITE_API_URL
RUN npm run build

# Estágio 2: Produção
FROM nginx:alpine
COPY --from=builder /app/dist /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
```

### Healthchecks

Todos os serviços possuem healthchecks configurados:

- **PostgreSQL**: `pg_isready`
- **MinIO**: `mc ready local`
- **API**: `/q/health/ready` (Quarkus health endpoint)
- **Frontend**: HTTP 200 em `/`

---

## WebSocket

### Notificações em Tempo Real

O sistema utiliza WebSocket para notificar usuários sobre operações em álbuns:

```
┌────────────┐                    ┌────────────┐
│  Frontend  │◄───────────────────│  Backend   │
│ useWebSock │  WebSocket conn    │  WS Server │
└─────┬──────┘                    └─────┬──────┘
      │                                 │
      │ connect()                       │
      │────────────────────────────────►│
      │                                 │
      │      onOpen()                   │
      │◄────────────────────────────────│
      │                                 │
      │      Album Created/Updated      │
      │◄────────────────────────────────│
      │                                 │
      │ NotificationToast displays      │
      │                                 │
```

### Eventos Suportados

| Evento | Descrição |
|--------|-----------|
| `ALBUM_CREATED` | Novo álbum criado |
| `ALBUM_UPDATED` | Álbum atualizado |

---

## Testes

### Backend

- **Framework**: JUnit 5 + REST Assured
- **Cobertura**: Resources, Services
- **Execução**: `./gradlew test`

### Frontend

- **Framework**: Vitest + React Testing Library
- **Cobertura**: Services, Facades, Components, Pages
- **Execução**: `npm run test:run`

#### Estrutura de Testes

```
src/
├── services/
│   └── auth.service.test.ts    # Testes de serviço
├── facades/
│   └── auth.facade.test.ts     # Testes de facade
├── components/
│   └── PrivateRoute.test.tsx   # Testes de componente
├── pages/
│   ├── Login/
│   │   └── Login.test.tsx      # Testes de página
│   └── Home/
│       └── Home.test.tsx
└── test/
    ├── setup.ts                # Configuração global
    └── test-utils.tsx          # Utilitários de teste
```

---

## Boas Práticas

### Backend

1. **Soft Delete**: Entidades não são removidas, apenas marcadas como inativas
2. **DTOs**: Separação clara entre entidades e objetos de transferência
3. **Versionamento**: API versionada (`/api/v1/`)
4. **Validação**: Bean Validation em todos os DTOs
5. **Documentação**: OpenAPI/Swagger automático
6. **Health Checks**: Endpoints de saúde para infraestrutura

### Frontend

1. **TypeScript Strict**: Tipagem forte em todo o código
2. **Facade Pattern**: Abstração da comunicação HTTP
3. **Context API**: Estado global mínimo e focado
4. **Lazy Loading**: Carregamento sob demanda de rotas
5. **Responsive Design**: Mobile-first com Tailwind CSS
6. **Acessibilidade**: Semântica HTML e ARIA labels

### Segurança

1. **Variáveis de Ambiente**: Credenciais externalizadas
2. **HTTPS**: Comunicação criptografada em produção
3. **CORS**: Configuração restritiva por ambiente
4. **Rate Limiting**: Proteção contra abuso
5. **Sanitização**: Validação de entrada em ambos os lados

---

## Referências

- [Quarkus Documentation](https://quarkus.io/guides/)
- [React Documentation](https://react.dev/)
- [Tailwind CSS v4](https://tailwindcss.com/docs)
- [Vite](https://vitejs.dev/guide/)
- [Docker Compose](https://docs.docker.com/compose/)

export interface LoginRequest {
  username: string
  password: string
}

export interface TokenResponse {
  accessToken: string
  tokenType: string
  expiresIn: number
}

export interface TokenPayload {
  sub: string
  iat: number
  exp: number
  groups?: string[]
}

export interface User {
  username: string
  roles: string[]
}

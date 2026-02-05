import api from './api'
import type { LoginRequest, TokenResponse, TokenPayload, User } from '../types'

export interface RegisterRequest {
  username: string
  password: string
}

export const authService = {
  async login(credentials: LoginRequest): Promise<TokenResponse> {
    const response = await api.post<TokenResponse>('/api/v1/auth/login', credentials)
    return response.data
  },

  async register(data: RegisterRequest): Promise<TokenResponse> {
    const response = await api.post<TokenResponse>('/api/v1/auth/register', data)
    return response.data
  },

  async refreshToken(): Promise<TokenResponse> {
    const response = await api.post<TokenResponse>('/api/v1/auth/refresh')
    return response.data
  },

  logout(): void {
    localStorage.removeItem('accessToken')
  },

  getToken(): string | null {
    return localStorage.getItem('accessToken')
  },

  setToken(token: string): void {
    localStorage.setItem('accessToken', token)
  },

  decodeToken(token: string): TokenPayload | null {
    try {
      const base64Url = token.split('.')[1]
      const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/')
      const jsonPayload = decodeURIComponent(
        atob(base64)
          .split('')
          .map((c) => '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2))
          .join('')
      )
      return JSON.parse(jsonPayload)
    } catch {
      return null
    }
  },

  isTokenExpired(token: string): boolean {
    const payload = this.decodeToken(token)
    if (!payload) return true
    const now = Math.floor(Date.now() / 1000)
    return payload.exp < now
  },

  getTimeUntilExpiry(token: string): number {
    const payload = this.decodeToken(token)
    if (!payload) return 0
    const now = Math.floor(Date.now() / 1000)
    return Math.max(0, (payload.exp - now) * 1000)
  },

  getUserFromToken(token: string): User | null {
    const payload = this.decodeToken(token)
    if (!payload) return null
    return {
      username: payload.sub,
      roles: payload.groups || [],
    }
  },
}

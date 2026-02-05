import { authService } from '../services/auth.service'
import { LoginRequest, User } from '../types/auth.types'

export interface AuthState {
  user: User | null
  isAuthenticated: boolean
  isLoading: boolean
  error: string | null
}

export const authFacade = {
  async login(credentials: LoginRequest): Promise<User> {
    const response = await authService.login(credentials)
    authService.setToken(response.accessToken)
    const user = authService.getUserFromToken(response.accessToken)
    if (!user) {
      throw new Error('Erro ao decodificar token')
    }
    return user
  },

  logout(): void {
    authService.logout()
  },

  isAuthenticated(): boolean {
    const token = authService.getToken()
    if (!token) return false
    return !authService.isTokenExpired(token)
  },

  getUser(): User | null {
    const token = authService.getToken()
    if (!token) return null
    return authService.getUserFromToken(token)
  },

  async refreshToken(): Promise<boolean> {
    try {
      const response = await authService.refreshToken()
      authService.setToken(response.accessToken)
      return true
    } catch {
      authService.logout()
      return false
    }
  },

  getTimeUntilExpiry(): number {
    const token = authService.getToken()
    if (!token) return 0
    return authService.getTimeUntilExpiry(token)
  },

  shouldRefreshToken(): boolean {
    const timeUntilExpiry = this.getTimeUntilExpiry()
    return timeUntilExpiry > 0 && timeUntilExpiry < 60000
  },
}

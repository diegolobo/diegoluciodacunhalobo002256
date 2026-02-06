import { describe, it, expect, beforeEach } from 'vitest'
import { authService } from './auth.service'

const mockToken = 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJyb2Nrc3RhciIsImdyb3VwcyI6WyJBRE1JTiJdLCJleHAiOjk5OTk5OTk5OTl9.test'
const expiredToken = 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJyb2Nrc3RhciIsImdyb3VwcyI6WyJBRE1JTiJdLCJleHAiOjE2MDAwMDAwMDB9.test'

describe('authService', () => {
  beforeEach(() => {
    localStorage.clear()
  })

  describe('getToken / setToken', () => {
    it('deve salvar e recuperar token do localStorage', () => {
      authService.setToken(mockToken)
      expect(authService.getToken()).toBe(mockToken)
    })

    it('deve retornar null quando não há token', () => {
      expect(authService.getToken()).toBeNull()
    })

    it('deve retornar token quando existe', () => {
      authService.setToken(mockToken)
      expect(authService.getToken()).toBe(mockToken)
    })
  })

  describe('logout', () => {
    it('deve remover token do localStorage', () => {
      authService.logout()
      expect(localStorage.removeItem).toHaveBeenCalledWith('accessToken')
    })
  })

  describe('decodeToken', () => {
    it('deve decodificar token JWT válido', () => {
      const payload = authService.decodeToken(mockToken)
      expect(payload).not.toBeNull()
      expect(payload?.sub).toBe('rockstar')
      expect(payload?.groups).toContain('ADMIN')
    })

    it('deve retornar null para token inválido', () => {
      const payload = authService.decodeToken('invalid-token')
      expect(payload).toBeNull()
    })
  })

  describe('isTokenExpired', () => {
    it('deve retornar false para token válido', () => {
      expect(authService.isTokenExpired(mockToken)).toBe(false)
    })

    it('deve retornar true para token expirado', () => {
      expect(authService.isTokenExpired(expiredToken)).toBe(true)
    })

    it('deve retornar true para token inválido', () => {
      expect(authService.isTokenExpired('invalid')).toBe(true)
    })
  })

  describe('getUserFromToken', () => {
    it('deve extrair usuário do token', () => {
      const user = authService.getUserFromToken(mockToken)
      expect(user).not.toBeNull()
      expect(user?.username).toBe('rockstar')
      expect(user?.roles).toContain('ADMIN')
    })

    it('deve retornar null para token inválido', () => {
      const user = authService.getUserFromToken('invalid')
      expect(user).toBeNull()
    })
  })

  describe('getTimeUntilExpiry', () => {
    it('deve retornar tempo restante em ms', () => {
      const time = authService.getTimeUntilExpiry(mockToken)
      expect(time).toBeGreaterThan(0)
    })

    it('deve retornar 0 para token expirado', () => {
      const time = authService.getTimeUntilExpiry(expiredToken)
      expect(time).toBe(0)
    })
  })
})

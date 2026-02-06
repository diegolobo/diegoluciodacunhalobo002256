import { describe, it, expect, vi, beforeEach } from 'vitest'
import { authFacade } from './auth.facade'
import { authService } from '../services/auth.service'

vi.mock('../services/auth.service', () => ({
  authService: {
    login: vi.fn(),
    logout: vi.fn(),
    refreshToken: vi.fn(),
    getToken: vi.fn(),
    setToken: vi.fn(),
    isTokenExpired: vi.fn(),
    getUserFromToken: vi.fn(),
    getTimeUntilExpiry: vi.fn(),
  },
}))

const mockToken = 'mock-jwt-token'
const mockUser = { username: 'rockstar', roles: ['ADMIN'] }

describe('authFacade', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  describe('login', () => {
    it('deve fazer login e retornar usuário', async () => {
      vi.mocked(authService.login).mockResolvedValue({ accessToken: mockToken })
      vi.mocked(authService.getUserFromToken).mockReturnValue(mockUser)

      const user = await authFacade.login({ username: 'rockstar', password: 'senha' })

      expect(authService.login).toHaveBeenCalledWith({ username: 'rockstar', password: 'senha' })
      expect(authService.setToken).toHaveBeenCalledWith(mockToken)
      expect(user).toEqual(mockUser)
    })

    it('deve lançar erro quando token não pode ser decodificado', async () => {
      vi.mocked(authService.login).mockResolvedValue({ accessToken: mockToken })
      vi.mocked(authService.getUserFromToken).mockReturnValue(null)

      await expect(authFacade.login({ username: 'rockstar', password: 'senha' }))
        .rejects.toThrow('Erro ao decodificar token')
    })
  })

  describe('logout', () => {
    it('deve chamar authService.logout', () => {
      authFacade.logout()
      expect(authService.logout).toHaveBeenCalled()
    })
  })

  describe('isAuthenticated', () => {
    it('deve retornar true quando token válido existe', () => {
      vi.mocked(authService.getToken).mockReturnValue(mockToken)
      vi.mocked(authService.isTokenExpired).mockReturnValue(false)

      expect(authFacade.isAuthenticated()).toBe(true)
    })

    it('deve retornar false quando não há token', () => {
      vi.mocked(authService.getToken).mockReturnValue(null)

      expect(authFacade.isAuthenticated()).toBe(false)
    })

    it('deve retornar false quando token está expirado', () => {
      vi.mocked(authService.getToken).mockReturnValue(mockToken)
      vi.mocked(authService.isTokenExpired).mockReturnValue(true)

      expect(authFacade.isAuthenticated()).toBe(false)
    })
  })

  describe('getUser', () => {
    it('deve retornar usuário quando token existe', () => {
      vi.mocked(authService.getToken).mockReturnValue(mockToken)
      vi.mocked(authService.getUserFromToken).mockReturnValue(mockUser)

      expect(authFacade.getUser()).toEqual(mockUser)
    })

    it('deve retornar null quando não há token', () => {
      vi.mocked(authService.getToken).mockReturnValue(null)

      expect(authFacade.getUser()).toBeNull()
    })
  })

  describe('refreshToken', () => {
    it('deve atualizar token e retornar true em caso de sucesso', async () => {
      vi.mocked(authService.refreshToken).mockResolvedValue({ accessToken: 'new-token' })

      const result = await authFacade.refreshToken()

      expect(authService.setToken).toHaveBeenCalledWith('new-token')
      expect(result).toBe(true)
    })

    it('deve fazer logout e retornar false em caso de erro', async () => {
      vi.mocked(authService.refreshToken).mockRejectedValue(new Error('Erro'))

      const result = await authFacade.refreshToken()

      expect(authService.logout).toHaveBeenCalled()
      expect(result).toBe(false)
    })
  })

  describe('shouldRefreshToken', () => {
    it('deve retornar true quando falta menos de 1 minuto para expirar', () => {
      vi.mocked(authService.getToken).mockReturnValue(mockToken)
      vi.mocked(authService.getTimeUntilExpiry).mockReturnValue(30000)

      expect(authFacade.shouldRefreshToken()).toBe(true)
    })

    it('deve retornar false quando falta mais de 1 minuto para expirar', () => {
      vi.mocked(authService.getToken).mockReturnValue(mockToken)
      vi.mocked(authService.getTimeUntilExpiry).mockReturnValue(120000)

      expect(authFacade.shouldRefreshToken()).toBe(false)
    })

    it('deve retornar false quando token já expirou', () => {
      vi.mocked(authService.getToken).mockReturnValue(mockToken)
      vi.mocked(authService.getTimeUntilExpiry).mockReturnValue(0)

      expect(authFacade.shouldRefreshToken()).toBe(false)
    })
  })
})

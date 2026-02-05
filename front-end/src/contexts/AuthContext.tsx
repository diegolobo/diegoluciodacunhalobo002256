import { createContext, useContext, useState, useEffect, useCallback, ReactNode } from 'react'
import { authFacade } from '../facades/auth.facade'
import { authService } from '../services/auth.service'
import type { User, LoginRequest } from '../types'

interface AuthContextData {
  user: User | null
  isAuthenticated: boolean
  isLoading: boolean
  login: (credentials: LoginRequest) => Promise<void>
  logout: () => void
}

const AuthContext = createContext<AuthContextData>({} as AuthContextData)

interface AuthProviderProps {
  children: ReactNode
}

export function AuthProvider({ children }: AuthProviderProps) {
  const [user, setUser] = useState<User | null>(null)
  const [isLoading, setIsLoading] = useState(true)

  const scheduleTokenRefresh = useCallback(() => {
    const timeUntilExpiry = authFacade.getTimeUntilExpiry()

    if (timeUntilExpiry <= 0) return

    const refreshTime = Math.max(timeUntilExpiry - 60000, 0)

    const timeoutId = setTimeout(async () => {
      const success = await authFacade.refreshToken()
      if (success) {
        scheduleTokenRefresh()
      } else {
        setUser(null)
      }
    }, refreshTime)

    return () => clearTimeout(timeoutId)
  }, [])

  useEffect(() => {
    const initAuth = async () => {
      setIsLoading(true)

      const token = authService.getToken()

      if (token && !authService.isTokenExpired(token)) {
        const currentUser = authService.getUserFromToken(token)
        setUser(currentUser)
        scheduleTokenRefresh()
      } else if (token) {
        const success = await authFacade.refreshToken()
        if (success) {
          const currentUser = authFacade.getUser()
          setUser(currentUser)
          scheduleTokenRefresh()
        }
      }

      setIsLoading(false)
    }

    initAuth()
  }, [scheduleTokenRefresh])

  const login = async (credentials: LoginRequest) => {
    const loggedUser = await authFacade.login(credentials)
    setUser(loggedUser)
    scheduleTokenRefresh()
  }

  const logout = () => {
    authFacade.logout()
    setUser(null)
  }

  return (
    <AuthContext.Provider
      value={{
        user,
        isAuthenticated: !!user,
        isLoading,
        login,
        logout,
      }}
    >
      {children}
    </AuthContext.Provider>
  )
}

export function useAuth() {
  const context = useContext(AuthContext)
  if (!context) {
    throw new Error('useAuth deve ser usado dentro de um AuthProvider')
  }
  return context
}

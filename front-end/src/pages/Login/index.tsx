import { useState, FormEvent } from 'react'
import { useNavigate, useLocation } from 'react-router-dom'
import { useAuth } from '../../contexts/AuthContext'
import { useTheme } from '../../contexts/ThemeContext'

function Login() {
  const [username, setUsername] = useState('')
  const [password, setPassword] = useState('')
  const [error, setError] = useState('')
  const [isLoading, setIsLoading] = useState(false)

  const { login } = useAuth()
  const { theme, toggleTheme } = useTheme()
  const navigate = useNavigate()
  const location = useLocation()

  const from = location.state?.from?.pathname || '/'

  const handleSubmit = async (e: FormEvent) => {
    e.preventDefault()
    setError('')

    if (!username.trim()) {
      setError('Usuário é obrigatório')
      return
    }

    if (!password.trim()) {
      setError('Senha é obrigatória')
      return
    }

    setIsLoading(true)

    try {
      await login({ username, password })
      navigate(from, { replace: true })
    } catch (err) {
      setError('Usuário ou senha inválidos')
    } finally {
      setIsLoading(false)
    }
  }

  return (
    <div className="min-h-screen flex items-center justify-center bg-cream-100 dark:bg-dark-800 transition-colors px-4">
      <div className="w-full max-w-md">
        <div className="bg-white dark:bg-dark-600 rounded-2xl shadow-lg p-8">
          <div className="text-center mb-8">
            <h1 className="text-3xl font-bold text-dark-800 dark:text-cream-100">
              Rockstars
            </h1>
            <p className="text-dark-500 dark:text-cream-200 mt-2">
              Faça login para continuar
            </p>
          </div>

          <form onSubmit={handleSubmit} className="space-y-6">
            <div>
              <label
                htmlFor="username"
                className="block text-sm font-medium text-dark-800 dark:text-cream-100 mb-2"
              >
                Usuário
              </label>
              <input
                id="username"
                type="text"
                value={username}
                onChange={(e) => setUsername(e.target.value)}
                className="w-full px-4 py-3 rounded-lg border border-cream-200 dark:border-dark-500 bg-cream-50 dark:bg-dark-700 text-dark-800 dark:text-cream-100 placeholder-dark-500 dark:placeholder-cream-200 focus:outline-none focus:ring-2 focus:ring-accent-gold transition-colors"
                placeholder="Digite seu usuário"
                disabled={isLoading}
              />
            </div>

            <div>
              <label
                htmlFor="password"
                className="block text-sm font-medium text-dark-800 dark:text-cream-100 mb-2"
              >
                Senha
              </label>
              <input
                id="password"
                type="password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                className="w-full px-4 py-3 rounded-lg border border-cream-200 dark:border-dark-500 bg-cream-50 dark:bg-dark-700 text-dark-800 dark:text-cream-100 placeholder-dark-500 dark:placeholder-cream-200 focus:outline-none focus:ring-2 focus:ring-accent-gold transition-colors"
                placeholder="Digite sua senha"
                disabled={isLoading}
              />
            </div>

            {error && (
              <div className="p-3 rounded-lg bg-red-100 dark:bg-red-900/30 text-red-700 dark:text-red-400 text-sm">
                {error}
              </div>
            )}

            <button
              type="submit"
              disabled={isLoading}
              className="w-full py-3 px-4 bg-accent-gold hover:bg-accent-gold-hover disabled:opacity-50 disabled:cursor-not-allowed text-white font-medium rounded-lg transition-colors"
            >
              {isLoading ? 'Entrando...' : 'Entrar'}
            </button>
          </form>
        </div>

        <div className="mt-6 text-center">
          <button
            onClick={toggleTheme}
            className="text-sm text-dark-500 dark:text-cream-200 hover:text-dark-800 dark:hover:text-cream-100 transition-colors"
          >
            {theme === 'light' ? '🌙 Modo escuro' : '☀️ Modo claro'}
          </button>
        </div>
      </div>
    </div>
  )
}

export default Login

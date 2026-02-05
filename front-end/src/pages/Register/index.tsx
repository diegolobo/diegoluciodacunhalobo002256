import { useState, FormEvent } from 'react'
import { useNavigate, Link } from 'react-router-dom'
import { useTheme } from '../../contexts/ThemeContext'
import { authService } from '../../services/auth.service'

function Register() {
  const [username, setUsername] = useState('')
  const [password, setPassword] = useState('')
  const [confirmPassword, setConfirmPassword] = useState('')
  const [error, setError] = useState('')
  const [isLoading, setIsLoading] = useState(false)

  const { theme, toggleTheme } = useTheme()
  const navigate = useNavigate()

  const handleSubmit = async (e: FormEvent) => {
    e.preventDefault()
    setError('')

    if (!username.trim()) {
      setError('Usuário é obrigatório')
      return
    }

    if (username.trim().length < 3) {
      setError('Usuário deve ter no mínimo 3 caracteres')
      return
    }

    if (!password.trim()) {
      setError('Senha é obrigatória')
      return
    }

    if (password.length < 6) {
      setError('Senha deve ter no mínimo 6 caracteres')
      return
    }

    if (password !== confirmPassword) {
      setError('As senhas não conferem')
      return
    }

    setIsLoading(true)

    try {
      await authService.register({ username, password })
      navigate('/login', { state: { message: 'Cadastro realizado com sucesso! Faça login para continuar.' } })
    } catch {
      setError('Erro ao cadastrar. O usuário pode já existir.')
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
              Crie sua conta
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

            <div>
              <label
                htmlFor="confirmPassword"
                className="block text-sm font-medium text-dark-800 dark:text-cream-100 mb-2"
              >
                Confirmar Senha
              </label>
              <input
                id="confirmPassword"
                type="password"
                value={confirmPassword}
                onChange={(e) => setConfirmPassword(e.target.value)}
                className="w-full px-4 py-3 rounded-lg border border-cream-200 dark:border-dark-500 bg-cream-50 dark:bg-dark-700 text-dark-800 dark:text-cream-100 placeholder-dark-500 dark:placeholder-cream-200 focus:outline-none focus:ring-2 focus:ring-accent-gold transition-colors"
                placeholder="Confirme sua senha"
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
              className="w-full py-3 px-4 bg-accent-gold hover:bg-accent-gold-hover disabled:opacity-50 disabled:cursor-not-allowed text-white font-medium rounded-lg transition-colors cursor-pointer"
            >
              {isLoading ? 'Cadastrando...' : 'Cadastrar'}
            </button>
          </form>

          <div className="mt-6 text-center">
            <p className="text-dark-500 dark:text-cream-200 text-sm">
              Já tem uma conta?{' '}
              <Link
                to="/login"
                className="text-accent-gold hover:text-accent-gold-hover font-medium cursor-pointer"
              >
                Faça login
              </Link>
            </p>
          </div>
        </div>

        <div className="mt-6 text-center">
          <button
            onClick={toggleTheme}
            className="px-4 py-2 text-sm border-2 border-accent-gold text-accent-gold hover:bg-accent-gold hover:text-white rounded-lg transition-colors cursor-pointer"
          >
            {theme === 'light' ? '🌙 Modo escuro' : '☀️ Modo claro'}
          </button>
        </div>
      </div>
    </div>
  )
}

export default Register

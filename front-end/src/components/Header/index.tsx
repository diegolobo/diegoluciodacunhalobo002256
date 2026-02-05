import { useState } from 'react'
import { Link } from 'react-router-dom'
import { useAuth } from '../../contexts/AuthContext'
import { useTheme } from '../../contexts/ThemeContext'

function Header() {
  const [menuAberto, setMenuAberto] = useState(false)
  const { user, logout } = useAuth()
  const { theme, toggleTheme } = useTheme()

  const fecharMenu = () => setMenuAberto(false)

  return (
    <header className="bg-white dark:bg-dark-600 shadow-sm border-b border-cream-200 dark:border-dark-500 transition-colors">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex items-center justify-between h-16">
          <div className="flex items-center gap-8">
            <Link to="/" className="text-xl font-bold text-dark-800 dark:text-cream-100 cursor-pointer">
              Rockstars
            </Link>
            <nav className="hidden md:flex items-center gap-6">
              <Link
                to="/"
                className="text-dark-500 dark:text-cream-200 hover:text-dark-800 dark:hover:text-cream-100 transition-colors cursor-pointer"
              >
                Artistas
              </Link>
            </nav>
          </div>

          <div className="hidden md:flex items-center gap-4">
            {user && (
              <div className="flex items-center gap-4">
                <span className="text-sm text-dark-500 dark:text-cream-200">
                  Olá, {user.username}!
                </span>
                <button
                  onClick={toggleTheme}
                  className="px-3 py-1.5 text-sm border-2 border-accent-gold text-accent-gold hover:bg-accent-gold hover:text-white rounded-lg transition-colors cursor-pointer"
                  title={theme === 'light' ? 'Modo escuro' : 'Modo claro'}
                >
                  {theme === 'light' ? '🌙 Escuro' : '☀️ Claro'}
                </button>
                <button
                  onClick={logout}
                  className="px-3 py-1.5 text-sm bg-accent-gold hover:bg-accent-gold-hover text-white rounded-lg transition-colors cursor-pointer"
                >
                  Sair
                </button>
              </div>
            )}
          </div>

          <button
            onClick={() => setMenuAberto(!menuAberto)}
            className="md:hidden p-2 text-dark-500 dark:text-cream-200 hover:text-dark-800 dark:hover:text-cream-100 transition-colors cursor-pointer"
            aria-label="Abrir menu"
          >
            <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              {menuAberto ? (
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M6 18L18 6M6 6l12 12" />
              ) : (
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M4 6h16M4 12h16M4 18h16" />
              )}
            </svg>
          </button>
        </div>
      </div>

      {menuAberto && (
        <div className="md:hidden border-t border-cream-200 dark:border-dark-500">
          <div className="px-4 py-4 space-y-4">
            {user && (
              <div className="pb-4 border-b border-cream-200 dark:border-dark-500">
                <span className="text-sm text-dark-500 dark:text-cream-200">
                  Olá, {user.username}!
                </span>
              </div>
            )}

            <nav className="space-y-2">
              <Link
                to="/"
                onClick={fecharMenu}
                className="block py-2 text-dark-500 dark:text-cream-200 hover:text-dark-800 dark:hover:text-cream-100 transition-colors cursor-pointer"
              >
                Artistas
              </Link>
            </nav>

            <div className="pt-4 border-t border-cream-200 dark:border-dark-500 space-y-3">
              <button
                onClick={() => {
                  toggleTheme()
                  fecharMenu()
                }}
                className="w-full px-3 py-2 text-sm border-2 border-accent-gold text-accent-gold hover:bg-accent-gold hover:text-white rounded-lg transition-colors cursor-pointer"
              >
                {theme === 'light' ? '🌙 Modo escuro' : '☀️ Modo claro'}
              </button>
              <button
                onClick={() => {
                  logout()
                  fecharMenu()
                }}
                className="w-full px-3 py-2 text-sm bg-accent-gold hover:bg-accent-gold-hover text-white rounded-lg transition-colors cursor-pointer"
              >
                Sair
              </button>
            </div>
          </div>
        </div>
      )}
    </header>
  )
}

export default Header

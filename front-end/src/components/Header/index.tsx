import { Link } from 'react-router-dom'
import { useAuth } from '../../contexts/AuthContext'
import { useTheme } from '../../contexts/ThemeContext'

function Header() {
  const { user, logout } = useAuth()
  const { theme, toggleTheme } = useTheme()

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

          <div className="flex items-center gap-4">
            {user && (
              <div className="flex items-center gap-4">
                <span className="hidden sm:block text-sm text-dark-500 dark:text-cream-200">
                  Olá, {user.username} !
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
        </div>
      </div>
    </header>
  )
}

export default Header

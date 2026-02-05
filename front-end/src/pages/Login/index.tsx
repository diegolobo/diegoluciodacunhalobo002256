import { useTheme } from '../../contexts/ThemeContext'

function Login() {
  const { theme, toggleTheme } = useTheme()

  return (
    <div className="min-h-screen flex items-center justify-center bg-cream-100 dark:bg-dark-800 transition-colors">
      <div className="text-center">
        <h1 className="text-2xl font-bold text-dark-800 dark:text-cream-100 mb-4">
          Login
        </h1>
        <button
          onClick={toggleTheme}
          className="px-4 py-2 bg-accent-gold hover:bg-accent-gold-hover text-white rounded-lg transition-colors"
        >
          Tema: {theme === 'light' ? 'Claro' : 'Escuro'}
        </button>
      </div>
    </div>
  )
}

export default Login

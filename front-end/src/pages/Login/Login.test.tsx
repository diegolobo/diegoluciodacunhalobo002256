import { describe, it, expect, vi, beforeEach } from 'vitest'
import { render, screen, fireEvent, waitFor } from '@testing-library/react'
import { BrowserRouter } from 'react-router-dom'
import Login from './index'

const mockLogin = vi.fn()
const mockNavigate = vi.fn()

vi.mock('../../contexts/AuthContext', () => ({
  useAuth: () => ({
    login: mockLogin,
    isAuthenticated: false,
    isLoading: false,
  }),
}))

vi.mock('../../contexts/ThemeContext', () => ({
  useTheme: () => ({
    theme: 'light',
    toggleTheme: vi.fn(),
  }),
}))

vi.mock('react-router-dom', async () => {
  const actual = await vi.importActual('react-router-dom')
  return {
    ...actual,
    useNavigate: () => mockNavigate,
    useLocation: () => ({ state: null, pathname: '/login' }),
  }
})

const renderLogin = () => {
  return render(
    <BrowserRouter>
      <Login />
    </BrowserRouter>
  )
}

describe('Login', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  it('deve renderizar formulário de login', () => {
    renderLogin()

    expect(screen.getByText('Rockstars')).toBeInTheDocument()
    expect(screen.getByLabelText('Usuário')).toBeInTheDocument()
    expect(screen.getByLabelText('Senha')).toBeInTheDocument()
    expect(screen.getByRole('button', { name: 'Entrar' })).toBeInTheDocument()
  })

  it('deve mostrar erro quando usuário está vazio', async () => {
    renderLogin()

    const submitButton = screen.getByRole('button', { name: 'Entrar' })
    fireEvent.click(submitButton)

    await waitFor(() => {
      expect(screen.getByText('Usuário é obrigatório')).toBeInTheDocument()
    })
  })

  it('deve mostrar erro quando senha está vazia', async () => {
    renderLogin()

    const usernameInput = screen.getByLabelText('Usuário')
    fireEvent.change(usernameInput, { target: { value: 'rockstar' } })

    const submitButton = screen.getByRole('button', { name: 'Entrar' })
    fireEvent.click(submitButton)

    await waitFor(() => {
      expect(screen.getByText('Senha é obrigatória')).toBeInTheDocument()
    })
  })

  it('deve chamar login com credenciais corretas', async () => {
    mockLogin.mockResolvedValue({ username: 'rockstar', roles: ['ADMIN'] })
    renderLogin()

    const usernameInput = screen.getByLabelText('Usuário')
    const passwordInput = screen.getByLabelText('Senha')

    fireEvent.change(usernameInput, { target: { value: 'rockstar' } })
    fireEvent.change(passwordInput, { target: { value: 'senha123' } })

    const submitButton = screen.getByRole('button', { name: 'Entrar' })
    fireEvent.click(submitButton)

    await waitFor(() => {
      expect(mockLogin).toHaveBeenCalledWith({
        username: 'rockstar',
        password: 'senha123',
      })
    })
  })

  it('deve mostrar erro quando login falha', async () => {
    mockLogin.mockRejectedValue(new Error('Credenciais inválidas'))
    renderLogin()

    const usernameInput = screen.getByLabelText('Usuário')
    const passwordInput = screen.getByLabelText('Senha')

    fireEvent.change(usernameInput, { target: { value: 'rockstar' } })
    fireEvent.change(passwordInput, { target: { value: 'senhaerrada' } })

    const submitButton = screen.getByRole('button', { name: 'Entrar' })
    fireEvent.click(submitButton)

    await waitFor(() => {
      expect(screen.getByText('Usuário ou senha inválidos')).toBeInTheDocument()
    })
  })

  it('deve mostrar estado de loading durante submit', async () => {
    mockLogin.mockImplementation(() => new Promise(() => {}))
    renderLogin()

    const usernameInput = screen.getByLabelText('Usuário')
    const passwordInput = screen.getByLabelText('Senha')

    fireEvent.change(usernameInput, { target: { value: 'rockstar' } })
    fireEvent.change(passwordInput, { target: { value: 'senha123' } })

    const submitButton = screen.getByRole('button', { name: 'Entrar' })
    fireEvent.click(submitButton)

    await waitFor(() => {
      expect(screen.getByRole('button', { name: 'Entrando...' })).toBeInTheDocument()
    })
  })

  it('deve ter link para cadastro', () => {
    renderLogin()

    expect(screen.getByText('Cadastre-se')).toBeInTheDocument()
  })

  it('deve ter botão de alternar tema', () => {
    renderLogin()

    expect(screen.getByRole('button', { name: /modo/i })).toBeInTheDocument()
  })
})

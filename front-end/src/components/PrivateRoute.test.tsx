import { describe, it, expect, vi } from 'vitest'
import { render, screen } from '@testing-library/react'
import { MemoryRouter, Routes, Route } from 'react-router-dom'
import PrivateRoute from './PrivateRoute'

const mockUseAuth = vi.fn()

vi.mock('../contexts/AuthContext', () => ({
  useAuth: () => mockUseAuth(),
}))

function renderWithRouter(isAuthenticated: boolean, isLoading = false) {
  mockUseAuth.mockReturnValue({ isAuthenticated, isLoading })

  return render(
    <MemoryRouter initialEntries={['/protected']}>
      <Routes>
        <Route path="/login" element={<div>Página de Login</div>} />
        <Route
          path="/protected"
          element={
            <PrivateRoute>
              <div>Conteúdo Protegido</div>
            </PrivateRoute>
          }
        />
      </Routes>
    </MemoryRouter>
  )
}

describe('PrivateRoute', () => {
  it('deve renderizar conteúdo quando autenticado', () => {
    renderWithRouter(true)

    expect(screen.getByText('Conteúdo Protegido')).toBeInTheDocument()
  })

  it('deve redirecionar para login quando não autenticado', () => {
    renderWithRouter(false)

    expect(screen.getByText('Página de Login')).toBeInTheDocument()
    expect(screen.queryByText('Conteúdo Protegido')).not.toBeInTheDocument()
  })

  it('deve mostrar loading enquanto verifica autenticação', () => {
    renderWithRouter(false, true)

    expect(screen.getByText('Carregando...')).toBeInTheDocument()
    expect(screen.queryByText('Conteúdo Protegido')).not.toBeInTheDocument()
    expect(screen.queryByText('Página de Login')).not.toBeInTheDocument()
  })
})

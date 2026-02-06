import { describe, it, expect, vi, beforeEach } from 'vitest'
import { render, screen, fireEvent, waitFor } from '@testing-library/react'
import { BrowserRouter } from 'react-router-dom'
import Home from './index'

const mockListar = vi.fn()

vi.mock('../../facades/artist.facade', () => ({
  artistFacade: {
    listar: (...args: unknown[]) => mockListar(...args),
  },
}))

vi.mock('../../components/Layout', () => ({
  default: ({ children }: { children: React.ReactNode }) => <div>{children}</div>,
}))

const renderHome = () => {
  return render(
    <BrowserRouter>
      <Home />
    </BrowserRouter>
  )
}

const mockArtists = [
  { id: 1, name: 'Queen', type: 'BAND', active: true, albumCount: 15 },
  { id: 2, name: 'Led Zeppelin', type: 'BAND', active: true, albumCount: 9 },
  { id: 3, name: 'David Bowie', type: 'SOLO', active: true, albumCount: 25 },
]

describe('Home', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  it('deve renderizar título e botão de novo artista', async () => {
    mockListar.mockResolvedValue({ content: [], totalPages: 0 })
    renderHome()

    expect(screen.getByText('Artistas')).toBeInTheDocument()
    expect(screen.getByText('Novo Artista')).toBeInTheDocument()
  })

  it('deve mostrar loading enquanto carrega', () => {
    mockListar.mockImplementation(() => new Promise(() => {}))
    renderHome()

    expect(screen.getByText('Carregando...')).toBeInTheDocument()
  })

  it('deve exibir lista de artistas', async () => {
    mockListar.mockResolvedValue({ content: mockArtists, totalPages: 1 })
    renderHome()

    await waitFor(() => {
      expect(screen.getByText('Queen')).toBeInTheDocument()
      expect(screen.getByText('Led Zeppelin')).toBeInTheDocument()
      expect(screen.getByText('David Bowie')).toBeInTheDocument()
    })
  })

  it('deve exibir mensagem quando não há artistas', async () => {
    mockListar.mockResolvedValue({ content: [], totalPages: 0 })
    renderHome()

    await waitFor(() => {
      expect(screen.getByText('Nenhum artista cadastrado.')).toBeInTheDocument()
    })
  })

  it('deve exibir mensagem de erro e botão de retry', async () => {
    mockListar.mockRejectedValue(new Error('Erro de rede'))
    renderHome()

    await waitFor(() => {
      expect(screen.getByText('Erro ao carregar artistas. Tente novamente.')).toBeInTheDocument()
      expect(screen.getByText('Tentar novamente')).toBeInTheDocument()
    })
  })

  it('deve chamar listar com parâmetros corretos', async () => {
    mockListar.mockResolvedValue({ content: mockArtists, totalPages: 1 })
    renderHome()

    await waitFor(() => {
      expect(mockListar).toHaveBeenCalledWith({
        page: 0,
        size: 10,
        name: undefined,
        sort: 'name',
        direction: 'asc',
      })
    })
  })

  it('deve filtrar por nome ao digitar na busca', async () => {
    mockListar.mockResolvedValue({ content: mockArtists, totalPages: 1 })
    renderHome()

    await waitFor(() => {
      expect(screen.getByText('Queen')).toBeInTheDocument()
    })

    const searchInput = screen.getByPlaceholderText('Buscar por nome...')
    fireEvent.change(searchInput, { target: { value: 'Queen' } })

    await waitFor(() => {
      expect(mockListar).toHaveBeenCalledWith(
        expect.objectContaining({ name: 'Queen' })
      )
    })
  })

  it('deve alternar ordenação ao clicar no botão', async () => {
    mockListar.mockResolvedValue({ content: mockArtists, totalPages: 1 })
    renderHome()

    await waitFor(() => {
      expect(screen.getByText('A-Z')).toBeInTheDocument()
    })

    const sortButton = screen.getByText('A-Z')
    fireEvent.click(sortButton)

    await waitFor(() => {
      expect(screen.getByText('Z-A')).toBeInTheDocument()
      expect(mockListar).toHaveBeenCalledWith(
        expect.objectContaining({ direction: 'desc' })
      )
    })
  })

  it('deve mostrar mensagem específica quando busca não retorna resultados', async () => {
    mockListar
      .mockResolvedValueOnce({ content: mockArtists, totalPages: 1 })
      .mockResolvedValueOnce({ content: [], totalPages: 0 })

    renderHome()

    await waitFor(() => {
      expect(screen.getByText('Queen')).toBeInTheDocument()
    })

    const searchInput = screen.getByPlaceholderText('Buscar por nome...')
    fireEvent.change(searchInput, { target: { value: 'XYZ123' } })

    await waitFor(() => {
      expect(screen.getByText('Nenhum artista encontrado para a busca.')).toBeInTheDocument()
    })
  })
})

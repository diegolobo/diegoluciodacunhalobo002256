import { useState, useEffect, useCallback } from 'react'
import { Link } from 'react-router-dom'
import Layout from '../../components/Layout'
import SearchInput from '../../components/ui/SearchInput'
import Pagination from '../../components/ui/Pagination'
import ArtistCard from '../../components/ui/ArtistCard'
import { artistFacade } from '../../facades/artist.facade'
import type { Artist } from '../../types'

function Home() {
  const [artists, setArtists] = useState<Artist[]>([])
  const [isLoading, setIsLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  const [search, setSearch] = useState('')
  const [page, setPage] = useState(0)
  const [totalPages, setTotalPages] = useState(0)
  const [sortDirection, setSortDirection] = useState<'asc' | 'desc'>('asc')

  const carregarArtistas = useCallback(async () => {
    setIsLoading(true)
    setError(null)

    try {
      const response = await artistFacade.listar({
        page,
        size: 10,
        name: search || undefined,
        sort: 'name',
        direction: sortDirection,
      })

      setArtists(response.content)
      setTotalPages(response.totalPages)
    } catch {
      setError('Erro ao carregar artistas. Tente novamente.')
    } finally {
      setIsLoading(false)
    }
  }, [page, search, sortDirection])

  useEffect(() => {
    carregarArtistas()
  }, [carregarArtistas])

  const handleSearchChange = (value: string) => {
    setSearch(value)
    setPage(0)
  }

  const toggleSortDirection = () => {
    setSortDirection((prev) => (prev === 'asc' ? 'desc' : 'asc'))
    setPage(0)
  }

  return (
    <Layout>
      <div className="space-y-6">
        <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
          <h1 className="text-2xl font-bold text-dark-800 dark:text-cream-100">
            Artistas
          </h1>
          <Link
            to="/artistas/novo"
            className="inline-flex items-center justify-center px-4 py-2 bg-accent-gold hover:bg-accent-gold-hover text-white font-medium rounded-lg transition-colors cursor-pointer"
          >
            <svg className="w-5 h-5 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 4v16m8-8H4" />
            </svg>
            Novo Artista
          </Link>
        </div>

        <div className="flex flex-col sm:flex-row gap-4">
          <div className="flex-1">
            <SearchInput
              value={search}
              onChange={handleSearchChange}
              placeholder="Buscar por nome..."
            />
          </div>
          <button
            onClick={toggleSortDirection}
            className="inline-flex items-center justify-center px-4 py-2 border-2 border-accent-gold text-accent-gold hover:bg-accent-gold hover:text-white rounded-lg transition-colors cursor-pointer"
          >
            <svg className="w-5 h-5 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path
                strokeLinecap="round"
                strokeLinejoin="round"
                strokeWidth={2}
                d="M3 4h13M3 8h9m-9 4h6m4 0l4-4m0 0l4 4m-4-4v12"
              />
            </svg>
            {sortDirection === 'asc' ? 'A-Z' : 'Z-A'}
          </button>
        </div>

        {isLoading ? (
          <div className="flex items-center justify-center py-12">
            <div className="text-dark-500 dark:text-cream-200">Carregando...</div>
          </div>
        ) : error ? (
          <div className="flex flex-col items-center justify-center py-12">
            <p className="text-red-600 dark:text-red-400 mb-4">{error}</p>
            <button
              onClick={carregarArtistas}
              className="px-4 py-2 bg-accent-gold hover:bg-accent-gold-hover text-white rounded-lg transition-colors cursor-pointer"
            >
              Tentar novamente
            </button>
          </div>
        ) : artists.length === 0 ? (
          <div className="flex flex-col items-center justify-center py-12">
            <svg
              className="w-16 h-16 text-dark-500 dark:text-cream-200 mb-4"
              fill="none"
              stroke="currentColor"
              viewBox="0 0 24 24"
            >
              <path
                strokeLinecap="round"
                strokeLinejoin="round"
                strokeWidth={1.5}
                d="M9.172 16.172a4 4 0 015.656 0M9 10h.01M15 10h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"
              />
            </svg>
            <p className="text-dark-500 dark:text-cream-200 text-lg">
              {search ? 'Nenhum artista encontrado para a busca.' : 'Nenhum artista cadastrado.'}
            </p>
            {!search && (
              <Link
                to="/artistas/novo"
                className="mt-4 px-4 py-2 bg-accent-gold hover:bg-accent-gold-hover text-white rounded-lg transition-colors cursor-pointer"
              >
                Cadastrar primeiro artista
              </Link>
            )}
          </div>
        ) : (
          <>
            <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-4">
              {artists.map((artist) => (
                <ArtistCard key={artist.id} artist={artist} />
              ))}
            </div>

            <Pagination page={page} totalPages={totalPages} onPageChange={setPage} />
          </>
        )}
      </div>
    </Layout>
  )
}

export default Home

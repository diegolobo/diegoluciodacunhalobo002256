import { useState, useEffect } from 'react'
import { useParams, Link } from 'react-router-dom'
import Layout from '../../components/Layout'
import AlbumCard from '../../components/ui/AlbumCard'
import { artistFacade } from '../../facades/artist.facade'
import type { Artist, Album } from '../../types'

function ArtistDetail() {
  const { id } = useParams<{ id: string }>()

  const [artist, setArtist] = useState<Artist | null>(null)
  const [albums, setAlbums] = useState<Album[]>([])
  const [isLoading, setIsLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  useEffect(() => {
    if (id) {
      carregarDados(Number(id))
    }
  }, [id])

  const carregarDados = async (artistId: number) => {
    setIsLoading(true)
    setError(null)

    try {
      const [artistData, albumsData] = await Promise.all([
        artistFacade.buscarPorId(artistId),
        artistFacade.buscarAlbuns(artistId),
      ])

      setArtist(artistData)
      setAlbums(albumsData)
    } catch {
      setError('Erro ao carregar dados do artista.')
    } finally {
      setIsLoading(false)
    }
  }

  if (isLoading) {
    return (
      <Layout>
        <div className="flex items-center justify-center py-12">
          <div className="text-dark-500 dark:text-cream-200">Carregando...</div>
        </div>
      </Layout>
    )
  }

  if (error || !artist) {
    return (
      <Layout>
        <div className="flex flex-col items-center justify-center py-12">
          <p className="text-red-600 dark:text-red-400 mb-4">
            {error || 'Artista não encontrado.'}
          </p>
          <Link
            to="/"
            className="px-4 py-2 bg-accent-gold hover:bg-accent-gold-hover text-white rounded-lg transition-colors cursor-pointer"
          >
            Voltar para listagem
          </Link>
        </div>
      </Layout>
    )
  }

  const tipoLabel = artist.type === 'SOLO' ? 'Artista Solo' : 'Banda'

  return (
    <Layout>
      <div className="space-y-8">
        <div className="flex items-center gap-4">
          <Link
            to="/"
            className="p-2 text-dark-500 dark:text-cream-200 hover:text-dark-800 dark:hover:text-cream-100 transition-colors cursor-pointer"
          >
            <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M15 19l-7-7 7-7" />
            </svg>
          </Link>
          <h1 className="text-2xl font-bold text-dark-800 dark:text-cream-100">
            Detalhes do Artista
          </h1>
        </div>

        <div className="bg-white dark:bg-dark-600 rounded-xl shadow-md p-6">
          <div className="flex flex-col sm:flex-row sm:items-start sm:justify-between gap-4">
            <div>
              <div className="flex items-center gap-3 mb-2">
                <h2 className="text-2xl font-bold text-dark-800 dark:text-cream-100">
                  {artist.name}
                </h2>
                {!artist.active && (
                  <span className="px-2 py-1 text-xs rounded bg-red-100 dark:bg-red-900/30 text-red-700 dark:text-red-400">
                    Inativo
                  </span>
                )}
              </div>
              <div className="flex items-center gap-4 text-dark-500 dark:text-cream-200">
                <span
                  className={`px-2 py-1 text-xs rounded-full ${
                    artist.type === 'SOLO'
                      ? 'bg-blue-100 dark:bg-blue-900/30 text-blue-700 dark:text-blue-400'
                      : 'bg-purple-100 dark:bg-purple-900/30 text-purple-700 dark:text-purple-400'
                  }`}
                >
                  {tipoLabel}
                </span>
                <span>{artist.albumCount} {artist.albumCount === 1 ? 'álbum' : 'álbuns'}</span>
              </div>
            </div>
            <Link
              to={`/artistas/${artist.id}/editar`}
              className="inline-flex items-center justify-center px-4 py-2 border-2 border-accent-gold text-accent-gold hover:bg-accent-gold hover:text-white rounded-lg transition-colors cursor-pointer"
            >
              <svg className="w-5 h-5 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path
                  strokeLinecap="round"
                  strokeLinejoin="round"
                  strokeWidth={2}
                  d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z"
                />
              </svg>
              Editar
            </Link>
          </div>
        </div>

        <div>
          <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4 mb-6">
            <h3 className="text-xl font-semibold text-dark-800 dark:text-cream-100">
              Álbuns
            </h3>
            <Link
              to={`/albuns/novo?artistId=${artist.id}`}
              className="inline-flex items-center justify-center px-4 py-2 bg-accent-gold hover:bg-accent-gold-hover text-white font-medium rounded-lg transition-colors cursor-pointer"
            >
              <svg className="w-5 h-5 mr-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 4v16m8-8H4" />
              </svg>
              Novo Álbum
            </Link>
          </div>

          {albums.length === 0 ? (
            <div className="flex flex-col items-center justify-center py-12 bg-white dark:bg-dark-600 rounded-xl">
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
                  d="M9 19V6l12-3v13M9 19c0 1.105-1.343 2-3 2s-3-.895-3-2 1.343-2 3-2 3 .895 3 2zm12-3c0 1.105-1.343 2-3 2s-3-.895-3-2 1.343-2 3-2 3 .895 3 2zM9 10l12-3"
                />
              </svg>
              <p className="text-dark-500 dark:text-cream-200 text-lg mb-4">
                Nenhum álbum cadastrado para este artista.
              </p>
              <Link
                to={`/albuns/novo?artistId=${artist.id}`}
                className="px-4 py-2 bg-accent-gold hover:bg-accent-gold-hover text-white rounded-lg transition-colors cursor-pointer"
              >
                Cadastrar primeiro álbum
              </Link>
            </div>
          ) : (
            <div className="grid grid-cols-2 sm:grid-cols-3 lg:grid-cols-4 xl:grid-cols-5 gap-4">
              {albums.map((album) => (
                <AlbumCard key={album.id} album={album} />
              ))}
            </div>
          )}
        </div>
      </div>
    </Layout>
  )
}

export default ArtistDetail

import { Link } from 'react-router-dom'
import type { Album } from '../../types'

interface AlbumCardProps {
  album: Album
  onDelete?: (albumId: number, albumTitle: string) => void
}

function AlbumCard({ album, onDelete }: AlbumCardProps) {
  const coverUrl = album.covers?.[0]?.presignedUrl

  const handleDelete = (e: React.MouseEvent) => {
    e.preventDefault()
    e.stopPropagation()
    onDelete?.(album.id, album.title)
  }

  return (
    <Link
      to={`/albuns/${album.id}/editar`}
      className="block rounded-xl overflow-hidden shadow-md hover:shadow-lg transition-all cursor-pointer group relative"
    >
      {onDelete && (
        <button
          onClick={handleDelete}
          className="absolute top-2 right-2 z-10 p-2 bg-red-500 text-white rounded-full opacity-0 group-hover:opacity-100 transition-opacity cursor-pointer hover:bg-red-600"
          title="Excluir álbum"
        >
          <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M6 18L18 6M6 6l12 12" />
          </svg>
        </button>
      )}
      <div className="relative aspect-square bg-dark-700">
        {coverUrl ? (
          <img
            src={coverUrl}
            alt={`Capa do álbum ${album.title}`}
            className="w-full h-full object-cover group-hover:scale-105 transition-transform duration-300"
          />
        ) : (
          <div className="w-full h-full flex items-center justify-center">
            <svg
              className="w-16 h-16 text-dark-500"
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
          </div>
        )}
      </div>
      <div className="bg-dark-800 p-4">
        <h3 className="font-semibold text-cream-100 truncate group-hover:text-accent-gold transition-colors">
          {album.title}
        </h3>
        {album.releaseYear && (
          <p className="text-sm text-cream-200 mt-1">{album.releaseYear}</p>
        )}
        {!album.active && (
          <span className="inline-block mt-2 px-2 py-0.5 text-xs rounded bg-red-900/50 text-red-400">
            Inativo
          </span>
        )}
      </div>
    </Link>
  )
}

export default AlbumCard

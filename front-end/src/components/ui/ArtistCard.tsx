import { Link } from 'react-router-dom'
import type { Artist } from '../../types'

interface ArtistCardProps {
  artist: Artist
}

function ArtistCard({ artist }: ArtistCardProps) {
  const tipoLabel = artist.type === 'SOLO' ? 'Artista Solo' : 'Banda'

  return (
    <Link
      to={`/artistas/${artist.id}`}
      className="block bg-white dark:bg-dark-600 rounded-xl shadow-md hover:shadow-lg transition-all cursor-pointer overflow-hidden group"
    >
      <div className="p-6">
        <div className="flex items-start justify-between mb-3">
          <h3 className="text-lg font-semibold text-dark-800 dark:text-cream-100 group-hover:text-accent-gold transition-colors">
            {artist.name}
          </h3>
          <span
            className={`px-2 py-1 text-xs rounded-full ${
              artist.type === 'SOLO'
                ? 'bg-blue-100 dark:bg-blue-900/30 text-blue-700 dark:text-blue-400'
                : 'bg-purple-100 dark:bg-purple-900/30 text-purple-700 dark:text-purple-400'
            }`}
          >
            {tipoLabel}
          </span>
        </div>

        <div className="flex items-center gap-4 text-sm text-dark-500 dark:text-cream-200">
          <div className="flex items-center gap-1">
            <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path
                strokeLinecap="round"
                strokeLinejoin="round"
                strokeWidth={2}
                d="M9 19V6l12-3v13M9 19c0 1.105-1.343 2-3 2s-3-.895-3-2 1.343-2 3-2 3 .895 3 2zm12-3c0 1.105-1.343 2-3 2s-3-.895-3-2 1.343-2 3-2 3 .895 3 2zM9 10l12-3"
              />
            </svg>
            <span>{artist.albumCount} {artist.albumCount === 1 ? 'álbum' : 'álbuns'}</span>
          </div>

          {!artist.active && (
            <span className="px-2 py-0.5 text-xs rounded bg-red-100 dark:bg-red-900/30 text-red-700 dark:text-red-400">
              Inativo
            </span>
          )}
        </div>
      </div>
    </Link>
  )
}

export default ArtistCard

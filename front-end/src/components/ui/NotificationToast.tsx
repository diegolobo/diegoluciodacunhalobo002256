import { useEffect, useState } from 'react'
import type { AlbumNotification } from '../../types'

interface NotificationToastProps {
  notification: AlbumNotification | null
  onClose: () => void
  duration?: number
}

function NotificationToast({ notification, onClose, duration = 5000 }: NotificationToastProps) {
  const [isVisible, setIsVisible] = useState(false)

  useEffect(() => {
    if (notification) {
      setIsVisible(true)
      const timer = setTimeout(() => {
        setIsVisible(false)
        setTimeout(onClose, 300)
      }, duration)
      return () => clearTimeout(timer)
    }
  }, [notification, duration, onClose])

  if (!notification) return null

  const getNotificationMessage = () => {
    switch (notification.type) {
      case 'ALBUM_CREATED':
        return `Novo álbum cadastrado: ${notification.albumTitle}`
      case 'ALBUM_UPDATED':
        return `Álbum atualizado: ${notification.albumTitle}`
      case 'ALBUM_DELETED':
        return `Álbum removido (ID: ${notification.albumId})`
      default:
        return 'Notificação recebida'
    }
  }

  const getNotificationIcon = () => {
    switch (notification.type) {
      case 'ALBUM_CREATED':
        return (
          <svg className="w-5 h-5 text-green-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 4v16m8-8H4" />
          </svg>
        )
      case 'ALBUM_UPDATED':
        return (
          <svg className="w-5 h-5 text-blue-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z" />
          </svg>
        )
      case 'ALBUM_DELETED':
        return (
          <svg className="w-5 h-5 text-red-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" />
          </svg>
        )
    }
  }

  return (
    <div
      className={`fixed top-4 right-4 z-50 transition-all duration-300 ${
        isVisible ? 'translate-x-0 opacity-100' : 'translate-x-full opacity-0'
      }`}
    >
      <div className="bg-white dark:bg-dark-600 rounded-lg shadow-lg border border-cream-200 dark:border-dark-500 p-4 max-w-sm">
        <div className="flex items-start gap-3">
          <div className="flex-shrink-0">{getNotificationIcon()}</div>
          <div className="flex-1 min-w-0">
            <p className="text-sm font-medium text-dark-800 dark:text-cream-100">
              {getNotificationMessage()}
            </p>
            {notification.releaseYear && (
              <p className="text-xs text-dark-500 dark:text-cream-200 mt-1">
                Ano: {notification.releaseYear}
              </p>
            )}
          </div>
          <button
            onClick={() => {
              setIsVisible(false)
              setTimeout(onClose, 300)
            }}
            className="flex-shrink-0 text-dark-500 dark:text-cream-200 hover:text-dark-800 dark:hover:text-cream-100 cursor-pointer"
          >
            <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M6 18L18 6M6 6l12 12" />
            </svg>
          </button>
        </div>
      </div>
    </div>
  )
}

export default NotificationToast

import { useEffect, useState, useCallback } from 'react'
import { websocketService } from '../services/websocket.service'
import type { AlbumNotification } from '../types'

export function useWebSocket() {
  const [isConnected, setIsConnected] = useState(false)
  const [lastNotification, setLastNotification] = useState<AlbumNotification | null>(null)

  useEffect(() => {
    websocketService.connect()

    const checkConnection = setInterval(() => {
      setIsConnected(websocketService.isConnected())
    }, 1000)

    const unsubscribe = websocketService.subscribe((notification) => {
      setLastNotification(notification)
    })

    return () => {
      clearInterval(checkConnection)
      unsubscribe()
    }
  }, [])

  const clearNotification = useCallback(() => {
    setLastNotification(null)
  }, [])

  return {
    isConnected,
    lastNotification,
    clearNotification,
  }
}

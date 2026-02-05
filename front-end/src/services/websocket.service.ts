import type { AlbumNotification } from '../types'

type MessageHandler = (notification: AlbumNotification) => void

class WebSocketService {
  private socket: WebSocket | null = null
  private messageHandlers: Set<MessageHandler> = new Set()
  private reconnectAttempts = 0
  private maxReconnectAttempts = 5
  private reconnectDelay = 3000

  connect(): void {
    if (this.socket?.readyState === WebSocket.OPEN) {
      return
    }

    const token = localStorage.getItem('accessToken')
    if (!token) {
      return
    }

    const baseUrl = import.meta.env.VITE_API_URL
      .replace(/^https/, 'wss')
      .replace(/^http/, 'ws')
    const wsUrl = `${baseUrl}/ws/albums?token=${encodeURIComponent(token)}`

    try {
      this.socket = new WebSocket(wsUrl)

      this.socket.onopen = () => {
        this.reconnectAttempts = 0
      }

      this.socket.onmessage = (event) => {
        try {
          const notification: AlbumNotification = JSON.parse(event.data)
          this.messageHandlers.forEach((handler) => handler(notification))
        } catch {
        }
      }

      this.socket.onclose = () => {
        this.handleReconnect()
      }

      this.socket.onerror = () => {
        this.socket?.close()
      }
    } catch {
    }
  }

  private handleReconnect(): void {
    if (this.reconnectAttempts < this.maxReconnectAttempts) {
      this.reconnectAttempts++
      setTimeout(() => this.connect(), this.reconnectDelay)
    }
  }

  disconnect(): void {
    if (this.socket) {
      this.socket.close()
      this.socket = null
    }
    this.reconnectAttempts = this.maxReconnectAttempts
  }

  subscribe(handler: MessageHandler): () => void {
    this.messageHandlers.add(handler)
    return () => {
      this.messageHandlers.delete(handler)
    }
  }

  isConnected(): boolean {
    return this.socket?.readyState === WebSocket.OPEN
  }
}

export const websocketService = new WebSocketService()

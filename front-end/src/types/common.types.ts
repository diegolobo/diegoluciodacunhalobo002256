export interface PageResponse<T> {
  content: T[]
  page: number
  size: number
  totalElements: number
  totalPages: number
  first: boolean
  last: boolean
}

export type AlbumNotificationType = 'ALBUM_CREATED' | 'ALBUM_UPDATED' | 'ALBUM_DELETED'

export interface AlbumNotification {
  type: AlbumNotificationType
  albumId: number
  albumTitle: string | null
  releaseYear: number | null
  timestamp: string
}

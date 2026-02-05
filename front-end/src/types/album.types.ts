import { Artist } from './artist.types'

export interface AlbumCover {
  id: number
  albumId: number
  fileName: string
  minioKey: string
  contentType: string
  fileSize: number
  presignedUrl?: string
  createdAt: string
}

export interface Album {
  id: number
  title: string
  releaseYear?: number
  active: boolean
  artists?: Artist[]
  covers?: AlbumCover[]
  createdAt: string
  updatedAt: string
}

export interface AlbumRequest {
  title: string
  releaseYear?: number
  active?: boolean
}

export interface AlbumFilter {
  title?: string
  artistId?: number
  active?: boolean
  page?: number
  size?: number
  sort?: string
}

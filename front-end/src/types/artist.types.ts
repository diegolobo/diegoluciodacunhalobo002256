export type ArtistType = 'SOLO' | 'BAND'

export interface Artist {
  id: number
  name: string
  type: ArtistType
  active: boolean
  albumCount: number
  regionalId?: number
  regionalName?: string
  createdAt: string
  updatedAt: string
}

export interface ArtistRequest {
  name: string
  type: ArtistType
  active?: boolean
  regionalId?: number
}

export interface ArtistFilter {
  name?: string
  type?: ArtistType
  active?: boolean
  page?: number
  size?: number
  sort?: string
}

import api from './api'
import type { Artist, ArtistRequest, ArtistType, Album, PageResponse } from '../types'

export interface ArtistFilterParams {
  page?: number
  size?: number
  name?: string
  type?: ArtistType
  active?: boolean
  regionalId?: number
  sort?: string
  direction?: 'asc' | 'desc'
}

export const artistService = {
  async findAll(params: ArtistFilterParams = {}): Promise<PageResponse<Artist>> {
    const response = await api.get<PageResponse<Artist>>('/api/v1/artists', { params })
    return response.data
  },

  async findById(id: number): Promise<Artist> {
    const response = await api.get<Artist>(`/api/v1/artists/${id}`)
    return response.data
  },

  async findAlbumsByArtistId(id: number): Promise<Album[]> {
    const response = await api.get<Album[]>(`/api/v1/artists/${id}/albums`)
    return response.data
  },

  async create(data: ArtistRequest): Promise<Artist> {
    const response = await api.post<Artist>('/api/v1/artists', data)
    return response.data
  },

  async update(id: number, data: ArtistRequest): Promise<Artist> {
    const response = await api.put<Artist>(`/api/v1/artists/${id}`, data)
    return response.data
  },

  async delete(id: number): Promise<void> {
    await api.delete(`/api/v1/artists/${id}`)
  },
}

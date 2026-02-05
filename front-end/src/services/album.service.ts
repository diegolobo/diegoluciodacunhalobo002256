import api from './api'
import type { Album, AlbumRequest, AlbumCover, Artist, ArtistType, PageResponse } from '../types'

export interface AlbumFilterParams {
  page?: number
  size?: number
  title?: string
  artistId?: number
  artistType?: ArtistType
  active?: boolean
  sort?: string
  direction?: 'asc' | 'desc'
}

export const albumService = {
  async findAll(params: AlbumFilterParams = {}): Promise<PageResponse<Album>> {
    const response = await api.get<PageResponse<Album>>('/api/v1/albums', { params })
    return response.data
  },

  async findById(id: number): Promise<Album> {
    const response = await api.get<Album>(`/api/v1/albums/${id}`)
    return response.data
  },

  async findArtistsByAlbumId(id: number): Promise<Artist[]> {
    const response = await api.get<Artist[]>(`/api/v1/albums/${id}/artists`)
    return response.data
  },

  async create(data: AlbumRequest): Promise<Album> {
    const response = await api.post<Album>('/api/v1/albums', data)
    return response.data
  },

  async update(id: number, data: AlbumRequest): Promise<Album> {
    const response = await api.put<Album>(`/api/v1/albums/${id}`, data)
    return response.data
  },

  async delete(id: number): Promise<void> {
    await api.delete(`/api/v1/albums/${id}`)
  },

  async addArtist(albumId: number, artistId: number): Promise<Album> {
    const response = await api.post<Album>(`/api/v1/albums/${albumId}/artists/${artistId}`)
    return response.data
  },

  async removeArtist(albumId: number, artistId: number): Promise<Album> {
    const response = await api.delete<Album>(`/api/v1/albums/${albumId}/artists/${artistId}`)
    return response.data
  },

  async getCovers(albumId: number): Promise<AlbumCover[]> {
    const response = await api.get<AlbumCover[]>(`/api/v1/albums/${albumId}/covers`)
    return response.data
  },

  async uploadCover(albumId: number, file: File): Promise<AlbumCover> {
    const formData = new FormData()
    formData.append('file', file)

    const response = await api.post<AlbumCover>(`/api/v1/albums/${albumId}/covers`, formData)
    return response.data
  },

  async uploadCovers(albumId: number, files: File[]): Promise<AlbumCover[]> {
    const formData = new FormData()
    files.forEach((file) => {
      formData.append('files', file)
    })

    const response = await api.post<AlbumCover[]>(`/api/v1/albums/${albumId}/covers/batch`, formData)
    return response.data
  },

  async deleteCover(albumId: number, coverId: number): Promise<void> {
    await api.delete(`/api/v1/albums/${albumId}/covers/${coverId}`)
  },
}

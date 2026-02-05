import { artistService } from '../services/artist.service'
import type { ArtistFilterParams } from '../services/artist.service'
import type { Artist, ArtistRequest, Album, PageResponse } from '../types'

export interface ArtistState {
  artists: Artist[]
  selectedArtist: Artist | null
  artistAlbums: Album[]
  pagination: {
    page: number
    size: number
    totalElements: number
    totalPages: number
  }
  isLoading: boolean
  error: string | null
}

export const artistFacade = {
  async listar(params: ArtistFilterParams = {}): Promise<PageResponse<Artist>> {
    return artistService.findAll(params)
  },

  async buscarPorId(id: number): Promise<Artist> {
    return artistService.findById(id)
  },

  async buscarAlbuns(artistId: number): Promise<Album[]> {
    return artistService.findAlbumsByArtistId(artistId)
  },

  async criar(data: ArtistRequest): Promise<Artist> {
    return artistService.create(data)
  },

  async atualizar(id: number, data: ArtistRequest): Promise<Artist> {
    return artistService.update(id, data)
  },

  async excluir(id: number): Promise<void> {
    return artistService.delete(id)
  },
}

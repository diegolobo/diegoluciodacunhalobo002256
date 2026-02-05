import { albumService, AlbumFilterParams } from '../services/album.service'
import type { Album, AlbumRequest, AlbumCover, Artist, PageResponse } from '../types'

export interface AlbumState {
  albums: Album[]
  selectedAlbum: Album | null
  albumArtists: Artist[]
  albumCovers: AlbumCover[]
  pagination: {
    page: number
    size: number
    totalElements: number
    totalPages: number
  }
  isLoading: boolean
  error: string | null
}

export const albumFacade = {
  async listar(params: AlbumFilterParams = {}): Promise<PageResponse<Album>> {
    return albumService.findAll(params)
  },

  async buscarPorId(id: number): Promise<Album> {
    return albumService.findById(id)
  },

  async buscarArtistas(albumId: number): Promise<Artist[]> {
    return albumService.findArtistsByAlbumId(albumId)
  },

  async criar(data: AlbumRequest): Promise<Album> {
    return albumService.create(data)
  },

  async atualizar(id: number, data: AlbumRequest): Promise<Album> {
    return albumService.update(id, data)
  },

  async excluir(id: number): Promise<void> {
    return albumService.delete(id)
  },

  async adicionarArtista(albumId: number, artistId: number): Promise<Album> {
    return albumService.addArtist(albumId, artistId)
  },

  async removerArtista(albumId: number, artistId: number): Promise<Album> {
    return albumService.removeArtist(albumId, artistId)
  },

  async buscarCapas(albumId: number): Promise<AlbumCover[]> {
    return albumService.getCovers(albumId)
  },

  async uploadCapa(albumId: number, file: File): Promise<AlbumCover> {
    return albumService.uploadCover(albumId, file)
  },

  async uploadCapas(albumId: number, files: File[]): Promise<AlbumCover[]> {
    return albumService.uploadCovers(albumId, files)
  },

  async excluirCapa(albumId: number, coverId: number): Promise<void> {
    return albumService.deleteCover(albumId, coverId)
  },
}

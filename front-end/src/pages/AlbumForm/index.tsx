import { useState, useEffect, FormEvent, ChangeEvent } from 'react'
import { useNavigate, useParams, useSearchParams, Link } from 'react-router-dom'
import Layout from '../../components/Layout'
import { InputField, CheckboxField } from '../../components/ui/FormField'
import { albumFacade } from '../../facades/album.facade'
import { artistFacade } from '../../facades/artist.facade'
import type { Artist, AlbumCover } from '../../types'

interface FormErrors {
  title?: string
  artists?: string
}

function AlbumForm() {
  const { id } = useParams<{ id: string }>()
  const [searchParams] = useSearchParams()
  const navigate = useNavigate()
  const isEditing = Boolean(id)
  const artistIdFromUrl = searchParams.get('artistId')

  const [title, setTitle] = useState('')
  const [releaseYear, setReleaseYear] = useState('')
  const [active, setActive] = useState(true)
  const [selectedArtists, setSelectedArtists] = useState<number[]>([])
  const [existingCovers, setExistingCovers] = useState<AlbumCover[]>([])
  const [newFiles, setNewFiles] = useState<File[]>([])
  const [previews, setPreviews] = useState<string[]>([])

  const [availableArtists, setAvailableArtists] = useState<Artist[]>([])
  const [errors, setErrors] = useState<FormErrors>({})
  const [isLoading, setIsLoading] = useState(false)
  const [isLoadingData, setIsLoadingData] = useState(true)
  const [submitError, setSubmitError] = useState<string | null>(null)

  useEffect(() => {
    carregarArtistas()
  }, [])

  useEffect(() => {
    if (isEditing && id) {
      carregarAlbum(Number(id))
    } else {
      setIsLoadingData(false)
      if (artistIdFromUrl) {
        setSelectedArtists([Number(artistIdFromUrl)])
      }
    }
  }, [isEditing, id, artistIdFromUrl])

  const carregarArtistas = async () => {
    try {
      const response = await artistFacade.listar({ size: 100, direction: 'asc' })
      setAvailableArtists(response.content)
    } catch {
      setSubmitError('Erro ao carregar lista de artistas.')
    }
  }

  const carregarAlbum = async (albumId: number) => {
    setIsLoadingData(true)
    try {
      const album = await albumFacade.buscarPorId(albumId)
      setTitle(album.title)
      setReleaseYear(album.releaseYear?.toString() || '')
      setActive(album.active)
      setSelectedArtists(album.artists?.map((a) => a.id) || [])
      setExistingCovers(album.covers || [])
    } catch {
      setSubmitError('Erro ao carregar dados do álbum.')
    } finally {
      setIsLoadingData(false)
    }
  }

  const handleFileChange = (e: ChangeEvent<HTMLInputElement>) => {
    const files = Array.from(e.target.files || [])
    if (files.length === 0) return

    setNewFiles((prev) => [...prev, ...files])

    files.forEach((file) => {
      const reader = new FileReader()
      reader.onload = () => {
        setPreviews((prev) => [...prev, reader.result as string])
      }
      reader.readAsDataURL(file)
    })

    e.target.value = ''
  }

  const removeNewFile = (index: number) => {
    setNewFiles((prev) => prev.filter((_, i) => i !== index))
    setPreviews((prev) => prev.filter((_, i) => i !== index))
  }

  const removeExistingCover = async (coverId: number) => {
    if (!id) return

    try {
      await albumFacade.excluirCapa(Number(id), coverId)
      setExistingCovers((prev) => prev.filter((c) => c.id !== coverId))
    } catch {
      setSubmitError('Erro ao remover capa.')
    }
  }

  const toggleArtist = (artistId: number) => {
    setSelectedArtists((prev) =>
      prev.includes(artistId)
        ? prev.filter((id) => id !== artistId)
        : [...prev, artistId]
    )
  }

  const validar = (): boolean => {
    const newErrors: FormErrors = {}

    if (!title.trim()) {
      newErrors.title = 'Título é obrigatório'
    }

    if (selectedArtists.length === 0) {
      newErrors.artists = 'Selecione pelo menos um artista'
    }

    setErrors(newErrors)
    return Object.keys(newErrors).length === 0
  }

  const handleSubmit = async (e: FormEvent) => {
    e.preventDefault()
    setSubmitError(null)

    if (!validar()) return

    setIsLoading(true)

    try {
      const data = {
        title: title.trim(),
        releaseYear: releaseYear ? Number(releaseYear) : undefined,
        active,
      }

      let albumId: number

      if (isEditing && id) {
        await albumFacade.atualizar(Number(id), data)
        albumId = Number(id)

        const currentArtists = (await albumFacade.buscarArtistas(albumId)).map((a) => a.id)

        for (const artistId of currentArtists) {
          if (!selectedArtists.includes(artistId)) {
            await albumFacade.removerArtista(albumId, artistId)
          }
        }

        for (const artistId of selectedArtists) {
          if (!currentArtists.includes(artistId)) {
            await albumFacade.adicionarArtista(albumId, artistId)
          }
        }
      } else {
        const album = await albumFacade.criar(data)
        albumId = album.id

        for (const artistId of selectedArtists) {
          await albumFacade.adicionarArtista(albumId, artistId)
        }
      }

      for (const file of newFiles) {
        await albumFacade.uploadCapa(albumId, file)
      }

      const returnTo = artistIdFromUrl ? `/artistas/${artistIdFromUrl}` : '/'
      navigate(returnTo)
    } catch {
      setSubmitError(
        isEditing
          ? 'Erro ao atualizar álbum. Tente novamente.'
          : 'Erro ao cadastrar álbum. Tente novamente.'
      )
    } finally {
      setIsLoading(false)
    }
  }

  if (isLoadingData) {
    return (
      <Layout>
        <div className="flex items-center justify-center py-12">
          <div className="text-dark-500 dark:text-cream-200">Carregando...</div>
        </div>
      </Layout>
    )
  }

  return (
    <Layout>
      <div className="max-w-2xl mx-auto">
        <div className="mb-6">
          <Link
            to={artistIdFromUrl ? `/artistas/${artistIdFromUrl}` : '/'}
            className="inline-flex items-center text-dark-500 dark:text-cream-200 hover:text-dark-800 dark:hover:text-cream-100 transition-colors cursor-pointer"
          >
            <svg className="w-5 h-5 mr-1" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M15 19l-7-7 7-7" />
            </svg>
            Voltar
          </Link>
        </div>

        <div className="bg-white dark:bg-dark-600 rounded-xl shadow-md p-6">
          <h1 className="text-2xl font-bold text-dark-800 dark:text-cream-100 mb-6">
            {isEditing ? 'Editar Álbum' : 'Novo Álbum'}
          </h1>

          <form onSubmit={handleSubmit} className="space-y-6">
            <InputField
              label="Título"
              required
              value={title}
              onChange={(e) => setTitle(e.target.value)}
              placeholder="Digite o título do álbum"
              error={errors.title}
              disabled={isLoading}
            />

            <InputField
              label="Ano de Lançamento"
              type="number"
              value={releaseYear}
              onChange={(e) => setReleaseYear(e.target.value)}
              placeholder="Ex: 2024"
              disabled={isLoading}
            />

            <div className="space-y-2">
              <label className="block text-sm font-medium text-dark-800 dark:text-cream-100">
                Artistas <span className="text-red-500">*</span>
              </label>
              <div className="max-h-48 overflow-y-auto border border-cream-200 dark:border-dark-500 rounded-lg p-3 space-y-2">
                {availableArtists.map((artist) => (
                  <label
                    key={artist.id}
                    className="flex items-center gap-2 cursor-pointer hover:bg-cream-100 dark:hover:bg-dark-700 p-1 rounded"
                  >
                    <input
                      type="checkbox"
                      checked={selectedArtists.includes(artist.id)}
                      onChange={() => toggleArtist(artist.id)}
                      className="w-4 h-4 rounded border-cream-200 dark:border-dark-500 text-accent-gold focus:ring-accent-gold cursor-pointer"
                      disabled={isLoading}
                    />
                    <span className="text-dark-800 dark:text-cream-100">{artist.name}</span>
                    <span className="text-xs text-dark-500 dark:text-cream-200">
                      ({artist.type === 'SOLO' ? 'Solo' : 'Banda'})
                    </span>
                  </label>
                ))}
              </div>
              {errors.artists && (
                <p className="text-sm text-red-500 dark:text-red-400">{errors.artists}</p>
              )}
            </div>

            <div className="space-y-2">
              <label className="block text-sm font-medium text-dark-800 dark:text-cream-100">
                Capas do Álbum
              </label>

              {(existingCovers.length > 0 || previews.length > 0) && (
                <div className="grid grid-cols-3 sm:grid-cols-4 gap-3 mb-3">
                  {existingCovers.map((cover) => (
                    <div key={cover.id} className="relative group">
                      <img
                        src={cover.presignedUrl}
                        alt="Capa existente"
                        className="w-full aspect-square object-cover rounded-lg"
                      />
                      <button
                        type="button"
                        onClick={() => removeExistingCover(cover.id)}
                        className="absolute top-1 right-1 p-1 bg-red-500 text-white rounded-full opacity-0 group-hover:opacity-100 transition-opacity cursor-pointer"
                      >
                        <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                          <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M6 18L18 6M6 6l12 12" />
                        </svg>
                      </button>
                    </div>
                  ))}
                  {previews.map((preview, index) => (
                    <div key={`new-${index}`} className="relative group">
                      <img
                        src={preview}
                        alt="Nova capa"
                        className="w-full aspect-square object-cover rounded-lg"
                      />
                      <button
                        type="button"
                        onClick={() => removeNewFile(index)}
                        className="absolute top-1 right-1 p-1 bg-red-500 text-white rounded-full opacity-0 group-hover:opacity-100 transition-opacity cursor-pointer"
                      >
                        <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                          <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M6 18L18 6M6 6l12 12" />
                        </svg>
                      </button>
                      <span className="absolute bottom-1 left-1 px-1 text-xs bg-accent-gold text-white rounded">
                        Novo
                      </span>
                    </div>
                  ))}
                </div>
              )}

              <label className="flex flex-col items-center justify-center w-full h-32 border-2 border-dashed border-cream-200 dark:border-dark-500 rounded-lg cursor-pointer hover:bg-cream-100 dark:hover:bg-dark-700 transition-colors">
                <svg className="w-8 h-8 text-dark-500 dark:text-cream-200 mb-2" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 6v6m0 0v6m0-6h6m-6 0H6" />
                </svg>
                <span className="text-sm text-dark-500 dark:text-cream-200">
                  Clique para adicionar capas
                </span>
                <input
                  type="file"
                  accept="image/*"
                  multiple
                  onChange={handleFileChange}
                  className="hidden"
                  disabled={isLoading}
                />
              </label>
            </div>

            <CheckboxField
              label="Álbum ativo"
              checked={active}
              onChange={(e) => setActive(e.target.checked)}
              disabled={isLoading}
            />

            {submitError && (
              <div className="p-3 rounded-lg bg-red-100 dark:bg-red-900/30 text-red-700 dark:text-red-400 text-sm">
                {submitError}
              </div>
            )}

            <div className="flex gap-4">
              <button
                type="submit"
                disabled={isLoading}
                className="flex-1 py-2 px-4 bg-accent-gold hover:bg-accent-gold-hover disabled:opacity-50 disabled:cursor-not-allowed text-white font-medium rounded-lg transition-colors cursor-pointer"
              >
                {isLoading ? 'Salvando...' : isEditing ? 'Salvar Alterações' : 'Cadastrar'}
              </button>
              <Link
                to={artistIdFromUrl ? `/artistas/${artistIdFromUrl}` : '/'}
                className="flex-1 py-2 px-4 border-2 border-accent-gold text-accent-gold hover:bg-accent-gold hover:text-white font-medium rounded-lg transition-colors cursor-pointer text-center"
              >
                Cancelar
              </Link>
            </div>
          </form>
        </div>
      </div>
    </Layout>
  )
}

export default AlbumForm

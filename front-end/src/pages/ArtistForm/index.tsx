import { useState, useEffect, FormEvent } from 'react'
import { useNavigate, useParams, Link } from 'react-router-dom'
import Layout from '../../components/Layout'
import { InputField, SelectField, CheckboxField } from '../../components/ui/FormField'
import { artistFacade } from '../../facades/artist.facade'
import type { ArtistType } from '../../types'

interface FormErrors {
  name?: string
  type?: string
}

function ArtistForm() {
  const { id } = useParams<{ id: string }>()
  const navigate = useNavigate()
  const isEditing = Boolean(id)

  const [name, setName] = useState('')
  const [type, setType] = useState<ArtistType | ''>('')
  const [active, setActive] = useState(true)

  const [errors, setErrors] = useState<FormErrors>({})
  const [isLoading, setIsLoading] = useState(false)
  const [isLoadingData, setIsLoadingData] = useState(isEditing)
  const [submitError, setSubmitError] = useState<string | null>(null)

  useEffect(() => {
    if (isEditing && id) {
      carregarArtista(Number(id))
    }
  }, [isEditing, id])

  const carregarArtista = async (artistId: number) => {
    setIsLoadingData(true)
    try {
      const artist = await artistFacade.buscarPorId(artistId)
      setName(artist.name)
      setType(artist.type)
      setActive(artist.active)
    } catch {
      setSubmitError('Erro ao carregar dados do artista.')
    } finally {
      setIsLoadingData(false)
    }
  }

  const validar = (): boolean => {
    const newErrors: FormErrors = {}

    if (!name.trim()) {
      newErrors.name = 'Nome é obrigatório'
    }

    if (!type) {
      newErrors.type = 'Tipo é obrigatório'
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
        name: name.trim(),
        type: type as ArtistType,
        active,
      }

      if (isEditing && id) {
        await artistFacade.atualizar(Number(id), data)
      } else {
        await artistFacade.criar(data)
      }

      navigate('/')
    } catch {
      setSubmitError(
        isEditing
          ? 'Erro ao atualizar artista. Tente novamente.'
          : 'Erro ao cadastrar artista. Tente novamente.'
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
            to="/"
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
            {isEditing ? 'Editar Artista' : 'Novo Artista'}
          </h1>

          <form onSubmit={handleSubmit} className="space-y-6">
            <InputField
              label="Nome"
              required
              value={name}
              onChange={(e) => setName(e.target.value)}
              placeholder="Digite o nome do artista"
              error={errors.name}
              disabled={isLoading}
            />

            <SelectField
              label="Tipo"
              required
              value={type}
              onChange={(e) => setType(e.target.value as ArtistType)}
              error={errors.type}
              disabled={isLoading}
            >
              <option value="">Selecione o tipo</option>
              <option value="SOLO">Artista Solo</option>
              <option value="BAND">Banda</option>
            </SelectField>

            <CheckboxField
              label="Artista ativo"
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
                to="/"
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

export default ArtistForm

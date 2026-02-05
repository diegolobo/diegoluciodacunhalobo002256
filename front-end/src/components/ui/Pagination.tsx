interface PaginationProps {
  page: number
  totalPages: number
  onPageChange: (page: number) => void
}

function Pagination({ page, totalPages, onPageChange }: PaginationProps) {
  if (totalPages <= 1) return null

  const gerarPaginas = () => {
    const paginas: (number | string)[] = []
    const maxVisiveis = 5

    if (totalPages <= maxVisiveis) {
      for (let i = 0; i < totalPages; i++) {
        paginas.push(i)
      }
    } else {
      paginas.push(0)

      if (page > 2) {
        paginas.push('...')
      }

      const inicio = Math.max(1, page - 1)
      const fim = Math.min(totalPages - 2, page + 1)

      for (let i = inicio; i <= fim; i++) {
        if (!paginas.includes(i)) {
          paginas.push(i)
        }
      }

      if (page < totalPages - 3) {
        paginas.push('...')
      }

      if (!paginas.includes(totalPages - 1)) {
        paginas.push(totalPages - 1)
      }
    }

    return paginas
  }

  return (
    <div className="flex items-center justify-center gap-2">
      <button
        onClick={() => onPageChange(page - 1)}
        disabled={page === 0}
        className="px-3 py-1 rounded-lg border border-cream-200 dark:border-dark-500 bg-white dark:bg-dark-700 text-dark-800 dark:text-cream-100 disabled:opacity-50 disabled:cursor-not-allowed hover:bg-cream-100 dark:hover:bg-dark-600 transition-colors cursor-pointer"
      >
        Anterior
      </button>

      <div className="flex items-center gap-1">
        {gerarPaginas().map((p, index) =>
          typeof p === 'string' ? (
            <span key={`ellipsis-${index}`} className="px-2 text-dark-500 dark:text-cream-200">
              {p}
            </span>
          ) : (
            <button
              key={p}
              onClick={() => onPageChange(p)}
              className={`px-3 py-1 rounded-lg transition-colors cursor-pointer ${
                p === page
                  ? 'bg-accent-gold text-white'
                  : 'border border-cream-200 dark:border-dark-500 bg-white dark:bg-dark-700 text-dark-800 dark:text-cream-100 hover:bg-cream-100 dark:hover:bg-dark-600'
              }`}
            >
              {p + 1}
            </button>
          )
        )}
      </div>

      <button
        onClick={() => onPageChange(page + 1)}
        disabled={page >= totalPages - 1}
        className="px-3 py-1 rounded-lg border border-cream-200 dark:border-dark-500 bg-white dark:bg-dark-700 text-dark-800 dark:text-cream-100 disabled:opacity-50 disabled:cursor-not-allowed hover:bg-cream-100 dark:hover:bg-dark-600 transition-colors cursor-pointer"
      >
        Próxima
      </button>
    </div>
  )
}

export default Pagination

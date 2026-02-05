function Footer() {
  const anoAtual = new Date().getFullYear()

  return (
    <footer className="bg-white dark:bg-dark-600 border-t border-cream-200 dark:border-dark-500 transition-colors">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-6">
        <div className="text-center text-sm text-dark-500 dark:text-cream-200">
          <p>© {anoAtual} Rockstars App. Todos os direitos reservados.</p>
        </div>
      </div>
    </footer>
  )
}

export default Footer

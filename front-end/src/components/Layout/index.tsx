import { ReactNode } from 'react'
import Header from '../Header'
import Footer from '../Footer'
import NotificationToast from '../ui/NotificationToast'
import { useWebSocket } from '../../hooks/useWebSocket'

interface LayoutProps {
  children: ReactNode
}

function Layout({ children }: LayoutProps) {
  const { lastNotification, clearNotification } = useWebSocket()

  return (
    <div className="min-h-screen flex flex-col bg-cream-100 dark:bg-dark-800 transition-colors">
      <Header />
      <main className="flex-1 max-w-7xl w-full mx-auto px-4 sm:px-6 lg:px-8 py-8">
        {children}
      </main>
      <Footer />
      <NotificationToast notification={lastNotification} onClose={clearNotification} />
    </div>
  )
}

export default Layout

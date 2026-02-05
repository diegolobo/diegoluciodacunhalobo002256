import { lazy, Suspense } from 'react'
import { Routes, Route } from 'react-router-dom'
import PrivateRoute from './components/PrivateRoute'

const Login = lazy(() => import('./pages/Login'))
const Home = lazy(() => import('./pages/Home'))
const ArtistDetail = lazy(() => import('./pages/ArtistDetail'))
const ArtistForm = lazy(() => import('./pages/ArtistForm'))
const AlbumForm = lazy(() => import('./pages/AlbumForm'))

function Loading() {
  return (
    <div className="min-h-screen flex items-center justify-center">
      <p className="text-lg">Carregando...</p>
    </div>
  )
}

function AppRoutes() {
  return (
    <Suspense fallback={<Loading />}>
      <Routes>
        <Route path="/login" element={<Login />} />
        <Route
          path="/"
          element={
            <PrivateRoute>
              <Home />
            </PrivateRoute>
          }
        />
        <Route
          path="/artistas/:id"
          element={
            <PrivateRoute>
              <ArtistDetail />
            </PrivateRoute>
          }
        />
        <Route
          path="/artistas/novo"
          element={
            <PrivateRoute>
              <ArtistForm />
            </PrivateRoute>
          }
        />
        <Route
          path="/artistas/:id/editar"
          element={
            <PrivateRoute>
              <ArtistForm />
            </PrivateRoute>
          }
        />
        <Route
          path="/albuns/novo"
          element={
            <PrivateRoute>
              <AlbumForm />
            </PrivateRoute>
          }
        />
        <Route
          path="/albuns/:id/editar"
          element={
            <PrivateRoute>
              <AlbumForm />
            </PrivateRoute>
          }
        />
      </Routes>
    </Suspense>
  )
}

export default AppRoutes

const BASE = import.meta.env.VITE_API_URL || 'http://localhost:8080'
const KEY = 'auth'

let auth = sessionStorage.getItem(KEY)

/** Chamada à API com as credenciais do usuário logado (HTTP Basic). Lança Error com a mensagem do servidor. */
export async function api(path, { method = 'GET', body, credentials } = {}) {
  const token = credentials ? btoa(`${credentials.login}:${credentials.senha}`) : auth
  const res = await fetch(BASE + path, {
    method,
    headers: {
      ...(body ? { 'Content-Type': 'application/json' } : {}),
      ...(token ? { Authorization: `Basic ${token}` } : {}),
    },
    body: body ? JSON.stringify(body) : undefined,
  })
  if (!res.ok) {
    const err = await res.json().catch(() => ({}))
    if (res.status === 401) throw new Error('Usuário ou senha inválidos')
    throw new Error(err.message || err.detail || `Erro ${res.status}`)
  }
  return res.status === 204 ? null : res.json()
}

export async function login(login, senha) {
  const usuario = await api('/api/auth/me', { credentials: { login, senha } })
  auth = btoa(`${login}:${senha}`)
  sessionStorage.setItem(KEY, auth)
  return usuario
}

export function logout() {
  auth = null
  sessionStorage.removeItem(KEY)
}

export const temSessao = () => !!auth

import { useEffect, useState } from 'react'
import { api, logout, temSessao } from './services/api'
import Login from './pages/Login'
import ClienteForm from './pages/ClienteForm'
import Pedidos from './pages/Pedidos'
import Clientes from './pages/Clientes'
import Automoveis from './pages/Automoveis'

export default function App() {
  const [usuario, setUsuario] = useState(null)
  const [tela, setTela] = useState('pedidos')
  const [carregando, setCarregando] = useState(temSessao())

  useEffect(() => {
    if (temSessao()) api('/api/auth/me').then(setUsuario).catch(logout).finally(() => setCarregando(false))
  }, [])

  const sair = () => { logout(); setUsuario(null); setTela('pedidos') }

  if (carregando) return <p className="container">Carregando...</p>

  if (!usuario) {
    return tela === 'cadastro'
      ? <div className="container"><h1>Cadastro de cliente</h1>
          <ClienteForm onSalvo={() => { alert('Cadastro realizado! Faça login.'); setTela('pedidos') }} />
          <button className="link" onClick={() => setTela('pedidos')}>Voltar ao login</button></div>
      : <Login onLogin={setUsuario} onCadastro={() => setTela('cadastro')} />
  }

  const agente = usuario.perfil === 'AGENTE'
  const abas = agente
    ? [['pedidos', 'Pedidos'], ['clientes', 'Clientes'], ['automoveis', 'Automóveis']]
    : [['pedidos', 'Meus pedidos'], ['perfil', 'Meu cadastro'], ['automoveis', 'Automóveis']]

  return (
    <>
      <header>
        <strong>🚗 Aluguel de Carros</strong>
        <nav>{abas.map(([id, nome]) =>
          <button key={id} className={tela === id ? 'ativo' : ''} onClick={() => setTela(id)}>{nome}</button>)}</nav>
        <span>{usuario.nome} ({agente ? usuario.tipoAgente : 'CLIENTE'}) <button onClick={sair}>Sair</button></span>
      </header>
      <main className="container">
        {tela === 'pedidos' && <Pedidos usuario={usuario} />}
        {tela === 'clientes' && <Clientes usuario={usuario} />}
        {tela === 'automoveis' && <Automoveis usuario={usuario} />}
        {tela === 'perfil' && <Perfil usuario={usuario} onExcluido={sair} />}
      </main>
    </>
  )
}

function Perfil({ usuario, onExcluido }) {
  const [cliente, setCliente] = useState(null)
  useEffect(() => { api(`/api/clientes/${usuario.id}`).then(setCliente) }, [usuario.id])
  if (!cliente) return <p>Carregando...</p>
  const excluir = async () => {
    if (!confirm('Excluir sua conta e todos os seus pedidos?')) return
    await api(`/api/clientes/${cliente.id}`, { method: 'DELETE' })
    onExcluido()
  }
  return (
    <>
      <h1>Meu cadastro</h1>
      <ClienteForm cliente={cliente} onSalvo={c => { setCliente(c); alert('Dados atualizados') }} />
      <button className="perigo" onClick={excluir}>Excluir minha conta</button>
    </>
  )
}

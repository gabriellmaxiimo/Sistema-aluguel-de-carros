import { useState } from 'react'
import { login } from '../services/api'

export default function Login({ onLogin, onCadastro }) {
  const [form, setForm] = useState({ login: '', senha: '' })
  const [erro, setErro] = useState('')

  const entrar = async e => {
    e.preventDefault()
    setErro('')
    try { onLogin(await login(form.login, form.senha)) } catch (err) { setErro(err.message) }
  }

  return (
    <div className="container login">
      <h1>🚗 Aluguel de Carros</h1>
      <form onSubmit={entrar} className="card">
        <label>Usuário<input required value={form.login} onChange={e => setForm({ ...form, login: e.target.value })} /></label>
        <label>Senha<input required type="password" value={form.senha} onChange={e => setForm({ ...form, senha: e.target.value })} /></label>
        {erro && <p className="erro">{erro}</p>}
        <button type="submit">Entrar</button>
      </form>
      <button className="link" onClick={onCadastro}>Não tem conta? Cadastre-se como cliente</button>
    </div>
  )
}

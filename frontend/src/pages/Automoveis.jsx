import { useEffect, useState } from 'react'
import { api } from '../services/api'

const VAZIO = { matricula: '', ano: '', marca: '', modelo: '', placa: '' }

export default function Automoveis({ usuario }) {
  const [automoveis, setAutomoveis] = useState([])
  const [form, setForm] = useState(VAZIO)
  const [erro, setErro] = useState('')

  const carregar = () => api('/api/automoveis').then(setAutomoveis)
  useEffect(() => { carregar() }, [])

  const salvar = async e => {
    e.preventDefault()
    setErro('')
    try { await api('/api/automoveis', { method: 'POST', body: form }); setForm(VAZIO); carregar() } catch (err) { setErro(err.message) }
  }

  return (
    <>
      <h1>Automóveis</h1>
      {usuario.perfil === 'AGENTE' &&
        <form onSubmit={salvar} className="card linha">
          {Object.keys(VAZIO).map(k =>
            <input key={k} required placeholder={k[0].toUpperCase() + k.slice(1)} type={k === 'ano' ? 'number' : 'text'}
                   value={form[k]} onChange={e => setForm({ ...form, [k]: e.target.value })} />)}
          <button type="submit">Cadastrar</button>
        </form>}
      {erro && <p className="erro">{erro}</p>}
      <table>
        <thead><tr><th>Matrícula</th><th>Ano</th><th>Marca</th><th>Modelo</th><th>Placa</th></tr></thead>
        <tbody>{automoveis.map(a =>
          <tr key={a.id}><td>{a.matricula}</td><td>{a.ano}</td><td>{a.marca}</td><td>{a.modelo}</td><td>{a.placa}</td></tr>)}
        </tbody>
      </table>
    </>
  )
}

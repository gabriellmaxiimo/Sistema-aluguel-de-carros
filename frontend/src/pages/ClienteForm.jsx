import { useState } from 'react'
import { api } from '../services/api'

const VAZIO = { login: '', senha: '', nome: '', rg: '', cpf: '', endereco: '', profissao: '', rendimentos: [] }
const MAX_RENDIMENTOS = 3

/** Cadastro (sem `cliente`) ou edição (com `cliente`) de cliente. */
export default function ClienteForm({ cliente, onSalvo }) {
  const [form, setForm] = useState(cliente ? { ...cliente, senha: '' } : VAZIO)
  const [erro, setErro] = useState('')

  const campo = (nome, rotulo, props = {}) => (
    <label>{rotulo}<input value={form[nome] ?? ''} onChange={e => setForm({ ...form, [nome]: e.target.value })} {...props} /></label>
  )
  const setRend = (i, k, v) =>
    setForm({ ...form, rendimentos: form.rendimentos.map((r, j) => j === i ? { ...r, [k]: v } : r) })

  const salvar = async e => {
    e.preventDefault()
    setErro('')
    try {
      const salvo = await api(cliente ? `/api/clientes/${cliente.id}` : '/api/clientes',
        { method: cliente ? 'PUT' : 'POST', body: form })
      onSalvo(salvo)
    } catch (err) { setErro(err.message) }
  }

  return (
    <form onSubmit={salvar} className="card">
      <div className="grid">
        {campo('login', 'Usuário', { required: true })}
        {campo('senha', cliente ? 'Nova senha (opcional)' : 'Senha', { type: 'password', required: !cliente })}
        {campo('nome', 'Nome', { required: true })}
        {campo('rg', 'RG', { required: true })}
        {campo('cpf', 'CPF', { required: true })}
        {campo('profissao', 'Profissão')}
        {campo('endereco', 'Endereço')}
      </div>
      <h3>Rendimentos (máximo {MAX_RENDIMENTOS})</h3>
      {form.rendimentos.map((r, i) => (
        <div key={i} className="linha">
          <input placeholder="Entidade empregadora" required value={r.entidadeEmpregadora}
                 onChange={e => setRend(i, 'entidadeEmpregadora', e.target.value)} />
          <input placeholder="Valor (R$)" type="number" min="0" step="0.01" required value={r.valor}
                 onChange={e => setRend(i, 'valor', e.target.value)} />
          <button type="button" onClick={() => setForm({ ...form, rendimentos: form.rendimentos.filter((_, j) => j !== i) })}>Remover</button>
        </div>
      ))}
      {form.rendimentos.length < MAX_RENDIMENTOS &&
        <button type="button" onClick={() => setForm({ ...form, rendimentos: [...form.rendimentos, { entidadeEmpregadora: '', valor: '' }] })}>
          + Adicionar rendimento</button>}
      {erro && <p className="erro">{erro}</p>}
      <button type="submit">{cliente ? 'Salvar alterações' : 'Cadastrar'}</button>
    </form>
  )
}

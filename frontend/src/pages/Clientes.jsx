import { useEffect, useState } from 'react'
import { api } from '../services/api'
import ClienteForm from './ClienteForm'

/** CRUD de clientes, visão do agente. */
export default function Clientes() {
  const [clientes, setClientes] = useState([])
  const [editando, setEditando] = useState(null)
  const [novo, setNovo] = useState(false)
  const [erro, setErro] = useState('')

  const carregar = () => api('/api/clientes').then(setClientes).catch(e => setErro(e.message))
  useEffect(() => { carregar() }, [])

  const excluir = async c => {
    if (!confirm(`Excluir ${c.nome} e todos os seus pedidos?`)) return
    try { await api(`/api/clientes/${c.id}`, { method: 'DELETE' }); carregar() } catch (e) { setErro(e.message) }
  }
  const salvo = () => { setEditando(null); setNovo(false); carregar() }

  return (
    <>
      <h1>Clientes</h1>
      {erro && <p className="erro">{erro}</p>}
      {novo || editando
        ? <><ClienteForm key={editando?.id ?? 'novo'} cliente={editando} onSalvo={salvo} />
            <button type="button" onClick={() => { setEditando(null); setNovo(false) }}>Fechar</button></>
        : <button onClick={() => setNovo(true)}>+ Novo cliente</button>}
      <div className="tabela"><table>
        <thead><tr><th>Nome</th><th>CPF</th><th>RG</th><th>Profissão</th><th>Rendimentos</th><th>Ações</th></tr></thead>
        <tbody>
          {clientes.map(c => (
            <tr key={c.id}>
              <td>{c.nome}</td><td>{c.cpf}</td><td>{c.rg}</td><td>{c.profissao}</td>
              <td>{c.rendimentos.map(r => `${r.entidadeEmpregadora}: R$ ${r.valor}`).join(' · ')}</td>
              <td className="acoes"><button onClick={() => setEditando(c)}>Editar</button>
                <button className="perigo" onClick={() => excluir(c)}>Excluir</button></td>
            </tr>
          ))}
        </tbody>
      </table></div>
    </>
  )
}

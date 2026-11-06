import { useEffect, useState } from 'react'
import { api } from '../services/api'

const VAZIO = { automovelId: '', dataInicio: '', dataFim: '' }
const carro = a => `${a.marca} ${a.modelo} ${a.ano} - ${a.placa}`

export default function Pedidos({ usuario }) {
  const agente = usuario.perfil === 'AGENTE'
  const banco = usuario.tipoAgente === 'BANCO'
  const [pedidos, setPedidos] = useState([])
  const [automoveis, setAutomoveis] = useState([])
  const [form, setForm] = useState(VAZIO)
  const [editando, setEditando] = useState(null)
  const [acao, setAcao] = useState(null) // { tipo: 'avaliar' | 'contrato', pedido }
  const [erro, setErro] = useState('')

  const carregar = () => api('/api/pedidos').then(setPedidos).catch(e => setErro(e.message))
  useEffect(() => { carregar(); api('/api/automoveis').then(setAutomoveis) }, [])

  const executar = async fn => {
    setErro('')
    try { await fn(); await carregar(); return true } catch (e) { setErro(e.message); return false }
  }

  const salvar = e => {
    e.preventDefault()
    executar(() => api(editando ? `/api/pedidos/${editando}` : '/api/pedidos',
      { method: editando ? 'PUT' : 'POST', body: form }))
      .then(ok => ok && (setForm(VAZIO), setEditando(null)))
  }

  const editar = p => {
    setEditando(p.id)
    setForm({ automovelId: p.automovel.id, dataInicio: p.dataInicio, dataFim: p.dataFim })
  }

  const podeEditar = p => p.status === 'PENDENTE' || (agente && p.status === 'APROVADO')

  return (
    <>
      <h1>{agente ? 'Pedidos de aluguel' : 'Meus pedidos'}</h1>
      {(!agente || editando) &&
        <form onSubmit={salvar} className="card">
          <h3>{editando ? `Modificar pedido #${editando}` : 'Novo pedido de aluguel'}</h3>
          <div className="linha">
            <select required value={form.automovelId} onChange={e => setForm({ ...form, automovelId: e.target.value })}>
              <option value="">Selecione o automóvel</option>
              {automoveis.map(a => <option key={a.id} value={a.id}>{carro(a)}</option>)}
            </select>
            <label>Início<input type="date" required value={form.dataInicio} onChange={e => setForm({ ...form, dataInicio: e.target.value })} /></label>
            <label>Fim<input type="date" required value={form.dataFim} onChange={e => setForm({ ...form, dataFim: e.target.value })} /></label>
            <button type="submit">{editando ? 'Salvar' : 'Fazer pedido'}</button>
            {editando && <button type="button" onClick={() => { setEditando(null); setForm(VAZIO) }}>Cancelar edição</button>}
          </div>
        </form>}

      {erro && <p className="erro">{erro}</p>}

      {acao?.tipo === 'avaliar' && <Avaliacao pedido={acao.pedido} onFechar={() => setAcao(null)}
        onEnviar={body => executar(() => api(`/api/pedidos/${acao.pedido.id}/avaliacao`, { method: 'POST', body })).then(ok => ok && setAcao(null))} />}
      {acao?.tipo === 'contrato' && <ContratoForm pedido={acao.pedido} banco={banco} onFechar={() => setAcao(null)}
        onEnviar={body => executar(() => api(`/api/pedidos/${acao.pedido.id}/contrato`, { method: 'POST', body })).then(ok => ok && setAcao(null))} />}

      <table>
        <thead><tr><th>#</th>{agente && <th>Cliente</th>}<th>Automóvel</th><th>Período</th><th>Status</th><th>Parecer / Contrato</th><th>Ações</th></tr></thead>
        <tbody>
          {pedidos.length === 0 && <tr><td colSpan="7">Nenhum pedido.</td></tr>}
          {pedidos.map(p => (
            <tr key={p.id}>
              <td>{p.id}</td>
              {agente && <td>{p.clienteNome}</td>}
              <td>{carro(p.automovel)}</td>
              <td>{p.dataInicio} a {p.dataFim}</td>
              <td><span className={`status ${p.status}`}>{p.status}</span></td>
              <td>
                {p.parecer && <div>{p.agenteAvaliador}: {p.parecer}</div>}
                {p.contrato && <div>Propriedade: {p.contrato.tipoPropriedade}
                  {p.contrato.banco && <> · Crédito {p.contrato.banco}: R$ {p.contrato.valorCredito} em {p.contrato.numeroParcelas}x ({p.contrato.taxaJuros}% a.m.)</>}</div>}
              </td>
              <td className="acoes">
                {podeEditar(p) && <button onClick={() => editar(p)}>Modificar</button>}
                {!agente && ['PENDENTE', 'APROVADO'].includes(p.status) &&
                  <button className="perigo" onClick={() => confirm('Cancelar pedido?') && executar(() => api(`/api/pedidos/${p.id}/cancelar`, { method: 'POST' }))}>Cancelar</button>}
                {agente && p.status === 'PENDENTE' && <button onClick={() => setAcao({ tipo: 'avaliar', pedido: p })}>Avaliar</button>}
                {agente && p.status === 'APROVADO' && <button onClick={() => setAcao({ tipo: 'contrato', pedido: p })}>Executar contrato</button>}
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </>
  )
}

function Avaliacao({ pedido, onEnviar, onFechar }) {
  const [parecer, setParecer] = useState('')
  return (
    <div className="card">
      <h3>Avaliação financeira do pedido #{pedido.id} ({pedido.clienteNome})</h3>
      <label>Parecer<textarea value={parecer} onChange={e => setParecer(e.target.value)} /></label>
      <div className="linha">
        <button onClick={() => onEnviar({ aprovado: true, parecer })}>Parecer positivo</button>
        <button className="perigo" onClick={() => onEnviar({ aprovado: false, parecer })}>Reprovar</button>
        <button onClick={onFechar}>Fechar</button>
      </div>
    </div>
  )
}

function ContratoForm({ pedido, banco, onEnviar, onFechar }) {
  const [tipoPropriedade, setTipo] = useState('CLIENTE')
  const [comCredito, setComCredito] = useState(false)
  const [credito, setCredito] = useState({ valor: '', taxaJuros: '', numeroParcelas: '' })
  const enviar = e => {
    e.preventDefault()
    onEnviar({ tipoPropriedade, credito: comCredito ? credito : null })
  }
  return (
    <form className="card" onSubmit={enviar}>
      <h3>Executar contrato do pedido #{pedido.id}</h3>
      <label>Automóvel registrado como propriedade de
        <select value={tipoPropriedade} onChange={e => setTipo(e.target.value)}>
          <option value="CLIENTE">Cliente</option><option value="EMPRESA">Empresa</option><option value="BANCO">Banco</option>
        </select>
      </label>
      {banco && <label className="check"><input type="checkbox" checked={comCredito} onChange={e => setComCredito(e.target.checked)} /> Associar contrato de crédito</label>}
      {comCredito &&
        <div className="linha">
          <input placeholder="Valor (R$)" type="number" min="0.01" step="0.01" required value={credito.valor} onChange={e => setCredito({ ...credito, valor: e.target.value })} />
          <input placeholder="Juros (% a.m.)" type="number" min="0" step="0.01" required value={credito.taxaJuros} onChange={e => setCredito({ ...credito, taxaJuros: e.target.value })} />
          <input placeholder="Parcelas" type="number" min="1" required value={credito.numeroParcelas} onChange={e => setCredito({ ...credito, numeroParcelas: e.target.value })} />
        </div>}
      <div className="linha"><button type="submit">Confirmar contrato</button><button type="button" onClick={onFechar}>Fechar</button></div>
    </form>
  )
}

# Histórias de Usuário (v1)

| ID | Caso de uso | História | Critérios de aceitação |
|----|-------------|----------|------------------------|
| HU01 | UC01 | **Como** usuário (cliente ou agente), **quero** fazer login com usuário e senha, **para** acessar o sistema com segurança. | Sem login nenhuma funcionalidade é acessível (exceto o cadastro); credenciais inválidas exibem erro; senhas são armazenadas com hash (BCrypt). |
| HU02 | UC02 | **Como** cliente, **quero** me cadastrar informando RG, CPF, nome, endereço, profissão e meus rendimentos, **para** poder solicitar aluguéis. | Login e CPF são únicos; no máximo 3 rendimentos (entidade empregadora + valor). |
| HU03 | UC03 | **Como** cliente, **quero** consultar, alterar e excluir meu cadastro, **para** manter meus dados atualizados. | Cliente só acessa o próprio cadastro; senha em branco na edição mantém a atual; excluir remove os pedidos do cliente. |
| HU04 | UC03 | **Como** agente, **quero** consultar e gerenciar os clientes, **para** apoiar a análise dos pedidos. | Agente lista, cria, edita e exclui clientes. |
| HU05 | UC04 | **Como** cliente, **quero** introduzir um pedido de aluguel escolhendo o automóvel e o período, **para** alugar um carro. | Pedido nasce com status PENDENTE; data final não pode ser anterior à inicial. |
| HU06 | UC06 | **Como** cliente, **quero** consultar meus pedidos e o status de cada um, **para** acompanhar a solicitação. | Cliente vê apenas os próprios pedidos, com status, parecer e dados do contrato. |
| HU07 | UC05 | **Como** cliente, **quero** modificar um pedido ainda pendente, **para** corrigir automóvel ou datas. | Só é possível modificar pedidos PENDENTES. |
| HU08 | UC07 | **Como** cliente, **quero** cancelar um pedido, **para** desistir do aluguel. | Só é possível cancelar pedidos PENDENTES ou APROVADOS; status passa a CANCELADO. |
| HU09 | UC05 | **Como** agente, **quero** modificar pedidos, **para** ajustar condições antes do contrato. | Agente modifica pedidos PENDENTES ou APROVADOS. |
| HU10 | UC08 | **Como** agente (empresa ou banco), **quero** avaliar financeiramente um pedido e emitir um parecer, **para** decidir se ele segue para contrato. | Parecer positivo → APROVADO; negativo → REPROVADO; registra o agente avaliador. |
| HU11 | UC09 | **Como** agente, **quero** executar o contrato de um pedido aprovado, definindo a quem o automóvel fica registrado (cliente, empresa ou banco), **para** formalizar o aluguel. | Só pedidos APROVADOS; status passa a CONTRATADO. |
| HU12 | UC10 | **Como** banco, **quero** associar um contrato de crédito ao aluguel, **para** financiar o cliente. | Apenas agentes do tipo BANCO concedem crédito (valor, juros, parcelas). |
| HU13 | UC11 | **Como** agente, **quero** cadastrar automóveis (matrícula, ano, marca, modelo, placa), **para** que possam ser alugados. | Matrícula e placa únicas. |

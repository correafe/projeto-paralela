import streamlit as st
import requests
import pandas as pd

# Configuração da Página
st.set_page_config(page_title="Gestão de Veículos", layout="wide", page_icon="🚗")
st.title("🚗 Sistema de Gestão de Veículos")

# URL do seu API Gateway
BASE_URL = "http://localhost:8082/veiculos"

# Menu Lateral
st.sidebar.header("Navegação")
menu = st.sidebar.radio(
    "Escolha uma opção:",
    ["Listar Todos", "Buscar por Placa", "Novo Veículo", "Editar Veículo", "Remover Veículo"]
)

# --- FUNÇÃO: LISTAR TODOS ---
if menu == "Listar Todos":
    st.header("📋 Lista de Veículos")
    
    # Botão para forçar atualização
    if st.button("🔄 Atualizar Lista"):
        st.rerun()

    try:
        response = requests.get(BASE_URL)
        if response.status_code == 200:
            dados = response.json()
            if dados:
                df = pd.DataFrame(dados)
                # Reordenar colunas para ficar mais bonito
                colunas_ordem = ["placa", "marca", "modelo", "ano", "cor", "quilometragem", "valor"]
                # Garante que só exibe colunas que existem no JSON
                cols_to_show = [c for c in colunas_ordem if c in df.columns]
                st.dataframe(df[cols_to_show], use_container_width=True)
            else:
                st.info("Nenhum veículo cadastrado no banco.")
        else:
            st.error(f"Erro ao conectar com o servidor. Status: {response.status_code}")
    except Exception as e:
        st.error(f"O API Gateway (Porta 8082) parece estar desligado. \n\nErro técnico: {e}")

# --- FUNÇÃO: BUSCAR ---
elif menu == "Buscar por Placa":
    st.header("🔍 Buscar Veículo")
    placa_busca = st.text_input("Digite a Placa para buscar:")
    
    if st.button("Pesquisar"):
        if placa_busca:
            try:
                response = requests.get(f"{BASE_URL}/{placa_busca}")
                if response.status_code == 200 and response.text:
                    veiculo = response.json()
                    st.success("✅ Veículo Encontrado!")
                    
                    # Mostra os dados bonitinhos em cartões
                    c1, c2, c3 = st.columns(3)
                    c1.metric("Modelo", veiculo.get("modelo", "-"))
                    c2.metric("Marca", veiculo.get("marca", "-"))
                    c3.metric("Ano", veiculo.get("ano", "-"))
                    
                    st.json(veiculo) # Mostra o JSON cru também
                else:
                    st.warning("❌ Veículo não encontrado.")
            except:
                st.error("Erro de conexão com o servidor.")

# --- FUNÇÃO: NOVO VEÍCULO ---
elif menu == "Novo Veículo":
    st.header("➕ Cadastrar Novo Carro")
    
    with st.form("form_cadastro"):
        col1, col2 = st.columns(2)
        with col1:
            placa = st.text_input("Placa (Ex: ABC-1234)")
            marca = st.text_input("Marca")
            modelo = st.text_input("Modelo")
            ano = st.number_input("Ano", min_value=1900, max_value=2025, step=1)
        
        with col2:
            cor = st.text_input("Cor")
            km = st.number_input("Quilometragem (KM)", min_value=0.0)
            valor = st.number_input("Valor (R$)", min_value=0.0)
            
        submit = st.form_submit_button("💾 Salvar Veículo")

        if submit:
            payload = {
                "placa": placa, "marca": marca, "modelo": modelo,
                "ano": ano, "cor": cor, "quilometragem": km, "valor": valor
            }
            try:
                response = requests.post(BASE_URL, json=payload)
                if response.status_code == 200:
                    st.success(f"Veículo {modelo} inserido com sucesso!")
                else:
                    st.error(f"Erro ao inserir: {response.status_code}")
            except Exception as e:
                st.error(f"Erro de conexão: {e}")

# --- FUNÇÃO: EDITAR (ALTERAR) ---
elif menu == "Editar Veículo":
    st.header("✏️ Editar Veículo Existente")
    
    placa_edit = st.text_input("Digite a Placa do veículo que deseja alterar:")
    
    # Variável de estado para controlar se achou o carro
    if 'dados_edit' not in st.session_state:
        st.session_state['dados_edit'] = None

    if st.button("Buscar para Editar"):
        try:
            res = requests.get(f"{BASE_URL}/{placa_edit}")
            if res.status_code == 200:
                st.session_state['dados_edit'] = res.json()
                st.success("Veículo encontrado! Edite os dados abaixo:")
            else:
                st.warning("Veículo não encontrado para edição.")
                st.session_state['dados_edit'] = None
        except:
            st.error("Erro de conexão.")

    # Se tiver dados carregados, mostra o formulário preenchido
    if st.session_state['dados_edit']:
        v = st.session_state['dados_edit']
        
        with st.form("form_editar"):
            c1, c2 = st.columns(2)
            with c1:
                # O value=... preenche o campo com o que veio do banco
                n_marca = st.text_input("Marca", value=v['marca'])
                n_modelo = st.text_input("Modelo", value=v['modelo'])
                n_ano = st.number_input("Ano", value=v['ano'])
            with c2:
                n_cor = st.text_input("Cor", value=v['cor'])
                n_km = st.number_input("KM", value=v['quilometragem'])
                n_valor = st.number_input("Valor", value=v['valor'])
            
            salvar_edit = st.form_submit_button("💾 Confirmar Alteração")
            
            if salvar_edit:
                novo_payload = {
                    "placa": placa_edit, # A placa não muda
                    "marca": n_marca, "modelo": n_modelo, "ano": n_ano,
                    "cor": n_cor, "quilometragem": n_km, "valor": n_valor
                }
                try:
                    # PUT é o verbo HTTP para alterar
                    resp = requests.put(f"{BASE_URL}/{placa_edit}", json=novo_payload)
                    if resp.status_code == 200:
                        st.success("Dados atualizados com sucesso!")
                        st.session_state['dados_edit'] = None # Limpa o form
                    else:
                        st.error("Erro ao atualizar.")
                except Exception as e:
                    st.error(f"Erro: {e}")

# --- FUNÇÃO: REMOVER ---
elif menu == "Remover Veículo":
    st.header("🗑️ Remover Veículo")
    placa_remove = st.text_input("Digite a Placa para remover:")
    
    if st.button("🚨 Remover Definitivamente"):
        if placa_remove:
            try:
                response = requests.delete(f"{BASE_URL}/{placa_remove}")
                if response.status_code == 200:
                    st.success("Veículo removido com sucesso!")
                else:
                    st.error("Erro ao remover (talvez placa não exista).")
            except:
                st.error("Erro de conexão.")
#!/bin/bash

echo "==========================================="
echo "   INICIANDO FRONTEND (STREAMLIT)          "
echo "==========================================="

# 1. Verifica se tem Python instalado
if ! command -v python3 &> /dev/null
then
    echo "ERRO: Python3 não encontrado. Por favor, instale o Python."
    exit 1
fi

# 2. Cria o ambiente virtual se não existir
if [ ! -d "venv" ]; then
    echo "Criando ambiente virtual isolado (venv)..."
    python3 -m venv venv
fi

# 3. Ativa o ambiente
echo "Ativando ambiente..."
source venv/bin/activate

# 4. Instala as dependências silenciosamente
echo "Instalando dependências (pode levar alguns segundos)..."
pip install -r requirements.txt > /dev/null 2>&1

# 5. Roda o aplicativo
echo "Abrindo o sistema..."
streamlit run app.py
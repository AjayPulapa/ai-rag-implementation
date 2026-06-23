# Azure RAG FastAPI Application - Docker Setup Guide

## Prerequisites

- Docker Desktop
- Azure OpenAI Resource
- Azure AI Search Service

Verify Docker:

```bash
docker --version
```

---

## Project Structure

```text
azure-rag-app/
│
├── app.py
├── Dockerfile
├── requirements.txt
├── .env
│
└── templates/
    └── index.html
```

---

## requirements.txt

```txt
fastapi
uvicorn[standard]
openai
python-dotenv
jinja2
python-multipart
requests
```

---

## .env

```properties
AZURE_OAI_ENDPOINT=https://your-openai-resource.openai.azure.com
AZURE_OAI_KEY=xxxxxxxxxxxxxxxxxxxxxxxx
AZURE_OAI_DEPLOYMENT=gpt-4o

AZURE_SEARCH_ENDPOINT=https://your-search-service.search.windows.net
AZURE_SEARCH_KEY=xxxxxxxxxxxxxxxxxxxxxxxx
AZURE_SEARCH_INDEX=knowledge-index
```

---

## Dockerfile

```dockerfile
FROM python:3.11-slim

WORKDIR /app

COPY requirements.txt .

RUN pip install --no-cache-dir -r requirements.txt

COPY . .

EXPOSE 8000

CMD ["uvicorn","app:app","--host","0.0.0.0","--port","8000"]
```

---

## Build Docker Image

```bash
cd azure-rag-app
docker build -t azure-rag-ui .
```

---

## Run Container

### Linux/macOS

```bash
docker run -d -p 8000:8000 --env-file .env --name rag-container azure-rag-ui
```

### Windows CMD

```cmd
docker run -d ^
-p 8000:8000 ^
--env-file .env ^
--name rag-container ^
azure-rag-ui
```

### PowerShell

```powershell
docker run -d `
-p 8000:8000 `
--env-file .env `
--name rag-container `
azure-rag-ui
```

---

## Verify

```bash
docker ps
docker logs -f rag-container
```

Open:

http://localhost:8000

---

## Stop / Start

```bash
docker stop rag-container
docker start rag-container
```

---

## Rebuild After Changes

```bash
docker stop rag-container
docker rm rag-container

docker build -t azure-rag-ui .

docker run -d -p 8000:8000 --env-file .env --name rag-container azure-rag-ui
```

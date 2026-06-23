import os
from dotenv import load_dotenv

from fastapi import FastAPI, Request, Form
from fastapi.responses import HTMLResponse
from fastapi.templating import Jinja2Templates

from openai import AzureOpenAI

load_dotenv()

app = FastAPI(title="Azure RAG Chatbot")

templates = Jinja2Templates(directory="templates")

client = AzureOpenAI(
    azure_endpoint=os.getenv("AZURE_OAI_ENDPOINT"),
    api_key=os.getenv("AZURE_OAI_KEY"),
    api_version="2024-12-01-preview"
)


@app.get("/", response_class=HTMLResponse)
async def home(request: Request):

    return templates.TemplateResponse(
        request=request,
        name="index.html",
        context={
            "question": "",
            "answer": ""
        }
    )


@app.post("/ask", response_class=HTMLResponse)
async def ask(
        request: Request,
        question: str = Form(...)
):

    answer = ""

    try:

        response = client.chat.completions.create(
            model=os.getenv("AZURE_OAI_DEPLOYMENT"),
            messages=[
                {
                    "role": "system",
                    "content": "You are a helpful assistant."
                },
                {
                    "role": "user",
                    "content": question
                }
            ],
            max_tokens=1000,
            temperature=0.3,
            extra_body={
                "data_sources": [
                    {
                        "type": "azure_search",
                        "parameters": {
                            "endpoint": os.getenv("AZURE_SEARCH_ENDPOINT"),
                            "index_name": os.getenv("AZURE_SEARCH_INDEX"),
                            "authentication": {
                                "type": "api_key",
                                "key": os.getenv("AZURE_SEARCH_KEY")
                            }
                        }
                    }
                ]
            }
        )

        answer = response.choices[0].message.content

    except Exception as ex:
        answer = f"Error: {str(ex)}"

    return templates.TemplateResponse(
        request=request,
        name="index.html",
        context={
            "question": question,
            "answer": answer
        }
    )


@app.get("/health")
async def health():
    return {
        "status": "UP"
    }


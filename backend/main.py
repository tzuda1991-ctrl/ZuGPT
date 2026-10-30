import os
import requests
from fastapi import FastAPI, HTTPException
from pydantic import BaseModel

app = FastAPI(title="ZuGPT AI Backend")

API_KEY = os.getenv("GEMINI_API_KEY")
MODEL = "gemini-3.8-flash"

class ChatRequest(BaseModel):
    message: str

@app.get("/")
def home():
    return {"app": "ZuGPT", "status": "online"}

@app.post("/chat")
def chat(data: ChatRequest):
    if not API_KEY:
        raise HTTPException(
            status_code=500,
            detail="Server API key is not configured"
        )

    message = data.message.strip()
    if not message:
        raise HTTPException(
            status_code=400,
            detail="Message cannot be empty"
        )

    url = (
        "https://generativelanguage.googleapis.com/"
        f"v1beta/models/{MODEL}:generateContent"
    )

    try:
        response = requests.post(
            url,
            headers={
                "Content-Type": "application/json",
                "x-goog-api-key": API_KEY
            },
            json={
                "contents": [{
                    "parts": [{"text": message}]
                }]
            },
            timeout=60
        )
        response.raise_for_status()
        result = response.json()

        answer = result["candidates"][0]["content"]["parts"][0]["text"]
        return {"reply": answer}

    except requests.RequestException:
        raise HTTPException(
            status_code=502,
            detail="AI provider request failed"
        )
    except (KeyError, IndexError, TypeError):
        raise HTTPException(
            status_code=502,
            detail="AI provider returned no usable answer"
        )

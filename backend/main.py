import os
import logging
import requests
from fastapi import FastAPI, HTTPException
from pydantic import BaseModel

logging.basicConfig(level=logging.INFO)
logger = logging.getLogger("zugpt")

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
            detail="GEMINI_API_KEY is not configured on the server"
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

        if not response.ok:
            logger.error(
                "Gemini API failed: HTTP %s; response: %s",
                response.status_code,
                response.text[:1000]
            )
            raise HTTPException(
                status_code=502,
                detail=f"Gemini API returned HTTP {response.status_code}. Check Render logs."
            )

        result = response.json()
        answer = result["candidates"][0]["content"]["parts"][0]["text"]

        return {"reply": answer}

    except HTTPException:
        raise

    except requests.RequestException as exc:
        logger.error("Gemini connection failed: %s", type(exc).__name__)
        raise HTTPException(
            status_code=502,
            detail="Could not connect to Gemini API. Check Render logs."
        )

    except (KeyError, IndexError, TypeError, ValueError):
        logger.exception("Gemini returned an unusable response")
        raise HTTPException(
            status_code=502,
            detail="Gemini returned no usable answer. Check Render logs."
        )

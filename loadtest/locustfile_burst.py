import itertools
import os
import random

import gevent
from gevent.pool import Pool
import requests
from locust import HttpUser, constant, events, task
from locust.exception import StopUser

TARGET = os.getenv("BURST_TARGET", "login")
TOTAL = int(os.getenv("BURST_REQUESTS", "100"))
RUN_ID = os.getenv("RUN_ID", str(random.randint(1000, 9999)))
PREP_CONCURRENCY = int(os.getenv("PREP_CONCURRENCY", "20"))
PASSWORD = "LoadTest@123"

MENU_ITEM_IDS = [
    "913ea87e-d553-408e-b031-478d1cf8f949",
    "57a8881d-6ff4-4535-9e96-169faea48a75",
    "0e7c6c63-fd1c-4e67-af5a-582fd329ac31",
    "9afaccfd-fea2-42f1-99a9-1ec687a741a3",
]

_ids = itertools.count()
_done = 0
_tokens = [None] * TOTAL


def _email(i):
    return f"burst_{TARGET}_{RUN_ID}_{i}@example.com"


@events.test_start.add_listener
def prepare(environment, **_kwargs):
    if TARGET not in ("login", "order"):
        return
    host = environment.host
    print(f"Preparing {TOTAL} accounts for target={TARGET} ...")

    def prep_one(i):
        r = requests.post(f"{host}/api/auth/register", json={
            "email": _email(i), "password": PASSWORD,
            "confirm_password": PASSWORD, "full_name": "Burst User",
        }, timeout=60)
        if r.status_code not in (201, 409):
            raise RuntimeError(f"register #{i} failed: {r.status_code} {r.text[:200]}")
        if TARGET == "order":
            r = requests.post(f"{host}/api/auth/login",
                              json={"email": _email(i), "password": PASSWORD}, timeout=60)
            if r.status_code != 200:
                raise RuntimeError(f"login #{i} failed: {r.status_code} {r.text[:200]}")
            _tokens[i] = r.json()["data"]["access_token"]

    Pool(PREP_CONCURRENCY).map(prep_one, range(TOTAL))
    print("Preparation done, starting burst.")

class BurstUser(HttpUser):
    wait_time = constant(0)

    @task
    def one_request(self):
        global _done
        i = next(_ids)
        if i >= TOTAL:
            raise StopUser()

        if TARGET == "menu":
            self.client.get("/api/menu-items", name="GET /api/menu-items")

        elif TARGET == "register":
            self.client.post("/api/auth/register", json={
                "email": _email(i), "password": PASSWORD,
                "confirm_password": PASSWORD, "full_name": "Burst User",
            }, name="POST /api/auth/register")

        elif TARGET == "login":
            self.client.post("/api/auth/login",
                             json={"email": _email(i), "password": PASSWORD},
                             name="POST /api/auth/login")

        elif TARGET == "order":
            self.client.post("/api/orders", json={
                "items": [{"menu_item_id": random.choice(MENU_ITEM_IDS), "quantity": 1}],
                "note": "burst test",
            }, headers={"Authorization": f"Bearer {_tokens[i]}"}, name="POST /api/orders")

        _done += 1
        if _done >= TOTAL:
            gevent.spawn_later(1, self.environment.runner.quit)
        raise StopUser()

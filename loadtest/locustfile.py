import itertools
import os
import random

from locust import HttpUser, between, task

PASSWORD = "LoadTest@123"
RUN_ID = os.getenv("RUN_ID", str(random.randint(1000, 9999)))
ENABLE_ORDER_TASKS = os.getenv("ENABLE_ORDER_TASKS", "1") == "1"

MENU_ITEM_IDS = [
    "913ea87e-d553-408e-b031-478d1cf8f949",  # Espresso
    "57a8881d-6ff4-4535-9e96-169faea48a75",  # Bac xiu
    "0e7c6c63-fd1c-4e67-af5a-582fd329ac31",  # Peach Tea
    "9afaccfd-fea2-42f1-99a9-1ec687a741a3",  # Croissant
]

_counter = itertools.count()


class GuestUser(HttpUser):


    weight = 7
    wait_time = between(1, 3)

    @task
    def browse_menu(self):
        self.client.get("/api/menu-items", name="GET /api/menu-items")


class CustomerUser(HttpUser):


    weight = 3
    wait_time = between(1, 3)

    def on_start(self):
        self.email = f"lt_{RUN_ID}_{next(_counter)}@example.com"
        self.headers = {}

        with self.client.post(
            "/api/auth/register",
            json={
                "email": self.email,
                "password": PASSWORD,
                "confirm_password": PASSWORD,
                "full_name": "Load Test User",
            },
            name="POST /api/auth/register",
            catch_response=True,
        ) as r:
            if r.status_code in (201, 409):
                r.success()
            else:
                r.failure(f"register {r.status_code}")

        self._login()

    def _login(self):
        with self.client.post(
            "/api/auth/login",
            json={"email": self.email, "password": PASSWORD},
            name="POST /api/auth/login",
            catch_response=True,
        ) as r:
            if r.status_code == 200:
                token = r.json()["data"]["access_token"]
                self.headers = {"Authorization": f"Bearer {token}"}
                r.success()
            else:
                r.failure(f"login {r.status_code}")

    @task(5)
    def browse_menu(self):
        self.client.get("/api/menu-items", headers=self.headers, name="GET /api/menu-items")

    @task(1)
    def login_again(self):
        self._login()


    @task(2)
    def view_profile(self):
        if ENABLE_ORDER_TASKS:
            self.client.get("/api/users/me", headers=self.headers, name="GET /api/users/me")

    @task(2)
    def create_and_view_order(self):
        if not ENABLE_ORDER_TASKS:
            return
        body = {
            "items": [{"menu_item_id": random.choice(MENU_ITEM_IDS), "quantity": random.randint(1, 3)}],
            "voucher_code": "WELCOME10" if random.random() < 0.3 else None,
            "note": "load test",
        }
        r = self.client.post("/api/orders", json=body, headers=self.headers, name="POST /api/orders")
        if r.status_code in (200, 201):
            order_id = r.json()["data"]["id"]
            self.client.get(f"/api/orders/{order_id}/bill", headers=self.headers,
                            name="GET /api/orders/{id}/bill")

    @task(1)
    def list_orders(self):
        if ENABLE_ORDER_TASKS:
            self.client.get("/api/orders", headers=self.headers, name="GET /api/orders")

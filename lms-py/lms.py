import random
import csv
import pandas as pd
import requests

from datetime import *
from faker import Faker

fake = Faker()
random.seed(42)

NUM_BOOKS = 100
NUM_USERS = 50
NUM_BORROWING_HISTORY = 200
BASE_URL = "http://localhost:8080"
BOOKs_ENDPOINT = "/books"
USERS_ENDPOINT = "/users/register"
BORROWING_ENDPOINT = "/borrow"
cnt = 0

headers = {
    "Authorization": "Bearer eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJMTVMiLCJzdWIiOiJUb2tlbiIsImVtYWlsIjoibWFyd2E0QGdtYWlsLmNvbSIsInVzZXJJZCI6MSwicm9sZSI6IkFETUlOIiwiaWF0IjoxNzMyMTE5MzQ4LCJleHAiOjE3MzQ3MTEzNDh9.Jyrb9JUFzntUFTb0mR9Y3DRj2CKxiF-2YSVWHbfxQwrtAXja6RgTRKdPzamKyVBxWDTkBR2g1SgRRBW6gF_7xA",
    "Content-Type": "application/json"
}


def post_data(url, data, header):
    try:
        if header is None:
            response = requests.post(url=BASE_URL + url, json=data)
        else:
            response = requests.post(url=BASE_URL + url, json=data, headers=headers)

        if response.status_code == 200 or response.status_code == 201:
            global cnt
            print("OK", cnt)
            cnt += 1
        else:
            print("Status Code:", response.status_code)
            print("Message: ", response.json()['message'])
        return response.json()
    except Exception as e:
        print("An error occurred:", e)


def generate_books():
    generated_books = []
    global cnt
    cnt = 0
    for i in range(NUM_BOOKS):
        book = {
            'title': fake.catch_phrase(),
            'author': fake.name(),
            'isbn': fake.isbn13(),
            'copiesAvailable': random.randint(1, 100)
        }
        response = post_data(url=BOOKs_ENDPOINT, data=book, header=headers)
        if response is not None:
            if response['status'] == 'success':
                book['id'] = response['body']['id']
                generated_books.append(book)
        else:
            i -= 1

    return generated_books


def generate_users():
    generated_users = []
    for i in range(NUM_USERS):
        user = {
            "name": fake.name(),
            "email": fake.email(),
            "password": fake.password(),
            "role": random.choice(['PATRON', 'ADMIN'])
        }
        response = post_data(url=USERS_ENDPOINT, data=user, header=None)
        if response is not None:
            if response['status'] == 'failure':
                i -= 1
            else:
                user['id'] = response['body']['id']
                generated_users.append(user)
        else:
            i -= 1
    return generated_users


def random_date_in_past(days_in_past):
    return fake.date_this_decade(before_today=True, after_today=False) - timedelta(days=random.randint(0, days_in_past))


def random_return_date(borrow_date):
    return borrow_date + timedelta(days=random.randint(1, 60))


def generate_borrowing_histories():
    histories = []
    for i in range(NUM_BORROWING_HISTORY):
        book = random.choice(books)
        borrow_date = random_date_in_past(365)
        return_date = random_return_date(borrow_date)
        borrowing_history = {
            'bookId': book['id'],
            'borrowDate': borrow_date.strftime("%Y-%m-%d"),
            'returnDate': return_date.strftime("%Y-%m-%d")
        }
        response = post_data(url=BORROWING_ENDPOINT, data=borrowing_history, header=headers)
        if response is not None:
            if response['status'] == 'failure':
                i -= 1
            else:
                histories.append(borrowing_history)
        else:
            i -= 1

    return histories


def get_books_not_borrowed_last_6_months():
    six_months_ago = datetime.now() - timedelta(days=180)
    six_months_ago_date = six_months_ago.date()

    borrowed_books = []
    for h in borrowing_histories:
        borrow_date = datetime.strptime(h['borrowDate'], "%Y-%m-%d")

        if borrow_date.date() > six_months_ago_date:
            borrowed_books.append(h['bookId'])
    not_borrowed_books = []
    for b in books:
        if b['id'] not in borrowed_books:
            not_borrowed_books.append(b)
    return not_borrowed_books


def export_to_csv(records, name):
    records_df = pd.DataFrame(records)
    records_df.to_csv(name, index=False)
    print(f"Exported {len(records)} record to {name} Successfully...")


if __name__ == '__main__':
    books = generate_books()
    users = generate_users()
    borrowing_histories = generate_borrowing_histories()
    not_borrowed_last_6_months = get_books_not_borrowed_last_6_months()
    export_to_csv(books, "books.csv")
    export_to_csv(users, "users.csv")
    export_to_csv(borrowing_histories, "borrowing_histories.csv")
    export_to_csv(not_borrowed_last_6_months, "not_borrowed_last_6_months.csv")

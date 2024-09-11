FROM python:3.10

ARG FILE=app.py

COPY ${FILE} app.py

CMD ["timeout" ,"10","python3", "app.py"]
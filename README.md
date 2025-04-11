## Описание
Приложение работает с кошельками.

Можно создать новый кошелек:

**POST api/v1/wallet/new?initialBalance=INIT_BALANCE**

Есть возможность получить баланс кошелька
**GET api/v1/wallets/{WALLET_UUID**

Выполняется логика по изменению счета в базе данных.

**POST api/v1/wallet

{

valletId: UUID,

operationType: DEPOSIT or WITHDRAW,

amount: 1000

}**

Миграции для базы данных с помощью liquibase.
Вся система поднимается с помощью docker-compose.

Пример запросов:

###
POST http://localhost:8080/api/v1/wallet/new?
initialBalance=4000

###
GET http://localhost:8080/api/v1/wallet/wallets/15071122-98d2-4f07-818d-b9449501832b


###
POST http://localhost:8080/api/v1/wallet
Content-Type: application/json

{
"walletId": "15071122-98d2-4f07-818d-b9449501832b",
"operationType": "DEPOSIT",
"amount": "5000"
}

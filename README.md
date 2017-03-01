# ananymous-02-2017

# Команда
* Перескоков Владислав
* Артюхов Владислав
* Набоков Денис
# API
### Sign up
**/api/signup**
POST
**Request**
{ "login":"...", "email":"...", "password":"..."}
**Response**
???
**Коды возврата**
* 200 - ОК
* 409 - пользователь уже зарегистрирован
* 403 - пользователь залогинен 

### Sign in
**/api/signin**
POST
**Request**
{ "login":"...", "email":"...", "password":"..."}
**Response**
???
**Коды возврата**
* 200 - ОК
* 403 - пользователь залогинен 
* 404 - пользователь не найден

### Get logged user
**/api/user**
GET
**Request**
???
**Response**
{ "login":"...", "email":"..."}
**Коды возврата**
* 200 - ОК
* 401 - пользователь не вошел 

### Change password
**/api/change-pass**
POST
**Request**
{ "oldPassword":"...", "newPassword":"..."}
**Response**
???
**Коды возврата**
* 200 - ОК
* 401 - пользователь не вошел 
* 403 - неверный логин и пароль

### Logout
**/api/logout**
POST
**Request**
???
**Response**
???
**Коды возврата**
* 200 - ОК
* 401 - пользователь не вошел

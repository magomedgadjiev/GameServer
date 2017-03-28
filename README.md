# dno-team-02-2017
## Игра MAGIKA
## Стратегия, в которой игроки будут бить магиями друг друга. 
## Команда:
  * **Кирил Солдатов**
  * **Алёхин Владислав**
  * **Щелавин Илья**
  * **Гаджиев Магомед**

## API
###  /auth
#### Авторизация пользователя.
>* /SignIn
>     *Request:{ "email": "stark@north.io", "password": "TheWall" }

>     *Response: {
                   "response": {
                     "key": 200,
                     "userProfile": {
                       "login": "JohnSnow",
                       "password": "TheWall",
                       "email": "stark@north.io",
                     },
                     "message": "Logged in succesfully"
                   }
                 }
                  200 - пользоваель создан
                  400 - не зарегестрирован
                  409 - введены неправильные данные
		  
#### Регистрация пользователя  
>* /signUp

>    * Request: { "email": "stark@north.io", "username": "JohnSnow", "password": "TheWall" }

>    * Responce:
             {
               "response": {
                 "key": 200,
                 "userProfile": {
                   "login": "JohnSnow",
                   "password": "TheWall",
                   "email": "stark@north.io",
                 },
                 "message": "User created successfully"
               }
             }    
         200 OK - удачная регистрация  
         400 Forbiden - уже зарегестрирован
	     409 Bad request - Введены неправильные данные
#### Получение пользователя текущей сессии  
>* /getInfoUser

>    * Request: 
            
>    * Responce:{
                  "response": {
                    "key": 200,
                    "userProfile": {
                      "login": "JohnSnow",
                      "password": "TheWall",
                      "email": "stark@north.io",
                      "empty": false
                    },
                    "message": "User created successfully"
                  }
                }
             200 OK - удачная операция  
             400 Bad Request - не авторизовался 

#### Обновление информации о пользователе  
>* /setInfoUser

>    * Request:{"login":"login1", "password":"prevpass","newpassword":"passnew"},
	    
>    * Responce:
                  {
                    "response": {
                      "key": 200,
                      "message": "User data succesfully updated"
                    }
                  }         
                      200 OK - удачная операция  
                      400 Bad Request - не авторизовался
#### Выход пользователя  
>* /signOut

>     * Request: 

>     * Responce:	{
                      "response": {
                        "key": 200,
                        "message": "succes"
                      }
                    }
		   	200 OK - удачная операция  


travis
[![Build Status](https://travis-ci.org/magomedgadjiev/dno-team-02-2017.svg?branch=master)](https://travis-ci.org/magomedgadjiev/dno-team-02-2017)
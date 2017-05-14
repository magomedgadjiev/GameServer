# dno-team-02-2017
## Игра MAGIKA
## Стратегия, в которой игроки будут бить магиями друг друга. 
## Команда:
  * **Кирил Солдатов**
  * **Алёхин Владислав**
  * **Щелавин Илья**
  * **Гаджиев Магомед**

## API
#### Авторизация пользователя.
#####POST
>* /api/DB/auth/username

>* Request:{ "email": "stark@north.io", "password": "TheWall" }

>     * 200 - запрос успешно выполнен

>      * Response: {
                    "key": 0,
                    "userProfiles": 
                    {
                        "username": "JohnSnow",
                        "password": "TheWall",
                        "email": "stark@north.io",
                    }   
                 }

>     * 400 - введены неправильные данные

>      * Responce:	
            {
               "key": 2,
               "message": "Not all required parameters provided"
            }
            
>     * 400 - не зарегестрирован

>      * Responce:	
            {
               "key": 1,
               "message": "You did't registration"
            }       
                 
>     * 500 - ошибка сервера

>      * Responce:	
            {
               "key": 4,
               "message": "Internal server error"
            }       
		  
#### Регистрация пользователя 
#####POST
>* /api/DB/auth/regirstration

>* Request: { "email": "stark@north.io", "username": "JohnSnow", "password": "TheWall" }

>     * 200 - запрос успешно выполнен

>      * Response: {
                    "key": 0,
                    "userProfiles": 
                    {
                        "username": "JohnSnow",
                        "password": "TheWall",
                        "email": "stark@north.io",
                    }   
                 } 
             
>     *400 - введены неправильные данные

>      * Responce:	
            {
               "key": 2,
               "message": "Not all required parameters provided"
            }
    
>     *409 - пользователь с такими данными уже существует

>      * Responce:	
            {
               "key": 3,
               "message": "This user already exist"
            }
                
>     *500 - ошибка сервера

>      * Responce:	
            {
               "key": 4,
               "message": "Internal server error"
            } 
            
#### Получение пользователя текущей сессии  
#####GET
>* /api/DB/getInfoUser

>* Request: 
            
>     * 200 - запрос успешно выполнен
            
>      * Response: {
                    "key": 0,
                    "userProfiles": 
                    {
                        "username": "JohnSnow",
                        "password": "TheWall",
                        "email": "stark@north.io"
                    }   
                 }

>     * 400 - не залогирован

>      * Responce:	
            {
               "key": 1,
               "message": "You did't username"
            }   
            
>     * 500 - ошибка сервер

>      * Responce:	
            {
               "key": 4,
               "message": "Internal server error"
            } 
            

#### Обновление информации о пользователе 
#####POST

>* /api/DB//user/setInfoUser

>* Request:{"username":"login1", "password":"prevpass","newpassword":"passnew"},
	   
>    * 200 - запрос успешно выполнен

>     * Responce:
            {
               "key": 0,
               "message": "User data succesfully updated"
            }     
               
>    * 400 - не залогирован

>     * Responce:	
            {
               "key": 1,
               "message": "You did't username"
            }   

>    * 400 - введены неправильные данные

>     * Responce:	
            {
               "key": 2,
               "message": "Not all required parameters provided"
            }

>    * 409 - пользователь с такими данными уже существует

>      * Responce:	
            {
               "key": 3,
               "message": "This user already exist"
            }
           
>    * 500 - ошибка сервера

>      * Responce:	
            {
               "key": 4,
               "message": "Internal server error"
            } 
            
                      
                      
#### Выход пользователя  
#####GET
>* /api/DB/auth/signOut

>* Request: 

>    * 200 - запрос успешно выполнен

>     * Responce:
            {
               "key": 0,
               "message": "success"
            }     
                     
#### Получение рейтинга
#####GET
>* /api/DB/stats/{count}

>* Request: 
            
>     * 200 - запрос успешно выполнен
            
>      * Response: {
                    "key": 0,
                    "userProfiles": 
                    [
                        "username": "JohnSnow",
                        "password": "TheWall",
                        "email": "stark@north.io"
                    ]  
                 }

>     * 400 - bad request 

>      * Responce:	
            {
               "key": 1,
               "message": "count > countUser"
            }   
            
>     * 500 - ошибка сервер

>      * Responce:	
            {
               "key": 4,
               "message": "Internal server error"
            } 
            



travis

[![Build Status](https://travis-ci.org/magomedgadjiev/dno-team-02-2017.svg?branch=master)](https://travis-ci.org/magomedgadjiev/dno-team-02-2017)
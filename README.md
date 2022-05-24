# 🖥 Admin-panel
## Тестовое задание
Нужно дописать приложение для администратора сетевой ролевой игры, где он сможет редактировать 
параметры персонажей (игроков), и раздавать баны. Должны быть реализованы следующие 
возможности:
1. получать список всех зарегистрированных игроков;
2. создавать нового игрока;
3. редактировать характеристики существующего игрока;
4. удалять игрока;
5. получать игрока по id;
6. получать отфильтрованный список игроков в соответствии с переданными фильтрами;
7. получать количество игроков, которые соответствуют фильтрам.
Для этого необходимо реализовать **REST API** в соответствии с документацией. 

Стек
------------------------------
* Spring Data JPA
* MySQL (база данных)
* Maven (для сборки проекта)
* Tomcat 9 (для запуска своего приложения)


<h2 align="left">Сортировка и добавление новых игроков</h2> 

<p align="center" target="blank">
  <img width="500" alt="image" src="https://user-images.githubusercontent.com/86875207/170055930-692870de-aa08-48f4-8890-c346da816f2c.png">
  <img width="500" alt="image" src="https://user-images.githubusercontent.com/86875207/170056632-bc6b4c7d-953a-47c9-b89d-290a48d0e049.png">
</p>

Основные моменты
--------------


В проекте должна использоваться сущность Player, которая имеет поля:
| Поля                      | Описание                                                         |
| --------------------------| -----------------------------------------------------------------|
| Long id                   | ID игрока                                                        |
| String name               | Имя персонажа (до 12 знаков включительно)                        |
| String title              | Титул персонажа (до 30 знаков включительно)                      |
| Race race                 | Расса персонажа                                                  | 
| Profession profession     | Профессия персонажа                                              |
| Integer experience        | Опыт персонажа. Диапазон значений 0..10,000,000                  | 
| Integer level             | Уровень персонажа                                                |
| Integer untilNextLevel    | Остаток опыта до следующего уровня                               |
| Date birthday             | Дата регистрации (диапазон значений года 2000..3000 включительно)|
| Boolean banned            | Забанен / не забанен                                             |


Также должна присутствовать бизнес-логика:
Перед сохранением персонажа в базу данных (при добавлении нового или при апдейте характеристик 
существующего), должны высчитываться:
- текущий уровень персонажа
- опыт необходимый для достижения следующего уровня
и сохраняться в БД. 

Текущий уровень персонажа рассчитывается по формуле: 𝐿 = _(sqrt(2500 + 200·exp) − 50)/100_, где:
_exp_ — опыт персонажа.

Опыт до следующего уровня рассчитывается по формуле:
_𝑁 = 50 ∙ (𝑙𝑣𝑙 + 1) ∙ (𝑙𝑣𝑙 + 2) − 𝑒𝑥𝑝_, где _lvl_ — текущий уровень персонажа;
_exp_ — опыт персонажа.

## Обратить внимание

1. Если в запросе на создание игрока нет параметра “banned”, то считаем, что пришло 
значение “false”.
2. Параметры даты между фронтом и сервером передаются в миллисекундах (тип Long)
начиная с 01.01.1970.
3. При обновлении или создании игрока игнорируем параметры “id”, “level” и 
“untilNextLevel” из тела запроса.
4. Если параметр order не указан – нужно использовать значение PlayerOrder.ID.
5. Если параметр pageNumber не указан – нужно использовать значение 0.
6. Если параметр pageSize не указан – нужно использовать значение 3.
7. Не валидным считается id, если он:
- не числовой
- не целое число
- не положительный
8. При передаче границ диапазонов (параметры с именами, которые начинаются на «min»
или «max») границы нужно использовать включительно.

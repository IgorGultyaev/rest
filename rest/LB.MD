# Part 1


Task:
- id : long (auto-generated)
- name : String
- deadline : Instant "2023-04-27T00:00:00Z"
- done : boolean
- created : Insant.now()


1. domain: Task
2. controller: TaskController ("/api/tasks")
3. service: TaskService
4. mapper: TaskMapper
5. test scripts


# Part 2


??????????? ????? ?? ??


# Part 3


Добавить валидацию тасков (Create, Update)


0. In Controller - @Min, @Max, ... (params validation)
1. Name - not blank
2. In Name - #critical, #medium (@PriorityLabel)
3. Name != bug, todo, fixme (@NoBuzzword)
4. Deadline in future (builtin)


# Part 4




Добавить к таскам авторов (менять нужно только БД, Сервис, DTO, модель и тестовые запросы)
1. При создании автоматически проставляется автор (владелец)
2. Изменять может только автор (владелец)
3. Удалять может только автор (владелец) или админ (роль админ)
4. Смотреть (`getAll` и `getById` могут все)




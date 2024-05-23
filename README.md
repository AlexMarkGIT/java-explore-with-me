# java-explore-with-me
Template repository for ExploreWithMe project.

Описание проекта:

Свободное время — ценный ресурс. 
Ежедневно мы планируем, как его потратить — куда и с кем сходить.
Сложнее всего в таком планировании поиск информации и переговоры. 
Нужно учесть много деталей: какие намечаются мероприятия, 
свободны ли в этот момент друзья, как всех пригласить и где собраться.

ExploreWithMe — афиша. 
В этой афише можно предложить какое-либо событие 
от выставки до похода в кино и собрать 
компанию для участия в нём.

Features:

В приложении реализована дополнительная функциональнось:
теперь к любому опубликованному событию можно оставлять комментарии

Основные эндпоинты:

    Основной сервис:
        Admin:
            /admin/users 
            /admin/categories
            /admin/events
            /admin/compilations
            /admin/comments
        Private:
            /users/{userId}/events
            /users/{userId}/requests
            /users/{userId}/comments
        Public:
            /categories
            /events
            /compilations
            /comments
    Сервис статистики:
            /hit
            /stats

ссылка на пулл-реквест:
https://github.com/AlexMarkGIT/java-explore-with-me/pull/3

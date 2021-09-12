% скільки цифр у загадуваному числі
game_lvl(4).

% процедура запуску гри
play :-
	game_lvl(Lvl),                                            % отримати довжину 
	create_secret(Lvl, Secret),                               % загадати число вказаної довжини
%	write(Secret),                                            % розкоментувати для тестування
    nb_setval(c, 0),                                          % set counter to 0
	repeat,                                                   % повторювати, поки не розгадано
	  write('Your guess : '),         
	  read(Guess),                                            % считуємо здогадку
	   (evaluate(Secret, Guess, Bulls, Cows) -> 
	   	  count, nb_getval(c, X),                             % +1 до лічильника, отримати значення
          format('Bulls : ~w Cows : ~w~nAttempts: ~w~n', [Bulls, Cows, X]),
	  	  Bulls = Lvl;                                        %перевіряємо чи вгадав гравець усі цифри	  	
	    format('Your guess must be ~w digits number, each between 1 and 9\n', [Lvl]), fail % після недавлого вводу знову вивести prompt
	   ),
	nb_getval(c, X),                                          % отримати значення лічильника
	format('Hurray! You won! It took you ~w attempts', [X]).  % гра завершена

% один крок лічильника, використовуються глобальні змінні
count :- nb_getval(c, X), X1 is X + 1, nb_setval(c, X1).

%create_secret(+Length, -Secret) - повертає Secret - список цифр (без дублікатів) довжини Length.
create_secret(N, Secret) :-
    repeat,                                    % через те, що задані додаткові умови, потрібно повторювати виконання, поки вони не будуть виконані, інкаше просто повернеться false
    length(Secret, N),                         % фіксуємо довжину списку цифр
    maplist(random_between(1, 9), Secret),     % генеруємо цифри від 1 до 9 включно
    no_dupes(Secret),                          % перевіряємо, чи нема дублікатів
    !.                                         % потрібен лише один розв'язок

% evaluate(+List, +Atom, ?Integer, ?Integer)
% evaluate(+Secret, +Guess, -Bulls, -Cows) : скільки точних (Bulls) та неточних (Cows) попадань
% є у здогадці користувача Guess при загаданому секреті Secret
evaluate(Secret, Guess, Bulls, Cows) :-
    check(Guess, IntList),                                 % перевіряємо валідність здогадки
	get_bulls(Secret, IntList, [], BullsList),             % знайти "биків" - однакові елементи списків (здогадка та секрет) на однакових позиціях
    length(BullsList, Bulls),                              % кількість биків - довжина списку биків
    subtract(IntList, BullsList, CowsCandList),            % відняти від "згогадки" уже вгаданих ("биків"), знаходимо "кандидатів в корови"
    intersection(Secret, CowsCandList, CowsList),          % скільки з кандидатів справді було в загаданому числі (intersection, оскільки числа у здогадці та секреті не повторюються)
    length(CowsList, Cows).                                % кількість корів - довжина списку корів


% check(+Atom) - чи є здогадка користувача валідною (4 цифри від 1 до 9, без дублікатів).
check(Guess, IntList) :-
	atom_chars(Guess, CharList),                          % перетворюємо здогадку-атом у charlist: 3245 => ['3', '2', '4', '5']
	maplist(atom_number, CharList, IntList),              % перетворюємо кожен Char у Integer: ['3', '2', '4', '5'] => [3, 2, 4, 5]
	no_dupes(IntList),                                    % перевіряємо наявність дублікатів
	not(member(0, IntList)).                              % серед цифр не може бути 0

%% get_bulls(+Secret, +Guess, +Initial_list, ?BullsList) - список вгаданих чисел з позиціями, при секреті Secret та здогадці Guess.
get_bulls([H|T1], [H|T2], Bulls, R) :- get_bulls(T1, T2, [H|Bulls], R). % додаємо спільну цифру до накопичувача, tail recursion
get_bulls([_|T1], [_|T2], Bulls, R) :- get_bulls(T1, T2, Bulls, R).     % нема збігів, продовжувати, tail recursion
get_bulls([],[], Bulls, Bulls).                                         % повертаємо віповідь з каунтера

% no_dupes(+List) - чи правда, що у списку List немає дублікатів
no_dupes(List) :-
    not((append(_,[Element|Rest], List), append(_,[Element|_],Rest))).

%% count_bulls(+Secret, +Guess, -Bulls) - скільки биків у здогадці користувача
%% count_bulls(L1, L2, R) :- get_bulls(L1, L2, [], Common), length(Common, R), !.

% Приклад гри:
% ?- play.
% Your guess : 5461.
% Bulls : 1 Cows : 2
% Attempts: 1
% Your guess : |: 6892.
% Bulls : 0 Cows : 1
% Attempts: 2
% Your guess : |: 1234.
% Bulls : 1 Cows : 1
% .....
% Hurray! You won! It took you 8 attempts
% true .
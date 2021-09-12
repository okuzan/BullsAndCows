:- use_module(library(lambda)).
:- use_module(library(clpfd)).

% Parameters of the server

% length of the guess
%proposition(4).


% Numbers of digits
% 0 -> 8
%digits(8).
%my_write(Label,Value) :- set_UI_result(Label, Value).


bulls_and_cows_server :-
%	proposition(4),
	length(Solution, 4),
	choose(Solution),
	repeat,
	write('Your guess : '),
	read(Guess),
	 (study(Solution, Guess, Bulls, Cows) ->  format('Bulls : ~w Cows : ~w~n', [Bulls, Cows]), Bulls = 4;
	    format('Guess must be of 4 digits between 1 and 9~n'), fail).



choose(Solution) :-
	Max is 9,
	repeat,
	maplist(\X^(X is random(Max) + 1), Solution),
	all_distinct(Solution),
	!.

check(Guess ) :-
	atom_chars(Guess, Chars),
	maplist(\X^Y^(atom_number(X, Y)), Chars, Ms),
	is_set(Ms).

is_set(Lst) :-
    setof(X, member(X, Lst), Set),
    length(Lst, N),
    length(Set, N).

study(Solution, Guess, Bulls, Cows) :-
%	proposition(4),

	% compute the transformation 1234 => [1,2,3,4]
	atom_chars(Guess, Chars),
	maplist(\X^Y^(atom_number(X, Y)), Chars, Ms),

	% check that the guess is well formed
	length(Ms, 4),
	maplist(\X^(X > 0, X =< 9), Ms),

	% compute the digit in good place
	foldl(\X^Y^V0^V1^((X = Y->V1 is V0+1; V1 = V0)),Solution, Ms, 0, Bulls),

	% compute the digits in bad place
	foldl(\Y1^V2^V3^(foldl(\X2^Z2^Z3^(X2 = Y1 -> Z3 is Z2+1; Z3 = Z2), Ms, 0, TT1),
			 V3 is V2+ TT1),
	      Solution, 0, TT),
	Cows is TT - Bulls.


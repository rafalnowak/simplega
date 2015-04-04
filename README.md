SimpleGA - basic genetic algorithm for Scala
============================================

SimpleGA is simple in use genetic algorithm library for Scala.
It provides predefined population model and allows to define new populations for all users needs.

The library comes with two predefined population types:

* binary population
* permutation population

For both of them are defined basic genetic operators for selection, crossover and mutation. 
With their generic interface it is very easy to add new custom operator functions.

TODO:
=====

* create operators for permutation population
* write more example
* use State monad and more "functional" way of program structure
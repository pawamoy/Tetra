# Tetranucleo

[![Join the chat at https://gitter.im/Ecole-des-collegues/Tetranucleo](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/Ecole-des-collegues/Tetranucleo?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

Fonction de croissance des codes circulaires de tétranucléotides autocomplémentaires.

## Libs

* JGraphT
* Apache Commons Math3
* dpaukov Combinatorics

## Optimisations possibles

- Utilisation de Disruptor (voir issues)
- Multi-threading sur la génération de graphes (1/3 - 3/1 et 2/2)
- Utilisation d'entiers / bytes pour les noeuds au lieu de chaînes
- Multi-threading sur le producteur (plusieurs producteurs)
- Compression des résultats (écriture de fichiers compressés)
- Découpage de la HashMap en plusieurs HashMap (une par longueur de code) ?
  Un petit test a montré que le temps d'accès à l'élément d'une HashMap de 100.000+ est inférieur à 1ms

## Questions

- Est-il utile ou contre-performant d'utiliser plusieurs niveaux de threads ?
  (plusieurs producteurs qui lancent plusieurs consommateurs qui lancent plusieurs calculs de graphes...)
  Au final le processeur gère correctement un certains nombre de threads, il faut éviter la surcharge.

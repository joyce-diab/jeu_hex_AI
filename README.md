# Jeu de Hex et bandits manchots
Le jeu de Hex est un jeu combinatoire abstrait pour deux joueurs qui se joue sur un tablier d'hexagones blancs de taille allant jusqu'à 14x14. 
Les joueurs, Bleu et Rouge, colorient tour à tour un hexagone blanc et l'objectif est de former une ligne de sa couleur qui relie Est et Ouest pour le joueur Bleu, et Nord et Sud pour le joueur Rouge. 
La combinatoire du jeu étant grande, il est pertinent d'utiliser un algorithme de recherche arborescente de Monte Carlo (MCTS) classiquement utilisé au Go pour résoudre le jeu. Cet algorithme, inspiré de stratégies de joueurs sur bandits machots, va explorer petit à petit l'arbre du jeu en simulant des parties entières, dans la limite d'un budget disponible. Plus le budget est élevé, plus un joueur pourra approximer les meilleurs coups à jouer. 
Nous pouvons alors nous demander quelle influence le rapport de budget entre les deux joueurs a sur leur probabilité de victoire, sachant que le premier joueur est fortement avantagé. 

Le but est donc de modéliser le jeu, implanter l'algorithme et l'expérimenter.

# Commandes pour le lancement du jeu

Lancer la commande suivant dans le répertoire jeu_de_hex. 
  -  ant run 

Par défaut on lance le jeu avec RED qui commence. Les deux joueur utilisent un budget de 10_000. 
RED utilise MCTS et BLUE utilise RAVE

#Pour modifier le budget, le joueur qui commence et les algos : src/hex/view/gui/HexFrame.java (dans le constructeur)
  - Pour utiliser MCTS : RobotMCTS(couleur_du_joueur, son_budget)
  - Pour utiliser RAVE : RobotRave(couleur_du_joueur, son_budget)
  - Le joueur ajouter dans la liste en premier est celui qui commence
  
  
# Expérimentation réalisée
 
Pour plus d'informations, consultez le rapport et les graphes

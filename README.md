# Membres du groupe 

ABOGOUNRIN Ayath

DIAB Joyce 

BOUAMAR Lina

KAMGANG KENMOE Miguel Jordan


# Commandes pour le lancement du jeu

Lancer la commande suivant dans le répertoire jeu_de_hex. 
  -  ant run 

Par défaut on lance le jeu avec RED qui commence. Les deux joueur utilisent un budget de 10_000. 
RED utilise MCTS et BLUE utilise RAVE

Pour modifier le budget, le joueur qui commence et les algos : src/hex/view/gui/HexFrame.java (dans le constructeur)
  - Pour utiliser MCTS : RobotMCTS(couleur_du_joueur, son_budget)
  - Pour utiliser RAVE : RobotRave(couleur_du_joueur, son_budget)
  - Le joueur ajouter dans la liste en premier est celui qui commence
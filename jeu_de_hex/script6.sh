#!/bin/bash

# Compilation du programme Java
ant compile

MAX_SIZE=24
STARTINGPLAYER="RED"
LOG_DIR="logs"
LOG_FILE="logs/experiments_size_8_18-23.log"
GRID_SIZE=1
RAPPORT=(5)

# Limite du nombre de processus simultanés
max_processes=2
running_processes=0

# Algorithmes utilisés (ici en dur à "RAVE" pour les deux)
algoRED="RAVE"
algoBLUE="RAVE"

#for somme in $(seq 17 $MAX_SIZE); do
    size=8
    budget=32768    #$(echo "2^($somme - $size)" | bc)
    
    for j in "${RAPPORT[@]}"; do
        budgetRED=$budget
        budgetBLUE=$(( budget * j ))
        
        # Lancement de la première exécution
        java -cp bin hex.analysis.Main "$size" "$STARTINGPLAYER" "$budgetRED" "$algoRED" "$budgetBLUE" "$algoBLUE" >> "$LOG_FILE" &
        running_processes=$((running_processes+1))
        
        # Si j > 1, on lance l'exécution avec les budgets inversés
        if (( j > 1 )); then
            java -cp bin hex.analysis.Main "$size" "$STARTINGPLAYER" "$budgetBLUE" "$algoRED" "$budgetRED" "$algoBLUE" >> "$LOG_FILE" &
            running_processes=$((running_processes+1))
        fi
        
        # Si on atteint le nombre maximum de processus, on attend que tous se terminent
        if (( running_processes >= max_processes )); then
            wait
            running_processes=0
        fi
    done
#done

# Attente de la fin des derniers processus lancés
wait

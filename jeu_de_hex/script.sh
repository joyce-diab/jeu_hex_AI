#!/bin/bash

#Compilation du programme Java
ant compile

# Paramètres d'expérimentation

MAX_SIZE=19
ALGORITHMS_RED=("RAVE")
ALGORITHMS_BLUE=("RAVE")
STARTINGPLAYER=("RED")
LOG_DIR="logs"
LOG_FILE="logs/mcts_mcts_size_4_15-19.log"

RAPPORT=(1 2 5)



    algoRED=MCTS
    algoBLUE=MCTS

    
    for somme in $(seq 15 $MAX_SIZE); do
       # for size in $(seq 1 $((16 < somme ? 16 : somme))); do
            size=4
            budget=$(echo "2^($somme - $size)" | bc)
            for j in ${RAPPORT[@]}; do
                budgetRED=$budget
                budgetBLUE=$((budget * j))
                #echo $size, $budget
                java -cp bin hex.analysis.Main $size $STARTINGPLAYER $budgetRED $algoRED $budgetBLUE $algoBLUE >> "$LOG_FILE"
                
                 if ((j > 1)); then
                   java -cp bin hex.analysis.Main $size $STARTINGPLAYER $budgetBLUE $algoRED $budgetRED $algoBLUE >> "$LOG_FILE"
                 fi
            done
        #done
      
    done



#!/bin/bash

# Compilation du programme Java
ant compile

# ParamÃ¨tres d'expÃ©rimentation
MAX_SIZE=18
ALGORITHMS_RED=("MCTS" "RAVE" "MCTS" "RAVE")
ALGORITHMS_BLUE=("MCTS" "RAVE" "RAVE" "MCTS")
STARTINGPLAYER=("RED")
LOG_DIR="logs"
LOG_FILE="logs/experiments_MCTS2_size_3.log"
GRID_SIZE=1

RAPPORT=(1 2 5)

# CrÃ©er le rÃ©pertoire de logs
#mkdir -p "$LOG_DIR"

calc_jobs() {
    local mem_per_job=1500  # 2.5GB par job
    local total_mem=$(free -m | awk '/Mem:/{print $7}')
    local safe_jobs=$(( total_mem / mem_per_job ))
    # Limiter Ã  4 jobs max mÃªme sur machines puissantes
    [[ $safe_jobs -gt 2 ]] && safe_jobs=2
    echo $safe_jobs
}

# Calculer le nombre de jobs (3/4 des cÅ“urs)
CORES=$(nproc)
JOBS=$(( CORES * 3 / 4 ))
[[ $JOBS -lt 1 ]] && JOBS=1

JVM_OPTS="-Xmx2g -Xms512m -XX:+UseSerialGC -Xss256k"

# GÃ©nÃ©rer toutes les commandes
commands=()
for somme in $(seq 3 $MAX_SIZE); do
    #for size in $(seq $GRID_SIZE $((16 < somme ? 16 : somme))); do
        size=3
        budget=$(echo "2^($somme - $size)" | bc)

        for j in ${RAPPORT[@]}; do
            budgetRED=$budget
            budgetBLUE=$((budget * j))
            
            cmd1="java -cp bin hex.analysis.Main $size $STARTINGPLAYER $budgetRED MCTS $budgetBLUE MCTS >> $LOG_FILE"
            commands+=("$cmd1")

            if ((j > 1)); then
                cmd2="java -cp bin hex.analysis.Main $size $STARTINGPLAYER $budgetBLUE MCTS $budgetRED MCTS >> $LOG_FILE"
                commands+=("$cmd2")
            fi
       # done
    done
    
   # if ((somme > 15)); then
   #     ((GRID_SIZE++))
   # fi
done

# ExÃ©cuter en parallÃ¨le avec verrouillage implicite de '>>'
printf "%s\n" "${commands[@]}" | xargs -P $JOBS -n 1 -I {} bash -c 'eval "$1"' _ {}
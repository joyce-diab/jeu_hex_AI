#!/bin/bash

# Compilation du programme Java
ant compile

# Paramètres d'expérimentation
MAX_SIZE=18
ALGORITHMS_RED=("MCTS" "RAVE" "MCTS" "RAVE")
ALGORITHMS_BLUE=("MCTS" "RAVE" "RAVE" "MCTS")
STARTINGPLAYER=("RED")
LOG_DIR="logs"
LOG_FILE="logs/experiments_size_MCTS_3_.log"
GRID_SIZE=7

RAPPORT=(1 2 5)

# Créer le répertoire de logs
mkdir -p "$LOG_DIR"

calc_jobs() {
    local mem_per_job=2500  # 2.5GB par job
    local total_mem=$(free -m | awk '/Mem:/{print $7}')
    local safe_jobs=$(( total_mem / mem_per_job ))
    # Limiter à 4 jobs max même sur machines puissantes
    [[ $safe_jobs -gt 4 ]] && safe_jobs=4
    echo $safe_jobs
}

# Calculer le nombre de jobs (3/4 des cœurs)
CORES=$(nproc)
JOBS=$(( CORES * 3 / 4 ))
[[ $JOBS -lt 1 ]] && JOBS=1

JVM_OPTS="-Xmx2g -Xms512m -XX:+UseSerialGC -Xss256k"


# Générer toutes les commandes
commands=()
for somme in $(seq 3 $MAX_SIZE); do
    #for size in $(seq $GRID_SIZE $((2 < somme ? 2 : somme))); do
        size=3
        budget=$(echo "2^($somme - $size)" | bc)

        for j in ${RAPPORT[@]}; do
            budgetRED=$budget
            budgetBLUE=$((budget * j))
            
            cmd1="java $JVM_OPTS -cp bin hex.analysis.Main $size $STARTINGPLAYER $budgetRED MCTS $budgetBLUE MCTS >> $LOG_FILE"
            commands+=("$cmd1")

            if ((j > 1)); then
                cmd2="java $JVM_OPTS -cp bin hex.analysis.Main $size $STARTINGPLAYER $budgetBLUE MCTS $budgetRED MCTS >> $LOG_FILE"
                commands+=("$cmd2")
            fi
        done
    #done
    
    #if ((somme > 15)); then
    #    ((GRID_SIZE++))
    #fi
done

# Exécuter en parallèle avec verrouillage implicite de '>>'
printf "%s\n" "${commands[@]}" | xargs -P $JOBS -n 1 -I {} bash -c 'eval "$1"' _ {}
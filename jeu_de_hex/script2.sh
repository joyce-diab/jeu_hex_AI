#!/bin/bash

# Configuration des ressources
MAX_CORES=$(nproc)                   # Utiliser tous les coeurs                        # Mémoire initiale par instance Java
JVM_OPTS="-XX:+UseG1GC -XX:MaxGCPauseMillis=50"  # Optimisation GC

# Compilation parallèle
ant -Djavac.executable=javac -Dbuild.parallel=yes compile

# Paramètres d'expérimentation
MAX_SIZE=32
GRID_SIZE=3
ALGORITHMS_RED=("RAVE")
ALGORITHMS_BLUE=("RAVE")
STARTINGPLAYER=("RED")
LOG_DIR="logs"
LOG_FILE="logs/experiment.log"
RAPPORT=(1 2 5)

# Création du pool de processus
task_queue() {
    local max_jobs=$MAX_CORES
    local job_count=0
    local pids=()

    for task in "$@"; do
        # Exécution en arrière-plan avec priorité élevée
        nice -n -15 $task &
        pids+=($!)
        
        ((job_count++))
        
        # Contrôle du parallélisme
        if ((job_count % max_jobs == 0)); then
            wait -n
            ((job_count--))
        fi
    done
    
    wait "${pids[@]}"
}

export -f task_queue
export JVM_XMX JVM_XMS JVM_OPTS

# Lancement des expériences
# find "$LOG_DIR" -name "*.log" -delete  # Nettoyage initial

for i in "${!ALGORITHMS_RED[@]}"; do
    algoRED=${ALGORITHMS_RED[$i]}
    algoBLUE=${ALGORITHMS_BLUE[$i]}

    seq 18 $MAX_SIZE | parallel -j $MAX_CORES --eta '
        size={}
        GRID_SIZE=$((3 + ({} - 18)/2))  # Ajustement dynamique
        
        for budget in $(bc <<< "2^(${} - $GRID_SIZE)"); do
            for j in ${RAPPORT[@]}; do
                budgetRED=$budget
                budgetBLUE=$((budget * j))
                
                # Fichier log unique par processus
                log_file="logs/experiment_${size}_${budgetRED}_${budgetBLUE}.log"
                
                # Commande Java optimisée
                java $JVM_OPTS \
                    -cp bin hex.analysis.Main \
                    $size $STARTINGPLAYER $budgetRED $algoRED $budgetBLUE $algoBLUE > "$log_file"
            done
        done
    '
done

# Consolidation des logs
find "$LOG_DIR" -name "experiment_*.log" -exec cat {} + > "$LOG_FILE"
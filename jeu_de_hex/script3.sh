#!/bin/bash

# Configuration des ressources
MAX_JOBS=$(( $(nproc)))  # Utilisation plus agressive des ressources
JVM_OPTS="-XX:+UseG1GC -XX:MaxGCPauseMillis=50"

# Paramètres d'expérimentation
MAX_SIZE=31
GRID_SIZE=4
ALGORITHMS_RED=("RAVE")
ALGORITHMS_BLUE=("RAVE")
STARTINGPLAYER=("RED")
LOG_DIR="logs"
MAIN_LOG="${LOG_DIR}/experiment.log"
TEMP_LOG_DIR="${LOG_DIR}/temp1"

# Nettoyage initial
#rm -rf "${LOG_DIR}" 2>/dev/null
mkdir -p "${TEMP_LOG_DIR}"

# Compilation
echo "🛠  Compilation du projet..."
ant compile

# Fonction pour exécuter une expérience
run_experiment() {
    local size=$1
    local budgetRED=$2
    local budgetBLUE=$3
    local algoRED=$4
    local algoBLUE=$5
    
    local log_file="${TEMP_LOG_DIR}/job_${size}_${budgetRED}_${budgetBLUE}_${RANDOM}.log"
    
    echo "=== START | Size:$size | BudgetRED:$budgetRED | BudgetBLUE:$budgetBLUE | $(date +'%Y-%m-%d %T') ===" > "$log_file"
    
    
    java $JVM_OPTS \
        -cp bin hex.analysis.Main \
        $size $STARTINGPLAYER $budgetRED $algoRED $budgetBLUE $algoBLUE >> "$log_file" 2>&1
    
    echo "=== END | Exit code:$? | Duration:${SECONDS}s ===" >> "$log_file"
}

# Gestion du parallélisme
current_jobs=0
for i in "${!ALGORITHMS_RED[@]}"; do
    algoRED=${ALGORITHMS_RED[$i]}
    algoBLUE=${ALGORITHMS_BLUE[$i]}
    
    for somme in $(seq 19 $MAX_SIZE); do

        for size in $(seq $GRID_SIZE $((16 < somme ? 16 : somme))); do
            budget=$(echo "2^($somme - $size)" | bc)
        
        for j in 1 2 5 10; do
            budgetRED=$budget
            budgetBLUE=$((budget * j))
            
            # Lancement en arrière-plan
            run_experiment $size $budgetRED $budgetBLUE $algoRED $algoBLUE 
             
            # Contrôle du nombre de jobs
           # current_jobs=$((current_jobs + 1))
            ##if (( current_jobs >= MAX_JOBS )); then
             #   wait -n
             #   current_jobs=$((current_jobs - 1))
            #fi
        done
    done
      ((GRID_SIZE++))
  done
done

# Attente de la fin de tous les jobs
wait

# Consolidation des logs
echo "📦 Consolidation des logs..."
find "${TEMP_LOG_DIR}" -name "*.log" -exec cat {} + > "${MAIN_LOG}"
rm -rf "${TEMP_LOG_DIR}"

echo "✅ Toutes les expériences sont terminées !"
echo "📄 Log consolidé : ${MAIN_LOG}"
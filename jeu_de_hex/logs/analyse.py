import pandas as pd
import matplotlib.pyplot as plt
from math import gcd, log2
import numpy as np

# -------------------------------------------------------------------------
# Lecture du fichier et définition des ratios autorisés
# -------------------------------------------------------------------------
df = pd.read_csv('MCTS_RAVE/experiments_size_1.log', sep='\s+', header=None, names=[
    'ROUNDS', 'SIZE', 'STARTING_PLAYER', 'WINNER', 
    'WINS_WINNER', 'BUDGET_WINNER', 'ALGO_WINNER', 
    'WINS_LOSER', 'BUDGET_LOSER', 'ALGO_LOSER'
])

allowed_ratios = {(1,1), (1,2), (1,5), (2,1), (5,1)}
algoRED = ""
algoBLUE = ""
res = 0
# -------------------------------------------------------------------------
# Nouveau traitement des données avec calcul des victoires réelles
# -------------------------------------------------------------------------
processed_data = []

for index, row in df.iterrows():
    # Déterminer les victoires de RED
    if row['WINNER'] == 'RED':
        red_wins = row['WINS_WINNER']
        red_budget = row['BUDGET_WINNER']
        blue_budget = row['BUDGET_LOSER']
        if res < 1:
            algoRED = row["ALGO_WINNER"]
            algoBLUE = row["ALGO_LOSER"]
            res = 1 
    else:
        red_wins = row['WINS_LOSER']
        red_budget = row['BUDGET_LOSER']
        blue_budget = row['BUDGET_WINNER']
        if res < 1:
            algoRED = row["ALGO_LOSER"]
            algoBLUE = row["ALGO_WINNER"]
            res = 1

    # Calcul du ratio simplifié
    current_gcd = gcd(red_budget, blue_budget)
    a = red_budget // current_gcd
    b = blue_budget // current_gcd
    
    if (a, b) in allowed_ratios:
        processed_data.append({
            'BASE': current_gcd,
            'RATIO': (a, b),
            'RED_WINS': red_wins,
            'TOTAL_ROUNDS': row['ROUNDS']
        })

# -------------------------------------------------------------------------
# Agrégation des résultats
# -------------------------------------------------------------------------
processed_df = pd.DataFrame(processed_data)
grouped = processed_df.groupby(['BASE', 'RATIO']).agg(
    TOTAL_RED_WINS=('RED_WINS', 'sum'),
    TOTAL_ROUNDS=('TOTAL_ROUNDS', 'sum')
).reset_index()

grouped['WIN_PERCENTAGE'] = (grouped['TOTAL_RED_WINS'] / grouped['TOTAL_ROUNDS']) * 100

# -------------------------------------------------------------------------
# Filtrage des puissances de 2
# -------------------------------------------------------------------------
def is_power_of_two(x):
    return (x & (x - 1) == 0) and x != 0

grouped = grouped[grouped['BASE'].apply(is_power_of_two)]
grouped = grouped.sort_values('BASE')

# -------------------------------------------------------------------------
# Création du tableau pivot
# -------------------------------------------------------------------------
pivot_df = grouped.pivot(index='BASE', columns='RATIO', values='WIN_PERCENTAGE')
pivot_df = pivot_df.dropna(how='all').sort_index()

# -------------------------------------------------------------------------
# Tracé du diagramme en barres
# -------------------------------------------------------------------------
ratios = pivot_df.columns
x = np.arange(len(pivot_df.index))
width = 0.15

fig, ax = plt.subplots(figsize=(12, 7))

for i, ratio in enumerate(ratios):
    offset = width * (i - (len(ratios)-1)/2)  # Centrage des barres
    ax.bar(x + offset, 
           pivot_df[ratio], 
           width, 
           label=f'{ratio[0]}:{ratio[1]}')

# Configuration des axes
print(pivot_df.index)
ax.set_xticks(x)
ax.set_xticklabels(pivot_df.index)
ax.set_ylabel('Taux de victoire RED (%)')
ax.set_xlabel('Budget de base (puissance de 2)')
ax.set_title(f"Évolution du taux de victoire RED par ratio de budget (R:{algoRED}, B:{algoBLUE})")
ax.legend(title='Ratio budget', bbox_to_anchor=(1.05, 1), loc='upper left')
ax.grid(True, linestyle='--', alpha=0.6)

plt.tight_layout()
plt.show()
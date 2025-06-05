import pandas as pd
import matplotlib.pyplot as plt
from math import gcd, log2
import numpy as np

# -------------------------------------------------------------------------
# Lecture du fichier et traitement des données (identique)
# -------------------------------------------------------------------------
df = pd.read_csv('experiments_size_4_C-0.1.log', sep='\s+', header=None, names=[
    'ROUNDS', 'SIZE', 'STARTING_PLAYER', 'WINNER', 
    'WINS_WINNER', 'BUDGET_WINNER', 'ALGO_WINNER', 
    'WINS_LOSER', 'BUDGET_LOSER', 'ALGO_LOSER'
])

allowed_ratios = {(1,1), (1,2), (1,5), (2,1), (5,1)}
algoRED = ""
algoBLUE = ""
res = 0

processed_data = []
for index, row in df.iterrows():
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

processed_df = pd.DataFrame(processed_data)
grouped = processed_df.groupby(['BASE', 'RATIO']).agg(
    TOTAL_RED_WINS=('RED_WINS', 'sum'),
    TOTAL_ROUNDS=('TOTAL_ROUNDS', 'sum')
).reset_index()
grouped['WIN_PERCENTAGE'] = (grouped['TOTAL_RED_WINS'] / grouped['TOTAL_ROUNDS']) * 100

# -------------------------------------------------------------------------
# Préparation des données pour le plot
# -------------------------------------------------------------------------
grouped = grouped[grouped['BASE'].apply(lambda x: (x & (x - 1) == 0 and x != 0))]
grouped = grouped.sort_values('BASE')

# Création d'un DataFrame complet avec toutes les combinaisons possibles
all_bases = sorted(grouped['BASE'].unique())
all_ratios = grouped['RATIO'].unique()

# Création d'une grille complète
index = pd.MultiIndex.from_product([all_bases, all_ratios], names=['BASE', 'RATIO'])
full_df = grouped.set_index(['BASE', 'RATIO']).reindex(index).reset_index()
full_df = full_df.dropna(subset=['WIN_PERCENTAGE'])


# -------------------------------------------------------------------------
# Tracé des courbes
# -------------------------------------------------------------------------
plt.figure(figsize=(12, 7))
markers = ['o', 's', 'D', '^', 'v']  # Différents marqueurs pour chaque ratio
colors = ['#1f77b4', '#ff7f0e', '#2ca02c', '#d62728', '#9467bd']  # Palette distincte

# Tracer les courbes pour chaque ratio
for i, ratio in enumerate(all_ratios):
    ratio_df = full_df[full_df['RATIO'] == ratio]
    ratio_str = f"{ratio[0]}:{ratio[1]}"
    
    plt.plot(np.array(ratio_df['BASE']), 
             np.array(ratio_df['WIN_PERCENTAGE']), 
             marker=markers[i % len(markers)],
             linestyle='-',
             color=colors[i % len(colors)],
             linewidth=2,
             markersize=8,
             label=ratio_str)

# Tracer la ligne de 50% en couleur spécifique (par exemple, rouge)
for i, ratio in enumerate(all_ratios):
    ratio_df = full_df[full_df['RATIO'] == ratio]
    # Extraire les données où le pourcentage de victoire est 50%
    fifty_percent_df = ratio_df[ratio_df['WIN_PERCENTAGE'] == 50]
    if not fifty_percent_df.empty:
        plt.plot(fifty_percent_df['BASE'], 
                 fifty_percent_df['WIN_PERCENTAGE'], 
                 marker='D',  # Utilisez un autre marqueur si nécessaire
                # color='red',  # Couleur rouge pour la ligne de 50%
                 linestyle='-', 
                 linewidth=2, 
                 markersize=8, 
                 #label=f'{ratio[0]}:{ratio[1]} - 50%'
                 )

# Configuration de l'échelle logarithmique en base 2
plt.xscale('log', base=2)
plt.xticks(all_bases, all_bases)
plt.ylim(0, 100)  # Pourcentage entre 0% et 100%

# Définir les pourcentages de victoire spécifiques pour l'axe Y
plt.yticks([10, 20, 40, 50, 60, 80, 100])

plt.xlabel('Taille du budget ')
plt.ylabel('Taux de victoire RED (%)')
plt.title(f"Évolution du taux de victoire RED par ratio de budget (R:{algoRED}, B:{algoBLUE}) avec un paramètre d'exploitation 0.1")
plt.grid(True, linestyle='--', alpha=0.6)
plt.legend(title='Ratio Budget', bbox_to_anchor=(1.05, 1), loc='upper left')

# Ajout d'une annotation pour la taille du plateau
plt.text(0.95, 0.05, f'Taille du plateau: {df["SIZE"].iloc[0]}',
         ha='right', va='bottom',
         transform=plt.gca().transAxes,
         fontsize=10, bbox=dict(facecolor='white', alpha=0.8))

plt.tight_layout()
plt.show()

import pandas as pd
import seaborn as sns
import matplotlib.pyplot as plt
import numpy as np

# Charger les données avec les bons paramètres
df = pd.read_csv(
    'experiments.log',
    sep=r'\s+',  # Raw string corrigée
    header=0,  # Utiliser la première ligne comme header
    names=[  # Noms de colonnes corrigés
        'ROUNDS', 'SIZE', 'STARTING_PLAYER', 'WINNER',
        'WINS_WINNER', 'BUDGET_WINNER', 'ALGO_WINNER',  # Correction de BUGET->BUDGET
        'WINS_LOSER', 'BUDGET_LOSER', 'ALGO_LOSER'
    ],
    dtype={  # Définir les types explicitement
        'ROUNDS': 'int',
        'WINS_WINNER': 'int',
        'WINS_LOSER': 'int',
        'SIZE': 'int'
    }
)

# 1. Visualisation des victoires
plt.figure(figsize=(10, 6))
sns.countplot(
    data=df,
    x='SIZE',
    hue='WINNER',
    palette={'RED': 'red', 'BLUE': 'blue'}
)
plt.title('Répartition des victoires par taille de jeu')
plt.xlabel('Taille du jeu')
plt.ylabel('Nombre de parties gagnées')
plt.legend(title='Vainqueur')
plt.show()

# 2. Calcul du taux de victoire
df['STARTING_WINS'] = df.apply(
    lambda row: row['WINS_WINNER'] if row['STARTING_PLAYER'] == row['WINNER'],
    axis=1
)

grouped = df.groupby(['SIZE', 'STARTING_PLAYER']).agg(
    TOTAL_WINS=('STARTING_WINS', 'sum'),
    TOTAL_ROUNDS=('ROUNDS', 'sum')
).reset_index()

grouped['WIN_RATE'] = grouped['TOTAL_WINS'] / grouped['TOTAL_ROUNDS']

# Visualisation du taux de victoire
plt.figure(figsize=(10, 6))
sns.barplot(
    data=grouped,
    x='SIZE',
    y='WIN_RATE',
    hue='STARTING_PLAYER',
    palette={'RED': 'red', 'BLUE': 'blue'}  # Palette corrigée
)
plt.axhline(0.5, color='gray', linestyle='--')
plt.title('Taux de victoire du joueur commençant par taille')
plt.ylim(0, 1)
plt.show()

# 3. Visualisation combinée
g = sns.catplot(
    data=df,
    x='SIZE',
    hue='WINNER',
    col='STARTING_PLAYER',
    kind='count',
    palette={'RED': 'red', 'BLUE': 'blue'},
    height=4,
    aspect=1.2
)
g.set_axis_labels('Taille du jeu', 'Nombre de parties')
g.set_titles('Joueur commençant: {col_name}')
g.fig.subplots_adjust(top=0.85)
g.fig.suptitle('Répartition des victoires par taille et joueur commençant')
plt.show()



# Calculer le ratio de budget
df['BUDGET_RATIO'] = df['BUDGET_WINNER'] / (df['BUDGET_LOSER'] + 1e-9)  # +1e-9 pour éviter la division par zéro

# Créer des catégories de ratio
df['RATIO_CAT'] = pd.cut(df['BUDGET_RATIO'], 
                        bins=[0, 0.5, 0.8, 1.2, 2, np.inf],
                        labels=['<0.5', '0.5-0.8', '0.8-1.2', '1.2-2', '>2'])

# Analyse par taille de grille
plt.figure(figsize=(14, 8))
heatmap_data = pd.crosstab(
    index=[df['SIZE'], df['RATIO_CAT']],
    columns=df['WINNER'],
    normalize='index'
)

sns.heatmap(
    heatmap_data * 100,  # Conversion en pourcentage
    annot=True,
    fmt='.1f',  # Format corrigé
    cmap='coolwarm',
    linewidths=.5,
    cbar_kws={'label': 'Pourcentage de victoire (%)'}
)

# Ajouter manuellement le symbole %
for t in plt.gca().texts:
    t.set_text(t.get_text() + " %")

plt.title('Pourcentage de victoire par ratio de budget et taille de grille')
plt.xlabel('Vainqueur')
plt.ylabel('Taille de grille - Catégorie de ratio')
plt.show()
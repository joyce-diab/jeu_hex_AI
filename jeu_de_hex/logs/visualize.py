import pandas as pd
import matplotlib.pyplot as plt
import seaborn as sns

# Lecture du fichier texte
file_path = 'res.txt'  # Remplacez par le chemin de votre fichier
column_names = ['ROUNDS', 'SIZE', 'STARTING_PLAYER', 'WINNER', 'WINS_WINNER', 'BUDGET_WINNER', 
                'ALGO_WINNER', 'WINS_LOSER', 'BUDGET_LOSER', 'ALGO_LOSER']  # Adaptez cela à vos colonnes

# Charger le fichier dans un DataFrame
df = pd.read_csv(file_path, delimiter=r'\s+', header=None, names=column_names)

# Vérifier les premières lignes pour s'assurer que tout est correct
print(df.head())

# 1. Répartition des gagnants par taille (SIZE)
plt.figure(figsize=(10, 6))
df.groupby(['SIZE', 'WINNER']).size().unstack().plot(kind='bar', stacked=True, color=['lightblue', 'lightgreen'])
plt.title("Répartition des gagnants par taille")
plt.xlabel('Taille')
plt.ylabel('Nombre de victoires')
plt.xticks(rotation=45)
plt.legend(title="Gagnant", labels=['Perdant', 'Gagnant'])
plt.show()

# 2. Distribution des budgets des gagnants par taille (BUDGET_WINNER)
plt.figure(figsize=(10, 6))
sns.boxplot(x='SIZE', y='BUDGET_WINNER', data=df, palette='Set2')
plt.title("Distribution des budgets des gagnants par taille")
plt.xlabel('Taille')
plt.ylabel('Budget du gagnant')
plt.show()

# 3. Performance des gagnants en fonction de la taille (WINS_WINNER)
plt.figure(figsize=(10, 6))
df.groupby(['SIZE', 'WINNER'])['WINS_WINNER'].sum().unstack().plot(kind='bar', stacked=True, color=['lightblue', 'lightgreen'])
plt.title("Performance des gagnants en fonction de la taille")
plt.xlabel('Taille')
plt.ylabel('Victoires totales du gagnant')
plt.xticks(rotation=45)
plt.legend(title="Gagnant", labels=['Perdant', 'Gagnant'])
plt.show()

# 4. Comparaison des budgets des gagnants en fonction de la taille et du joueur de départ
plt.figure(figsize=(10, 6))
sns.boxplot(x='SIZE', y='BUDGET_WINNER', hue='STARTING_PLAYER', data=df, palette='Set2')
plt.title("Comparaison des budgets des gagnants en fonction de la taille et du joueur de départ")
plt.xlabel('Taille')
plt.ylabel('Budget du gagnant')
plt.show()

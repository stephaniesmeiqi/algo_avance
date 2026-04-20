# Rendu de Monnaie — Backtracking avec Élagage

## Structure des fichiers

```
RenduMonnaie/
├── Main.java               → Point d'entrée, compare les 3 stratégies
├── BacktrackingBase.java   → Classe abstraite commune
├── BacktrackingBrut.java   → Stratégie 1 : force brute sans élagage
├── BacktrackingTri.java    → Stratégie 2 : élagage par tri décroissant
└── BacktrackingBorne.java  → Stratégie 3 : élagage par borne supérieure
```

## Compilation et exécution

```bash
# Compiler tous les fichiers
javac *.java

# Exécuter
java Main
```

## Stratégies implémentées

### 1. Brut (`BacktrackingBrut`)
- Aucune optimisation
- Explore toutes les combinaisons
- Sert de référence de comparaison

### 2. Tri décroissant (`BacktrackingTri`)
- Trie les pièces de la plus grande à la plus petite
- Converge rapidement vers une bonne solution
- Permet d'élaguer les branches déjà trop longues

### 3. Borne supérieure (`BacktrackingBorne`)
- Combine le tri décroissant
- Calcule une estimation optimiste du nombre de pièces restantes
- Coupe une branche si elle ne peut mathématiquement pas améliorer la solution connue

## Exemple de sortie attendue

```
=== Rendu de monnaie : 87 centimes ===
Pièces disponibles : [1, 2, 5, 10, 20, 50, 100, 200]

--- Brut (sans élagage) ---
  Meilleure solution : [50, 20, 10, 5, 2]
  Nombre de pièces   : 5
  Nombre d'essais    : ~50000+

--- Élagage par tri décroissant ---
  Meilleure solution : [50, 20, 10, 5, 2]
  Nombre de pièces   : 5
  Nombre d'essais    : ~1000

--- Élagage par borne supérieure ---
  Meilleure solution : [50, 20, 10, 5, 2]
  Nombre de pièces   : 5
  Nombre d'essais    : ~200
```

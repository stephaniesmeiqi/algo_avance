import java.util.*;

/**
 * Classe pour résoudre le problème du rendu de monnaie optimal
 * en utilisant la programmation dynamique.
 */
public class RendreMonnaie {
    
    // Constante pour représenter l'infini (somme impossible)
    static final int INF = Integer.MAX_VALUE / 2;
    
    /**
     * Résout le problème du rendu de monnaie optimal.
     * @param pieces tableau des valeurs des pièces
     * @param somme la somme à composer
     * @return un objet contenant la solution complète
     */
    static Solution resoudre(int[] pieces, int somme) {
        int n = pieces.length;
        
        // Matrice de programmation dynamique
        // dp[i][j] = nombre minimal de pièces pour composer j avec les i premières pièces
        int[][] dp = new int[n + 1][somme + 1];
        
        // Matrice de traçage : enregistre le choix effectué
        // choix[i][j] = -1 : pièce i non utilisée (provient de dp[i-1][j])
        // choix[i][j] = 0  : pièce i utilisée (provient de 1 + dp[i][j-pieces[i-1]])
        int[][] choix = new int[n + 1][somme + 1];
        
        // === INITIALISATION  ===
        dp[0][0] = 0;  // 0 pièce pour une somme de 0
        for (int j = 1; j <= somme; j++) {
            dp[0][j] = INF;  // Impossible de composer j sans pièces (j > 0)
        }
        
        // === REMPLISSAGE DE LA MATRICE DP ===
        // On remplit ligne par ligne (pièces) et colonne par colonne (sommes)
        for (int i = 1; i <= n; i++) {
            for (int j = 0; j <= somme; j++) {
                // Cas 1 : On n'utilise pas la pièce pieces[i-1]
                // On prend la valeur de la ligne précédente
                dp[i][j] = dp[i - 1][j];
                choix[i][j] = -1;  // Pièce non utilisée
                
                // Cas 2 : On utilise la pièce pieces[i-1] (au moins une fois)
                // Vérifier qu'il reste assez d'argent après prendre une pièce
                if (j >= pieces[i - 1] && dp[i][j - pieces[i - 1]] != INF) {
                    int avecPiece = 1 + dp[i][j - pieces[i - 1]];
                    
                    // Si utiliser la pièce donne une meilleure solution
                    if (avecPiece < dp[i][j]) {
                        dp[i][j] = avecPiece;
                        choix[i][j] = 0;  // Marquer que cette pièce est utilisée
                    }
                }
            }
        }
        
        // RECONSTRUCTION DE LA SOLUTION 
        // Remonter de dp[n][somme] en suivant les choix effectués
        List<Integer> solution = new ArrayList<>();
        if (dp[n][somme] != INF) {
            int i = n, j = somme;
            while (j > 0) {
                if (choix[i][j] == 0) {  // Cette pièce a été utilisée
                    solution.add(pieces[i - 1]);
                    j -= pieces[i - 1];  // Réduire la somme restante
                } else {  // Cette pièce n'a pas été utilisée
                    i--;  // Passer à la pièce précédente
                }
            }
            Collections.sort(solution, Collections.reverseOrder());
        }
        
        return new Solution(
            dp[n][somme],
            solution,
            dp,
            pieces
        );
    }
    
    /**
     * Classe pour encapsuler la solution complète.
     */
    static class Solution {
        int nombrePieces;
        List<Integer> pieces;
        int[][] dp;
        int[] piecesPossibles;
        
        Solution(int nb, List<Integer> p, int[][] d, int[] pp) {
            nombrePieces = nb;
            pieces = p;
            dp = d;
            piecesPossibles = pp;
        }
        
        @Override
        public String toString() {
            if (nombrePieces == Integer.MAX_VALUE / 2) {
                return " Aucune solution ";
            }
            
            StringBuilder sb = new StringBuilder();
            sb.append(" Solution trouvée\n");
            sb.append("  Nombre de pièces : ").append(nombrePieces).append("\n");
            sb.append("  Composition : ").append(pieces).append("\n");
            
            // Compter les occurrences de chaque pièce
            Map<Integer, Integer> compteur = new TreeMap<>((a, b) -> Integer.compare(b, a));
            for (int p : pieces) {
                compteur.put(p, compteur.getOrDefault(p, 0) + 1);
            }
            
            sb.append("  Détail :\n");
            for (Map.Entry<Integer, Integer> entry : compteur.entrySet()) {
                sb.append(String.format("    %d centimes : %d pièce(s)\n", 
                    entry.getKey(), entry.getValue())); 
            }
            
            return sb.toString();
        }
    }
    
    /**
     * Affiche une partie du tableau DP pour visualisation et analyse.
     */
    static void afficherTableau(int[][] dp, int[] pieces, int maxSomme) {
        int n = pieces.length;
        
        // En-têtes colonnes
        System.out.print("Pièces\\Somme ");
        for (int j = 0; j <= maxSomme; j++) {
            System.out.printf("%5d ", j);
        }
        System.out.println();
        System.out.println("-".repeat(15 + (maxSomme + 1) * 6));
        
        // Ligne 0 : cas de base (aucune pièce disponible)
        System.out.print("∅ (0 pièces) ");
        System.out.print("    0 ");
        for (int j = 1; j <= maxSomme; j++) {
            System.out.print("  ∞  ");
        }
        System.out.println();
        
        // Lignes 1 à n : chaque pièce
        for (int i = 1; i <= n && i <= 4; i++) {
            System.out.printf("c%d (%d cents) ", i, pieces[i - 1]);
            for (int j = 0; j <= maxSomme; j++) {
                if (dp[i][j] == INF) {
                    System.out.print("  ∞  ");
                } else {
                    System.out.printf("%5d ", dp[i][j]);
                }
            }
            System.out.println();
        }
        if (n > 4) {
            System.out.println("  [... autres pièces ...]");
        }
    }
    
    public static void main(String[] args) {
        
        // ========== TEST 1 : Système Euro ==========
        System.out.println("┌─ TEST 1 : SYSTÈME EURO ─────────────────────────────────────┐");
        System.out.println("│ Pièces : 200, 100, 50, 20, 10, 5, 2, 1 centimes              │");
        System.out.println("│ Somme à composer : 190 centimes (1€90)                        │");
        System.out.println("└─────────────────────────────────────────────────────────────┘\n");
        
        int[] piecesEuro = {200, 100, 50, 20, 10, 5, 2, 1};
        Solution sol1 = resoudre(piecesEuro, 190);
        System.out.println(sol1);
        
        System.out.println("Explication :");
        System.out.println("  190 = 100 + 50 + 20 + 20 (4 pièces est l'optimal)");
        System.out.println("  ou 190 = 100 + 50 + 2×20 avec les mêmes 4 pièces");
        System.out.println();
        
        // Tableau DP pour TEST 1
        System.out.println("Tableau DP (aperçu pour sommes 0-15) :\n");
        afficherTableau(sol1.dp, sol1.piecesPossibles, 15);
        System.out.println();
        
        // ========== TEST 2 : Problème simple ==========
        System.out.println("\n┌─ TEST 2 : PROBLÈME SIMPLE ──────────────────────────────────┐");
        System.out.println("│ Pièces : 1, 5, 10                                            │");
        System.out.println("│ Somme à composer : 11                                        │");
        System.out.println("└─────────────────────────────────────────────────────────────┘\n");
        
        int[] pieces2 = {1, 5, 10};
        Solution sol2 = resoudre(pieces2, 11);
        System.out.println(sol2);
        
        System.out.println("Explication :");
        System.out.println("  11 = 10 + 1 + 1 (3 pièces est l'optimal)");
        System.out.println("  (mieux que 5 + 5 + 1 qui aurait 3 pièces aussi, ou 1+1+...+1 qui en aurait 11)");
        System.out.println();
        
        // Tableau DP pour TEST 2
        System.out.println("Tableau DP (pour sommes 0-11) :\n");
        afficherTableau(sol2.dp, sol2.piecesPossibles, 11);
        System.out.println();
        
        // ========== TEST 3 : Cas impossible ==========
        System.out.println("\n┌─ TEST 3 : CAS IMPOSSIBLE ───────────────────────────────────┐");
        System.out.println("│ Pièces : 2, 5                                                │");
        System.out.println("│ Somme à composer : 3                                         │");
        System.out.println("└─────────────────────────────────────────────────────────────┘\n");
        
        int[] pieces3 = {2, 5};
        Solution sol3 = resoudre(pieces3, 3);
        System.out.println(sol3);
        
        System.out.println("Explication :");
        System.out.println("  3 ne peut être composé avec que des pièces de 2 et 5");
        System.out.println("  (2+2=4 > 3, impossible)");
        System.out.println();
        
        // Tableau DP pour TEST 3
        System.out.println("Tableau DP (pour sommes 0-7) :\n");
        afficherTableau(sol3.dp, sol3.piecesPossibles, 7);
        System.out.println();
        
        // ========== ANALYSE ==========
        System.out.println("\n╔═════════════════════════════════════════════════════════════╗");
        System.out.println("║                        ANALYSE DE COMPLEXITÉ                     ║");
        System.out.println("╚═════════════════════════════════════════════════════════════╝\n");
        
        System.out.println("Complexité temporelle : O(n × N)");
        System.out.println("  où n = nombre de pièces différentes");
        System.out.println("  où N = somme à composer\n");
        
        System.out.println("Complexité spatiale : O(n × N)");
        System.out.println("  Matrice DP : O(n × N)");
        System.out.println("  Matrice CHOIX : O(n × N)\n");
        
        System.out.println("Pour les tests exécutés :");
        System.out.println("  Test 1 : 8 × 190 = 1440 cellules");
        System.out.println("  Test 2 : 3 × 11 = 33 cellules");
        System.out.println("  Test 3 : 2 × 7 = 14 cellules\n");
        
        System.out.println("Avantages de cette approche :");
        System.out.println("  ✓ Garantit une solution optimale (si elle existe)");
        System.out.println("  ✓ Fonctionne pour tous les systèmes de monnaie");
        System.out.println("  ✓ Évite les erreurs du glouton (ex: {1, 3, 4} pour 6)\n");
        
        System.out.println("Note : Pour le système Euro, un algorithme glouton");
        System.out.println("      (prendre la plus grande pièce possible) donne");
        System.out.println("      aussi une solution optimale, mais ce n'est pas");
        System.out.println("      garanti pour tous les systèmes de monnaie.\n");
    }
}
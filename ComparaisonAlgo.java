import java.util.*;

/**
 * Programme comparant l'algorithme glouton et la programmation dynamique
 * pour le problème du rendu de monnaie optimal.
 */
public class ComparaisonAlgo {
    
    static final int INF = Integer.MAX_VALUE / 2;
    
    // ==================== ALGORITHME GLOUTON ====================
    
    /**
     * Algorithme glouton : prendre le plus de chaque pièce (par ordre décroissant)
     */
    static class ResultatGlouton {
        boolean possible;
        List<Integer> pieces;
        int nombrePieces;
        
        ResultatGlouton(boolean p, List<Integer> pc) {
            possible = p;
            pieces = pc;
            nombrePieces = pc.size();
        }
        
        @Override
        public String toString() {
            if (!possible) {
                return "Impossible";
            }
            return nombrePieces + " pièces : " + pieces;
        }
    }
    
    static ResultatGlouton glouton(int[] pieces, int somme) {
        // Trier en décroissant
        int[] piecesSorted = pieces.clone();
        Arrays.sort(piecesSorted);
        reverse(piecesSorted);
        
        List<Integer> solution = new ArrayList<>();
        int montantRestant = somme;
        
        for (int valeur : piecesSorted) {
            int nb = montantRestant / valeur;
            for (int i = 0; i < nb; i++) {
                solution.add(valeur);
            }
            montantRestant = montantRestant % valeur;
        }
        
        return new ResultatGlouton(montantRestant == 0, solution);
    }
    
    static void reverse(int[] arr) {
        int n = arr.length;
        for (int i = 0; i < n / 2; i++) {
            int temp = arr[i];
            arr[i] = arr[n - 1 - i];
            arr[n - 1 - i] = temp;
        }
    }
    
    // ==================== PROGRAMMATION DYNAMIQUE ====================
    
    /**
     * Programmation dynamique : cherche l'optimal garanti
     */
    static class ResultatDP {
        int nombrePieces;
        List<Integer> pieces;
        boolean possible;
        
        ResultatDP(int nb, List<Integer> pc) {
            nombrePieces = nb;
            pieces = pc;
            possible = (nb != INF);
        }
        
        @Override
        public String toString() {
            if (!possible) {
                return "Impossible";
            }
            return nombrePieces + " pièces : " + pieces;
        }
    }
    
    static ResultatDP programmationDynamique(int[] pieces, int somme) {
        int n = pieces.length;
        int[][] dp = new int[n + 1][somme + 1];
        int[][] choix = new int[n + 1][somme + 1];
        
        // Initialisation
        dp[0][0] = 0;
        for (int j = 1; j <= somme; j++) {
            dp[0][j] = INF;
        }
        
        // Remplissage
        for (int i = 1; i <= n; i++) {
            for (int j = 0; j <= somme; j++) {
                dp[i][j] = dp[i - 1][j];
                choix[i][j] = -1;
                
                if (j >= pieces[i - 1] && dp[i][j - pieces[i - 1]] != INF) {
                    int avecPiece = 1 + dp[i][j - pieces[i - 1]];
                    if (avecPiece < dp[i][j]) {
                        dp[i][j] = avecPiece;
                        choix[i][j] = 0;
                    }
                }
            }
        }
        
        // Reconstruction
        List<Integer> solution = new ArrayList<>();
        if (dp[n][somme] != INF) {
            int i = n, j = somme;
            while (j > 0) {
                if (choix[i][j] == 0) {
                    solution.add(pieces[i - 1]);
                    j -= pieces[i - 1];
                } else {
                    i--;
                }
            }
            Collections.sort(solution, Collections.reverseOrder());
        }
        
        return new ResultatDP(dp[n][somme], solution);
    }
    
    // ==================== TESTS ====================
    
    static void afficherComparaison(String titre, int[] pieces, int somme) {
        System.out.println("\n┌─ " + titre + " ─────────────────────────┐");
        
        // Afficher les données
        System.out.print("│ Pièces : ");
        for (int p : pieces) System.out.print(p + " ");
        System.out.println();
        System.out.println("│ Somme : " + somme);
        System.out.println("└─────────────────────────────────────────────────┘\n");
        
        // Exécuter les deux algorithmes
        long debut, fin;
        
        System.out.println("Algorithme GLOUTON :");
        debut = System.nanoTime();
        ResultatGlouton resG = glouton(pieces, somme);
        fin = System.nanoTime();
        System.out.println("  Résultat : " + resG);
        System.out.println("  Temps : " + (fin - debut) + " ns");
        
        System.out.println("\nProgrammation DYNAMIQUE :");
        debut = System.nanoTime();
        ResultatDP resDP = programmationDynamique(pieces, somme);
        fin = System.nanoTime();
        System.out.println("  Résultat : " + resDP);
        System.out.println("  Temps : " + (fin - debut) + " ns");
        
        // Comparer
        System.out.println("\nComparaison :");
        if (resG.nombrePieces == resDP.nombrePieces) {
            System.out.println("  ✓ Les deux algorithmes trouvent le MÊME nombre de pièces : " + resG.nombrePieces);
        } else {
            System.out.println("  ✗ Résultats DIFFÉRENTS !");
            System.out.println("    Glouton : " + resG.nombrePieces + " pièces");
            System.out.println("    DP : " + resDP.nombrePieces + " pièces");
            System.out.println("    Glouton est SUBOPTIMAL de " + (resG.nombrePieces - resDP.nombrePieces) + " pièces");
        }
    }
    
    public static void main(String[] args) {
        System.out.println("\n╔═══════════════════════════════════════════════════════════╗");
        System.out.println("║  COMPARAISON : GLOUTON vs PROGRAMMATION DYNAMIQUE         ║");
        System.out.println("╚═══════════════════════════════════════════════════════════╝");
        
        // Test 1 : Contre-exemple classique
        afficherComparaison(
            "TEST 1 : CONTRE-EXEMPLE {6, 4, 1}, N = 8",
            new int[]{6, 4, 1},
            8
        );
        
        // Test 2 : Système Euro
        afficherComparaison(
            "TEST 2 : SYSTÈME EURO {200, 100, 50, 20, 10, 5, 2, 1}, N = 190",
            new int[]{200, 100, 50, 20, 10, 5, 2, 1},
            190
        );
        
        // Test 3 : Système C'
        afficherComparaison(
            "TEST 3 : SYSTÈME C' {50, 30, 10, 5, 3, 1}, N = 6",
            new int[]{50, 30, 10, 5, 3, 1},
            6
        );
        
        afficherComparaison(
            "TEST 3b : SYSTÈME C' {50, 30, 10, 5, 3, 1}, N = 60",
            new int[]{50, 30, 10, 5, 3, 1},
            60
        );
        
        // Test 4 : Autre contre-exemple
        afficherComparaison(
            "TEST 4 : {1, 3, 4}, N = 6",
            new int[]{1, 3, 4},
            6
        );
        
        // Analyse finale
        System.out.println("\n╔═══════════════════════════════════════════════════════════╗");
        System.out.println("║                    ANALYSE ET CONCLUSIONS                  ║");
        System.out.println("╚═══════════════════════════════════════════════════════════╝\n");
        
        System.out.println("RÉSUMÉ :");
        System.out.println("━━━━━━━");
        System.out.println("\n1. GLOUTON : O(n) mais NON OPTIMAL en général");
        System.out.println("   • Fonctionne pour les systèmes CANONIQUES (Euro)");
        System.out.println("   • Échoue pour les systèmes NON CANONIQUES");
        System.out.println("   • Très rapide, utilisé par les commerçants");
        
        System.out.println("\n2. PROGRAMMATION DYNAMIQUE : O(n×N) mais TOUJOURS OPTIMAL");
        System.out.println("   • Garantit le nombre minimum de pièces");
        System.out.println("   • Plus lent mais fiable");
        System.out.println("   • Peut être optimisé en espace : O(N)");
        
        System.out.println("\n3. SYSTÈME CANONIQUE :");
        System.out.println("   • Euro EST canonique → glouton optimal");
        System.out.println("   • {6, 4, 1} N'EST PAS canonique → glouton peut échouer");
        System.out.println("   • {50, 30, 10, 5, 3, 1} N'EST PAS canonique → glouton échoue");
        
        System.out.println("\n4. EN PRATIQUE :");
        System.out.println("   • Tous les vrais systèmes de monnaie (Euro, $, £, ¥) sont canoniques");
        System.out.println("   • Les commerçants utilisent le glouton car il est rapide ET optimal");
        System.out.println("   • Les mathématiciens/informaticiens utilisent la DP pour la garantie");
        
        System.out.println("\n" + "═".repeat(62) + "\n");
    }
}
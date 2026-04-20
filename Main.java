import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        int[] pieces = {1, 2, 5, 10, 20, 50, 100, 200}; 
        int montant = 87;

        System.out.println("=== Rendu de monnaie : " + montant + " centimes ===");
        System.out.println("Pièces disponibles : " + Arrays.toString(pieces));
        System.out.println();

        // Stratégie 1 : Backtracking brut (sans élagage) 
        BacktrackingBrut brut = new BacktrackingBrut(pieces, montant);
        brut.resoudre();
        brut.afficherResultat("Brut (sans élagage)");

        //Stratégie 2 : Élagage par tri décroissant
        BacktrackingTri tri = new BacktrackingTri(pieces, montant);
        tri.resoudre();
        tri.afficherResultat("Élagage par tri décroissant");

        //Stratégie 3 : Élagage par borne supérieure 
        BacktrackingBorne borne = new BacktrackingBorne(pieces, montant);
        borne.resoudre();
        borne.afficherResultat("Élagage par borne supérieure");

        //Comparaison finale 
        System.out.println("\n========== COMPARAISON ==========");
        System.out.printf("%-35s | %-12s | %-10s%n", "Stratégie", "Essais", "Nb pièces");
        System.out.println("-".repeat(65));
        System.out.printf("%-35s | %-12d | %-10d%n", "Brut (sans élagage)",
                brut.getNbEssais(), brut.getMeilleureSolution().size());
        System.out.printf("%-35s | %-12d | %-10d%n", "Élagage par tri décroissant",
                tri.getNbEssais(), tri.getMeilleureSolution().size());
        System.out.printf("%-35s | %-12d | %-10d%n", "Élagage par borne supérieure",
                borne.getNbEssais(), borne.getMeilleureSolution().size());
    }
}

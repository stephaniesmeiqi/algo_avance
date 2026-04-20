/**
 * Backtracking BRUT — aucun élagage.
 * Explore toutes les combinaisons possibles sans optimisation.
 */
public class BacktrackingBrut extends BacktrackingBase {

    public BacktrackingBrut(int[] pieces, int montant) {
        super(pieces, montant);
    }

    @Override
    public void resoudre() {
        backtrack(montantCible);
    }

    private void backtrack(int montantRestant) {
        nbEssais++;

        // Cas de base : solution trouvée
        if (montantRestant == 0) {
            mettreAJourSolution();
            return;
        }

        // Essayer chaque pièce disponible
        for (int piece : pieces) {
            if (piece <= montantRestant) {
                cheminCourant.add(piece);
                backtrack(montantRestant - piece);
                cheminCourant.remove(cheminCourant.size() - 1); // backtrack
            }
        }
    }
}

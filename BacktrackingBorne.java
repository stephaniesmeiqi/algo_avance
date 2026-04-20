import java.util.Arrays;

/**
 * Backtracking avec ÉLAGAGE PAR BORNE SUPÉRIEURE.
 *
 * Optimisations combinées :
 * 1. Tri décroissant des pièces (comme BacktrackingTri)
 * 2. Borne supérieure : si (pièces déjà utilisées + minimum théorique restant)
 *    >= meilleure solution connue → on coupe la branche
 * 3. Le minimum théorique est estimé par : montantRestant / plusGrandePièce
 */
public class BacktrackingBorne extends BacktrackingBase {

    public BacktrackingBorne(int[] pieces, int montant) {
        super(pieces, montant);

        // Tri décroissant
        this.pieces = Arrays.copyOf(pieces, pieces.length);
        Arrays.sort(this.pieces);
        for (int i = 0, j = this.pieces.length - 1; i < j; i++, j--) {
            int tmp = this.pieces[i];
            this.pieces[i] = this.pieces[j];
            this.pieces[j] = tmp;
        }
    }

    @Override
    public void resoudre() {
        backtrack(montantCible, 0);
    }

    /**
     * @param montantRestant  
     * @param profondeur      nombre de pièces déjà utilisées dans ce chemin
     */
    private void backtrack(int montantRestant, int profondeur) {
        nbEssais++;

        // Cas de base : solution trouvée
        if (montantRestant == 0) {
            mettreAJourSolution();
            return;
        }

        // ÉLAGAGE 1 : borne supérieure
        // Estimation optimiste du nb de pièces encore nécessaires
        int estimationMin = (int) Math.ceil((double) montantRestant / pieces[0]);
        if (!meilleureSolution.isEmpty() &&
            profondeur + estimationMin >= meilleureSolution.size()) {
            return; // Cette branche ne peut pas améliorer la solution → on coupe
        }

        for (int piece : pieces) {
            if (piece <= montantRestant) {

                // ÉLAGAGE 2 : borne locale par pièce
                int estimationAvecCettePiece = (int) Math.ceil((double)(montantRestant - piece) / pieces[0]);
                if (!meilleureSolution.isEmpty() &&
                    profondeur + 1 + estimationAvecCettePiece >= meilleureSolution.size()) {
                    continue; // Inutile d'explorer cette pièce
                }

                cheminCourant.add(piece);
                backtrack(montantRestant - piece, profondeur + 1);
                cheminCourant.remove(cheminCourant.size() - 1); // backtrack
            }
        }
    }
}

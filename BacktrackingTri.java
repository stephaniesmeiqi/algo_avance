import java.util.Arrays;

/**
 * Backtracking avec ÉLAGAGE PAR TRI DÉCROISSANT.
 *
 * Optimisation : on trie les pièces de la plus grande à la plus petite.
 * En explorant d'abord les grandes pièces, on atteint une bonne solution
 * rapidement, ce qui permet d'élaguer plus tôt les branches trop longues.
 */
public class BacktrackingTri extends BacktrackingBase {

    public BacktrackingTri(int[] pieces, int montant) {
        super(pieces, montant);

        // Tri décroissant — clé de l'optimisation
        this.pieces = Arrays.copyOf(pieces, pieces.length);
        Arrays.sort(this.pieces);
        // Inverser manuellement pour ordre décroissant
        for (int i = 0, j = this.pieces.length - 1; i < j; i++, j--) {
            int tmp = this.pieces[i];
            this.pieces[i] = this.pieces[j];
            this.pieces[j] = tmp;
        }
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

        for (int piece : pieces) {
            if (piece <= montantRestant) {

                // ÉLAGAGE : si chemin courant est déjà >= meilleure solution connue → inutile de continuer
                if (!meilleureSolution.isEmpty() &&
                    cheminCourant.size() + 1 >= meilleureSolution.size()) {
                    // On peut continuer seulement si on peut encore améliorer
                    if (cheminCourant.size() + 1 >= meilleureSolution.size()) {
                        // Vérifie si la pièce seule suffit à couvrir le reste
                        if (piece != montantRestant) continue;
                    }
                }

                cheminCourant.add(piece);
                backtrack(montantRestant - piece);
                cheminCourant.remove(cheminCourant.size() - 1); // backtrack
            }
        }
    }
}

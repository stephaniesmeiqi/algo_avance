import java.util.ArrayList;
import java.util.List;

public abstract class BacktrackingBase {

    protected int[] pieces;
    protected int montantCible;
    protected List<Integer> meilleureSolution;
    protected List<Integer> cheminCourant;
    protected int nbEssais;

    public BacktrackingBase(int[] pieces, int montant) {
        this.pieces = pieces;
        this.montantCible = montant;
        this.meilleureSolution = new ArrayList<>();
        this.cheminCourant = new ArrayList<>();
        this.nbEssais = 0;
    }

    public abstract void resoudre();

    protected void mettreAJourSolution() {
        if (meilleureSolution.isEmpty() || cheminCourant.size() < meilleureSolution.size()) {
            meilleureSolution = new ArrayList<>(cheminCourant);
        }
    }

    public List<Integer> getMeilleureSolution() {
        return meilleureSolution;
    }

    public int getNbEssais() {
        return nbEssais;
    }

    public void afficherResultat(String nomStrategie) {
        System.out.println("--- " + nomStrategie + " ---");
        System.out.println("  Meilleure solution : " + meilleureSolution);
        System.out.println("  Nombre de pièces   : " + meilleureSolution.size());
        System.out.println("  Nombre d'essais    : " + nbEssais);
        System.out.println();
    }
}

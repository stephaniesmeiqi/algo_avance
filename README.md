# Programme de Programmation Dynamique : 

### Compilation
```bash
javac RendreMonnaie.java
```

### Exécution
Pour utiliser le programme, on peut creer une classe principale ou appeler la méthode statique `resoudre` depuis une autre classe.

Exemple d'utilisation dans une classe Main :

```java
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        int[] pieces = {1, 2, 5, 10, 20, 50, 100, 200};
        int somme = 87;

        RendreMonnaie.Solution solution = RendreMonnaie.resoudre(pieces, somme);

        if (solution.nombrePieces != Integer.MAX_VALUE / 2) {
            System.out.println("Nombre minimal de pièces : " + solution.nombrePieces);
            System.out.println("Pièces utilisées : " + solution.pieces);
        } else {
            System.out.println("Impossible de rendre la monnaie.");
        }
    }
}
```


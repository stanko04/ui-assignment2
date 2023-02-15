public class Player {
    DataCell[] originalGenes;
    int fitness;
    int numberOfMoves;
    String moves = "";
    boolean isBest = false;

    Player(DataCell[] originalGenes) {
        this.originalGenes = originalGenes;
        this.moves = "";
    }

    Player(DataCell[] originalGenes, int fitness, String moves, int numberOfMoves) {
        this.originalGenes = originalGenes;
        this.fitness = fitness;
        this.moves = moves;
        this.numberOfMoves = numberOfMoves;
    }

    Player(Player object) {
        this(object.getOriginalGenes(), object.getFitness(), object.getMoves(), object.getNumberOfMoves());
    }


    public int getFitness() {
        return fitness;
    }

    public DataCell[] getOriginalGenes() {
        return originalGenes;
    }

    public int getNumberOfMoves() {
        return numberOfMoves;
    }

    public String getMoves() {
        return moves;
    }
}

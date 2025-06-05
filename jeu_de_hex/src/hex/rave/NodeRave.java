package hex.rave;

import java.util.*;
import hex.model.*;
import hex.mcts.*;

public class NodeRave {
    
    private int victories, visits;
    private State state;
    private List<NodeRave> children;
    private NodeRave parent;
    private Position lastMove;
    private List<Position> movesNotExplored;
    
  
    private Map<Position, Integer> raveVictories;
    private Map<Position, Integer> raveVisits;
    
    public NodeRave(State state) {
        this.victories = 0;
        this.visits = 0;
        this.state = state;
        this.children = new ArrayList<>();
        this.parent = null;
        this.lastMove = null;
        this.movesNotExplored = new ArrayList<>(state.getPossibleMoves());
        
        
        this.raveVictories = new HashMap<>();
        this.raveVisits = new HashMap<>();
        for (Position pos : state.getPossibleMoves()) {
            raveVictories.put(pos, 0);
            raveVisits.put(pos, 0);
        }
    }
    
   
    public void setLastMove(Position move) {
        this.lastMove = move;
    }
    public Position getLastMove() {
        return this.lastMove;
    }
    public void setParent(NodeRave parent) {
        this.parent = parent;
    }
    public List<NodeRave> getChildren() {
        return children;
    }
  
   public NodeRave getParent() {
       return parent;
   }
   
    public State getState() {
        return this.state;
    }
    public List<Position> getMovesNotExplored() {
        return this.movesNotExplored;
    }
    public void removeExploredMove(Position move) {
        this.movesNotExplored.remove(move);
    }
    public void addChild(NodeRave child) {
        this.children.add(child);
    }
    public void visited() {
        this.visits++;
    }
    public void victory() {
        this.victories++;
    }
    public double getValue() {
        return victories / (double) visits;
    }
    public int getVisits() {
        return this.visits;
    }
    public int getVictories() {
        return this.victories;
    }

    public double getRaveVictories(Position pos) {
        return raveVictories.getOrDefault(pos, 0);
    }
    

    /**
     * 
     * @param move Posittion
     * @param win True pouur victoire et False pour défaite
     * @ensures que les statistiques pour la position donnée a bien été mise à jour
     */
    public void updateRaveStats(Position move, boolean win) {
        raveVisits.put(move, raveVisits.getOrDefault(move, 0) + 1);
        if (win) {
            raveVictories.put(move, raveVictories.getOrDefault(move, 0) + 1);
        }
    }
    
    public int getRaveVisits(Position move) {
        return raveVisits.getOrDefault(move, 0);
    }
    
    public double getRaveValue(Position move) {
        int visits = getRaveVisits(move);
        if (visits == 0) return 0;
        return raveVictories.getOrDefault(move, 0) / (double) visits;
    }

    /**
     * 
     * @param k la constante
     * @return une valeur calculée via la formule de RAVE
     */
    public double getRaveFormula(double k){

        int nRave = getRaveVisits(lastMove);
    
        double mcValue = (visits > 0) ? ((double) victories / visits) : 0;
        double beta = Math.sqrt(k / (3 * (double)visits + k));
        double raveValue = (nRave > 0) ? getRaveVictories(lastMove) / (double) nRave : 0;
        return (1 - beta) * mcValue + beta * raveValue + ucb();
    }

    /**
     * 
     * @return retourne la partie exploitation de la formule UCB
     */
    public double ucb(){
        if(visits == 0){
            return Double.MAX_VALUE;
        }
        return  Math.sqrt(2) *  Math.sqrt(Math.log(parent.getVisits())/(double)visits);
    }
    
    @Override
    public boolean equals(Object obj) { //attention il faut bien verifier le fait qu'un etat peut avoir plusieurs parents??
        if(obj == null || !(obj instanceof NodeRave)){
            return false;
        }

        NodeRave n = (NodeRave) obj;
        return n.getState().equals(this.state);  
    }


    @Override
    public int hashCode() { //a verifier
        return Objects.hash(state);
    }
}

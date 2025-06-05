package hex.mcts;

import java.util.*;

import hex.model.*;

public class NodeMcts {
    private int victories,visites;
    private State state;
    private List<NodeMcts> children;
    private NodeMcts parent;
    private Position lastMove;

    private List<Position> movesNotExplored;
    
    public NodeMcts(State state){
        this.victories = 0;
        this.visites = 0;
        this.state = state;
        this.children = new ArrayList<>();
        this.parent = null;
        this.lastMove = null;

        this.movesNotExplored = new ArrayList<>(this.state.getPossibleMoves());
    }

    public void setLastMove(Position pos){
        this.lastMove = pos;
    }

    public Position getLastMove(){
        return this.lastMove;
    }

    public void setParent(NodeMcts parent) {
        this.parent = parent;
    }

    public List<Position> getMovesNotExplored(){
        return this.movesNotExplored;
    }

    public void removeExploredMove(Position p){
        this.movesNotExplored.remove(p);
    }

    public List<NodeMcts> getChildren() {
        return children;
    }

    public NodeMcts getParent() {
        return parent;
    }

    public State getState() {
        return state;
    }

    public double getValue() {
        return victories / (double) visites; 
    }

    public int getVictories() {
        return victories;
    }

    public int getVisites() {
        return visites;
    }

    public void visited(){
        visites++;
    }

    public void victory(){
        victories++;
    }

    /**
     * 
     * @return la valeur calculée via la formule UCB (Upper Confidence Bound)
     */
    public double ucb(){
        if(visites == 0){
            return Double.MAX_VALUE;
        }
        return (victories / (double) visites) + 0.1 *  Math.sqrt(Math.log(parent.getVisites())/(double)visites);
    }

    public void addChild(NodeMcts child){
        this.children.add(child);
    }

    @Override
    public boolean equals(Object obj) { //attention il faut bien verifier le fait qu'un etat peut avoir plusieurs parents??
        if(obj == null || !(obj instanceof NodeMcts)){
            return false;
        }

        NodeMcts n = (NodeMcts) obj;
        return n.getState().equals(this.state);  
    }


    @Override
    public int hashCode() { //a verifier
        return Objects.hash(state);
    }


    @Override
    public String toString() {
        return "("+victories+"/"+visites+")  "; 
    }
    
}

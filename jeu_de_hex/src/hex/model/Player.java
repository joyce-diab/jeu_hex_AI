package hex.model;


public abstract class Player {

    protected Color color;


    public Player(Color color){
        this.color = color;
    }

    /**
     * 
     * @return la couleur du joueur
     */
    public Color getColor(){ 
        return color;
        
    }


    /**
     * 
     * @param board plateauu du jeu
     * @return une position choisie via son algorithme
     */
    public abstract Position play(Board board);

    public String toString(){
        return color.toString();
    }
}
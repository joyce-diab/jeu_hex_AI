package hex.model;

import java.util.Random;

import hex.rave.*;

public class RobotRave extends Player {
    private RAVE rave;
     
    public RobotRave(Color c , int budget){
        super(c);
        this.rave = new RAVE(budget, c);
    }

     public RobotRave(Color c , int budget,Random rand){
        super(c);
        this.rave = new RAVE(budget, c, rand);
    }
    
    public RobotRave(Color c){
        this(c,10000);
    }

    /* Retourne la position choisie par le robot via son algorithme RAVE*/
    @Override
    public Position play(Board board) {
        Position best = rave.getBestMove(board);
        return best;
    }
    
}

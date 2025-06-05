package hex.model;

import java.util.Random;

import hex.mcts.*;


public class RobotMCTS2 extends Player {

    private MCTS mcts;
    
    public RobotMCTS2(Color c , int budget){
        super(c);
        this.mcts = new MCTS2(budget, c);
    }

    public RobotMCTS2(Color c , int budget,Random rand){
        super(c);
        this.mcts = new MCTS2(budget, c, rand);
    }
    
    public RobotMCTS2(Color c){
        this(c,10_000);
    }

    /* Retourne la position choisie par le robot via son algorithme MCTS*/
    public Position play(Board board){
        Position best = mcts.getBestMove(board);
        return best;
    }
    
}


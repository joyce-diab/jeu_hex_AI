package hex.model;

import java.util.*;

public class Robot extends Player {


    public Robot(Color c){
        super(c);
    } 


    /* Retourne la position choisie par le robot de facon aléatoire*/
    public Position play(Board board){
        int size = board.getSize();
        Random rand = new Random();
        Position p;

        do {

            int x = rand.nextInt(size);
            int y = rand.nextInt(size);

            p = new Position(x,y);

            
        } while (!board.isAvailable(p));

        return p;
    }
    
}

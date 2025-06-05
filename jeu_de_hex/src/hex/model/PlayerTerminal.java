package hex.model;

import java.util.Scanner;


public class PlayerTerminal extends Player{

    public PlayerTerminal(Color c){
        super(c);
       
    }


/* retourne la position choisi par le joueur */
    @Override
    public Position play(Board board){

        @SuppressWarnings("resource")
        Scanner prompt = new Scanner(System.in);

        while (true) {
            System.out.println("Enter a coordinate separated with one space (example for line 1 column 0 : 1 0)");
                
            String inputPlay = prompt.nextLine().trim();

            String[] parts = inputPlay.split("\\s+");

            if(parts.length != 2){
                System.out.println("Error Wrong syntax!!! ");
                
            }else{
                
                int x = Integer.parseInt(parts[0]);
                int y = Integer.parseInt(parts[1]);
                Position pos = new Position(x,y);
                
                if(board.isAvailable(pos)){
                   // prompt.close();            /////////////////////////////////////////////////////////////////demander quoi faire si on a 2 prompt
                    return pos;
                }else{
                    System.out.println("Position invalid! ");
                }
               

            }
        }
        
    }
}
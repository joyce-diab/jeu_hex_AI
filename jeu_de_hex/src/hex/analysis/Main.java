package hex.analysis;

import java.util.Arrays;
import java.util.Random;
import hex.model.*;

public class Main {
    public static void main(String[] args) {
        
        int blueWins = 0;
        int redWins = 0;

        int rounds = 500;
        if(args.length != 4 && args.length != 6){
            System.out.println(Arrays.toString(args));
            System.out.println("Error Usage of"+args+":\n   <grid size> <startingPlayer> <budgetRED> <budgetBlue>");
            System.out.println("   <grid size> <startingPlayer> <budgetRED> <algoRED> <budgetBlue> <algoBLUE>");
            System.out.println("Make sure:\n -size, budgetRED and budgetBLUE are numbers(int)\n -startingPlayer: 'red' or 'blue'\n - algoRED and algoBLUE: 'mcts' or 'mcts2' or 'rave'");
            return;
        }
        try {

            Random rand = new Random();
            long seed = rand.nextLong();
            rand.setSeed(seed);
            int size = Integer.parseInt(args[0]);
            Color startingPlayer = args[1].toLowerCase().equals("red") ? Color.RED : Color.BLUE;
        
            Game game=null;
            if(args.length == 4){
                int budgetRED = Integer.parseInt(args[2]);
                int budgetBLUE = Integer.parseInt(args[3]);
                
                for (int i = 0; i < rounds; i++) {
                    game = new Game( size,startingPlayer ,budgetRED, budgetBLUE ,rand);
                    if( game.getWinner() == Color.RED){
                        redWins++;
                    }else{
                        blueWins += 1;
                    }
                }

                System.out.println(startingPlayer);

                if(redWins > blueWins){
                    System.out.println(""+rounds+" "+size+" "+startingPlayer+" red "+redWins+" "+budgetRED+" MCTS "+blueWins+" "+budgetBLUE+" MCTS");
    
                }else{
                    System.out.println(""+rounds+" "+size+" "+startingPlayer+" blue "+blueWins+" "+budgetBLUE+" MCTS "+redWins+" "+budgetRED+" MCTS");
                }
                
            }else{
                int budgetRED = Integer.parseInt(args[2]);
                String algoRED = args[3];
                int budgetBLUE = Integer.parseInt(args[4]);
                String algoBLUE = args[5];
                for (int i = 0; i < rounds; i++) {
                    game = new Game( size,startingPlayer ,budgetRED, algoRED, budgetBLUE, algoBLUE ,rand);
                   if( game.getWinner() == Color.RED){
                    redWins += 1;
                   }else{
                    blueWins += 1;
                   }
                }

                if(redWins > blueWins){
                    System.out.println(""+rounds+" "+size+" "+startingPlayer+" RED "+redWins+" "+budgetRED+" "+algoRED+" "+blueWins+" "+budgetBLUE+" "+algoBLUE);
    
                }else{
                    System.out.println(""+rounds+" "+size+" "+startingPlayer+" BLUE "+blueWins+" "+budgetBLUE+" "+algoBLUE+" "+redWins+" "+budgetRED+" "+algoRED);
                }
                
            }


        }catch(NumberFormatException e){
            System.err.println("Error in argument format: "+ e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }catch (Exception e){
            System.err.println("An error occurred during game excecution: "+e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
        


        

    }
}

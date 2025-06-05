package hex.analysis;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import hex.model.*;

public class Game {

    private List<Player> players;
    private Battle battle;


    public Game(int size, Color startingPlayer,int budgetRed,int budgetBlue,Random rand){
        this.players = new ArrayList<>();
        this.players.add(new RobotMCTS(Color.BLUE, budgetBlue,rand));
        this.players.add(new RobotMCTS(Color.RED, budgetRed,rand));
        this.battle = new Battle(new Board(size), players);
        this.battle.setPlayerToStart(startingPlayer);
        launch();
    }

    public Game(int size, Color startingPlayer,int budgetRed,String algoRED,int budgetBlue, String algoBLUE, Random rand){
        this.players = new ArrayList<>();
        if (algoRED.toLowerCase().equals("mcts2")){
            this.players.add(new RobotMCTS2(Color.BLUE, budgetBlue,rand));
        }else if(algoRED.toLowerCase().equals("rave")){
            this.players.add(new RobotRave(Color.BLUE, budgetBlue,rand));
        }else{
            this.players.add(new RobotMCTS(Color.BLUE, budgetBlue,rand));
        }

        if (algoRED.toLowerCase().equals("mcts2")){
            this.players.add(new RobotMCTS2(Color.RED, budgetRed,rand));
        }else if(algoRED.toLowerCase().equals("rave")){
            this.players.add(new RobotRave(Color.RED, budgetBlue,rand));
        }else{
            this.players.add(new RobotMCTS(Color.RED, budgetRed,rand));
        }
        
        
        this.battle = new Battle(new Board(size), players);
        this.battle.setPlayerToStart(startingPlayer);
        launch();
    }

    public Color getWinner(){
        return battle.getCurrentPlayer().getColor();
    }

    public void launch(){

        boolean finished = false;
        
        Board board = battle.getBoard();
        while (!finished) {

            Player player = battle.getCurrentPlayer();
            Position p = player.play(board);

            if(board.placePawn(player.getColor(),p)){

                finished = battle.gameFinished();
                board.setGameEnd(finished);
                if(!finished){
                    battle.next();
                }
                
            }

        }
    }

}

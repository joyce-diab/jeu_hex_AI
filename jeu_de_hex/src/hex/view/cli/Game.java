package hex.view.cli;

import java.util.*;

import hex.model.*;

public class Game {
    private Scanner prompt;
    private List<Player> players;
    private int size;

    private Battle battle;

    public Game(){
        this.prompt = new Scanner(System.in);
        this.players = new ArrayList<>();
        players.add(new RobotMCTS(Color.RED));
        players.add(new RobotMCTS(Color.BLUE));
        this.size = 9;
        menu();

    }

    public void menu(){
        System.out.println("Menu : ");
        System.out.println("1) Choose players (default robot vs robot) ");
        System.out.print("2) Choose size (default 9) \n");
        System.out.print("3) Lauch battle \n");
        prompter();
    }

    public void playerMenu(){
        System.out.println("1) human vs human \n");
        System.out.println("2) human vs robot \n");
        System.out.println("3) robot vs robot \n");

        String input = prompt.nextLine().trim();
        int choice = Integer.parseInt(input);

        if(choice != 1 && choice != 2 && choice != 3){
            System.out.println("Enter a valid number !");

            playerMenu();
        }else {
            createPlayers(choice);
            menu();
        }
    }

    public void createPlayers(int choice){

        switch (choice) {
            case 1:
                players.removeAll(players);
                players.add(new PlayerTerminal(Color.RED));
                players.add(new PlayerTerminal(Color.BLUE));
                break;
            case 2:
                players.removeAll(players);
                players.add(new PlayerTerminal(Color.RED));
                players.add(new RobotMCTS(Color.BLUE));
                System.out.println(players);
                break;
        
            default:
                break;
        }
    }



    public void prompter(){
        String input = prompt.nextLine().trim();
        int choice = Integer.parseInt(input);

     

        switch (choice) {
            case 1:
                playerMenu();
                break;
            case 2:
                System.out.println("Enter a valid number (>2), otherwise the default size (9) will be taken: ");
                String inputSize = prompt.nextLine().trim();
                int sizeI = Integer.parseInt(inputSize);
                this.size = sizeI > 2 ? sizeI : size;
                menu();
                break;
            case 3:
                this.battle = new Battle(new Board(this.size), players);
                launch();
                break;
        
            default:
                System.out.println("Choose a valid number");
                menu();
                break;
        }

        
        this.prompt.close();
    }

    public void launch(){

        boolean finished = false;
        
        Board board = battle.getBoard();
        while (!finished) {
            //System.out.print("\033[H\033[2J");
            //System.out.flush();
   
            board.showBoard();

            Player player = battle.getCurrentPlayer();

            System.out.println("Player "+player+": ");


            try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

            
            Position p = player.play(board);

            if(board.placePawn(player.getColor(),p)){

                finished = battle.gameFinished();
                board.setGameEnd(finished);
                if(!finished){
                    battle.next();
                }
                
            }

        }

        board.showBoard();

        System.out.println("Player : " + battle.getCurrentPlayer() + " wins !!");
    }

   
}

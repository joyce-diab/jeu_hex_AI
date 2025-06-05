package hex.model;
import java.util.*;

import hex.view.util.AbstractListenable;

public class Battle extends AbstractListenable{

    private Board board;
    private List<Player> players;
    private int currentPlayer;

    public Battle(Board board, List<Player> players){
        this.board = board;
        this.players = players;
        this.currentPlayer = 0;
    }


    /**
     * 
     * @return le joueur courrant du jeu
     */
    public Player getCurrentPlayer(){ 
        return players.get(currentPlayer);
    }

    public void setPlayerToStart(Color c){
        if(c == null){
            currentPlayer = 0;
        }
        if(!players.get(0).getColor().equals(c)){
            next();
        }
    }
    

    /**
     * 
     * @return True si le jeu est terminé et False sinon
     */
    public boolean gameFinished(){
        return board.isAWin(getCurrentPlayer().getColor());
    }

    /* Pour passer la main a l'autre joueur */
    public void next(){
        currentPlayer = currentPlayer == 0 ? 1 : 0 ;
    }

    /* notifier tous ceux qui ll'écoute l'orsqu'il y a un changement sur la grille de jeu */
    public void notifyListeners(){
        fireChanges();
    }

    public Board getBoard(){ 
        return board;
    }



}
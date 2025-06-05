package hex.view.gui;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import hex.model.*;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

public class HexFrame extends JFrame implements ActionListener{

    private JButton button;
    private HexGrid hexGrid;
    private Battle battle;
    private JLabel label;

    public HexFrame(){
        button = new JButton("Launch");
        button.addActionListener(this);
        label = new JLabel("",SwingConstants.CENTER);
        List<Player> players = new ArrayList<>();
        players.add(new RobotMCTS(Color.RED,10000));
        players.add(new RobotRave(Color.BLUE,10000));
        battle = new Battle(new Board(5), players);
        hexGrid = new HexGrid(battle);
        this.add(hexGrid,BorderLayout.CENTER);
       // this.add(button,BorderLayout.SOUTH);
        this.add(label,BorderLayout.SOUTH);
        this.setSize(1000, 500);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        launch();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == button) {
            launch();
            button.setEnabled(false);
        }
    }
    public void launch(){

        boolean finished = false;
        
        Board board = battle.getBoard();

        while (!finished) {
          
            Player player = battle.getCurrentPlayer();
    
              /* 
            try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
            */
    
            
            Position p = player.play(board);
    
            if(board.placePawn(player.getColor(),p)){
                battle.notifyListeners();
                finished = battle.gameFinished();
                board.setGameEnd(finished);
                if(!finished){
                    battle.next();
                }
                
            }
    
        }
    
            label.setText(battle.getCurrentPlayer() + " Wins");
    }

    

}


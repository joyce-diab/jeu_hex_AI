package hex.view.gui;

import javax.swing.*;

import hex.model.Battle;
import hex.model.Board;
import hex.view.util.Listener;

import java.awt.*;
import java.awt.geom.Path2D;

public class HexGrid extends JPanel implements Listener {
    private int hexSize; 
    private int gridRows; 
    private int gridCols;
    private Battle battle;

    public HexGrid(Battle battle){
        this.gridCols = battle.getBoard().getSize();
        this.gridRows = this.gridCols;
        this.battle = battle;
        this.battle.addListener(this);
    }
  
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    
        int panelWidth = getWidth();
        int panelHeight = getHeight();
        Board board = battle.getBoard();
        
        if(board.getSize()==1){
            hexSize = 50;
        }else if(board.getSize()<=3){
            hexSize = Math.min(panelWidth / (gridCols * 2), panelHeight / (gridRows * 2))-30;
        }else{
            hexSize = Math.min(panelWidth / (gridCols * 2), panelHeight / (gridRows * 2))-6;
        }
        
    
        int offsetX = (panelWidth - (int) ((gridCols + 0.5) * Math.sqrt(3) * hexSize)) / 2;
        int offsetY = (panelHeight - (int) (gridRows * 1.5 * hexSize)) / 2;
        double factor = 0;
        for (int row = 0; row < gridRows; row++) {
            
            for (int col = 0; col < gridCols; col++) {
                double x = offsetX + factor + col * Math.sqrt(3) * hexSize;
                double y = offsetY + row * 1.5 * hexSize;
                
              

               
               boolean isTopBorder = (row == 0); 
               boolean isBottomBorder = (row == gridRows - 1); 
               boolean isLeftBorder = (col == 0); 
               boolean isRightBorder = (col == gridCols - 1); 

               drawHexagon(g2d, x, y, isTopBorder, isBottomBorder, isLeftBorder, isRightBorder,row,board.getGrid()[row][col].getColorText());
           }
           factor += Math.sqrt(3) / 2 * hexSize;
       }

        
   }

   private void drawHexagon(Graphics2D g2d, double x, double y, boolean top, boolean bottom, boolean left, boolean right,int row,String c) {
       Path2D hexagon = new Path2D.Double();
       double[] xPoints = new double[6];
       double[] yPoints = new double[6];

       for (int i = 0; i < 6; i++) {
        double angle = Math.toRadians(60 * i - 30); 
        xPoints[i] = x + hexSize * Math.cos(angle);
           yPoints[i] = y + hexSize * Math.sin(angle);
       }

       hexagon.moveTo(xPoints[0], yPoints[0]);
       for (int i = 1; i < 6; i++) {
           hexagon.lineTo(xPoints[i], yPoints[i]);
       }
       hexagon.closePath();


       if(c.equals("R")){
        g2d.setColor(Color.RED);

       }else if(c.equals("B")){
        g2d.setColor(Color.BLUE);

       }else{
        g2d.setColor(Color.WHITE);

       }
       g2d.fill(hexagon);

 
       g2d.setColor(Color.BLACK);
       for (int i = 0; i < 6; i++) {
           int next = (i + 1) % 6;

           
           Stroke defaultStroke = g2d.getStroke();
           Stroke thickStroke = new BasicStroke(3); 

           if ((top && i == 4) || (top && i == 5)) { 
               g2d.setColor(Color.RED);
               g2d.setStroke(thickStroke);
           } else if ((bottom && i == 2) || (bottom && i == 1)) { 
               g2d.setColor(Color.RED);
               g2d.setStroke(thickStroke);
           } else if ((left && i == 2) || (left && i == 3)) { 
            g2d.setColor(Color.BLUE);
            g2d.setStroke(thickStroke);
        }  else if ((right && i == 0) || (right && i == 5)) { 
               g2d.setColor(Color.BLUE);
               g2d.setStroke(thickStroke);
           } else {
               g2d.setColor(Color.BLACK); 
               g2d.setStroke(defaultStroke);
           }

     
           g2d.drawLine((int) xPoints[i], (int) yPoints[i], (int) xPoints[next], (int) yPoints[next]);

          
           g2d.setStroke(defaultStroke);
       }
   }

    @Override
    public void updatedModel(Object source) {
        repaint();
    }




}
    
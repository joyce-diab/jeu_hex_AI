package hex.mcts;

import java.util.List;
import java.util.Random;


import hex.model.*;

public class MCTS2 extends MCTS{

    public MCTS2(int budget, Color playerColor,Random random){
        super(budget, playerColor, random);
    }

    public MCTS2(int budget, Color playerColor){
        this(budget, playerColor, new Random());
    }

    @Override
    /*noeud ayant le plus grand pourcentage de gain */
    public Position getBestPosition(NodeMcts node){
        NodeMcts argmax = null;
        double max = Double.MIN_VALUE;

        List<NodeMcts> children = node.getChildren();

        for(NodeMcts child : children){
            double value= child.getVictories()/(double)child.getVisites();

            if(value>max){
                max=value;
                argmax=child;
            }
        }

        if(argmax==null){
            List<Position> availablePosition = node.getMovesNotExplored();
            return availablePosition.get(random.nextInt(availablePosition.size()));
        }
        return argmax.getLastMove();
    
    }
}

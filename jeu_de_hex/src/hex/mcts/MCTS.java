package hex.mcts;

import java.util.*;

import hex.model.*;


public class MCTS {
    private Map<State, NodeMcts> nodes;
    private int budget;
    private Color playerColor;
    private Color player;
    private List<Set<Position>> red_ends;
    private List<Set<Position>> blue_ends;
    private DFS dfs;
    protected Random random;


    public MCTS(int budget, Color playerColor,Random random){
        this.budget = budget;
        this.random = random;
        this.playerColor = playerColor;
        this.dfs = new DFS();
        this.nodes = new CacheMap<>(10000);
    }

    public MCTS(int budget, Color playerColor){
        this(budget, playerColor, new Random());
    }

    /**
     * 
     * @param board la grille du jeu
     * @return la meilleur position
     */
    public Position getBestMove(Board board){
        this.player = playerColor;
        this.red_ends = board.getRedEnds();
        this.blue_ends = board.getBlueEnds();
        
        State s = new State(Board.cloneGrid(board.getGrid()));
        NodeMcts actual = new NodeMcts(s);

        for (int i = 0; i <budget; i++) {
            NodeMcts leaf = selectAndExpand(actual);
            boolean res = simulation(leaf);
            backPropagation(leaf, res, actual);
        }
        return getBestRatio(actual);
    }


     /**
     * 
     * @param node le noeud choisi
     * @return la dernière position éffectuée pour le noeud enfant qui a été visité le plus
     */
    public Position getBestPosition(NodeMcts node){
        NodeMcts argmax = null;
        double max = -Double.MIN_VALUE;

        List<NodeMcts> children = node.getChildren();

        for(NodeMcts child : children){
            double value= child.getVisites();

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

    /**
     * 
     * @param node le noeud choisi
     * @return la dernière position éffectuée pour le noeud enfant qui a le meilleur ratio (victoires/visites)
     */
    public Position getBestRatio(NodeMcts node){
        NodeMcts argmax = null;
        double max = -Double.MIN_VALUE;

        List<NodeMcts> children = node.getChildren();

        for(NodeMcts child : children){
            double value= child.getVictories()/(double)child.getVisites();

            if(value>max){
                max=value;
                argmax=child;
            }
        }

        return argmax.getLastMove();
    
    }

    

    /**
     * 
     * @param parent noeud parent
     * @return le noeud enfant ayant la plus grande valeur calculée avec UCB
     */
    public NodeMcts getBestUcbChild(NodeMcts parent){
        NodeMcts argmax = null;
        double max = -Double.MIN_VALUE;

        List<NodeMcts> children = parent.getChildren();

        for(NodeMcts child : children){
            int visites = child.getVisites();
            if( visites == 0){
                return child;
            }
            double ucb= child.ucb();


            if(ucb>max){
                max=ucb;
                argmax=child;
               
            }
        }

        return argmax;
    
    }

    /**
     * 
     * @param root le noeud racine
     * @return le qui a gagné ou le noeud qui n'a pas encore été exploré
     */
    public NodeMcts selectAndExpand(NodeMcts root){
        NodeMcts node = root;
    

        while (true) {
            
            if(isAWin(node.getState().getGrid())){
                
                return node;
            }


            List<Position> movesNotExplored = node.getMovesNotExplored();


            if(!movesNotExplored.isEmpty()){
              
                Position move = movesNotExplored.get(random.nextInt(movesNotExplored.size()));
                
                
                NodeMcts child = createChild(node,move);
                node.removeExploredMove(move);
                return child;
            }

            player = player.getOppositeColor();
           
            node = getBestUcbChild(node); 
            
            
        }
    }

    /**
     * 
     * @param parent noeud parent
     * @param move la position qui doit être colorer avec la couleur du joueur couran dans la grille
     * @return le noeud enfant du parent creér
     */
    public NodeMcts createChild(NodeMcts parent, Position move){ //equivalent a expansion 
        Color [][] grid = Board.cloneGrid(parent.getState().getGrid()); 

        grid[move.getX()][move.getY()] = player;

        State state = new State(grid);

        NodeMcts child; 

        if(nodes.containsKey(state)){
            child = nodes.get(state);
        }else{
            child = new NodeMcts(state);
            nodes.put(state,child);
        }
        
        child.setLastMove(move);
        child.setParent(parent);
        parent.addChild(child);

        return child;
    }


    /**
     * 
     * @param node le noeuds a partir du quel onn fait la simulation aléatoire
     * @return True si le joueur qui doit renvoyer la meilleur coup gagne et False sinon
     */
    private boolean simulation(NodeMcts node){

        Color[][] grid = Board.cloneGrid(node.getState().getGrid());

        Color actual = player.getOppositeColor();

       
        
        List<Position> moves = new ArrayList<>(node.getState().getPossibleMoves());
        
      
        //mieux que de verifier si isAWin parce que c est plus couteux
        while(!moves.isEmpty()){

            Position p = moves.get(random.nextInt(moves.size()));

            grid[p.getX()][p.getY()] = actual;
            moves.remove(p);
            
                
            actual = actual.getOppositeColor();

        }

        

        return  isAWin(playerColor, grid);   /// retourne vrai si le joueur gagne et faux sinon 
    }

    /**
     * 
     * @param node le noeud sur le quel on a fait la simulation
     * @param result le resultat de la simulation (True ou False)
     * @param start le noeud racine
     */
    private void backPropagation(NodeMcts node, boolean result, NodeMcts start){ 
        while(node != null ){
            
            if(result){
                node.victory();
            }
            
            node.visited();

            node = node.getParent();
            
        
        }

    }

    /**
     * 
     * @param c couleur du joueur
     * @param grid la grille
     * @return True si le joueur gagne et False sinon
     */
    public boolean isAWin(Color c, Color[][] grid){
        List<Set<Position>> playerEnds = c == Color.RED ? red_ends: blue_ends;
        return dfs.hasPath(grid, playerEnds,c);
    }

    public boolean isAWin( Color[][] grid){
        return dfs.hasPath(grid, red_ends,Color.RED) ||dfs.hasPath(grid, blue_ends,Color.BLUE);
    }


    public int getBudget() {
        return budget;
    }

    public Color getPlayerColor() {
        return playerColor;
    }

    public void setBlue_ends(List<Set<Position>> blue_ends) {
        this.blue_ends = blue_ends;
    }

    public void setRed_ends(List<Set<Position>> red_ends) {
        this.red_ends = red_ends;
    }
    
}



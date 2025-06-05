package hex.rave;

import java.util.*;
import hex.model.*;
import hex.mcts.*;

public class RAVE {
    private Map<State, NodeRave> nodes;
    private int budget;
    private Color playerColor; 
    private Color player;      
    private List<Set<Position>> redEnds;
    private List<Set<Position>> blueEnds;
    private DFS dfs;
    private Random random;

    private static final double k = 300;

    public RAVE(int budget, Color playerColor, Random random) {
        this.budget = budget;
        this.playerColor = playerColor;
        this.random = random;
        this.dfs = new DFS();
        this.nodes = new CacheMap<>(10000); 
    }

    public RAVE(int budget, Color playerColor) {
        this(budget, playerColor, new Random());
    }

    /**
     * 
     * @param board la grille du jeu
     * @return la meilleur position
     */
    public Position getBestMove(Board board) {
        this.player = playerColor;
        this.redEnds = board.getRedEnds();
        this.blueEnds = board.getBlueEnds();
        
        State s = new State(Board.cloneGrid(board.getGrid()));
        NodeRave root = new NodeRave(s);

        for (int i = 0; i < budget; i++) {
            NodeRave leaf = selectAndExpand(root);
            SimulationResult simResult = simulation(leaf);
            backPropagation(leaf, simResult);
        }
        return getBestPosition(root);
    }
    
     /**
     * 
     * @param node le noeud choisi
     * @return la dernière position éffectuée pour le noeud enfant qui a été visité le plus
     */
    private Position getBestPosition(NodeRave node) {
        NodeRave best = null;
        double maxVisits = -Double.MAX_VALUE;
        for (NodeRave child : node.getChildren()) {
            if (child.getVisits() > maxVisits) {
                maxVisits = child.getVisits();
                best = child;
            }
        }
        return best.getLastMove();
    }
    
   /**
     * 
     * @param root le noeud racine
     * @return le qui a gagné ou le noeud qui n'a pas encore été exploré
     */
    private NodeRave selectAndExpand(NodeRave root) {
        NodeRave node = root;
        while (true) {

            if (isTerminal(node.getState().getGrid())) {
                return node;
            }

            List<Position> notExplored = node.getMovesNotExplored();
            if (!notExplored.isEmpty()) {
                Position move = notExplored.get(random.nextInt(notExplored.size()));
                NodeRave child = createChild(node, move);
                node.removeExploredMove(move);
                return child;
            }

            node = getBestUcbChild(node);
            player = player.getOppositeColor();
        }
    }
    
   /**
     * 
     * @param parent noeud parent
     * @param move la position qui doit être colorer avec la couleur du joueur couran dans la grille
     * @return le noeud enfant du parent creér
     */
    private NodeRave createChild(NodeRave parent, Position move) {
        Color[][] grid = Board.cloneGrid(parent.getState().getGrid());
        grid[move.getX()][move.getY()] = player;
        State newState = new State(grid);
        
        NodeRave child;
        if (nodes.containsKey(newState)) {
            child = nodes.get(newState);
        } else {
            child = new NodeRave(newState);
            nodes.put(newState, child);
        }
        child.setLastMove(move);
        child.setParent(parent);
        parent.addChild(child);
        return child;
    }
    
   
    /**
     * 
     * @param node le noeuds a partir du quel onn fait la simulation aléatoire
     * @return le resultat de la simulation
     */
    private SimulationResult simulation(NodeRave node) {
        Color[][] grid = Board.cloneGrid(node.getState().getGrid());
        Color actual = player.getOppositeColor();
        List<Position> simulationMoves = new ArrayList<>();
        List<Position> moves = new ArrayList<>(node.getState().getPossibleMoves());
        
        while (!moves.isEmpty()) {

            Position move = moves.get(random.nextInt(moves.size()));
            grid[move.getX()][move.getY()] = actual;
            simulationMoves.add(move);
            moves.remove(move);
            actual = actual.getOppositeColor();
            
        }
        
        boolean win = isAWin(playerColor, grid);
        return new SimulationResult(win, simulationMoves);
    }
    
    /**
     * 
     * @param node le noeud sur le quel on a fait la simulation
     * @param simResult le resultat de la simulation 
     */
    private void backPropagation(NodeRave node, SimulationResult simResult) {

        boolean result = simResult.getResult();
        int taille = simResult.getMoves().size()-1;
        int t = 0;

        while (node != null) {
            if (result ) {
                node.victory();
            }
            node.visited();
            
            if(node.getParent() != null){
                for (int i = taille-t; i <= taille; i++) {
                    if(i>-1){
                        Position move = simResult.getMoves().get(i);
                        node.updateRaveStats(move, result);
                    }
                   
                }
                
                t++;
                
            }
           
            node = node.getParent();
           // result = !result; 
        }
    }
    
    /**
     * 
     * @param parent noeud parent
     * @return le noeud enfant ayant la plus grande valeur calculée avec UCB
     */
    private NodeRave getBestUcbChild(NodeRave parent) {
        NodeRave bestChild = null;
        double maxValue = -Double.MAX_VALUE;
        
        for (NodeRave child : parent.getChildren()) {
            
            if (child.getVisits() == 0) {
                return child;
            }
           
            double value = child.getRaveFormula(k);
            
            if (value > maxValue) {
                maxValue = value;
                bestChild = child;
            }
        }
        return bestChild;
    }
    
   
    /**
     * 
     * @param grid la grille
     * @return True si un des deux joueurs gagne et False sinon
     */
    private boolean isTerminal(Color[][] grid) {
        return dfs.hasPath(grid, redEnds, Color.RED) || dfs.hasPath(grid, blueEnds, Color.BLUE);
    }
    
    /**
     * 
     * @param c couleur du joueur
     * @param grid la grille
     * @return True si le joueur gagne et False sinon
     */
    private boolean isAWin(Color c, Color[][] grid) {
        List<Set<Position>> playerEnds = c == Color.RED ? redEnds : blueEnds;
        return dfs.hasPath(grid, playerEnds, c);
    }
    
    

}

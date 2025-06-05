package hex.model;

import java.util.*;

public class DFS {
    
    private Color[][] grid;
    private int size;

    public DFS(){
        
    }

    /**
     * 
     * @param grid la grille
     * @param ends les bords du joueur (south et north ou east et west)
     * @param c la couleur du joueur
     * @return Treu si le joueur a un  chemin qui rélis ses deux bords et False sinon
     */
    public boolean hasPath(Color[][] grid, List<Set<Position>> ends, Color c){
        this.grid = grid;
        this.size = grid.length;

        Set<Position> start = ends.get(0);
        Set<Position> end = ends.get(1);

        
        Set<Position> visited = new HashSet<>();

        for(Position p : start){

            int x = p.getX();
            int y = p.getY();
            
            if ( grid[x][y] == c && !visited.contains(p)){
                if (dfs(p,end,visited, c)){
                    return true;
                }
            }
        }

        return false;
    }


    private boolean dfs(Position p, Set<Position> end, Set<Position> visited, Color c){
        if( end.contains(p)){
            return true;
        }

        if(visited.contains(p)){
           
            return false;
        }

        visited.add(p);

        List<Position> neighbors = Board.getNeighbors(p,size);


        for(Position child : neighbors){
            
            int x = child.getX();
            int y = child.getY();
            if(grid[x][y] == c && dfs(child,end,visited,c)){
              
                return true;
            }
        }

        return false;
    }


}

package hex.model;

import java.util.*;


public class Board {
    
    protected Color grid[][];
    protected int size;

    private DFS pathFinder;
    private boolean gameEnd;

    private List<Set<Position>> red_ends;
    private List<Set<Position>> blue_ends;
    
    private final static int[] POS_X={0,0,1,-1,1,-1};
    private final static int[] POS_Y={1,-1,0,0,-1,1};


    /**
     * 
     * @param p position
     * @param size la taille de la grille (n*n)
     * @return True la position donnée est valide et False sinon
     */
    public static final boolean isPositionValid(Position p, int size){        
        return p.getX() >=0 && p.getX() < size && p.getY() >=0 && p.getY()<size;
    }


    /**
     * 
     * @param p position
     * @param size la taille de la grille (n*n)
     * @return la liste des positions des cases adjacente à la position donnée 
     */
    public static final List<Position> getNeighbors(Position p, int size){
        int l = POS_X.length;
        int x = p.getX();
        int y = p.getY();
        List<Position> res = new ArrayList<>();
        for (int i = 0; i < l; i++){
            Position possible_p = new Position(x+POS_X[i], y+POS_Y[i]);
            if (isPositionValid(possible_p,size)){
                res.add(possible_p);
            }
        }
        return res;
    }

    /**
     * 
     * @param grid grille 2D de même taille
     * @return un clone de la grille passé en paramètre
     */
    public static final Color[][] cloneGrid(Color[][] grid){
        int size = grid.length;
        Color[][ ] copy = new Color[size][size];

        for (int i = 0; i < size; i++) {
            System.arraycopy(grid[i], 0, copy[i], 0, size);
        }
        return copy;
    }


    public Board(int size){
        this.size = size;
        this.grid = new Color[this.size][this.size];

        this.red_ends = new ArrayList<>();
        this.blue_ends = new ArrayList<>();

        boardInit();

        this.pathFinder = new DFS();
        this.gameEnd = false;
    }

    public Board(Color [][] grid){
        this(grid.length);
        this.grid = grid;
        
    }

    public Color[][] getGrid() {
        return grid;
    }

    /* methode qui permet d'initialiser la grille et fixé les red et bleu ends  */
    public void boardInit(){
       this.grid = new Color[this.size][this.size];

       Set<Position> north = new HashSet<>();
       Set<Position> south = new HashSet<>();

       Set<Position> west = new HashSet<>();
       Set<Position> east = new HashSet<>();

        for(int i =0; i < this.size; i++){
            for(int j = 0; j < this.size; j++){
                this.grid[i][j] = Color.NONE;

                Position particular = new Position(i, j);

                if(i==0){
                    north.add(particular);
                } if(i == size-1){
                    south.add(particular);
                }
                if(j==0){
                    west.add(particular);
                } if(j == size-1){
                    east.add(particular);
                }
            }
        }

        this.red_ends.add(north);
        this.red_ends.add(south);
        this.blue_ends.add(west);
        this.blue_ends.add(east);

    }

    /**
     * 
     * @param p position
     * @return la couleur qui est a la position donnée
     */
    public Color getPawn(Position p){
        if(isPositionValid(p,size)){
            return grid[p.getX()][p.getY()];
        }
        throw new IllegalAccessError("no such position");
    }

    /**
     * 
     * @param p position
     * @return True si la case qui est à la position donnée en paramètre est libre et False sinon
     */
    public boolean isAvailable(Position p){
        int i = p.getX();
        int j = p.getY();

        if( isPositionValid(p,size) && grid[i][j]== Color.NONE){
            return true;
        }

        return false;
    }

    /**
     * 
     * @param p1 première position
     * @param p2 deuxième position
     * @return True si les 2 positions sont voisins et False sinon
     */
    public boolean areNeighbors(Position p1, Position p2){
        return getNeighbors(p1,size).contains(p2);
    }

    

    public boolean isAvailable(int x, int y){
        return isAvailable(new Position(x,y));
    }
     
    public boolean placePawn(Color color, Position p){
        if (isAvailable(p)){
            grid[p.getX()][p.getY()] = color;
            return true;
        }
        return false;
    }


    /**
     * 
     * @param color couleur
     * @param x coordonné x
     * @param y coordonné y
     * @return True si on a pu colorer la case a la position donnée avec la couleur donnée et False sinon
     */
    public boolean placePawn(Color color, int x, int y){
        return placePawn(color, new Position(x,y));
    }

    public void removePawn(Position p){
        if(isPositionValid(p,size)){
            grid[p.getX()][p.getY()] = Color.NONE;
        }
    }


    public void setGameEnd(boolean gameEnd) {
        this.gameEnd = gameEnd;
    }

    public boolean isGameEnd() {
        return gameEnd;
    }

    /**
     * 
     * @param c couleur
     * @return True le joueur avec la couleur donnée a gagné et False sinon
     */
    public boolean isAWin(Color c){
      
        List<Set<Position>> playerEnds = c == Color.RED ? red_ends: blue_ends;
        return pathFinder.hasPath(grid, playerEnds,c);
    }

    /* Pour afficher la grille du jeu */
    public void showBoard(){
        System.out.println();

        System.out.println("BLUE (Horizontal → ) RED(Vertical ↓)");
        System.out.println();
        String space = "  ";
        System.out.print("   ");
        
        for(int i = 0; i<= size; i++){
            for (int j = 0; j<= size; j++){
                if (i==0 && j ==0){
                    System.out.print(space);
                }
                else if(i == 0){
                    System.out.print(j-1+" ");
                }
                else if (j==0){
                    System.out.print(i-1+space);
                }else{
                    System.out.print(grid[i-1][j-1].getColorText()+" ");
                }
                
            }
            System.out.println();
            space +="  ";
        }

        System.out.println();
        
    }
    public int getSize() {
        return size;
    }

    public List<Set<Position>> getRedEnds(){
        return this.red_ends;
    }

    public List<Set<Position>> getBlueEnds(){
        return this.blue_ends;
    }

}

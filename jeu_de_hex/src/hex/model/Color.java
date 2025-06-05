package hex.model;

public enum Color {
    RED,
    BLUE,
    NONE;


    /**
     * 
     * @return la couleur inverse
     */
    public Color getOppositeColor(){
     if(this == RED){ return BLUE;}

     return RED;
    }

    public String getColorText(){
        return this == NONE ? "." : this == BLUE ? "B" : "R";
    }

    
}


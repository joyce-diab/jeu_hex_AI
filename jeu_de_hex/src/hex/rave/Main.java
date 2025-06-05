package hex.rave;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        List<Integer> liste = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            liste.add(i);
        }
        

        int taille = liste.size()-1;
        int t = 0;
        
        while (t <= taille) {
            System.out.println(t);
            List<Integer> res = new ArrayList<>();
            for (int i = taille-t; i <=taille; i++) {
                
               res.add(liste.get(i));
            }
            t++;
            System.out.println( res +"\n");
        }
    }
}

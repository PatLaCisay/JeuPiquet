package piquetbarreauandre;

import javax.swing.Icon;

public class Carte {
    
    private String forme; // carreaux pique etc
    private String couleur; // false-->noir true-->rouge
    private int val; //numero de la carte
    private Boolean figure; // true-->la carte est un roi/reine/valet
    private int point; //valeur de la carte
    private Icon image;
    
    public Carte(String couleur_select, int val_select, String forme_select){
        //ce constructeur permet de generer le jeu de carte en combinaison avec 
        //le constructeur de Jeu.
        
        this.couleur=couleur_select;
        this.val=val_select;
        this.forme=forme_select;

        if ((val>10)&(val<=13)){ //on repartis les points que valent les cartes et leur nombre
            this.figure=true;
            this.point=10;
        }else if (val==14){
            this.figure=false;
            this.point=14;
        }else{
            this.figure=false;
            this.point=this.val;
        }
        
    }
    
    @Override
    public String toString() {
        /* Cette methode permet d'afficher de jolies cartes dans la console :)*/
        
        String val_explicite="";
        String forme_explicite="";
        if (this.val<11){
            val_explicite=Integer.toString(this.val);
            
        }else{
            switch(this.val) { //en fonction du compteur on attribue la figure
                case 11:
                  val_explicite="J";
                  break;
                case 12:
                  val_explicite="Q";
                  break;
                case 13:
                  val_explicite="K";
                  break;                
                default:
                  val_explicite="A";
            }
        }
        switch(this.forme) { //en fonction du compteur on attribue la forme
            case "Carreau":
              forme_explicite="♦";
              break;
            case "Coeur":
              forme_explicite="♥";
              break;
            case "Pique":
              forme_explicite="♠";
              break;                
            default:
              forme_explicite="♣";
        }
        return val_explicite + forme_explicite ;
    }

    public Boolean getFigure() {
        return figure;
    }

    public int getVal() {
        return val;
    }

    public String getForme() {
        return forme;
    }

    public String getCouleur() {
        return couleur;
    }

    public int getPoint() {
        return point;
    }

    public Icon getImage() {
        return image;
    }
      
}
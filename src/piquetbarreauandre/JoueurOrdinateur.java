/*
Sous-classe de Joueur, pour l'instant le joueur joue aleatoirement
 */
package piquetbarreauandre;

import java.util.ArrayList;
import javax.swing.ImageIcon; 
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class JoueurOrdinateur extends Joueur{
   public JoueurOrdinateur() { //constructeur de l'objet joueur
        super("");
        this.nom="Ordinateur";
    }
   
   @Override
    public void ecarte(Jeu jeu,ArrayList<JLabel> bMain, JFrame frame, ImageIcon icon){
        /*Cette methode est la meme que celle de Joueur mais sans interface 
         utilisateur, tout est aleatoire.
         Entree : jeu --> liste de 8 objets Carte, c'est le talon
         modifier le systeme avec carte nulle */
        int nbr_carte=0;
        while (nbr_carte==0){
            nbr_carte=ran.nextInt(3)+1; //nbr de carte que l'ordi ecarte 1 a 3 cartes
        }
        int carte_select=0; //indice de selection de la carte dans la liste main
        int compt_carte=5; //compteur qui permet de definir le nombre de carte du talon que le joueuer peut consulter
        compt_carte=5-nbr_carte;
        while (nbr_carte>0){
            carte_select=ran.nextInt(6); //entier aleatoire de 0 a 5 (c'est la ou sont les moins bonnes cartes)
            this.main.remove(carte_select); //on retire la carte de la main
            nbr_carte--;
            
        }
        
        
        //on ajoute les nouvelles
        for (int i=0;i<5-compt_carte;i++){
            this.main.add(jeu.getJeu_complet().get(jeu.getJeu_complet().size()-1)); //on redistribue la deriniere carte du paquet au joueur
            jeu.getJeu_complet().remove(jeu.getJeu_complet().size()-1); //et on la retire du paquet

        }
        classement(); //on trie les cartes
    }
   @Override
     public Carte selectCarte(boolean change,Carte carte_jouee,ArrayList<JLabel> bMain,JFrame frame){
        selectcarte=1; //par defaut l'ordi joue sa pire carte
        Carte carte_stock;        
        if (change){ //si l'ordi ne prend pas la main
            for (int i=this.main.size(); i>0;i--){
                if(carte_jouee!=null){ //si l'ordi joue une carte en reponse a son adversaire
                    //l'ordi joue sa meilleure carte de la forme demandee s'il en a une
                    if ( this.main.get(i-1).getForme().equals(carte_jouee.getForme())){
                        selectcarte=i;
                        break;
                    }
                }
            }
        }else{ //s'il prend la main
            selectcarte=this.main.size(); //il joue sa meilleure carte
        }         
         
        carte_stock=this.main.get(selectcarte-1);
        this.main.remove(selectcarte-1);
        JOptionPane.showMessageDialog(frame,this.nom+" joue "+carte_stock.toString());
        return carte_stock;
     }
}

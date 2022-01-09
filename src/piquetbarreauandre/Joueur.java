
package piquetbarreauandre;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class Joueur {
    protected String nom;
    protected List<Carte>main=new ArrayList<Carte>() ;
    protected int score;
    protected int score_total;
    protected Scanner input = new Scanner(System.in);
    protected int point;
    protected int point_score;
    protected int sequence;
    protected int plis;
    protected int selectcarte;
    private int nbr_carte;
    private int compt_carte;
    private int index_carte; //indice de selection de la carte retiree
    private String brelan="Pas de brelan";
    private String quatorze="Pas de quatorze";
    private String talon_str;
    protected int[] brelan_val;
    protected int[] quatorze_val;
    protected Carte carte_seq;
    protected String carte_forme;
    protected Random ran= new Random();
    private Object[] choix_nbr_carte; 
    private Object[] index;
    private Object[] ouiOuNon;
    private boolean talon_check;
    private boolean erreur=true;
    private int oui;
    
    
    public Joueur(String nom) { //constructeur de l'objet joueur
        this.main.clear(); //creer une liste de carte initialement vide, c'est la main du joueur
        this.score=0; // score du joueur de cette manche
        this.score_total=0; // score total du joueur
        this.nom=nom;
        this.point=0; //valeur du point du joueur
        this.point_score=0; //nombre de carte constituant ce dit point
        this.sequence=1; //1 car une carte forme une sequence de  carte
        this.nbr_carte=0; //correspond au nombre de cartes retirees par le joueur
        this.compt_carte=5; //compteur qui sert a definir le nombre de cartes que le joueur peut consulter dans le talon
        this.carte_seq=new Carte(null,0,null); //instance vide de Carte qui servira a stocker la premiere carte de la sequence
        this.plis=0; //nombre de plis remporte par le joueur
        this.brelan="Pas de brelan";
        this.quatorze="Pas de quatorze";
        this.brelan_val=new int[]{0,0};
        this.quatorze_val=new int[]{0,0};  
        this.carte_forme="";
        this.talon_check=true; //autorise ou non le joueur a consulter les cartes du talon
        this.choix_nbr_carte = new Object[]{1, 2, 3, 4, 5}; //empeche l'utilisateur de rentrer autre chose qu'un entier de 1 à 5
        this.index = new Object[]{1,2,3,4,5,6,7,8,9,10,11,12}; //empeche les erreurs d'indexage dans le choix des cartes
        this.ouiOuNon=new Object[]{"Oui","Non"};
    }
    
    //Methodes principales//
  
    public void classement(){
        //tri les cartes de la main par ordre croissant en fonction de leur valeur
        for (int i=0; i<this.main.size()-1; i++){
            int index = i;  
            for (int j=i+1; j<this.main.size(); j++){
                if (this.main.get(j).getVal()<this.main.get(index).getVal()){ 
                    index=j;
                }
            }
            Carte min = this.main.get(index);
            this.main.set(index,this.main.get(i)); 
            this.main.set(i,min);
        }
    }
    
    public void afficherMain(ArrayList<JLabel> bMain){
        for (int i=0;i<this.main.size();i++){
            bMain.get(i).setText(String.valueOf(this.main.get(i).toString()));
            if(this.main.get(i).getCouleur().equals("Rouge")){ //si c'est une carte rouge
                bMain.get(i).setForeground(new Color(255,0,0)); //on met le texte en rouge
            }else{
                bMain.get(i).setForeground(new Color(0,0,0)); //on met le texte en noir
            }
        }
    }
    
    public void ecarte(Jeu jeu,ArrayList<JLabel> bMain, JFrame frame, ImageIcon icon){
        /*Cette methode permet au joueur correspondant de se separer d'une a 5
        cartes de son jeu et d'en recuperer le meme nombre.
        Entree : jeu --> liste de 8 objets Carte, c'est le talon. 
                 infos--> texte d'info au milieu de l'ecran
                 input--> espace de saisie 
                 bMain--> liste des boutons correspondant aux cartes du joueur
                 bOui--> bouton de navigation
                 bNon--> "" ""
        */
        while(erreur){ //empeche que l'utilisateur ferme la fenetre sans rentrer un nbr de carte
            try{
                this.nbr_carte=(Integer)JOptionPane.showInputDialog(frame,"Combien de carte voulez-vous échanger "+this.nom+" ?","",JOptionPane.PLAIN_MESSAGE,icon,this.choix_nbr_carte,"");
                erreur=false;
            }catch(java.lang.NullPointerException e){
                JOptionPane.showMessageDialog(frame,"Veuillez selectionner une carte.");
            }
        } 
        erreur=true; //reset erreur qui nous sert pour nos futurs tests
        this.compt_carte=5; //compteur qui permet de definir le nombre de carte du talon que le joueuer peut consulter
        this.talon_check=true;
        if (this.nbr_carte==5){
            this.talon_check=false;
            compt_carte=0;
        }else{
            compt_carte=5-nbr_carte;
        }
        while (nbr_carte>0){
            
            while(erreur){ //empeche que l'utilisateur ferme la fenetre sans rentrer un index de carte
                try{
                    this.index_carte=(Integer)JOptionPane.showInputDialog(frame,"Sélectionnez la carte à retirer "+this.nom+" ?","",JOptionPane.PLAIN_MESSAGE,icon,this.index,"");
                    erreur=false;
                }catch(java.lang.NullPointerException e){
                    JOptionPane.showMessageDialog(frame,"Veuillez selectionner une carte.");
                }
            } 
            erreur=true; //reset erreur qui nous sert pour nos futurs tests
            
            this.index=retire(this.index_carte,this.index);
            if (bMain.get(this.index_carte-1).isEnabled()){ //verifie que la carte n'a pas deja ete retiree
                this.nbr_carte--;
                bMain.get(this.index_carte-1).setEnabled(false); //on desactive le bouton de la carte pour pas la reutiliser
                this.main.remove(this.index_carte-1); //on supprime la carte de la liste 
            }
        }
        for (int i=0;i<this.main.size();i++){bMain.get(i).setEnabled(true);} //on reactive toutes les cartes

        
        for (int i=0;i<5-compt_carte;i++){
            this.main.add(jeu.getJeu_complet().get(jeu.getJeu_complet().size()-1)); //on redistribue la deriniere carte du paquet au joueur
            jeu.getJeu_complet().remove(jeu.getJeu_complet().size()-1); //et on la retire du paquet

        }
        
        
        afficherMain(bMain);
        JOptionPane.showMessageDialog(frame, "Voici votre nouvelle main");
        classement(); //on trie les cartes
        afficherMain(bMain);
        this.index= new Object[]{1,2,3,4,5,6,7,8,9,10,11,12} ; //on reinitisalise l'index on en aura besoin pour seletionner les cartes des plis
    }
    public void talonCheck(boolean premier_joueur,JFrame frame,Jeu jeu){
        talon_str="";
        if (premier_joueur & this.talon_check){
            oui=JOptionPane.showOptionDialog(frame,"Voulez-vous consulter le talon ?","",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,null,ouiOuNon,"");
            if (oui==0){
                for (int i=0; i<this.compt_carte & i<jeu.getJeu_complet().size(); i++){ //double verification de la borne de i pour empecher les "out Of bonds"
                     talon_str+=jeu.getJeu_complet().get(i).toString()+" ";//recupere les str des cartes du talon 
                }
                JOptionPane.showMessageDialog(frame,talon_str);             
            }
        }          
    }
    public void infoMain(JFrame frame){
        
        //Calcul de la valeur du point//
        
        
        int compt_car=0;
        int compt_tre=0;
        int compt_coe=0;
        int compt_piq=0;
        
        List<Integer> compt_stock= new ArrayList<Integer>();
        
        String forme_select="";
        int forme_select_select=0;
        boolean passe_etape=false;
        
        for (int i=0; i<this.main.size();i++){
            switch(this.main.get(i).getForme()){
                case "Carreau":
                  compt_car++;
                  if (compt_car>6){ //cas de la majorite absolue
                      forme_select="Carreau";
                      passe_etape=true; //on passe l'etape de recherche de la couleur dominante
                  }
                  break;
                case "Coeur":
                  compt_coe++;
                  if (compt_coe>6){
                      forme_select="Coeur";
                      passe_etape=true;
                  }                  
                  break;
                case "Pique":
                  compt_piq++;
                  if (compt_piq>6){
                      forme_select="Pique";
                      passe_etape=true;
                  }                  
                  break;                
                default:
                  compt_tre++;
                  if (compt_tre>6){
                      forme_select="Trèfle";
                      passe_etape=true;
                  }
            }
        }
        
        compt_stock.add(compt_car);
        compt_stock.add(compt_tre);
        compt_stock.add(compt_coe);
        compt_stock.add(compt_piq);
        
        this.point_score=Collections.max(compt_stock); //la valeur du point en terme de score est egale au nombre de carte constituant la plus grande famille
        
        if (passe_etape==false){ //cette etape est passee si la majorite absolue est atteinte
            for (int i=0; i<compt_stock.size(); i++){ //on cherche l'indice de a valeur max de la liste
                if (compt_stock.get(i).equals(Collections.max(compt_stock))){
                    forme_select_select=i;
                    break;
                }
            }
            
            switch (forme_select_select){ //en fonction de cet indice on defini la forme majoritaire
                case 0:
                    forme_select="Carreau";
                    break;
                case 1:
                    forme_select="Trèfle";
                    break;
                case 2:
                    forme_select="Coeur";
                    break;
                default:
                    forme_select="Pique";
            }
        }
        for(int i=0; i<this.main.size(); i++){
            if (this.main.get(i).getForme().equals(forme_select)){
                this.point+=this.main.get(i).getPoint();
            }
        }
        
        //calcul sequence//
        
        List<Integer> sequence_stock=new ArrayList<Integer>();
        sequence_stock.add(0); //on met 0 pour signifier le cas ou le joueur n'a pas de sequence
        int valeur=0;
        boolean sequence_reinit=false;
        
        valeur=this.main.get(0).getVal();
        carte_seq=this.main.get(0);
        for (int i=1; i<this.main.size(); i++){
            if (valeur+1==this.main.get(i).getVal()){ //si la i-eme carte a pour valeur l'entier suivant "valeur"
                this.sequence++; //la taille de la sequence augmente de 1
                valeur=this.main.get(i).getVal(); //la valeur de repere est remplace par la i-eme valeur du tableau
            }else if (valeur!=this.main.get(i).getVal()& valeur+1!=this.main.get(i).getVal() || i==this.main.size()-1){
                //si la i-eme valeur est differente de l'entier suivant "valeur" et que cette valeur est differente de "valeur"
                carte_seq=this.main.get(i);
                valeur=carte_seq.getVal(); //on change la valeur de repere
                sequence_reinit=true; //on permet la reinitialisaton de la sequence
            }
            if (this.sequence>2){
                sequence_stock.add(this.sequence); //on stock toutes les valeurs de sequence superieure ou egale a 3 dans une liste
            }
            if (sequence_reinit){ //reinitialise la valeur de la sequence
                this.sequence=1;
                sequence_reinit=false;
            }
        }
        switch(Collections.max(sequence_stock)){ //on recupere la valeur de la plus grande sequence stockee dans la liste
            
            //en fonction de la valeur de la sequence max, on attribue les points correspondants 
            case 3:
                this.sequence=3;
                break;
            case 4:
                this.sequence=4;
                break;
            case 5:
                this.sequence=15;
                break;
            case 6:
                this.sequence=16;
                break;
            case 7:
                this.sequence=17;
                break;
            case 8:
                this.sequence=18;
                break;
            default:
                this.sequence=0;
        }
        
        //calcul des quatorzes et des brelans//
        

        Integer [][] val_select=new Integer[][]{{10,0},{11,0},{12,0},{13,0},{14,0}}; //defini le nbr de carte identique par rapport a leur valeur
        List<Carte> main_signifiante= new ArrayList<Carte>();
        
        for (int i=0; i<this.main.size(); i++){
            if (this.main.get(i).getVal()>9){
                main_signifiante.add(this.main.get(i)); //on reduit la taille de la liste aux cartes qui nous interesse pour optimiser le programme
            }
        }
        
        for (int i=0; i<5; i++){
            for(int j=0; j<main_signifiante.size(); j++){
                if(val_select[i][0]==main_signifiante.get(j).getVal()){
                    val_select[i][1]++;
                }                
            }
        }
        
        for(int i=4; i>-1;i--){
            if(val_select[i][1]==4){
                this.quatorze_val[0]=14;
                switch (val_select[i][0]){
                    case 10:
                        this.quatorze="Quatorze de 10";
                        this.quatorze_val[1]=10;
                        break;
                    case 11:
                        this.quatorze="Quatorze de Valet";
                        this.quatorze_val[1]=11;
                        break;
                    case 12:
                        this.quatorze="Quatorze de Reine";
                        this.quatorze_val[1]=12;
                        break;
                    case 13:
                        this.quatorze="Quatorze de Roi";
                        this.quatorze_val[1]=13;
                        break;
                    case 14:
                        this.quatorze="Quatorze d'As";
                        this.quatorze_val[1]=14;
                        break;                        
                }
                break; //comme on commence par la fin et que les cartes sont dans l'ordre croissant des qu'on trouve un quatorze c'est le plus puissant de la main

            }
        }
        for(int i=4; i>-1;i--){
            if(val_select[i][1]==3){
                this.brelan_val[0]=3;
                switch (val_select[i][0]){
                    case 10:
                        this.brelan="Brelan de 10";
                        this.brelan_val[1]=10;
                        break;
                    case 11:
                        this.brelan="Brelan de Valet";
                        this.brelan_val[1]=11;
                        break;
                    case 12:
                        this.brelan="Brelan de Reine";
                        this.brelan_val[1]=12;
                        break;
                    case 13:
                        this.brelan="Brelan de Roi";
                        this.brelan_val[1]=13;
                        break; 
                    case 14:
                        this.brelan="Brelan d'As";
                        this.brelan_val[1]=14;
                        break;                        
                }
                break; //meme principe
            }
        }
    }
    
    public Carte selectCarte(boolean change,Carte carte_jouee,ArrayList<JLabel> bMain,JFrame frame){
        selectcarte=0;
        Carte carte_stock; 
        erreur=true;
        index_carte=0;
        while(erreur){ //empeche que l'utilisateur ferme la fenetre sans selectionner une carte
            try{
                index_carte=(Integer)JOptionPane.showInputDialog(frame,"Séléctionner la carte que vous voulez jouer "+this.nom+" ?","",JOptionPane.PLAIN_MESSAGE,null,this.index,"");
                erreur=false;
            }catch(java.lang.NullPointerException e){
                JOptionPane.showMessageDialog(frame,"Veuillez selectionner une carte.");
            }
        }
        if (bMain.get(index_carte-1).isVisible()){ //verifie que la carte n'a pas deja ete retiree
            this.index=retire(index_carte,this.index);
            nbr_carte--;
            bMain.get(index_carte-1).setEnabled(false); //on desactive le bouton de la carte pour pas la reutiliser
        }   
        this.carte_forme=this.main.get(index_carte-1).getForme();
        carte_stock=this.main.get(index_carte-1);
        this.main.set(index_carte-1,null);
        JOptionPane.showMessageDialog(frame,this.nom+" joue "+carte_stock.toString());
        return carte_stock;
    }
    
    //Methodes secondaires//    
    public Object[] retire(int nbr,Object[] obj_init){
        
        /*
        Cette methode permet de retirer les objets deja selectionnes par le joueur
        Elle est complexe car il fallait obligatoirement manipuler des Object[]
        pour que les JOptionPane fonctionnent.
        */
        
        List<Object> obj_intermediaire=new ArrayList<Object>();
        for (int i=0; i<obj_init.length;i++){
            if (Integer.valueOf(obj_init[i].toString())!=nbr){
                obj_intermediaire.add(obj_init[i]);
            }
        }
        Object[] obj_fin=new Object[obj_intermediaire.size()];
        for (int i=0;i<obj_fin.length;i++){
                obj_fin[i]=obj_intermediaire.get(i);      
        }
        return obj_fin;
    }

    public int getScore() {
        return score;
    }

    public String getNom() {
        return nom;
    }
       
    public int getPoint() {
        return point;
    }

    public int getSequence() {
        return sequence;
    }

    public String getBrelan() {
        return brelan;
    }

    public String getQuatorze() {
        return quatorze;
    }

    public int[] getBrelan_val() {
        return brelan_val;
    }

    public int[] getQuatorze_val() {
        return quatorze_val;
    }
    
    public int getPlis(){
        return this.plis;
    }

    public int getPoint_score() {
        return point_score;
    }

    public Carte getCarte_seq() {
        return carte_seq;
    }
    
    public List<Carte> getMain() {
        return main;
    }

    public void setScore(int val) {
        this.score+= val;
    }
 
    public void setPlis() {
        this.plis++;
    }

    public void setScore_total(int score) {
        this.score_total+=score;
    }

    public void setNom(String nom) {
        this.nom+=nom;
    }

    public String getCarte_forme() {
        return carte_forme;
    }

    public int getScore_total() {
        return score_total;
    }
    

    public Object[] getOuiOuNon() {
        return ouiOuNon;
    }
    
    public void joueurReinit(){
        //Reinitialise l'objet joueur
        this.main.clear(); //creer une liste de carte initialement vide, c'est la main du joueur
        this.point=0; //valeur du point du joueur
        this.point_score=0; //nombre de carte constituant ce dit point
        this.sequence=1; //1 car une carte forme une sequence de  carte
        this.nbr_carte=0; //correspond au nombre de cartes retirees par le joueur
        this.compt_carte=5; //compteur qui sert a definir le nombre de cartes que le joueur peut consulter dans le talon
        this.carte_seq=new Carte(null,0,null); //instance vide de Carte qui servira a stocker la premiere carte de la sequence
        this.plis=0; //nombre de plis remporte par le joueur
        this.brelan="Pas de brelan";
        this.quatorze="Pas de quatorze";
        this.brelan_val=new int[]{0,0};
        this.quatorze_val=new int[]{0,0};  
        this.carte_forme="";
        this.talon_check=true; //autorise ou non le joueur a consulter les cartes du talon
        this.choix_nbr_carte = new Object[]{1, 2, 3, 4, 5}; //empeche l'utilisateur de rentrer autre chose qu'un entier de 1 à 5
        this.index = new Object[]{1,2,3,4,5,6,7,8,9,10,11,12}; //empeche les erreurs d'indexage dans le choix des cartes
        this.ouiOuNon=new Object[]{"Oui","Non"};     
    }
}


package piquetbarreauandre;

import java.awt.Color;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Scanner;
import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Jeu {
    
    private List<Carte> jeu_complet = new ArrayList<Carte>();
    private int val_select=7;
    private int compteur;
    private int compteur_carte;
    private String couleur_select="Noir";
    private String stock;
    private String forme_select; //permet de selectionner une forme de carte
    private int etape; //permet de reprendre une partie a l'etape de distribution
    private boolean joueur1_gagne; //permet de verifier si le premier joueur a remporter le pli
    private boolean erreur=true; //permet de gerer les exeptions 
    private int oui;
    public Jeu(){
        this.compteur=0;
        //le jeu de carte est construit de cette maniere
        while(this.compteur<4){ //tant que ce compteur est inferieur ou egale a 3 qui correspond aux trois formes de cartes
            this.compteur_carte++;
            if (this.val_select<15){ // si la valeur attribuee a la carte est inferieur ou egale au max (14 --> As)

                switch(this.compteur) { //en fonction du compteur on attribue la forme
                    case 0: //on commence donc par trefle
                      this.forme_select="Pique";
                      break;
                    case 1:
                      this.forme_select="Trèfle";
                      break;
                    case 2:
                      this.forme_select="Coeur";
                      break;
                    case 3:
                      this.forme_select="Carreau";
                  }
                Carte carte=new Carte(this.couleur_select, this.val_select, this.forme_select); //on creer un objet carte correspondant a nos caracteristiques
                this.jeu_complet.add(carte); // on l'ajoute au jeu
                this.val_select++; //on incremente la valeur de 1 cette valeur commence a 7 et finie a 14
                
            }if (this.val_select==15){ //quand la valeur depasse 14
                this.compteur++; //le compteur de couleur augmente de 1
                if (this.compteur==2){couleur_select="Rouge";}  //si le compteur vaut 2 on change la couleur
                this.val_select=7; //on reinitialise la valeur de la carte
            }
        }
    }

    
    public void melange(){ //melange le jeu
        Collections.shuffle(this.jeu_complet);
    }

    public List<Carte> getJeu_complet() {
        return jeu_complet;
    }
    
    public void distribue(Joueur joueur1, Joueur joueur2){
        for (int i=0; i<8; i++){ //8 actions pour distribuer 4 fois 3 cartes aux 2 joueurs
            switch(i%2) { //en fonction de la parite du compteur on attribue le joueur auquel on distribue les cartes
                case 0:
                    for (int j=0;j<3;j++){ 
                        joueur1.getMain().add(this.jeu_complet.get(j)); //ajoute la premiere carte du talon au joueur
                        this.jeu_complet.remove(j); //retire la carte du paquet
                    }
                    break;
                default:
                    for (int j=0;j<3;j++){ //meme systeme
                        joueur2.getMain().add(this.jeu_complet.get(j));
                        this.jeu_complet.remove(j);
                    }                  
            }
        }
        joueur1.classement(); //on trie les cartes par ordre croissant
        joueur2.classement();
    }
    
    public void annonce(Joueur joueur1, Joueur joueur2,JFrame frame,JLabel score1,JLabel score2){
        /*cette procedure sert a simuler un discours entre les joueurs et leurs
         annonces. Elle sert aussi a augmenter leur score en fonction de leurs 
         combinaisons.
        */

       JOptionPane.showMessageDialog(frame,"Le point de "+joueur1.getNom()+" est: "+joueur1.getPoint()+"\n"+"Le point de "+joueur2.getNom()+" est: "+joueur2.getPoint());
        
        //point//
        
        if (joueur1.getPoint()>joueur2.getPoint()){
            if (joueur2.nom!="Ordinateur"){
                JOptionPane.showMessageDialog(frame,joueur2.getNom()+" votre point est inferieur à celui de votre adversaire.");           
            }
            JOptionPane.showMessageDialog(frame,joueur1.getNom()+" gagne "+""+joueur1.getPoint_score()+""+" points.");
            joueur1.setScore(joueur1.getPoint_score());
        }else if (joueur1.getPoint()<joueur2.getPoint()){
            
            if (!joueur2.nom.equals("Ordinateur")){
                JOptionPane.showMessageDialog(frame,joueur2.getNom()+" votre point est superieur à celui de votre adversaire.");
            }
            
            JOptionPane.showMessageDialog(frame,joueur2.getNom()+" gagne "+joueur2.getPoint_score()+""+" points.");
            joueur2.setScore(joueur2.getPoint_score());
        }else if (joueur1.getPoint()==joueur2.getPoint()){
            JOptionPane.showMessageDialog(frame,"Points égaux, c'est fou comme la vie est bien faite !");
        }
        score1.setText(String.valueOf(joueur1.getScore()));
        score2.setText(String.valueOf(joueur2.getScore()));
        // sequence //
        
        JOptionPane.showMessageDialog(frame,"Séquence de "+joueur1.getNom()+" : "+joueur1.getSequence()+"\n"+"Séquence de "+joueur2.getNom()+" : "+joueur2.getSequence());
        
        if (joueur1.getSequence()>joueur2.getSequence()){
            stock=joueur2.getNom()+" votre sequence est inferieure à celle de votre adversaire."+"\n"+joueur1.getNom()+" gagne "+joueur1.getSequence()+" points.";
            joueur1.setScore(joueur1.getSequence()); //on augmente son score
        }else if (joueur1.getSequence()<joueur2.getSequence()){
            stock=joueur1.getNom()+" votre sequence est inferieure à celle de votre adversaire."+"\n"+joueur2.getNom()+" gagne "+joueur2.getSequence()+" points.";
            joueur2.setScore(joueur2.getSequence()); //on augmente son score
        }else if (joueur1.getSequence()==joueur2.getSequence()){
                stock="La séquence de "+joueur1.getNom()+" est issue de "+joueur1.getCarte_seq()+"\n"+"La séquence de "+joueur2.getNom()+" est issue de "+joueur2.getCarte_seq()+"\n";
            if (joueur1.getCarte_seq().getVal()>joueur2.getCarte_seq().getVal()){
                stock+=joueur1.getNom()+" gagne "+joueur1.getSequence()+" points.";
                joueur1.setScore(joueur1.getSequence()); //on augmente son score
            }else{
               stock+=joueur2.getNom()+" gagne "+joueur2.getSequence()+" points.";
               joueur2.setScore(joueur2.getSequence()); //on augmente son score
            }
        }
        score1.setText(String.valueOf(joueur1.getScore()));
        score2.setText(String.valueOf(joueur2.getScore()));
        JOptionPane.showMessageDialog(frame,stock);
        //brelan//
        
        if (joueur1.getBrelan_val()[1]>joueur2.getBrelan_val()[1]){
            stock=joueur2.getNom()+" votre brelan est inferieur à celui de votre adversaire."+"\n"+joueur1.getNom()+" gagne "+joueur1.getBrelan_val()[0]+" points.";
            joueur1.setScore(joueur1.getBrelan_val()[0]); //on augmente son score
        }else if (joueur1.getBrelan_val()[1]<joueur2.getBrelan_val()[1]){
            stock=joueur2.getNom()+" votre brelan est superieure à celui de votre adversaire."+"\n" +joueur2.getNom()+" gagne "+joueur2.getBrelan_val()[0]+" points.";
           
            joueur2.setScore(joueur2.getBrelan_val()[0]); //on augmente son score 
        }else if (joueur1.getBrelan_val()[1]==joueur2.getBrelan_val()[1]){
            stock="Vos brelans sont les mêmes, ils s'annulent mutuellement.";
        }
        score1.setText(String.valueOf(joueur1.getScore()));
        score2.setText(String.valueOf(joueur2.getScore()));
        JOptionPane.showMessageDialog(frame,stock);
        //quatorze//
        
        if (joueur1.getQuatorze_val()[1]>joueur2.getQuatorze_val()[1]){
            stock=joueur2.getNom()+" votre Quatorze est inferieur à celui de votre adversaire."+"\n"+joueur1.getNom()+" gagne "+joueur1.getQuatorze_val()[0]+" points.";
            joueur1.setScore(joueur1.getQuatorze_val()[0]); //on augmente son score
        }else if (joueur1.getQuatorze_val()[1]<joueur2.getQuatorze_val()[1]){
            stock=joueur2.getNom()+" votre Quatorze est superieure à celui de votre adversaire."+"\n"+joueur2.getNom()+" gagne "+joueur2.getQuatorze_val()[0]+" points.";
            joueur2.setScore(joueur2.getQuatorze_val()[0]); //on augmente son score
        }else if (joueur1.getQuatorze_val()[1]==joueur2.getQuatorze_val()[1]){
            stock="Vos Quatorzes sont les mêmes, ou bien vous n'en avez pas"+"\n" +"tout les deux, ils s'annulent mutuellement.";
        }
        JOptionPane.showMessageDialog(frame,stock);
        score1.setText(String.valueOf(joueur1.getScore()));
        score2.setText(String.valueOf(joueur2.getScore()));
    }
    
    public void pli(Joueur joueur1,Joueur joueur2,ArrayList<JLabel> bMain1,ArrayList<JLabel> bMain2,JFrame frame,JLabel lCarte1,JLabel lCarte2,int choix,boolean partie,JPanel pMainJoueur1,JPanel pMainJoueur2){
        
        // Cette procedure correspond a la phase de jeu des cartes ! //
        
        Carte carte1;
        Carte carte2;
        if (choix==0){ //initialise qui joue en premier en fonction de choix qui vaut 0 ou 1 dans main
            joueur1_gagne=true;
        }
        if (joueur1_gagne){ //si le joueur qui commence a jouer a gagne le pli
            if (partie){
                pMainJoueur2.setVisible(false);
                JOptionPane.showMessageDialog(frame, "Tour de "+joueur1.getNom());
                pMainJoueur1.setVisible(true);
            }
            
            carte1=joueur1.selectCarte(joueur1_gagne,null,bMain1,frame);
            if(carte1.getCouleur().equals("Rouge")){ //si c'est une carte rouge
                lCarte1.setForeground(new Color(255,0,0)); //on met le texte en rouge
            }else{
                lCarte1.setForeground(new Color(0,0,0)); //on met le texte en noir
            }             
            lCarte1.setText(carte1.toString());
            
            if (partie){
                pMainJoueur1.setVisible(false);
                JOptionPane.showMessageDialog(frame, "Tour de "+joueur2.getNom());
                pMainJoueur2.setVisible(true);
            }
            JOptionPane.showMessageDialog(frame,joueur2.getNom()+" vous devez joueur un "+ carte1.getForme()+"\n"+" sinon vous perdez le pli.");
            carte2=joueur2.selectCarte(joueur1_gagne,carte1,bMain2,frame);
            if(carte2.getCouleur().equals("Rouge")){ //si c'est une carte rouge
                lCarte2.setForeground(new Color(255,0,0)); //on met le texte en rouge
            }else{
                lCarte2.setForeground(new Color(0,0,0)); //on met le texte en noir
            }             
            lCarte2.setText(carte2.toString());
            if (carte1.getVal()>carte2.getVal() || carte1.getForme()!=carte2.getForme()){
                stock=joueur1.getNom()+" remporte le pli et prend la main !";
                joueur1.setPlis(); //le nombre de pli remporte augmente de 1
                joueur1_gagne=true; //on pose que c'est le joueur1 qui reprend la main
            }else{
                stock=joueur2.getNom()+" remporte le pli et prend la main !";
                joueur2.setPlis();
                joueur1_gagne=false; //on pose que c'est le joueur 2 qui reprend la main
            }
            JOptionPane.showMessageDialog(frame,stock);
        }else{
            if (partie){
                pMainJoueur1.setVisible(false);
                JOptionPane.showMessageDialog(frame, "Tour de "+joueur2.getNom());
                pMainJoueur2.setVisible(true);
            }            
            carte2=joueur2.selectCarte(joueur1_gagne,null,bMain2,frame);
            if(carte2.getCouleur().equals("Rouge")){ //si c'est une carte rouge
                lCarte2.setForeground(new Color(255,0,0)); //on met le texte en rouge
            }else{
                lCarte2.setForeground(new Color(0,0,0)); //on met le texte en noir
            } 
            lCarte2.setText(carte2.toString());
            JOptionPane.showMessageDialog(frame,joueur1.getNom()+" vous devez joueur un "+ carte2.getForme()+"\n"+" sinon vous perdez le pli.");
            if (partie){
                pMainJoueur2.setVisible(false);
                JOptionPane.showMessageDialog(frame, "Tour de "+joueur1.getNom());
                pMainJoueur1.setVisible(true);
            }            
            carte1=joueur1.selectCarte(joueur1_gagne,null,bMain1,frame);
            lCarte1.setText(carte1.toString());
            if(carte1.getCouleur().equals("Rouge")){ //si c'est une carte rouge
                lCarte1.setForeground(new Color(255,0,0)); //on met le texte en rouge
            }else{
                lCarte1.setForeground(new Color(0,0,0)); //on met le texte en noir
            }            
            if (carte2.getVal()>carte1.getVal() || carte2.getForme()!=carte1.getForme()){
                stock=joueur2.getNom()+" remporte le pli et prend la main !";
                joueur2.setPlis(); //le nombre de pli remporte augmente de 1
                joueur1_gagne=false; //on pose que c'est le joueur1 qui reprend la main
            }else{
                stock=joueur1.getNom()+" remporte le pli et prend la main !";
                joueur1.setPlis();
                joueur1_gagne=true; //on pose que c'est le joueur 2 qui reprend la main
            }
            JOptionPane.showMessageDialog(frame,stock);
        }
        lCarte1.setText(null);
        lCarte2.setText(null);

    }
    
    public int finDeManche(Joueur joueur1, Joueur joueur2,JFrame frame){
        JOptionPane.showMessageDialog(frame, "Score de "+joueur1.getNom()+" : "+joueur1.getScore()+"\n"+"Score de "+joueur2.getNom()+" : "+joueur2.getScore());
        if (joueur1.getScore()>joueur2.getScore()){
            stock=joueur1.getNom()+" gagne la manche !";
        }else{
            stock=joueur2.getNom()+" gagne la manche !";
        }
        
        stock+="\n"+"Voulez-vous rejouer ? 0-->non 1-->oui";
        while(erreur){ //empeche que l'utilisateur ferme la fenetre sans rentrer un index de carte
            try{
                oui=JOptionPane.showOptionDialog(frame,"Voulez vous refaire une manche ?","",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,null,joueur1.getOuiOuNon(),"");
                erreur=false;
            }catch(java.lang.NullPointerException e){
                JOptionPane.showMessageDialog(frame,"Faites un choix svp.");
            }
        } 
        erreur=true; //reset erreur qui nous sert pour nos futurs tests        
        
        switch (oui){
            case 0 : //l'utilisateur veut jouer une nouvelle manche
                etape=0;//on reprend a l'etape de distribution
                jeuReinit();
                joueur1.joueurReinit();
                joueur2.joueurReinit();
                break;            
            default : //l'utilisateur veut arreter
                etape=12; //valeur sans le moindre sens, elle est juste differente de 0
                stock="Score total de "+joueur1.getNom()+" : "+joueur1.getScore_total()+""+"\n"+"Score total de "+joueur2.getNom()+" : "+joueur2.getScore_total()+""+"\n";
                if(joueur1.getScore_total()>joueur2.getScore_total()){
                    stock+=joueur1.getNom()+" remporte la partie !";
                }else if(joueur1.getScore_total()<joueur2.getScore_total()){
                    stock+=joueur2.getNom()+" remporte la partie !";
                }else{
                    stock+="Egalité ! Bien joué à vous !";
                }
                JOptionPane.showMessageDialog(frame,stock);
                break;
        }
        return etape;
    }
    public void jeuReinit(){
        
        //Reinitialise le jeu
        this.compteur=0;
        this.compteur_carte=0;
        while(this.compteur<4){ //tant que ce compteur est inferieur ou egale a 3 qui correspond aux trois formes de cartes
            if (this.val_select<15){ // si la valeur attribuee a la carte est inferieur ou egale au max (14 --> As)

                switch(this.compteur) { //en fonction du compteur on attribue la forme
                    case 0: //on commence donc par trefle
                      this.forme_select="Pique";
                      break;
                    case 1:
                      this.forme_select="Trèfle";
                      break;
                    case 2:
                      this.forme_select="Coeur";
                      break;
                    case 3:
                      this.forme_select="Carreau";
                  }
                Carte carte=new Carte(this.couleur_select, this.val_select, this.forme_select); //on creer un objet carte correspondant a nos caracteristiques
                this.jeu_complet.add(carte); // on l'ajoute au jeu
                this.val_select++; //on incremente la valeur de 1 cette valeur commence a 7 et finie a 14
                this.compteur_carte++;
            }if (this.val_select>14){ //quand la valeur depasse 14
                this.compteur++; //le compteur de couleur augmente de 1
                if (this.compteur==2){couleur_select="Rouge";}  //si le compteur vaut 2 on change la couleur
                this.val_select=7; //on reinitialise la valeur de la carte
            }
            melange();
        }        
    }
    public boolean isChange() {
        return joueur1_gagne;
    }  
}

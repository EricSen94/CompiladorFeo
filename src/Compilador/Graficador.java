/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Compilador;

import java.awt.Color;
import java.awt.Graphics;
import static java.lang.Thread.sleep;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;

/**
 *
 * @author ESML1
 */
public class Graficador extends JPanel {
    String matrizDibujo[][];
    public Graficador() {
        this.setBackground(Color.white);
        this.setBounds(0,0,250,500);
    }
    public void addMatriz(String matrizD[][]){
        if(matrizDibujo==null){
            matrizDibujo=matrizD;
        }
        else{
            int tamanio=matrizDibujo.length;
            String temp[][];
            temp=matrizDibujo;
            int i;
            //Agregamos los nuevos valores a la matriz
            for(i=tamanio+1; i<=matrizD.length; i++){
                temp[i][0]=matrizDibujo[i][0];
            }
            this.matrizDibujo=temp;
        }
    }
    public String checador(String matriz2D[][]){
        String bandera="NP";
        this.matrizDibujo=matriz2D;
        int tamaño = matrizDibujo.length;
        
        int matrizColiciones[][] =  new int[tamaño][8];
        for(int h=0;h<=tamaño;h++){
                matrizColiciones[h][0]=Integer.parseInt(matrizDibujo[h][0]);//x1
                matrizColiciones[h][1]=Integer.parseInt(matrizDibujo[h][1]);//y1
                matrizColiciones[h][2]=Integer.parseInt(matrizDibujo[h][0])+Integer.parseInt(matrizDibujo[h][2]);
                matrizColiciones[h][3]=Integer.parseInt(matrizDibujo[h][1])+Integer.parseInt(matrizDibujo[h][2]);
                matrizColiciones[h][4]=Integer.parseInt(matrizDibujo[h][0]);
                matrizColiciones[h][5]=Integer.parseInt(matrizDibujo[h][1]+Integer.parseInt(matrizDibujo[h][2]));
                matrizColiciones[h][6]=Integer.parseInt(matrizDibujo[h][0]);
                matrizColiciones[h][7]=Integer.parseInt(matrizDibujo[h][1])+Integer.parseInt(matrizDibujo[h][2]);    
        }
        for(int i=0;i<=tamaño;i++){
            if(i==tamaño) break;
            else{
                if(matrizColiciones[i+1][0]>=matrizColiciones[i][0]&&matrizColiciones[i+1][2]>=matrizColiciones[i][0] 
                   && matrizColiciones[i+1][4]<=matrizColiciones[i][0] && matrizColiciones[i+1][0] >= matrizColiciones[i][0] 
                   && matrizColiciones[i+1][1]>=matrizColiciones[i][1]&&matrizColiciones[i+1][3]>=matrizColiciones[i][1]
                   && matrizColiciones[i+1][7]<=matrizColiciones[i][1]&&matrizColiciones[i+1][7]>=matrizColiciones[i][1]){
                    bandera="colicion";
                    break;
                }
            }
        }
        return bandera;
    }
    @Override
    public void paint(Graphics g){
        super.paint(g); 
        
        int aux1;
        int aux2;
        int aux3;
        int tamaño = matrizDibujo.length;
        while(true){
                try{
                     sleep(1);
                }
                 catch (InterruptedException ex) {
                     Logger.getLogger(Graficador.class.getName()).log(Level.SEVERE, null, ex);
                 }
        int i=0;
        
        while(matrizDibujo[i][0]!=null){
            //duerme como se indique, lo toma de la pos 4, espera los segundos guardados en la pos 0
             if(matrizDibujo[i][4]=="sleep"){
                 try{
                     sleep(Integer.parseInt(matrizDibujo[i][0]));}
                 catch (InterruptedException ex) {
                     Logger.getLogger(Graficador.class.getName()).log(Level.SEVERE, null, ex);
                 }
            }
             //revisa si no se debe borrar una cara, revisa la poscicion 4 y reccorre eliminando, en la pos 0 esta el nombre de la cara a borrar
             if(matrizDibujo[i][4]=="delete"){
                 for(int h=0;h<tamaño;h++){
                     //Buscamos en toda la matriz algun valor de dibujar cara en la posicion 3
                     if(matrizDibujo[h][3]==matrizDibujo[i][0]){
                         int aux4=h;
                         while(matrizDibujo[aux4+1][0]!=null){
                            matrizDibujo[aux4][0]=matrizDibujo[aux4+1][0];
                            matrizDibujo[aux4][1]=matrizDibujo[aux4+1][1];
                            matrizDibujo[aux4][2]=matrizDibujo[aux4+1][2];
                            matrizDibujo[aux4][3]=matrizDibujo[aux4+1][3];
                            matrizDibujo[aux4][4]=matrizDibujo[aux4+1][4];
                            aux4++;
                         }
                         matrizDibujo[aux4]=null;
                     }
                 }
                 if(matrizDibujo[i][4]=="change"){
                     //Buscamos en toda la matriz el valor que vamos a cambiar
                     for(int h=0;h<i;h++){
                         //Buscamos las caras en los métodos de draw en donde coincida la cara
                         if(matrizDibujo[h][3]==matrizDibujo[i][1] && h!=i){
                             matrizDibujo[h][3]=matrizDibujo[i][0];
                         }
                     }
                 }
             //Para no hacer las siguientes comparaciones de if
             break;
             }
             //En caso que sea draw
            if(matrizDibujo[i][4]=="feliz"){
               
                g.setColor(Color.YELLOW);
                aux1 = Integer.parseInt(matrizDibujo[i][0]);
                aux2 = Integer.parseInt(matrizDibujo[i][1]);
                aux3 = Integer.parseInt(matrizDibujo[i][2]);
                g.fillOval(aux1,aux2 ,aux3*2 ,aux3*2);
                g.setColor(Color.BLACK);
                g.drawString(matrizDibujo[i][3],aux1+aux3/2,aux2+aux3/3);
                g.drawArc(aux1+(aux3/3),aux2+(aux3/3),(aux3/3)+aux3,(aux3/3)+aux3, 180, 180);
                g.fillOval(aux1+aux3/2,aux2+aux3/2 ,aux3/4 ,aux3/4);
                g.fillOval(aux1+(aux3+aux3/4),aux2+aux3/2 ,aux3/4 ,aux3/4);
            }
            if(matrizDibujo[i][4]=="triste"){
               
                g.setColor(Color.YELLOW);
                aux1 = Integer.parseInt(matrizDibujo[i][0]);
                aux2 = Integer.parseInt(matrizDibujo[i][1]);
                aux3 = Integer.parseInt(matrizDibujo[i][2]);
                g.fillOval(aux1,aux2 ,aux3*2 ,aux3*2);
                g.setColor(Color.BLACK);
                g.drawString(matrizDibujo[i][3],aux1+aux3/2,aux2+aux3/3);
                g.drawArc(aux1+(aux3/3),aux2+(aux3),(aux3/3)+aux3,(aux3/3)+aux3, 180, -180);
                g.fillOval(aux1+aux3/2,aux2+aux3/2 ,aux3/4 ,aux3/4);
                g.fillOval(aux1+(aux3+aux3/4),aux2+aux3/2 ,aux3/4 ,aux3/4);
            }
            if(matrizDibujo[i][4]=="dormida"){
                
                g.setColor(Color.YELLOW);
                aux1 = Integer.parseInt(matrizDibujo[i][0]);
                aux2 = Integer.parseInt(matrizDibujo[i][1]);
                aux3 = Integer.parseInt(matrizDibujo[i][2]);
                g.fillOval(aux1,aux2 ,aux3*2 ,aux3*2);
                g.setColor(Color.BLACK);
                g.drawString(matrizDibujo[i][3],aux1+aux3/2,aux2+aux3/3);
                g.drawArc(aux1+aux3/2,aux2+(aux3),aux3/4,aux3/4, 180, 180);
                g.drawArc(aux1+(aux3+aux3/4),aux2+(aux3),aux3/4,aux3/4, 180, 180);
            }
            if(matrizDibujo[i][4]=="neutral"){
                
                g.setColor(Color.YELLOW);
                aux1 = Integer.parseInt(matrizDibujo[i][0]);
                aux2 = Integer.parseInt(matrizDibujo[i][1]);
                aux3 = Integer.parseInt(matrizDibujo[i][2]);
                g.fillOval(aux1,aux2 ,aux3*2 ,aux3*2);
                g.setColor(Color.BLACK);
                g.drawString(matrizDibujo[i][3],aux1+aux3/2,aux2+aux3/3);
                g.drawLine(aux1+aux3/2, aux2+aux3,aux1+ aux3 + aux3/2, aux2+aux3);
                g.fillOval(aux1+aux3/2,aux2+aux3/2 ,aux3/4 ,aux3/4);
                g.fillOval(aux1+(aux3+aux3/4),aux2+aux3/2 ,aux3/4 ,aux3/4);
           }
            if(matrizDibujo[i][4]=="enojada"){
                
                g.setColor(Color.YELLOW);
                aux1 = Integer.parseInt(matrizDibujo[i][0]);
                aux2 = Integer.parseInt(matrizDibujo[i][1]);
                aux3 = Integer.parseInt(matrizDibujo[i][2]);
                g.fillOval(aux1,aux2 ,aux3*2 ,aux3*2);
                g.setColor(Color.BLACK);
                g.drawString(matrizDibujo[i][3],aux1+aux3/2,aux2+aux3/3);
                g.drawLine(aux1+aux3/2, aux2+aux3/3,aux1+ aux3,aux2 + aux3/2);
                g.drawLine(aux1+ aux3, aux2+aux3/2,aux1+ aux3 + aux3/2,aux2 + aux3/3);
                g.drawLine(aux1+aux3/2, aux2+aux3,aux1+ aux3 + aux3/2, aux2+aux3);
                g.fillOval(aux1+aux3/2,aux2+aux3/2 ,aux3/4 ,aux3/4);
                g.fillOval(aux1+(aux3+aux3/4),aux2+aux3/2 ,aux3/4 ,aux3/4);
           }
            
            i++;
        }
        
        
        
        
        }
    }
    @Override
    public void repaint(){
        
    }
}
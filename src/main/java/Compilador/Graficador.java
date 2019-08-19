/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Compilador;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;

/**
 *
 * @author ESML1
 */
public class Graficador extends JPanel {
    public void Graficador() {
        this.setBackground(Color.white);
        this.setBounds(0,0,250,500);
    }
    @Override
    public void paint(Graphics g){
        super.paint(g); 
        g.setColor(Color.BLACK);
        g.fillRect(50,100,50,100);
        
    }
    @Override
    public void repaint(){
        
    }
}


    

  
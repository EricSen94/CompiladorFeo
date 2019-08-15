/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Compilador;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;

/**
 *
 * @author ESML1
 */
public class Graficador extends Canvas {
    public void Graficador() {
        this.setBackground(Color.white);
        this.setBounds(0,0,50,150);
    }
    @Override
    public void paint(Graphics g){
        super.paint(g);
        g.setColor(Color.yellow);
        g.drawLine(0,0,100,200);
        g.drawOval(10,10,25,25);
    }
    @Override
    public void repaint(){
        
    }
}

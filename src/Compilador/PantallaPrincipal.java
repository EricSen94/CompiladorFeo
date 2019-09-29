/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Compilador;

import java.io.*;
import Interprete.Interprete;
import java.util.Scanner;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 *
 * @author ESML1
 */
public class PantallaPrincipal extends javax.swing.JFrame {
    //inicializacion de manjeo de archivos y graficos
    Interprete inter;
    JFileChooser seleccionar;
    File archivo;
    FileInputStream entrada;
    FileOutputStream salida;
    Graficador graficator;
    
    public PantallaPrincipal() {
        initComponents();
        seleccionar = new JFileChooser();
        archivo = new File("");
        graficator = null;
    }
    public String getContenido(){
        return Texto.getText();
    }
    public void setGraficador(Graficador g){
        graficator = g;
    }
    public String AbrirArchivo(File Archivo){
        String documento="";
        try{
            entrada =  new FileInputStream(Archivo);
            documento = new Scanner(entrada).useDelimiter("\\A").next();
            entrada.close();
            
        }catch (IOException e){
            return e.getMessage();
        }
        return documento;
    }
    public String GuardarArchivo(File Archivo,String Documento){
        String mensaje=null;
        try{
            salida = new FileOutputStream(archivo);
            byte[] bytxt=Documento.getBytes();
            salida.write(bytxt);
            mensaje="Archivo Guardado";
        } catch(Exception e){
            
        }
        return mensaje;
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        Texto = new javax.swing.JTextArea();
        Lienzo = new java.awt.ScrollPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        ErroresL = new javax.swing.JTextArea();
        jScrollPane3 = new javax.swing.JScrollPane();
        ErroresSin = new javax.swing.JTextArea();
        jScrollPane4 = new javax.swing.JScrollPane();
        ErroresSem = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        abrir = new javax.swing.JMenuItem();
        guardar = new javax.swing.JMenuItem();
        guardarComo = new javax.swing.JMenuItem();
        ejecutation = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        Texto.setColumns(20);
        Texto.setRows(5);
        jScrollPane1.setViewportView(Texto);

        Lienzo.setMinimumSize(new java.awt.Dimension(250, 500));

        ErroresL.setEditable(false);
        ErroresL.setColumns(20);
        ErroresL.setRows(5);
        jScrollPane2.setViewportView(ErroresL);

        ErroresSin.setEditable(false);
        ErroresSin.setColumns(20);
        ErroresSin.setRows(5);
        jScrollPane3.setViewportView(ErroresSin);

        ErroresSem.setEditable(false);
        ErroresSem.setColumns(20);
        ErroresSem.setRows(5);
        jScrollPane4.setViewportView(ErroresSem);

        jLabel1.setText("Errores Lexico");

        jLabel2.setText("Errores Sintactico");

        jLabel3.setText("Errores Semantico");

        jMenu1.setText("Archivo");

        abrir.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        abrir.setText("Abrir");
        abrir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                abrirActionPerformed(evt);
            }
        });
        jMenu1.add(abrir);

        guardar.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        guardar.setText("Guardar");
        guardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                guardarActionPerformed(evt);
            }
        });
        jMenu1.add(guardar);

        guardarComo.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        guardarComo.setText("Guardar como");
        guardarComo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                guardarComoActionPerformed(evt);
            }
        });
        jMenu1.add(guardarComo);

        jMenuBar1.add(jMenu1);

        ejecutation.setText("Ejecutar");
        ejecutation.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ejecutationMouseClicked(evt);
            }
        });
        ejecutation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ejecutationActionPerformed(evt);
            }
        });
        ejecutation.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                ejecutationKeyPressed(evt);
            }
        });
        jMenuBar1.add(ejecutation);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 257, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 294, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addGap(0, 220, Short.MAX_VALUE))
                            .addComponent(jScrollPane4)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 237, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Lienzo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(Lienzo, javax.swing.GroupLayout.PREFERRED_SIZE, 347, Short.MAX_VALUE)
                    .addComponent(jScrollPane1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void abrirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_abrirActionPerformed
        if(seleccionar.showDialog(null,"Abrir")==JFileChooser.APPROVE_OPTION){
            archivo=seleccionar.getSelectedFile();
            if(archivo.canRead()){
                if(archivo.getName().endsWith("txt")){
                    String documento=AbrirArchivo(archivo);
                    Texto.setText(documento);
                }else{
                    JOptionPane.showMessageDialog(null,"Archivo no compatible");
                }
            }
        }
    }//GEN-LAST:event_abrirActionPerformed

    private void guardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_guardarActionPerformed
        if(!"".equals(archivo.getPath()))
            GuardarArchivo(archivo, Texto.getText());
        else
            JOptionPane.showMessageDialog(null,"Archivo vac�o");
    }//GEN-LAST:event_guardarActionPerformed

    private void guardarComoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_guardarComoActionPerformed
        if( !"".equals(Texto.getText()) ){
            if(seleccionar.showDialog(null,"Guardar")==JFileChooser.APPROVE_OPTION){
                archivo = seleccionar.getSelectedFile();
                if(archivo.getName().endsWith("txt")){
                    String Documento = Texto.getText();
                    String mensaje=GuardarArchivo(archivo,Documento);
                    if(mensaje!=null){
                        JOptionPane.showMessageDialog(null,mensaje);
                    }else{
                        JOptionPane.showMessageDialog(null,"Archivo no compatible");
                    }
                }else{
                    JOptionPane.showMessageDialog(null,"Guardar Documento de texto");
                }
            }
        }
        else JOptionPane.showMessageDialog(null,"Archivo vac�o");
    }//GEN-LAST:event_guardarComoActionPerformed

    private void ejecutationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ejecutationActionPerformed
    
    }//GEN-LAST:event_ejecutationActionPerformed

    public void setInter(Interprete inter) {
        this.inter = inter;
    }
    
    private void ejecutationMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ejecutationMouseClicked
        inter = new Interprete(this);
        inter.vaciarDatos();
        inter.lexico(Texto.getText());
        if(graficator!=null){
            Lienzo.add(graficator);
            graficator.repaint();
        }
    }//GEN-LAST:event_ejecutationMouseClicked

    private void ejecutationKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_ejecutationKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_ejecutationKeyPressed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JTextArea ErroresL;
    public javax.swing.JTextArea ErroresSem;
    public javax.swing.JTextArea ErroresSin;
    public java.awt.ScrollPane Lienzo;
    private javax.swing.JTextArea Texto;
    private javax.swing.JMenuItem abrir;
    private javax.swing.JMenu ejecutation;
    private javax.swing.JMenuItem guardar;
    private javax.swing.JMenuItem guardarComo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    // End of variables declaration//GEN-END:variables
}

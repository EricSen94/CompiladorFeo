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
    Graficador g;
    
    public PantallaPrincipal(Interprete I) {
        initComponents();
        inter = I;
        seleccionar = new JFileChooser();
        archivo = new File("");
        g = new Graficador();
    }
    public String getContenido(){
        return Texto.getText();
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
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
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
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        abrir = new javax.swing.JMenuItem();
        guardar = new javax.swing.JMenuItem();
        guardarComo = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        compilar = new javax.swing.JMenuItem();
        ejecutar = new javax.swing.JMenuItem();

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

        jMenu1.setText("Archivo");

        abrir.setText("Abrir");
        abrir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                abrirActionPerformed(evt);
            }
        });
        jMenu1.add(abrir);

        guardar.setText("Guardar");
        guardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                guardarActionPerformed(evt);
            }
        });
        jMenu1.add(guardar);

        guardarComo.setText("Guardar como");
        guardarComo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                guardarComoActionPerformed(evt);
            }
        });
        jMenu1.add(guardarComo);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Compilar/Ejecutar");

        compilar.setText("Compilar");
        compilar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                compilarActionPerformed(evt);
            }
        });
        jMenu2.add(compilar);

        ejecutar.setText("Ejecutar");
        ejecutar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ejecutarActionPerformed(evt);
            }
        });
        jMenu2.add(ejecutar);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 237, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Lienzo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 267, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 298, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 274, Short.MAX_VALUE)))
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
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>                        

    private void abrirActionPerformed(java.awt.event.ActionEvent evt) {                                      
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
    }                                     

    private void guardarActionPerformed(java.awt.event.ActionEvent evt) {                                        
        if(!"".equals(archivo.getPath()))
            GuardarArchivo(archivo, Texto.getText());
        else
            JOptionPane.showMessageDialog(null,"Archivo vac�o");
    }                                       

    private void ejecutarActionPerformed(java.awt.event.ActionEvent evt) {                                         
        
        Lienzo.add(g);
        g.repaint();
        System.out.println("Entro");
    }                                        

    private void guardarComoActionPerformed(java.awt.event.ActionEvent evt) {                                            
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
    }                                           

    private void compilarActionPerformed(java.awt.event.ActionEvent evt) {                                         
        inter.lexico(Texto.getText());
        inter.getArraySintactico();
        inter.vaciarDatos();
    }                                        

    // Variables declaration - do not modify                     
    public javax.swing.JTextArea ErroresL;
    public javax.swing.JTextArea ErroresSem;
    public javax.swing.JTextArea ErroresSin;
    private java.awt.ScrollPane Lienzo;
    private javax.swing.JTextArea Texto;
    private javax.swing.JMenuItem abrir;
    private javax.swing.JMenuItem compilar;
    private javax.swing.JMenuItem ejecutar;
    private javax.swing.JMenuItem guardar;
    private javax.swing.JMenuItem guardarComo;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    // End of variables declaration                   
}

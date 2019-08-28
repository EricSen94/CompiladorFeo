/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interprete;
import javax.swing.JOptionPane;
import Compilador.PantallaPrincipal;
import java.util.ArrayList;

/**
 *
 * @author lalos
 */
public class Interprete{
    private final tablaSimbolos tablaS;
    private final PantallaPrincipal w;
    //Sin es un arreglo para ir llevando el conteo de los "nodos" de las sentencias
    private ArrayList<String> sin;
    
    public Interprete(){
        tablaS = new tablaSimbolos();
        sin = new  ArrayList();
        w = new PantallaPrincipal(this);
        w.setBounds(0,0,800,600);
        w.setTitle("Intérprete");
        w.setLocationRelativeTo(null);
        w.setVisible(true);
    }
    public void lexico(String contenido){
        w.ErroresL.setText("");
        w.ErroresSem.setText("");
        w.ErroresSin.setText("");
        sin.clear();
        tablaS.ids.clear();
        //Si el contenido no esta vacio
        if(!contenido.isEmpty()){
            
            String[] tokens, lineas;
            //temp para ir guardando la palabra o id 
            //caracter es el caracter actual leido
            String temp="", caracter,tipo, ope, pr;
            //dividimos nuestro contenido por lineas
            lineas = contenido.split("\n"); 
            
            //cont = columna
            int i, cantLineas,cont=0;
            cantLineas = lineas.length;
            
            //Nos movemos a lo largo de todo el documento
            for(i=0; i<cantLineas; i++){
            System.out.println("Analizando Léxico");
                //Revisamos caracter por caracter en la linea
                while(cont<lineas[i].length()){
                    //guardamos el caracter que estamos leyendo
                    caracter = String.valueOf(lineas[i].charAt(cont));
                    //Si el caracter no es un separador
                    if( !tablaS.esSeparador(caracter)){
                        //si el caracter es una letra O numero lo sumamos a nuestra palabra
                        if( caracter.matches(".*[a-zA-Z0-9].*") ){
                            temp+=caracter;
                        }
                        //si es un operador
                        else if( tablaS.operadores.containsValue(caracter) ){
                            //Revisamos si antes habia una palabra
                            if(temp.matches(".*[a-zA-Z].*")){ 
                                //Revisarmos si es palabra reservada
                                if(tablaS.isPR(temp)){
                                    pr = tablaS.cualPrES(temp);
                                    mandarMensajeLexico("Linea "+(i+1)+", Columna "+(cont+1)+", Palabra reservada: "+pr+"\n");
                                    //Lo mandamos al sintactico
                                    sinSem(pr,"pr",i,cont);
                                    temp="";
                                }
                                else{
                                    //Sino era palabra reservada, entonces era ID
                                    mandarMensajeLexico("Linea "+(i+1)+", Columna "+(cont+1)+", ID: "+temp+"\n");
                                    sinSem(temp,"ID",i,cont);
                                    temp="";
                                }
                            }
                            //o si entonces habia un numero?
                            else if(temp.matches(".*[0-9].*")){
                                mandarMensajeLexico("Linea "+(i+1)+", Columna "+(cont+1)+", Numero: "+temp+"\n");
                                sinSem(temp,"num",i,cont);
                                temp="";
                            }
                            //Entonces la palabra anterior es una combinacion de letras y numeros (un ID)
                            else{
                                 mandarMensajeLexico("Linea "+(i+1)+", Columna "+(cont+1)+", ID: "+temp+"\n");
                                 sinSem(temp,"ID",i,cont);
                                 temp="";
                            }
                            //Ya que revisamos si antes habia algo, pasamos el operador actual al sintactico
                            ope = tablaS.queOpeEs(caracter);
                            mandarMensajeLexico("Linea "+(i+1)+", Columna "+(cont+1)+", operador: "+ope+"\n");
                            sinSem(caracter, ope, i, cont);
                        }
                        else mandarErrorLexico(i,cont);
                    }
                    //Si el caracter es un separador y si temp no ha sido ya vaciado
                    else if(!temp.isEmpty()){
                        //Pero antes habia alguna palabra
                        if(temp.matches(".*[a-zA-Z].*")){ 
                                //Revisarmos si es palabra reservada
                                if(tablaS.isPR(temp)){
                                    pr = tablaS.cualPrES(temp);
                                    mandarMensajeLexico("Linea "+(i+1)+", Columna "+(cont+1)+", Palabra reservada: "+pr+"\n");
                                    //Lo mandamos al sintactico
                                    sinSem(pr,"pr",i,cont);
                                    temp="";
                                }
                                else{
                                    //Sino, lo guardamos como ID
                                    mandarMensajeLexico("Linea "+(i+1)+", Columna "+(cont+1)+", ID: "+temp+"\n");
                                    //lo mandamos al sintactico
                                    sinSem(temp,"ID",i,cont);
                                    temp="";
                                }
                            }
                        //Entonces habia un numero?
                        else if(temp.matches(".*[0-9].*")){
                                mandarMensajeLexico("Linea "+(i+1)+", Columna "+(cont+1)+", Numero: "+temp+"\n");
                                //lo mandamos al sintactico
                                sinSem(temp,"num",i,cont);
                                temp="";
                        }
                        //Entonces la palabra anterior es una combinacion de letras y numeros (un ID)
                        else{
                            mandarMensajeLexico("Linea "+(i+1)+", Columna "+(cont+1)+", ID: "+temp+"\n");
                            sinSem(temp,"ID",i,cont);
                            temp="";
                        }
                    }
                    cont++;
                }
                cont=0;
                temp="";
            }
        }
        else JOptionPane.showMessageDialog(null,"No has escrito nada");
    }
    //Token de la PR, valor del ID o valor del Numero
    public void sinSem(String token, String QueEs, int linea, int columna){
        System.out.println("Analizando Sintactico");
        if(sin.isEmpty() ){
            //ken.equals(tablaS.palabrasReservadas.get("pro")
            sin.add(token);
        }
        //Revisamos lo que deberia esperar en base al ultimo elemento ingresado
        else{
            //los primeros 3 valores deben ser PRO nombre BEGIN afuera
           String ultimo = sin.get(sin.size()-1);
           //Para cuando sean palabras reservadas
           if(tablaS.isPR(ultimo)){
               switch(ultimo){
               //Solo Programa espera un ID después
                   case "pro":
                       if(QueEs.equals("ID")) sin.clear();
                       else mandarErrorSintactico(linea, columna);
                   default:
                       if(!QueEs.equals("(")) mandarErrorSintactico(linea, columna);
                       else sin.add(token);
               }
           }
           else if(tablaS.isID(ultimo)){
               
           }
        }
        
        if(QueEs.equals("ID")){
            //revisamos si no estaba antes para agregarlo
            if(!tablaS.isID(token)) tablaS.agregarID(token, linea, columna);
        }
    }
    public void graficar(){   
    }
    //metodo para ir comparanto la tablaS.metodos e Interprete.sin
    public void revisarOrden(String valorActual){
    }
    public void vaciarDatos(){
        tablaS.vaciarIds();
    }
    public void mandarMensajeLexico(String mensaje){
        w.ErroresL.append(mensaje);
    }
    public void mandarErrorLexico(int fila, int columna){
        fila+=1;
        columna+=1;
        w.ErroresL.append("[Error]: Token inesperado. Linea: "+fila+" Columna: "+columna+"\n");
    }
    public void mandarMensajeSintactico(String mensaje){
        w.ErroresSin.append(mensaje);
    }
    public void mandarErrorSintactico(int fila, int columna){
        fila+=1;
        columna+=1;
        w.ErroresSin.append("[Error]: Token inesperado. Linea: "+fila+" Columna: "+columna+"\n");
    }
    public void mandarMensajeSemantico(String mensaje){
        w.ErroresSem.append(mensaje);
    }
    public void getArraySintactico(){
        System.out.println("Tabla de ID´s");
        for(int i=0; i<tablaS.ids.size(); i++){
            System.out.println(i+" "+tablaS.ids.get(i)+"\n");
        }
    }
    
}

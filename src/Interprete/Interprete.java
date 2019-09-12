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
    //SinTokens es un arreglo para ir llevando los tokens leidos
    private ArrayList<String> sinTokens;
    //sinQueEs lleva el tipo de token 
    private ArrayList<String> sinQueEs;
    //Los arreglos de posicion del token
    private ArrayList <Integer> sinLinea;
    private ArrayList <Integer> sinColumna;
    //Esta bandera es para cuando no haya error en el codigo
    private boolean noHayErrorSintactico;
    //Variable que guardará el valor de lo que espera al siguiente, es un array porque puede esperar varias cosas
    private ArrayList<String> esperoEsto;
    
    public Interprete(){
        tablaS = new tablaSimbolos();
        sinTokens = new  ArrayList();
        sinQueEs = new ArrayList();
        sinLinea = new ArrayList();
        sinColumna = new ArrayList();
        noHayErrorSintactico = false;
        w = new PantallaPrincipal(this);
        w.setBounds(0,0,800,600);
        w.setTitle("Intérprete");
        w.setLocationRelativeTo(null);
        w.setVisible(true);
    }
    public void lexico(String contenido){
        //Vaciamos los valores porque en cada ejecucion de la compilación se ocupan estos valores
        w.ErroresL.setText("");
        w.ErroresSem.setText("");
        w.ErroresSin.setText("");
        sinTokens.clear();
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
                                    sintactico(pr,"pr",i,cont);
                                    temp="";
                                }
                                else{
                                    //Sino era palabra reservada, entonces era ID
                                    mandarMensajeLexico("Linea "+(i+1)+", Columna "+(cont+1)+", ID: "+temp+"\n");
                                    sintactico(temp,"ID",i,cont);
                                    temp="";
                                }
                            }
                            //o si entonces habia un numero?
                            else if(temp.matches(".*[0-9].*")){
                                mandarMensajeLexico("Linea "+(i+1)+", Columna "+(cont+1)+", Numero: "+temp+"\n");
                                sintactico(temp,"num",i,cont);
                                temp="";
                            }
                            //Entonces la palabra anterior es una combinacion de letras y numeros (un ID)
                            else{
                                 mandarMensajeLexico("Linea "+(i+1)+", Columna "+(cont+1)+", ID: "+temp+"\n");
                                 sintactico(temp,"ID",i,cont);
                                 temp="";
                            }
                            //Ya que revisamos si antes habia algo, pasamos el operador actual al sintactico
                            ope = tablaS.queOpeEs(caracter);
                            mandarMensajeLexico("Linea "+(i+1)+", Columna "+(cont+1)+", operador: "+ope+"\n");
                            sintactico(caracter, ope, i, cont);
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
                                    sintactico(pr,"pr",i,cont);
                                    temp="";
                                }
                                else{
                                    //Sino, lo guardamos como ID
                                    mandarMensajeLexico("Linea "+(i+1)+", Columna "+(cont+1)+", ID: "+temp+"\n");
                                    //lo mandamos al sintactico
                                    sintactico(temp,"ID",i,cont);
                                    temp="";
                                }
                            }
                        //Entonces habia un numero?
                        else if(temp.matches(".*[0-9].*")){
                                mandarMensajeLexico("Linea "+(i+1)+", Columna "+(cont+1)+", Numero: "+temp+"\n");
                                //lo mandamos al sintactico
                                sintactico(temp,"num",i,cont);
                                temp="";
                        }
                        //Entonces la palabra anterior es una combinacion de letras y numeros (un ID)
                        else{
                            mandarMensajeLexico("Linea "+(i+1)+", Columna "+(cont+1)+", ID: "+temp+"\n");
                            sintactico(temp,"ID",i,cont);
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
    public void sintactico(String token, String QueEs, int linea, int columna){
        System.out.println("Analizando Sintactico");
        //Si es el primer valor en ingresar, añadimos todo a la lista
        if(sinTokens.isEmpty() ){
            //Lo primero que debe esperar debe ser un PRO
            if(token.equals("PRO")){
                //ken.equals(tablaS.palabrasReservadas.get("pro")
                sinTokens.add(token);
                sinQueEs.add(QueEs);
                sinLinea.add(linea);
                sinColumna.add(columna);
                //Ahora espera el nombre
                esperoEsto.add("ID");
            }
            else{
                mandarErrorSintactico(linea, columna);
            }
        }
        //Revisamos lo que deberia esperar ahora en base a la variable
        else{
           //Los primeros 3 valores debe ser afuerza --PRO ID BEGIN--
           if(sinTokens.size() < 3){
                //Si es el segundo valor en entrar y es correcto
                if(sinTokens.size() == 1 && QueEs.equals(esperoEsto.get(0))){
                    esperoEsto.clear();
                    esperoEsto.add(QueEs);
                }
                else mandarErrorSintactico(linea, columna);
           }
           //Cuando ya esta bien el los primeros 3 tokens
           else{
               
           }
           
//           String ultimo = sinTokens.get(sinTokens.size()-1);
//           //Para cuando sean palabras reservadas
//           if(tablaS.isPR(ultimo)){
//               switch(ultimo){
//               //Solo Programa espera un ID después
//                   case "pro":
//                       int i;
//                       for(i=0; i<sinTokens.size()-1; i++){
//                           
//                       }
//                       if(QueEs.equals("ID")) sinTokens.clear();
//                       else mandarErrorSintactico(linea, columna);
//                   default:
//                       if(!QueEs.equals("(")) mandarErrorSintactico(linea, columna);
//                       else sinTokens.add(token);
//               }
//           }
//           else if(tablaS.isID(ultimo)){
//               
//           }
        }
        
        if(QueEs.equals("ID")){
            //revisamos si no estaba antes para agregarlo
            if(!tablaS.isID(token)) tablaS.agregarID(token, linea, columna);
        }
        
        if(noHayErrorSintactico){
            semantico();
        }
    }
    public void semantico(){
        //Al finalizar la ejecución mostrar y vaciar los datos
        getArraySintactico();
        vaciarDatos();
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

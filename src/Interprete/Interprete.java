/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interprete;
import javax.swing.JOptionPane;
import Compilador.PantallaPrincipal;
import java.util.ArrayList;
import java.util.stream.Stream;
import javafx.scene.text.Text;

/**
 *
 * @author lalos
 */
public class Interprete{
    private final tablaSimbolos tablaS;
    private final PantallaPrincipal w;
    //SinTokens es un arreglo para ir llevando los tokens leidos
    private final ArrayList<String> sinTokens;
    //sinQueEs lleva el tipo de token 
    private final ArrayList<String> sinQueEs;
    //Los arreglos de posicion del token
    private final ArrayList<Integer> sinLinea;
    private final ArrayList<Integer> sinColumna;
    //Variable que guardará el valor de lo que espera al siguiente, es un array porque puede esperar varias cosas
    private final ArrayList<String> esperoEsto;
    //Esta bandera es para cuando no haya error en el codigo
    private final boolean noHayErrorSintactico;
    //Lo mismo pero cuando se queda mocho
    private final boolean faltaDato;
    //Para revisar el orden en el sintactico
    int revisarPos=0;
    //un arreglo que mantiene los valores de algun metodo que se esta revisndo
    ArrayList<String> revisando;
    
    public Interprete(){
        tablaS = new tablaSimbolos();
        sinTokens = new  ArrayList();
        sinQueEs = new ArrayList();
        sinLinea = new ArrayList();
        sinColumna = new ArrayList();
        esperoEsto = new ArrayList();
        revisando = new ArrayList();
        noHayErrorSintactico = false;
        faltaDato=true;
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
        sinQueEs.clear();
        sinLinea.clear();
        sinColumna.clear();
        esperoEsto.clear();
        tablaS.ids.clear();
        //Si el contenido no esta vacio
        if(!contenido.isEmpty()){
            String[] lineas;
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
                    //Si el caracter es un separador y si temp no ha sido vaciado
                    else if(!temp.isEmpty()){
                        //antes habia alguna palabra?
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
        int i;
        //Revisar almacena el metodo que se está revisando
        String revisar="";
        System.out.println("Analizando Sintactico");
        //Si es el primer valor en ingresar, añadimos todo a la lista
        if(sinTokens.isEmpty()){
            //Lo primero que debe esperar debe ser un PRO
            if(token.equals("pro")){
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
                //Si es el segundo valor en entrar y coindice con lo que se esperaba
                if(sinTokens.size() == 1 && QueEs.equals(esperoEsto.get(0))){
                    //Se hace clear porque solo espera a un BEGIN
                    esperoEsto.clear();
                    esperoEsto.add("begin");
                    //Agregamos los valores a los arreglos
                    sinTokens.add(token);
                    sinQueEs.add(QueEs);
                    sinLinea.add(linea);
                    sinColumna.add(columna);
                }
                //Si es el tercer valor ingresado y es correcto
                else if(sinTokens.size() == 2 && token.equals(esperoEsto.get(0))){
                    esperoEsto.clear();
                    //Ahora solo puede esperar métodos y un 'end'
                    esperoEsto.add("draw");//3
                    esperoEsto.add("delete");
                    esperoEsto.add("sleep");
                    esperoEsto.add("change");//6
                    esperoEsto.add("end");
                    //Agregamos los valores
                    sinTokens.add(token);
                    sinQueEs.add(QueEs);
                    sinLinea.add(linea);
                    sinColumna.add(columna);
                    
                }
                else mandarErrorSintactico(linea, columna);
           }
           //Cuando ya esta bien el los primeros 3 tokens
           else{
                //Si es un nuevo metodo a revisar
                if(revisar.isEmpty()){
                //Comparamos lo que esperamos y lo que tenemos
                for(i=0; i<esperoEsto.size(); i++){
                    if(token.equals(esperoEsto.get(i))){
                        revisar=token;
                        break;
                    }
                }
                //Si en el ciclo no se cumplio
                if(revisar.isEmpty())
                    mandarErrorSintactico(i, columna);
                //Si si hay coincidencia revisamos el orden
                else
                    //Usamos el metodo
                    revisarOrden(token, revisarPos);
                
                sinTokens.add(token);
                sinQueEs.add(QueEs);
                sinLinea.add(linea);
                sinColumna.add(columna);
               }
               //Si ya se está revisando un metodo
               else{
                   
               }
           }
        }
        
        if(QueEs.equals("ID")){
            //revisamos si no estaba antes para agregarlo
            if(!tablaS.isID(token)) tablaS.agregarID(token, linea, columna);
        }
        
        if(faltaDato){
            mandarErrorSintactico(linea, columna, faltaDato);
        }
        if(noHayErrorSintactico){
            semantico();
        }
    }
    public void semantico(){
    }
    public void graficar(){   
    }
    //metodo para ir comparanto la tablaS.metodos e Interprete.sin
    public void revisarOrden(String token, int pos){
        int i;
        //Revisamos si el token es el nombre del metodo a revisar
        if(tablaS.metodos.containsKey(token)){
            System.out.println("size del arreglo: "+sinTokens.size());
            //Guardamos el array de la estructura de ese metodo
            int tamanio = tablaS.metodos.get(token).size();
            String valor="";
            for(i=1; i<tamanio; i++){
                valor = tablaS.metodos.get(token).get(i);
                revisando.add(valor);
                System.out.println(valor+"\n");
            }
            
        }
        else{
            
        }
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
    //Sobre escrito
    public void mandarErrorSintactico(int fila, int columna, boolean falta){
        fila+=1;
        columna+=1;
        w.ErroresSin.append("[Error]: Falta Token. Linea: "+fila+" Columna: "+columna+"\n");
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

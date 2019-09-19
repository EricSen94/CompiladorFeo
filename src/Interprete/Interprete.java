/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interprete;
import Compilador.Graficador;
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
    public boolean sePuedeGraf;
    public Graficador graficator;
    private String matrizDibujo[][];
    private final tablaSimbolos tablaS;
    private final PantallaPrincipal w;
    //SinTokens es un arreglo para ir llevando los tokens leidos
    private final ArrayList<String> sinTokens;
    //Variable que guardará el valor de lo que espera al siguiente, es un array porque puede esperar varias cosas
    private final ArrayList<String> esperoEsto;
    //Esta bandera es para cuando no haya error en el codigo
    private boolean noHayErrorSintactico;
    //Lo mismo pero cuando se queda mocho
    private boolean faltaDato;
    //Para revisar el orden en el sintactico
    int revisarPos=1;
    private String revisar="";
    //un arreglo que mantiene los valores de algun metodo que se esta revisndo
    private final ArrayList<String> revisando;
    
    public Interprete(){
        graficator= new Graficador();
        tablaS = new tablaSimbolos();
        sinTokens = new  ArrayList();
        esperoEsto = new ArrayList();
        revisando = new ArrayList();
        noHayErrorSintactico = false;
        faltaDato=false;
        sePuedeGraf=false;
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
                                    //mandarMensajeLexico("Linea "+(i+1)+", Columna "+(cont+1)+", Palabra reservada: "+pr+"\n");
                                    //Lo mandamos al sintactico
                                    sintactico(pr,"pr",i,cont);
                                    temp="";
                                }
                                else{
                                    //Sino era palabra reservada, entonces era ID
                                    //mandarMensajeLexico("Linea "+(i+1)+", Columna "+(cont+1)+", ID: "+temp+"\n");
                                    sintactico(temp,"ID",i,cont);
                                    temp="";
                                }
                            }
                            //o si entonces habia un numero?
                            else if(temp.matches(".*[0-9].*")){
                                //mandarMensajeLexico("Linea "+(i+1)+", Columna "+(cont+1)+", Numero: "+temp+"\n");
                                sintactico(temp,"num",i,cont);
                                temp="";
                            }
                            //Entonces la palabra anterior es una combinacion de letras y numeros (un ID)
                            else{
                                 //mandarMensajeLexico("Linea "+(i+1)+", Columna "+(cont+1)+", ID: "+temp+"\n");
                                 sintactico(temp,"ID",i,cont);
                                 temp="";
                            }
                            //Ya que revisamos si antes habia algo, pasamos el operador actual al sintactico
                            ope = tablaS.queOpeEs(caracter);
                            //mandarMensajeLexico("Linea "+(i+1)+", Columna "+(cont+1)+", operador: "+ope+"\n");
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
                                    //mandarMensajeLexico("Linea "+(i+1)+", Columna "+(cont+1)+", Palabra reservada: "+pr+"\n");
                                    //Lo mandamos al sintactico
                                    sintactico(pr,"pr",i,cont);
                                    temp="";
                                }
                                else{
                                    //Sino, lo guardamos como ID
                                    //mandarMensajeLexico("Linea "+(i+1)+", Columna "+(cont+1)+", ID: "+temp+"\n");
                                    //lo mandamos al sintactico
                                    sintactico(temp,"ID",i,cont);
                                    temp="";
                                }
                            }
                        //Entonces habia un numero?
                        else if(temp.matches(".*[0-9].*")){
                                //mandarMensajeLexico("Linea "+(i+1)+", Columna "+(cont+1)+", Numero: "+temp+"\n");
                                //lo mandamos al sintactico
                                sintactico(temp,"num",i,cont);
                                temp="";
                        }
                        //Entonces la palabra anterior es una combinacion de letras y numeros (un ID)
                        else{
                            //mandarMensajeLexico("Linea "+(i+1)+", Columna "+(cont+1)+", ID: "+temp+"\n");
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
    public void sintactico(String token, String QueEs, int fila, int columna){
        int i;
        //Revisar almacena el metodo que se está revisando
        System.out.println("Analizando Sintactico");
        //Si es el primer valor en ingresar, añadimos todo a la lista
        if(sinTokens.isEmpty()){
            //Lo primero que debe esperar debe ser un PRO
            if(token.equals("pro")){
                //ken.equals(tablaS.palabrasReservadas.get("pro")
                sinTokens.add(token);
                //Ahora espera el nombre
                esperoEsto.add("ID");
            }
            else{
                mandarErrorSintactico("Se esperaba la sentencia PRO.",fila, columna);
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
                }else
                //Si es el tercer valor ingresado y es correcto
                if(sinTokens.size() == 2 && token.equals(esperoEsto.get(0))){
                    esperoEsto.clear();
                    //Ahora solo puede esperar métodos y un 'end'
                    esperoEsto.add("draw");//3
                    esperoEsto.add("delete");
                    esperoEsto.add("sleep");
                    esperoEsto.add("change");//6
                    esperoEsto.add("end");
                }
                else{
                    if(sinTokens.size()==1) mandarErrorSintactico("Se esperaba un valor ID.",fila, columna);
                    else if(sinTokens.size()==2) mandarErrorSintactico("Se esperaba la palabra reservada BEGIN",fila, columna);
                }
                //Agregamos los valores
                sinTokens.add(token);
           }
           //Cuando ya esta bien el los primeros 3 tokens
           else{
                //Si es un nuevo metodo a revisar
                if(revisar.isEmpty()){
                //Comparamos lo que esperamos y lo que tenemos
                for(i=0; i<esperoEsto.size(); i++){
                    if(token.equals(esperoEsto.get(i))){
                        revisar=token;
                    }
                }
                //Si en el ciclo no se cumplio
                if(revisar.isEmpty())
                    mandarErrorSintactico("Se esperaba una palabra reservada.",fila, columna);
                //Si hay coincidencia revisamos el orden
                else
                    revisarOrden(token, QueEs, revisarPos,fila, columna);
                
                sinTokens.add(token);
               }
           }
        }
        if(QueEs.equals("ID"))
            tablaS.agregarID(token, fila, columna);
        
        if(faltaDato){
          mandarErrorSintactico(fila, columna, faltaDato);
        }
    }
    //metodo para ir comparanto la tablaS.metodos e Interprete.sin
    public void revisarOrden(String valor, String queEs, int pos, int fila, int columna){
        System.out.println("Revisando orden...");
        int i;
        //Revisamos si el token es el nombre del metodo a revisar (osea es una metodo)
        if(tablaS.metodos.containsKey(valor) && revisando.isEmpty()){
            System.out.println("size del arreglo: "+sinTokens.size());
            //Guardamos el array de la estructura de ese metodo
            int tamanio = tablaS.metodos.get(valor).size();
            String tmp="";
            //Primer valor de lo que se revisa es el nombre del metodo
            revisando.add(valor);
            System.out.println(valor+"\n");
            for(i=1; i<tamanio; i++){
                tmp = tablaS.metodos.get(valor).get(i);
                //Añadimos la estructura
                revisando.add(tmp);
                System.out.println(tmp+"\n");
            }
        }
        //sino es que ya estamos revisando una parte avanzada
        else{
            if(valor.equals("end")) revisando.clear();
            
            if(!revisando.isEmpty()){
                //Revisamos si nuestro valor actual coincide con nuestra estructura
                if(revisando.get(revisarPos).equals(queEs)){
                    //////////////////////////////////////////////////////////////
                    //Importante
                    //Pasar al valor ya revisado actual de nuestro método al arreglo de revisado
                    //Se podria ocupar otro arreglo, pero seria exactamente igual
                    revisando.set(revisarPos, valor);
                    revisarPos++;
                    noHayErrorSintactico=true;
                    faltaDato=false;
                }
                else{
                    mandarErrorSintactico("Se esperaba un valor "+revisando.get(revisarPos)+".",fila, columna);
                    noHayErrorSintactico=false;
                    faltaDato=true;
                }
                //Si ya se revisó todo, vaciar para revisar otra cosa
                if(revisarPos>revisando.size()){
                    if(noHayErrorSintactico){
                        //La estrucutra cuenta los parentesis y comas.
                        //revisando.get(0) es el nombre del método
                        //Si es un draw
                        if(revisando.get(0).equals("draw")){
                            semantico(revisando.get(0), revisando.get(8),Integer.parseInt(revisando.get(2)), Integer.parseInt(revisando.get(3)), Integer.parseInt(revisando.get(6)), fila, columna);
                        }
                        //Si es delete
                        if(revisando.get(0).equals("delete")){
                            semantico(revisando.get(0), revisando.get(2),0,0, 0, fila, columna);
                        }
                        //Si es sleep
                        if(revisando.get(0).equals("sleep")){
                            semantico(revisando.get(0), revisando.get(2), 0, 0, 0, fila, pos);
                        }
                        //Si es change
                        if(revisando.get(0).equals("change")){
                            //como el metodo semantico no cuenta con 3 String de parámetros, le agregamos al parámetro de cara las 2
                            //Primero la cara original, y luego la nueva
                            //Separado por una coma
                            semantico(revisando.get(0), revisando.get(2)+","+revisando.get(4), 0, 0, 0, fila, pos);
                        }
                    }
                    revisando.clear();
                    revisarPos=1;
                }
            }
            else{
                mandarErrorSintactico("Ya se ha sentenciado el fnal del programa",fila, columna); //Token inesperado
            }
        }
    }
    public void semantico(String metodo, String cara, int x, int y, int tam, int fila, int col){
            switch(metodo){
                case "draw":
                    matrizDibujo[0][0]=String.valueOf(x);//x1 //nombre de la cara o si es borrar o algo asi
                    matrizDibujo[0][1]=String.valueOf(y);//y1
                    matrizDibujo[0][2]=Integer.toString(tam);
                    matrizDibujo[0][3]=cara;
                    matrizDibujo[0][4]=metodo;
                    break;
                case "delete":
                    matrizDibujo[0][0]=String.valueOf(x);//id
                    matrizDibujo[0][1]="";
                    matrizDibujo[0][2]="";
                    matrizDibujo[0][3]="";
                    matrizDibujo[0][4]=metodo; //
                    break;
                case "sleep":
                    matrizDibujo[0][0]=String.valueOf(x);//id
                    matrizDibujo[0][1]="";
                    matrizDibujo[0][2]="";
                    matrizDibujo[0][3]="";
                    matrizDibujo[0][4]=metodo; //
                    break;
                case "change":
                    String[] caras = cara.split(",");
                    matrizDibujo[0][0]=caras[1];//nueva cara
                    matrizDibujo[0][1]=caras[0];//Cara original
                    matrizDibujo[0][2]="";
                    matrizDibujo[0][3]="";
                    matrizDibujo[0][4]=metodo; //
                    break;
                default:
                    mandarErrorMensajeSemantico("No se pudo elegir correctamente el método.",fila, col);
                    break;
            }
        if(graficator.checador(matrizDibujo) == "NP"){
            sePuedeGraf=true;
            System.out.println("Se puede graficar!");
            graficator.addMatriz(matrizDibujo);
        }
        else{
            mandarErrorMensajeSemantico("Error por colicion", fila, col);
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
    public void mandarErrorSintactico(String mensaje, int fila, int columna){
        fila+=1;
        columna+=1;
        w.ErroresSin.append("[Error]: "+mensaje+" Linea: "+fila+" Columna: "+columna+"\n");
    }
    //Sobrecarga
    public void mandarErrorSintactico(int fila, int columna, boolean falta){
        fila+=1;
        columna+=1;
        w.ErroresSin.append("[Error]: Falta Token. Linea: "+fila+" Columna: "+columna+"\n");
    }
    public void mandarErrorMensajeSemantico(String mensaje, int fila, int columna){
        w.ErroresSem.append("[Error]: "+mensaje+"Linea: "+fila+" Columna: "+columna+"\n");
    }
    public void getArraySintactico(){
        System.out.println("Tabla de ID´s");
        for(int i=0; i<tablaS.ids.size(); i++){
            System.out.println(i+" "+tablaS.ids.get(i)+"\n");
        }
    }
    
}

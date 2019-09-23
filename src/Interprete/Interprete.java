package Interprete;
import Compilador.Graficador;
import javax.swing.JOptionPane;
import Compilador.PantallaPrincipal;
import java.util.ArrayList;

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
        esperoEsto.clear();
        tablaS.ids.clear();
        //Si el contenido no esta vacio
        if(!contenido.isEmpty()){
            String[] lineas;
            //temp para ir guardando la palabra o id 
            //caracter es el caracter actual leido
            String temp="", caracter, ope, pr;
            //dividimos nuestro contenido por lineas
            lineas = contenido.split("\n"); 
            //cont = columna  i = fila
            int i, cantLineas,cont=0;
            cantLineas = lineas.length;
            ArrayList<String> nuevo;
            String lineaActual;
            System.out.println("Analizando Léxico");
            //Nos movemos a lo largo de todo el docu-mento
            for(i=0; i<cantLineas; i++){
                lineaActual = lineas[i].replace("\t", " ");
                lineaActual = lineaActual.replace("\r", " ");
                //Revisamos caracter por caracter en la linea
                while(cont<lineaActual.length()){
                    //guardamos el caracter que estamos leyendo
                    //.trim quia los espacios en blanco etc
                    caracter = String.valueOf(lineaActual.charAt(cont));
                    //System.out.println("Caracter: "+caracter);
                        //si el caracter es una letra O numero lo sumamos a nuestra palabra
                        if( caracter.matches(".*[a-zA-Z0-9].*") ){
                            temp+=caracter;
                        }
                        //si es un operador o un espacio
                        else if( tablaS.operadores.containsValue(caracter) || tablaS.esSeparador(caracter)){
                            nuevo = new ArrayList<>();
                            //Revisamos si antes habia una palabra
                            if(temp.matches(".*[a-zA-Z].*")){ 
                                //Revisarmos si es palabra reservada
                                if(tablaS.isPR(temp)){
                                    pr = tablaS.cualPrES(temp);
                                    //System.out.println("Linea "+(i+1)+", Palabra reservada: "+pr+"\n");
                                    //Lo mandamos al arreglo
                                    //****************************
                                    //PR
                                    //****************************
                                    //Token del PR, valor, linea, columa
                                    nuevo.add(pr);
                                    nuevo.add(pr);
                                    nuevo.add(Integer.toString(i));
                                    nuevo.add(Integer.toString(cont));
                                    tablaS.arrayLexico.add(nuevo);
                                    temp="";
                                }
                                else{
                                    //Sino era palabra reservada, entonces era ID
                                    //System.out.println("Linea "+(i+1)+", ID: "+temp+"\n");
                                    //****************************
                                    //ID
                                    //****************************
                                    //Token de ID, valor del ID, linea , columna
                                    nuevo.add("ID");
                                    nuevo.add(temp);
                                    nuevo.add(Integer.toString(i));
                                    nuevo.add(Integer.toString(cont));
                                    tablaS.arrayLexico.add(nuevo);
                                    temp="";
                                }
                            }
                            //o si entonces habia un numero?
                            else if(temp.matches(".*[0-9].*")){
                                //System.out.println("Linea "+(i+1)+", Numero: "+temp+"\n");
                                //****************************
                                //number
                                //****************************
                                //Token number, valor del token, linea, columa
                                nuevo.add("number");
                                nuevo.add(temp);
                                nuevo.add(Integer.toString(i));
                                nuevo.add(Integer.toString(cont));
                                tablaS.arrayLexico.add(nuevo);
                                temp="";
                            }
                            //Entonces la palabra anterior es una combinacion de letras y numeros (un ID)
                            else{
                                    //System.out.println("Linea "+(i+1)+", ID: "+temp+"\n");
                                    //****************************
                                    //ID
                                    //****************************
                                    //Token de ID, valor del ID, linea, columna
                                    nuevo.add("ID");
                                    nuevo.add(temp);
                                    nuevo.add(Integer.toString(i));
                                    nuevo.add(Integer.toString(cont));
                                    tablaS.arrayLexico.add(nuevo);
                                    temp="";
                            }
                            //Ya que revisamos si antes habia algo, pasamos el operador actual al arreglo
                            if (!tablaS.esSeparador(caracter)) {
                                ope = tablaS.queOpeEs(caracter);
                                //System.out.println("Linea "+(i+1)+", operador: "+ope+"\n");
                                //****************************
                                //Operador
                                //****************************
                                //Token de operador, valor del operador, linea, columa
                                nuevo.add("ope");
                                nuevo.add(ope);
                                nuevo.add(Integer.toString(i));
                                nuevo.add(Integer.toString(cont));
                                tablaS.arrayLexico.add(nuevo);
                                }
                        }
                        else mandarErrorLexico(i, cont);
                    cont++;
                }
                cont=0;
                temp="";
            }
            //Cuando ya se paso todo
            sintactico(tablaS.arrayLexico);
        }
        else JOptionPane.showMessageDialog(null,"No has escrito nada");
    }
    //Token de la PR, valor del ID o valor del Numero
    public void sintactico(ArrayList<ArrayList<String>> arraySin){
        int i, tamArrayTokenActual,linea, col;
        int cantTokens = arraySin.size();
        System.out.println("Tokens leidos: "+cantTokens);
        String token, valorToken;
        //Revisar almacena el metodo que se está revisando
        System.out.println("Analizando Sintactico");
        esperoEsto.add("pro");
        for(i=0; i<cantTokens; i++){
            //El primer valor del arreglo asociado en la posicion i
            token = arraySin.get(i).get(0);
            System.out.println("Espero esto '"+esperoEsto.get(0)+"'");
            System.out.println("Token: "+token);
            valorToken = arraySin.get(i).get(1);
            tamArrayTokenActual = arraySin.get(i).size();
            linea = Integer.parseInt( arraySin.get(i).get(tamArrayTokenActual-2) );
            col= Integer.parseInt( arraySin.get(i).get(tamArrayTokenActual-1) );
            //Los primeros tres valores deben ser PRO ID BEGIN
            if(i<3){
                switch (i) {
                    case 0:
                        if (token.equals(esperoEsto.get(0))) {
                            esperoEsto.clear();
                            //Ahora espera el nombre
                            esperoEsto.add("ID");
                        }
                        else{
                            mandarErrorSintactico("Se esperaba la sentencia PRO.", linea, col);
                        }   break;
                    case 1:
                        if (token.equals(esperoEsto.get(0))) {
                            //Se hace clear porque solo espera a un BEGIN
                            esperoEsto.clear();
                            esperoEsto.add("begin");
                        }
                        else{
                            mandarErrorSintactico("Se esperaba un valor ID.",linea, col);
                        }   break;
                    case 2:
                        if (token.equals(esperoEsto.get(0))) {
                            esperoEsto.clear();
                            //Ahora solo puede esperar métodos y un 'end'
                            esperoEsto.add("draw");//3
                            esperoEsto.add("delete");
                            esperoEsto.add("sleep");
                            esperoEsto.add("change");//6
                            esperoEsto.add("end");
                        }
                        else {
                            mandarErrorSintactico("Se esperaba la palabra reservada BEGIN",linea, col);
                        }   break;
                    default:
                        mandarErrorSintactico("Se esperaba la palabra reservada",linea, col);
                        break;
                }
            }
            else{
                //Cuando ya se pasaron el los primeros 3 tokens
                //*******************************************
                //Si es un nuevo metodo/funcion a revisar
                if(revisar.isEmpty()){
                    //Comparamos lo que esperamos y lo que tenemos
                    for(i=0; i<esperoEsto.size(); i++){
                        if(token.equals(esperoEsto.get(i))){
                            revisar=token;
                        }
                    }
                    //Despues de revisar si coincidio con algo esperado 
                    //Si no coincidio
                    if (revisar.isEmpty()) {
                        mandarErrorSintactico("Se esperaba una palabra reservada.",linea, col);
                    }
                }
                //Si hay coincidencia revisamos el orden
                else{
                    revisarOrden(valorToken, token, revisarPos,linea, col);
                }

                if(token.equals("ID"))
                tablaS.agregarID(token, linea);
            }
        }
    }
    //metodo para ir comparanto la tablaS.metodos e Interprete.sin
    public void revisarOrden(String valor, String queEs, int pos, int fila, int col){
        System.out.println("Revisando orden...");
        int i;
        //Revisamos si el token es el nombre del metodo a revisar (osea es un metodo)
        if(tablaS.metodos.containsKey(valor) && revisando.isEmpty()){
            //Guardamos el array de la estructura de ese metodo
            int tamanio = tablaS.metodos.get(valor).size();
            String tmp="";
            //Primer valor de lo que se revisa es el nombre del metodo
            revisando.add(valor);
            //System.out.println(valor);
            for(i=1; i<tamanio; i++){
                tmp = tablaS.metodos.get(valor).get(i);
                //Añadimos la estructura
                revisando.add(tmp);
                System.out.println(tmp);
            }
        }
        //sino es que ya estamos revisando una parte avanzada
        else{
            //if(valor.equals("end")) revisando.clear();
            
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
                    mandarErrorSintactico("Se esperaba un valor "+revisando.get(revisarPos)+".",fila, col);
                    noHayErrorSintactico=false;
                    faltaDato=true;
                }
                //Si ya se revisó todo, vaciar para revisar otra cosa
                if(revisarPos>revisando.size()){
                    revisar="";
                    if(noHayErrorSintactico){
                        //La estrucutra cuenta los parentesis y comas.
                        //revisando.get(0) es el nombre del método
                        String nombre = revisando.get(0);
                        //Si es un draw
                        if(revisando.get(0).equals("draw")){
                            String IdCara = revisando.get(8);
                            int x =Integer.parseInt(revisando.get(2));
                            int y=Integer.parseInt(revisando.get(3));
                            int tam = Integer.parseInt(revisando.get(6));
                            semantico(nombre, IdCara,x,y , tam, fila,col );
                        }
                        //Si es delete
                        if(revisando.get(0).equals("delete")){
                            semantico(nombre, revisando.get(2),0,0, 0, fila, col);
                        }
                        //Si es sleep
                        if(revisando.get(0).equals("sleep")){
                            semantico(nombre, revisando.get(2), 0, 0, 0, fila, col);
                        }
                        //Si es change
                        if(revisando.get(0).equals("change")){
                            //como el metodo semantico no cuenta con 3 String de parámetros, le agregamos al parámetro de cara las 2
                            //Primero la cara original, y luego la nueva
                            //Separado por una coma
                            semantico(nombre, revisando.get(2)+","+revisando.get(4), 0, 0, 0, fila,col);
                        }
                    }
                    revisando.clear();
                    revisarPos=1;
                }
            }
            else{
                if(sePuedeGraf){
                    w.Lienzo.add(graficator);
                    graficator.repaint();
                    System.out.println("Entro a graficar");
                }
                else{
                    System.out.println("No se puede graficar");
                }
                mandarErrorSintactico("Ya se ha sentenciado el fnal del programa",fila, col); //Token inesperado
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
        if("NP".equals(graficator.checador(matrizDibujo))){
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
    public void mandarErrorLexico(int fila, int col){
        fila+=1;
        w.ErroresL.append("[Error]: Token inesperado. Linea: "+fila+"Columna: "+col+"\n");
    }
    public void mandarMensajeSintactico(String mensaje){
        w.ErroresSin.append(mensaje);
    }
    public void mandarErrorSintactico(String mensaje, int fila, int col){
        fila+=1;
        w.ErroresSin.append("[Error]: "+mensaje+" Linea: "+fila+", Columna "+col+"\n");
    }
    //Sobrecarga
    public void mandarErrorSintactico(int fila, boolean falta){
        fila+=1;
        w.ErroresSin.append("[Error]: Falta Token. Linea: "+fila+"\n");
    }
    public void mandarErrorMensajeSemantico(String mensaje, int fila, int col){
        w.ErroresSem.append("[Error]: "+mensaje+"Linea: "+fila+", Columna: "+col+"\n");
    }
    public void getArraySintactico(){
        System.out.println("Tabla de ID´s");
        for(int i=0; i<tablaS.ids.size(); i++){
            System.out.println(i+" "+tablaS.ids.get(i)+"\n");
        }
    }
    
}

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
    private String matrizCol[][];
    private final tablaSimbolos tablaS;
    private final PantallaPrincipal w;
    //Variable que guardará el valor de lo que espera al siguiente, es un array porque puede esperar varias cosas
    private final ArrayList<String> esperoEsto;
    //Para revisar el orden en el sintactico
    int revisarPos=1;
    private String revisar;
    //un arreglo que mantiene los valores de algun metodo que se esta revisndo
    private final ArrayList<String> revisando;
    private int contMetodos=0;
    
    public Interprete(){
        graficator= new Graficador();
        tablaS = new tablaSimbolos();
        esperoEsto = new ArrayList();
        revisando = new ArrayList();
        sePuedeGraf=false;
        revisar="";
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
        revisar="";
        revisarPos=1;
        sePuedeGraf=false;
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
            ArrayList<String> nuevo, nuevo2;
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
                                else if (tablaS.isModo(temp)){
                                    pr = tablaS.cualModoES(temp);
                                    //System.out.println("Linea "+(i+1)+", Palabra reservada: "+pr+"\n");
                                    //Lo mandamos al arreglo
                                    //****************************
                                    //PR
                                    //****************************
                                    //Token del PR, valor, linea, columa
                                    nuevo.add("modo");
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
                            else if( caracter.matches(".*[a-zA-Z0-9].*") ){
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
                                nuevo2 = new ArrayList<>();
                                ope = tablaS.queOpeEs(caracter);
                                //System.out.println("Linea "+(i+1)+", operador: "+ope+"\n");
                                //****************************
                                //Operador
                                //****************************
                                //Token de operador, valor del operador, linea, columa
                                nuevo2.add(ope);
                                nuevo2.add(caracter);
                                nuevo2.add(Integer.toString(i));
                                nuevo2.add(Integer.toString(cont));
                                tablaS.arrayLexico.add(nuevo2);
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
    public int getCantidadMetodos(){
        int i,tamArray,k=0;
        tamArray = tablaS.arrayLexico.size();
        ArrayList<String> temp;
        for(i=0; i<tamArray; i++){
            temp=tablaS.arrayLexico.get(i);
            if(tablaS.isMetodo(temp.get(i))){
                k++;
            }
        }
        return k;
    }
    //Token de la PR, valor del ID o valor del Numero
    public void sintactico(ArrayList<ArrayList<String>> arraySin){
        int i,j, tamArrayTokenActual,linea, col;
        int cantTokens = arraySin.size();
        matrizDibujo= new String[getCantidadMetodos()][5];
        System.out.println("Tokens leidos: "+cantTokens);
        String token, valorToken;
        //Revisar almacena el metodo que se está revisando
        System.out.println("Analizando Sintactico");
        esperoEsto.add("pro");
        for(i=0; i<cantTokens; i++){
            //El token primer valor del arreglo asociado en la posicion i
            token = arraySin.get(i).get(0);
            //Valores a ocupar
            if(!revisando.isEmpty()) System.out.println("Revisando el "+revisando.get(0)+", espera: "+revisando.get(revisarPos));
            System.out.println("Token: "+token);
            valorToken = arraySin.get(i).get(1);
            tamArrayTokenActual = arraySin.get(i).size();
            linea = Integer.parseInt( arraySin.get(i).get(tamArrayTokenActual-2) );
            col= Integer.parseInt( arraySin.get(i).get(tamArrayTokenActual-1) );
            //Los primeros tres valores deben ser PRO ID BEGIN
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
                //*******************************************
                //Si es un nuevo metodo/funcion a revisar
                default:
                    if(revisar.isEmpty()){
                        //Comparamos lo que esperamos y lo que tenemos
                        for(j=0; j<esperoEsto.size(); j++){
                            if(token.equals(esperoEsto.get(j))){
                                revisar=token;
                                break;
                            }
                        }
                        //Despues de revisar si coincidio con algo esperado 
                        //Si no coincidio
                        if ( !revisar.isEmpty() ){
                            revisarOrden(valorToken, token, revisarPos,linea, col);
                        }
                        else{
                            mandarErrorSintactico("Se esperaba una palabra reservada.",linea, col);
                        }
                    }
                    //Si ya hay algo que se esta revisando
                    else{
                        revisarOrden(valorToken, token, revisarPos,linea, col);
                    }
                    if(token.equals("ID")) tablaS.agregarID(token, linea);
                    break;
            }
        }
    }
    //metodo para ir comparanto la tablaS.metodos e Interprete.sin
    public void revisarOrden(String valor, String queEs, int pos, int fila, int col){
        System.out.println("Revisando orden...");
        int i;
        if(queEs.equals("end")) {
            revisando.clear();
            revisarPos=1;
            sePuedeGraf=true;
            revisar="";
        }
        //Revisamos si el token es el nombre del metodo a revisar (osea es un metodo)
        if( tablaS.metodos.containsKey(valor) && revisando.isEmpty() ){
            //Guardamos el array de la estructura de ese metodo
            int tamanio = tablaS.metodos.get(valor).size();
            String tmp;
            //Primer valor de lo que se revisa es el nombre del metodo
            revisando.add(valor);
            //System.out.println(valor);
            for(i=1; i<=tamanio; i++){
                tmp = tablaS.metodos.get(valor).get(i-1);
                //Añadimos la estructura
                revisando.add(tmp);
                //System.out.println(tmp);
            }
        }
        //sino es que ya estamos revisando una parte avanzada
        else if(!revisando.isEmpty()){
            //Revisamos si nuestro valor actual coincide con nuestra estructura
            if(revisando.get(revisarPos).equals(queEs)){
                //////////////////////////////////////////////////////////////
                //Importante
                //Pasar al valor ya revisado actual de nuestro método al arreglo de revisado
                //Se podria ocupar otro arreglo, pero seria exactamente igual
                revisando.set(revisarPos, valor);
                revisarPos++;
            }
            else{
                mandarErrorSintactico("Se esperaba un valor "+revisando.get(revisarPos)+".",fila, col);
            }
            //Si ya se revisó todo, vaciar para revisar otra cosa
            if(revisarPos>=revisando.size()){
                revisarPos=1;
                revisar="";
                //La estrucutra cuenta los parentesis y comas.
                //revisando.get(0) es el nombre del método
                String nombre = revisando.get(0);
                //Si es un draw
                if(revisando.get(0).equals("draw")){
                    String IdCara = revisando.get(8);
                    int x =Integer.parseInt(revisando.get(2));
                    int y=Integer.parseInt(revisando.get(4));
                    int tam = Integer.parseInt(revisando.get(6));
                    semantico(nombre, IdCara,x,y , tam, fila,col );
                }
                //Si es delete
                if(revisando.get(0).equals("delete")){
                    String Idcara= revisando.get(2);
                    semantico(nombre, Idcara,0,0, 0, fila, col);
                }
                //Si es sleep
                if(revisando.get(0).equals("sleep")){
                    String IdCara= revisando.get(2);
                    semantico(nombre, IdCara, 0, 0, 0, fila, col);
                }
                //Si es change
                if(revisando.get(0).equals("change")){
                    //como el metodo semantico no cuenta con 3 String de parámetros, le agregamos al parámetro de cara las 2
                    //Primero la cara original, y luego la nueva
                    //Separado por una coma
                    String caras = revisando.get(2)+","+revisando.get(4);
                    semantico(nombre, caras, 0, 0, 0, fila,col);
                }
                revisando.clear();
            }
        }
        else{
            if(sePuedeGraf){
                String v1="graficar",v2="";
                semantico(v1, v2, 0, 0, 0, fila, col);
            }
            if(!valor.equals("end")){
                //Token inesperado
                mandarErrorSintactico("Token inesperado depués del END.",fila, col);
            }
        }
    }
    public void semantico(String metodo, String cara, int x, int y, int tam, int fila, int col){
            switch(metodo){
                case "draw":
                    matrizDibujo[contMetodos][0]=String.valueOf(x);//x1 //nombre de la cara o si es borrar o algo asi
                    matrizDibujo[contMetodos][1]=String.valueOf(y);//y1
                    matrizDibujo[contMetodos][2]=String.valueOf(tam);
                    matrizDibujo[contMetodos][3]=cara;
                    matrizDibujo[contMetodos][4]=metodo;
                    contMetodos++;
                    break;
                case "delete":
                    matrizDibujo[contMetodos][0]=String.valueOf(x);//id
                    matrizDibujo[contMetodos][1]="0";
                    matrizDibujo[contMetodos][2]="0";
                    matrizDibujo[contMetodos][3]="0";
                    matrizDibujo[contMetodos][4]=metodo; //
                    contMetodos++;
                    break;
                case "sleep":
                    matrizDibujo[contMetodos][0]=String.valueOf(x);//id
                    matrizDibujo[contMetodos][1]="0";
                    matrizDibujo[contMetodos][2]="0";
                    matrizDibujo[contMetodos][3]="0";
                    matrizDibujo[contMetodos][4]=metodo; //
                    contMetodos++;
                    break;
                case "change":
                    String[] caras = cara.split(",");
                    matrizDibujo[contMetodos][0]=caras[1];//nuevo estado
                    matrizDibujo[contMetodos][1]=caras[0];//Cara original
                    matrizDibujo[contMetodos][2]="0";
                    matrizDibujo[contMetodos][3]="0";
                    matrizDibujo[contMetodos][4]=metodo; //
                    contMetodos++;
                    break;
                    //CUANDO vaya a graficar
                case "graficar":
                    /*String res= graficator.checador(matrizDibujo);
                    if("NP".equals(res)){
                        System.out.println("Se puede graficar!");
                        graficator.addMatriz(matrizDibujo);
                    }
                    else{
                        mandarErrorMensajeSemantico("Error por colicion en"+res, fila, col);
                    }
                    */
                    graficator.addMatriz(matrizDibujo);
                    w.Lienzo.add(graficator);
                    graficator.repaint();
                    System.out.println("Entro a graficar");
                    break;
            }
            
    }
    public void revisarColicion(){
    }
    public void vaciarDatos(){
        tablaS.vaciarDatos();
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

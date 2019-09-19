/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interprete;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 *
 * @author lalos
 */
public class tablaSimbolos {
    public Map<String,String> palabrasReservadas;
    public Map<String,String> operadores;
    public Map<String,String> separadores;
    public Map<String,String> tipoDato;
    public Map<String,Integer> lineas;
    public ArrayList<ArrayList<String>> ids;
    //metodos es para ir comparando las sentencias del Sintanctico (Interprete.sin) que concuerden con los valores de los metodos
    public Map<String,ArrayList<String>> metodos;
    public ArrayList <String>draw;
    public ArrayList <String>delete;
    public ArrayList <String>sleep;
    public ArrayList <String>change;
    
    //constructor inicializacion
    public tablaSimbolos(){
        //Aqui iran los ids que se vayan ocupando
         ids = new ArrayList<>();
         
         //Los espacios en blanco, saltos y final del archivo.
         separadores = new HashMap<>();
         separadores.put("espacio", " ");
         separadores.put("enter1", "\r");
         separadores.put("enter2", "\n");
         separadores.put("tab", "\t");
         separadores.put("tab", "/0");
         
         //Las palabras reservadas ocupadas en el lenguaje
         palabrasReservadas = new HashMap<>();
         palabrasReservadas.put("pro", "Programa");
         palabrasReservadas.put("begin", "Inicio");
         palabrasReservadas.put("draw", "DibujarCara");//3
         palabrasReservadas.put("delete", "EliminarCara");
         palabrasReservadas.put("sleep", "Dormir");
         palabrasReservadas.put("change", "CambiarModo");//6
         palabrasReservadas.put("sad", "triste");
         palabrasReservadas.put("angry", "enojada");
         palabrasReservadas.put("happy", "feliz");
         palabrasReservadas.put("sleepy", "dormida");
         palabrasReservadas.put("serio", "neutral");
         palabrasReservadas.put("end", "Fin");
         
         //Tipos de datos que pueden ingresarse en los metodos
         tipoDato = new HashMap<>();
         tipoDato.put("entero", "int");
         tipoDato.put("flotante", "float");
         tipoDato.put("cadena", "String");
        
         //A falta de suma, resta ... etc, se colcan estos divisores
         operadores = new HashMap<>();
         operadores.put("Parentesis Izquierdo", "(");
         operadores.put("Parentesis Derecho", ")");
         operadores.put("Coma", ",");
         
         //Defimos la estructura de cada uno de los metodos
         draw = new ArrayList();
         draw.add("(");
         draw.add("num");
         draw.add("Coma");
         draw.add("num");
         draw.add("Coma");
         draw.add("num");
         draw.add("Coma");
         draw.add("ID");
         draw.add("Coma");
         draw.add("modo");
         draw.add(")");
         
         delete = new ArrayList();
         delete.add("(");
         delete.add("ID");
         delete.add(")");
         
         sleep = new ArrayList();
         sleep.add("(");
         sleep.add("num");
         sleep.add(")");
         
         change = new ArrayList();
         change.add("(");
         change.add("ID");
         change.add("Coma");
         change.add("modo");
         change.add(")");
         
         //Agregamos a la lista de métodos, los que tenemos, a manera de usarlo como un id en Base de Datos
         metodos = new HashMap<>();
         metodos.put("draw", draw);
         metodos.put("delete", delete);
         metodos.put("sleep", sleep);
         metodos.put("change", change);
         
    }
    //agregar un identificador a la tabla
    public void agregarID(String nombre,int linea, int columna){
        ArrayList<String> nuevo = new ArrayList<>();
        nuevo.add(nombre);
        nuevo.add(Integer.toString(linea));
        nuevo.add(Integer.toString(columna));
        if(isID(nombre)){
            int pos=0;
            for(int i=0; i<ids.size(); i++){
                if( ids.get(i).get(0).equals(nombre) ){
                    pos=i;
                    break;
                }
            }
            ids.set(pos, nuevo);
        }
        else{
            ids.add(nuevo);
        }
    }
    //ver si el token es palabra reservada
    public boolean isPR(String token){
        //revisar si es palabra reservada
        return palabrasReservadas.values().stream().anyMatch((valor) -> ( valor.equals(token)));
    }
    //revisar si el token es un identificador
    public boolean isID(String token){
        //revisar si es un ID
        if(ids.isEmpty()){
            return false;
        }
        else{       
            for(int i=0; i<ids.size(); i++){
                if( ids.get(i).get(0).equals(token) ){
                    return true;
                }
            }
        return false;
        }   
    }
    //busca si pertenece a la tabla de simbolos y devolver el tipo
    public boolean isUnknow(String token){
        return !isPR(token) && !isID(token);
    }
    
    public String queOpeEs(String token){
        String valor="";
        for( Map.Entry entrada : operadores.entrySet()){
            //Si el valor del token es el mismo al regisro
            if(entrada.getValue().equals(token)){
                valor = (String)(entrada.getKey());
                break;
            }
        }
        return valor;
    }
    
    public String queTipoEs(String token){
        String valor="";
        for( Map.Entry entrada : tipoDato.entrySet()){
            //Si el valor del token es el mismo al regisro
            if(entrada.getValue().equals(token.getClass())){
                valor = (String)(entrada.getValue());
                break;
            }
        }
        return valor;
    }
    
    public String cualPrES(String token){
        String valor="";
        for( Map.Entry entrada : palabrasReservadas.entrySet() ){
            //Si el valor del token es el mismo al regisro
            if(entrada.getValue().equals(token)){
                valor = (String)(entrada.getKey());
                return valor;
            }
        }
        return null;
    }
    
    public boolean esSeparador(String token){
        for( Map.Entry entrada : separadores.entrySet()){
            //Si el valor del token es el mismo al regisro
            if(entrada.getValue().equals(token)){
                return true;
            }
        }
        return false;
    }
    
    public void vaciarIds(){
        if(!ids.isEmpty()) ids.clear();
    }
}

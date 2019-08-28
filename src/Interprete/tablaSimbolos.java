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
    
    //constructor inicializacion
    public tablaSimbolos(){
         ids = new ArrayList<>();
         metodos = new HashMap<>();
         tipoDato = new HashMap<>();
         separadores = new HashMap<>();
         operadores = new HashMap<>();
         palabrasReservadas = new HashMap<>();
         
         separadores.put("espacio", " ");
         separadores.put("enter", "\r");
         
         palabrasReservadas.put("pro", "Programa");
         palabrasReservadas.put("begin", "Inicio");
         palabrasReservadas.put("end", "Fin");
         palabrasReservadas.put("draw", "DibujarCara");
         palabrasReservadas.put("delete", "EliminarCara");
         palabrasReservadas.put("sleep", "Dormir");
         palabrasReservadas.put("change", "CambiarModo");
         palabrasReservadas.put("sad", "triste");
         palabrasReservadas.put("angry", "enojada");
         palabrasReservadas.put("happy", "feliz");
         palabrasReservadas.put("sleepy", "dormida");
         palabrasReservadas.put("serio", "neutral");
         
         tipoDato.put("entero", "int");
         tipoDato.put("flotante", "float");
         tipoDato.put("cadena", "String");
        
         operadores.put("Parentesis Izquierdo", "(");
         operadores.put("Parentesis Derecho", ")");
         operadores.put("Coma", ",");
         
         ArrayList draw = new ArrayList();
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
         metodos.put("draw", draw);
         
         ArrayList delete = new ArrayList();
         delete.add("(");
         delete.add("ID");
         delete.add(")");
         metodos.put("draw", delete);
         
         ArrayList sleep = new ArrayList();
         sleep.add("(");
         sleep.add("num");
         sleep.add(")");
         metodos.put("draw", sleep);
         
         ArrayList change = new ArrayList();
         change.add("(");
         change.add("ID");
         change.add("Coma");
         change.add("modo");
         change.add(")");
         metodos.put("draw", change);
         
    }
    //agregar un identificador a la tabla
    public void agregarID(String nombre,int linea, int columna){
        ArrayList<String> nuevo = new ArrayList<>();
        nuevo.add(nombre);
        nuevo.add(Integer.toString(linea));
        nuevo.add(Integer.toString(columna));
        ids.add(nuevo);
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

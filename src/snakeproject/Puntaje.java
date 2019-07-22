package snakeproject;

import java.io.Serializable;
import java.util.ArrayList;
    
    /**
     *  Clase que contiene los 10 nombres y sus puntajes que se guardaran
     * @author Geiver Botello
     * @version 1.0 Feb/2017
     */

public class Puntaje implements Serializable {
    /**
     * 10 registros de puntos
     * 10 registros de nombres
     */
    ArrayList<Integer> Puntos;
    ArrayList<String> Nombres;
    /**
     *   Constructor que carga 10 registros de nombre y puntos 
     * @param Nombres arraylist con 10 datos
     * @param Puntos arraylist con 10 datos
     */
    public Puntaje(ArrayList<String> Nombres, ArrayList<Integer> Puntos) {
        
        this.Nombres = Nombres;
        this.Puntos = Puntos;  
        
    }
   /**
    * Constructor que inicializa los constructores vacios 
    */
    public Puntaje() {
        Nombres = new ArrayList<String>();
        Puntos = new ArrayList<Integer>();
    }
}

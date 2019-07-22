package snakeproject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

   /**
    * Guarda y lee los 10 mejores registros del juego
    * @author Geiver Botello
    * @version 1.0 Feb/2017
    */

public class Archivo {
    //Atributos necesarios de la clase
    Puntaje ObjetoPuntos;

    /**
     * Constructor que me carga el objeto de tipo Puntaje
     * @param p Trae un objeto de tipo Puntaje
     */
    public Archivo(Puntaje p) {
        ObjetoPuntos = p;
    }
    
       
    
    /**
     *  Guarda un Objeto de tipo puntaje en un archivo creado llamado Puntaje.obj
     * @throws FileNotFoundException
     * @throws IOException 
     */
    
    public void GuardarRegistro() throws FileNotFoundException, IOException {
    
        FileOutputStream fileOut = new FileOutputStream("Puntaje.obj");
        ObjectOutputStream salida = new ObjectOutputStream(fileOut);
        salida.writeObject(ObjetoPuntos);
        salida.close();
        
    }
    
    /**
     * Lee un objeto de tipo puntaje del archivo Puntaje.obj
     * @throws FileNotFoundException
     * @throws IOException
     * @throws ClassNotFoundException 
     */
    
    public void LeerRegistro() throws FileNotFoundException, IOException, ClassNotFoundException {
         
        FileInputStream fileIn = new FileInputStream("Puntaje.obj");
        ObjetoPuntos = null;
        while (fileIn.available() > 0) {
            ObjectInputStream entrada = new ObjectInputStream(fileIn);
            ObjetoPuntos = (Puntaje) entrada.readObject();

        }
        fileIn.close();
       
       
    }
}

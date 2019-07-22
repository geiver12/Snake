
package snakeproject;

import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
* Se puede crear una ventana a partir de esta clase
* @author Geiver Botello
* @version 1.0
*/
 public class Ventana extends JFrame{
    
    /**
     * Constructor para crear una ventana a partir de los parametros respectivos.
     * @param snakeProject Nombre de la ventana
     * @param ancho Ancho de la ventana
     * @param alto  Alto de la ventana
     */
    
    public  Ventana(String snakeProject,int ancho,int alto) { 
        super(snakeProject);
        
        
        setSize(ancho,alto);
        setResizable(false);
        setLocationRelativeTo(null);
        setLayout(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setIconImage((new ImageIcon(getClass().getResource("/Imagenes/inst.jpg"))).getImage());
        setVisible(true);
          
    }
}

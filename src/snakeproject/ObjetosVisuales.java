package snakeproject;

import java.awt.Image;
import java.net.URL;
import java.util.ArrayList;
import javax.swing.ImageIcon;

    /**
     *  Carga objetos visuales para el panel del juego
     * @author Geiver Botello
     * @version 1.0 Feb/2017
     */
public class ObjetosVisuales {
    /**
     * Ancho :: Indica el ancho de un objeto visual
     * Alto :: Indica el alto de un objeto visual
     * y :: Posicion vertical  de un objeto visual
     * x :: Posicion horizontal  de un objeto visual
     * Tiempo:: Cuanto tiempo durara una imagen en el lienzo
     */
    int ancho, alto, y, x,Tiempo;;
    /**
     * Direccion :: Indica el punto cardinal hacia donde se pueda mover una imagen
     */
    char Direccion;
    /**
     *  L_Limite y L_Sentido  Son listas que guardan todos los movimientos que seguira el cuerpo de la culebra 
     *  teniendo como pionera la cabeza
     */
    ArrayList<Integer> L_limite;
    ArrayList<Character> L_Sentido;
    /**
     * Imagen:: Contenedor de las variables externas que se mostraran en el lienzo
     */
    Image Imagen;
    
    /**
     * Constructor que carga las variables necesarias de imagenes procesadas con paint
     * @param x Ubicacion horizontal de la imagen
     * @param y Ubicacion vertical de la imagen
     * @param ancho Ancho de la imagen
     * @param alto Alto de la imagen 
     * @param Direccion punto cardinal hacia donde se movera una imagen
     */
    public ObjetosVisuales(int x, int y, int ancho, int alto, char Direccion) {
    
        this.ancho =  ancho;
        this.alto =  alto;
        this.y =  y;
        this.x =  x;
        this.Direccion = Direccion;
        L_Sentido = new ArrayList<Character>();
        L_limite = new ArrayList<>();
       
    }
    
    /**
    * Constructor que carga imagenes externas con rutas
    * @param x Posicion horizontal de la imagen en el lienzo
    * @param y Posicion vertical de la imagen en el lienzo
    * @param desc Se usa para carga una imagen distinta
    * @param Tiempo Cuanto durara la imagen en eliminarse si no es atrapada por la culebra
    */
    public ObjetosVisuales(int x, int y, char desc,int Tiempo) {
             
        this.y = (short) y;
        this.x = (short) x;
        CargarImagen(desc);
        this.Tiempo=Tiempo;
         
    }
    /**
     * Todas las imagenes se mueven por este metodo
     * @param Direccion Punto cardinal de la imagen, hacia esa direccion se movera
     */
    public void MovimientoGeneral(char Direccion)//Posicion 0
    {
        
        switch (Direccion) {
            case 'n':
                y--;
                break;
            case 's':
                y++;
                break;
            case 'o':
                x--;
                break;
            case 'e':
                x++;
                break;
        }
       
    }
        /**
         * El cuerpo de la culebra seguira a la cabeza, estas serviran para el corecto funcionamiento de la secuencia
         * @param direccion Punto Cardinal hacia donde se movera el cuerpo 
         * @param posicionfinal Carga una posicion final para dicho movimiento e iniciar uno nuevo
         */
        public void AnadirPosicion(char direccion, int posicionfinal) {
            
        L_Sentido.add(direccion);
        L_limite.add(posicionfinal);
     
    }
        /**
         *  Movimiento del Cuerpo y actualizacion del ultimo limite
         */
    public void MovimientoCuerpo() {
                   
        //Va Modificando el nuevo limite!!
        if (L_Sentido.get(L_Sentido.size() - 1) == 'e') {
            L_limite.set(L_limite.size() - 1,  (L_limite.get((L_limite.size()) - 1) + 1));
        }
        if (L_Sentido.get(L_Sentido.size() - 1) == 'o') {
            L_limite.set(L_limite.size() - 1,  (L_limite.get((L_limite.size()) - 1) - 1));
        }
        if (L_Sentido.get(L_Sentido.size() - 1) == 'n') {
            L_limite.set(L_limite.size() - 1,  (L_limite.get((L_limite.size()) - 1) - 1));
        }
        if (L_Sentido.get(L_Sentido.size() - 1) == 's') {
            L_limite.set(L_limite.size() - 1, (L_limite.get((L_limite.size()) - 1) + 1));
        }

        MovimientoGeneral(L_Sentido.get(0));//Se mueve el cuerpo

        if ((L_Sentido.get(0) == 'e' && x >= L_limite.get(0))//Borra el primer registro de la serpiente
                || (L_Sentido.get(0) == 'o' && x <= L_limite.get(0))
                || (L_Sentido.get(0) == 'n' && y <= L_limite.get(0))
                || (L_Sentido.get(0) == 's' && y >= L_limite.get(0))) {
            L_Sentido.remove(0);
            L_limite.remove(0);
        }
 
    }
    /**
     *  Metodo para determinar si los objetos chocan entre ellos
     * @param y Posicion vertical del objeto a evaluar
     * @param x Posicion horizontal del objeto a evaluar
     * @param dimy Alto de la imagen a evaluar
     * @param dimx Ancho de la imagen a evaluar
     * @return Indica si las imagenes chocan entre ellas
     */
    public boolean Colision(int y, int x, int dimy, int dimx) {
 

        return ((((y >= this.y && y < this.y + alto) || (this.y >= y && this.y <= y + dimy)))
                && ((x >= this.x && x <= this.x + ancho) || (this.x >= x && this.x <= x + dimx)));
    }
       /**
        *  Metodo que carga todas las imagenes externas
        * @param desc Indica que tipo de imagen se cargara
        */
    private void CargarImagen(char desc) {
        
            int al=0;
            if(desc=='f')
            {
                al=(int) (Math.random()*9+1);
                ImageIcon imagen = new ImageIcon(getClass().getResource("/Imagenes/"+al+".png"));
                Imagen = imagen.getImage();
                ancho= imagen.getIconWidth();
                alto = imagen.getIconHeight();
            }else
            {
                   al=(int) (Math.random()*2+1);
                ImageIcon imagen = new ImageIcon(getClass().getResource("/Imagenes/o"+al+".png"));
                Imagen = imagen.getImage();
                ancho= imagen.getIconWidth();
                alto = imagen.getIconHeight();
            }
       
    }
       /**
        * Metodo para quitar imagenes del lienzo
        * @param Tiempo Cuanto va en el juego desde que nacio la imagen
        * @param tardanza Indica si se esta en el tiempo de borrar la imagen
        * @return Indica si se borra o no la imagen
        */
    public boolean LimiteTiempo(int Tiempo,int tardanza)
    {
    
        if(Tiempo>this.Tiempo+tardanza)
            return true;
        
        return false;
    }
}

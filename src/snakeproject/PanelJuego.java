package snakeproject;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;

    /**
     * Contiene el lienzo del juego y los metodos mas importantes para el correcto funcionamiento
     * @author Geiver Botello
     * @version 1.0 Feb/2017
     */
public class PanelJuego extends JPanel implements Runnable, KeyListener {
    /**
    * Sentido:: Direccion en el punto cardinal general 
    * direccionSentido:: Hay una pequeña diferencia de tiempo para que la culebra cargue un  
    * nuevo punto cardinal hasta este momento aqui esta contenida la direccion
    * */
    char RetardadorSentido, Sentido;
    /**
     * Puntaje:: Cantidad acumulada hasta una muerte que se actualizara una vez que muera el jugador 
     * vidas :: Numero de vidas disponibles por partida
     * NumeroComida:: Sirve para saber en que momento agregar obstaculos y comida extr
     */
    short Puntaje, Vidas, NumeroComida;
    
    /**
     * Cuerpo de la culebra
     */
    ArrayList<ObjetosVisuales> Snake;
    /**
     * Indica los obstaculos del juego
     */
    ArrayList<ObjetosVisuales> Obstaculos;
    /**
     * Comida para la culebra
     */
    ArrayList<ObjetosVisuales> Comida;
    /**
     * NuevoMov:: Indica el tiempo de espera para cargar un nuevo movimiento, con ella la culebra no choca con ella misma
     *  Tiempo:: Reloj del juego, con esta sabremos cuanto tardara un objeto en borrarse
     */
    int NuevoMov, Tiempo;
    /**
     * ActivadorMuerte:: Detiene el juego cada vez que la culebra muere
     * Finalizojuego :: Detiene el juego cuando no quedan mas vidas
     * La cabeza nunca deja de moverse, funciona para que no se separe del cuerpo
     */ 
    boolean ActivadorMuerte, FinalizoJuego, Pulso;
    /**
     * Es el nombre del usuario introducido en el joptionpane
     */
    String Nombre;
    /**
     * Contenedor de los mejores registros con ella podremos hacer actualizaciones a los mejores puntajes
     */
    Archivo Lista;
    /**
     *  Constructor que inicializa las variables del juego
     * @param ancho Ancho del lienzo del juego
     * @param alto Alto del lienzo del juego
     * @param Nombre Descripcion del jugador de momento
     * @param ar Archivos donde estan contenidos los mejores registros
     */
    
    public PanelJuego(short ancho, short alto, String Nombre, Archivo ar) {
        
        setSize(ancho, alto);
        setBackground(Color.red);
        
        Snake = new ArrayList<>();
        Comida = new ArrayList<>();
        Obstaculos = new ArrayList<>();

        this.Nombre = Nombre;
        Lista = ar;
        Reiniciar();
        this.addKeyListener(this);
       
    }

    /**
     *  Metodo para pintar en el panel o lienzo
     * @param g Es el lienzo donde se podra pintar
     */
    public void paint(Graphics g) {
        super.paint(g);
       
        
        g.setColor(Color.white); //Pintamos el area de juego
        g.fillRect(50, 50, 700, 500);
        g.setColor(Color.blue);

        for (int i = 0; i < Comida.size(); i++) {   // Pintamos todos los tipos de comdia
            if (i == 0) {
                g.fillOval(Comida.get(i).x, Comida.get(i).y, 10, 10);
            } else {
                g.drawImage(Comida.get(i).Imagen, Comida.get(i).x, Comida.get(i).y, null);
            }
        }

        for (ObjetosVisuales Obstaculo : Obstaculos) {//Pintamos los obstaculos 
            g.drawImage(Obstaculo.Imagen, Obstaculo.x, Obstaculo.y, null);
        }

        for (int i = 0; i < Snake.size(); i++) { // Pintamos toda la culebra
            ObjetosVisuales get = Snake.get(i);
            g.setColor(Color.green);
            g.fillOval(get.x, get.y, get.ancho, get.alto);
            if (i == 0) {
                g.setColor(Color.white);
                g.fillOval(get.x + 5, get.y + 5, 6, 6);
                g.fillOval(get.x + 12, get.y + 5, 6, 6);
            }
        }

        g.setColor(Color.black);        //Aqui inicia todos los tableros extras al juego
        g.setFont(new Font("Arial", Font.ITALIC, 20));
        g.drawString(Nombre + ":::" + Puntaje, 20, 20);
        g.drawString(Lista.ObjetoPuntos.Nombres.get(0) + ":::" + Lista.ObjetoPuntos.Puntos.get(0), 220, 20);
        g.setColor(Color.blue);
        g.drawString("Vidas::" + Vidas, 700, 20);

        if (!FinalizoJuego) {   
            if (ActivadorMuerte) { // Se muestra cuando muere el jugador
                g.setColor(Color.red);
                g.setFont(new Font("Serif", Font.BOLD, 30));
                g.drawString("Muerto en batalla", 300, 200);
                g.setColor(Color.green);
                g.drawString("Pulse espacio para continuar", 250, 400);
            }
        } else {
            g.setColor(Color.blue);
            g.setFont(new Font("Serif", Font.BOLD, 30));    // Se muestra cuando se acaban las vidas
            g.drawString("Juego Finalizado", 250, 200);
            g.drawString("Pulse ENTER para jugar de nuevo", 150, 300);
            g.drawString("Puntaje::", 250, 400);
            g.setColor(Color.black);
            g.drawString("" + Puntaje, 400, 400);
        }
      
    }

    /**
     * Metodo que sera el motor del juego
     */
    public void run() {
        
        while (true) {
            long  in,fin;
        System.out.println("Inicio: "+(in=System.nanoTime()));
        
            try {
                Thread.sleep(7);
            } catch (InterruptedException ex) {
                Logger.getLogger(PanelJuego.class.getName()).log(Level.SEVERE, null, ex);
            }
            this.repaint();
            Tiempo++;
            if (!FinalizoJuego) {
                if (!ActivadorMuerte) {
                    ActualizarMovimiento();
                    NuevoMov++;
                }
            }
            System.out.println("Final: "+(fin=System.nanoTime()));
        System.out.println("Diferencia: "+(fin-in)); 
        }
    }

    /**
     * Evento de teclado
     * @param e parametro para trabajar las pulsaciones
     */
    public void keyTyped(KeyEvent e) {
    }
    /**
     * Evento de teclado
     * @param e parametro para trabajar las pulsaciones
     */
    public void keyReleased(KeyEvent e) {
    }
    /**
     * Evento de teclado
     * @param e parametro para trabajar las pulsaciones
     */
    public void keyPressed(KeyEvent e) {
        //Presiono flecha arriba
        if (e.getKeyCode() == KeyEvent.VK_UP && Sentido != 's') {
            RetardadorSentido = 'n';
            //Presiono flecha abajo
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN && Sentido != 'n') {
            RetardadorSentido = 's';
            //Presiono flecha izquierda
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT && Sentido != 'e') {
            RetardadorSentido = 'o';
            //Presiono flecha derecha
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT && Sentido != 'o') {
            RetardadorSentido = 'e';
        }
        if ((e.getKeyCode() == KeyEvent.VK_SPACE && ActivadorMuerte) && !FinalizoJuego) {
            ReiniciarCulebra();
        }
        if (e.getKeyCode() == KeyEvent.VK_ENTER && FinalizoJuego) {
            Reiniciar();
        }
    }

    /**
     * Actualiza el movimiento de la culebra
     */
    private void ActualizarMovimiento() {
        
        if (NuevoMov > 20) {
            Sentido = RetardadorSentido;
            NuevoMov = 0;
            Pulso = true;
        }
        if (Pulso) {
            Pulso = false;
            CambiarLimite(Sentido);
        }

        Snake.get(0).MovimientoGeneral(Sentido);

        for (int i = 1; i < Snake.size(); i++) {
            Snake.get(i).MovimientoCuerpo();
        }
        ListaColisiones();
        
    }

    /**
     * genera la comida para el juego y llama el metodo GenerarObstaculos si puede cumplirse las condiciones
     */
    private void GenerarComida() {
        short y = 0, x = 0;
        boolean nocolisiona;
            
        if (Comida.isEmpty()) {
            Comida.add(new ObjetosVisuales(0, 0, 10, 10, ' '));
        }

        for (int k = 0; k < 2; k++) {
            nocolisiona = true;
            while (nocolisiona) {
                if(k==0){
                x = (short) (Math.random() * 690 + 55);
                y = (short) (Math.random() * 490 + 55);
                }else
                {
                x = (short) (Math.random() * 650 + 55);
                y = (short) (Math.random() * 450 + 55);
                }
                for (ObjetosVisuales Snake1 : Snake) {
                    if (Snake1.Colision(y, x, (short) 10, (short) 10)) {
                        nocolisiona = true;
                        break;
                    } else {
                        nocolisiona = false;
                    }
                }
                for (ObjetosVisuales Obstaculo : Obstaculos) {
                    if (Obstaculo.Colision(y, x, (short) 10, (short) 10)) {
                        nocolisiona = true;
                        break;
                    } else {
                        nocolisiona = false;
                    }
                }
            }
            if (NumeroComida % 3 == 0 && NumeroComida != 0 && k == 1) {
                Comida.add(new ObjetosVisuales(x, y, 'f', Tiempo));
                if (Comida.get(1).Colision(Comida.get(0).y, Comida.get(0).x, 10,  10)) {
                    k--;
                    Comida.remove(1);
                }
            }
            if (k == 0) {
                Comida.get(k).x = x;
                Comida.get(k).y = y;
            }
        }
        if (NumeroComida % 2 == 0 && NumeroComida != 0) {
            GenerarObstaculo();
        }
         
    }
    /**
     * Genera obstaculos para darle dificultad al jugador
     */
    private void GenerarObstaculo() {
        short y = 0, x = 0;
        boolean nocolisiona = true;
        
    
        while (nocolisiona) {
            x = (short) (Math.random() * 650 +55);
            y = (short) (Math.random() * 450 + 55);
            nocolisiona = false;
            Obstaculos.add(new ObjetosVisuales(x, y, 'a', Tiempo));
            for (ObjetosVisuales Snake1 : Snake) {
                if (!Obstaculos.isEmpty()) {
                    if (Snake1.Colision(y, x, Obstaculos.get(Obstaculos.size() - 1).alto, Obstaculos.get(Obstaculos.size() - 1).ancho)) {
                        Obstaculos.remove(Obstaculos.size() - 1);
                        nocolisiona = true;
                    }
                }
            }
            for (ObjetosVisuales Comida1 : Comida) {
                if (!Obstaculos.isEmpty()) {
                    if (Comida1.Colision(y, x, Obstaculos.get(Obstaculos.size() - 1).alto, Obstaculos.get(Obstaculos.size() - 1).ancho)) {
                        Obstaculos.remove(Obstaculos.size() - 1);
                        nocolisiona = true;
                    }
                }
            }
        }
        
    }


    /**
     * Actualiza los nuevos limites que debera seguir el cuerpo de la culebra
     * @param Sentido Indica el punto ordinal que se cargara
     */
    private void CambiarLimite(char Sentido) {
        for (int i = 1; i < Snake.size(); i++) {
            if (Sentido == 'n' || Sentido == 's') {
                Snake.get(i).AnadirPosicion(Sentido, (short) (Snake.get(0).y));
            } else {
                Snake.get(i).AnadirPosicion(Sentido, (short) (Snake.get(0).x));
            }
        }
    }

    /**
     * reinicia el juego cuando no hay vidas
     */
    private void Reiniciar() {
        
        Vidas = 4;
        NuevoMov = 0;
        ReiniciarCulebra();
        FinalizoJuego = false;
        
    }

    /**
     * Reinicia el juego cuando la culebra muere
     */
    private void ReiniciarCulebra() {
        
        
        RetardadorSentido = 'e';
        Puntaje = 0;
        Sentido = 'e';
        Vidas--;
        Tiempo = 0;
        Pulso = false;
        ActivadorMuerte = false;
        NumeroComida = 0;

        if (!Snake.isEmpty()) {
            Snake.clear();
        }

        Snake.add(new ObjetosVisuales(400, 300, 20, 20, Sentido));
        Snake.add(new ObjetosVisuales(380, 300, 20, 20, Sentido));
        Snake.get(1).AnadirPosicion(Sentido, (short) (Snake.get(0).x));

        for (int i = 0; i < 3; i++) {
            CrecerCulebra();
        }
        GenerarComida();
        
    }

    /**
     * Anadirle una nueva porcion de cuerpo a la culebra
     */
    private void CrecerCulebra() {
        
        if (Snake.get(Snake.size() - 1).L_Sentido.get(0) == 'n') {
            Snake.add(new ObjetosVisuales(Snake.get(Snake.size() - 1).x, Snake.get(Snake.size() - 1).y + 20, 20, 20, Snake.get(Snake.size() - 1).L_Sentido.get(0)));
            for (int i = 0; i < Snake.get(Snake.size() - 2).L_limite.size(); i++) {
                Snake.get(Snake.size() - 1).AnadirPosicion(Snake.get(Snake.size() - 2).L_Sentido.get(i), Snake.get(Snake.size() - 2).L_limite.get(i));
            }
        }
        if (Snake.get(Snake.size() - 1).L_Sentido.get(0) == 's') {
            Snake.add(new ObjetosVisuales(Snake.get(Snake.size() - 1).x, Snake.get(Snake.size() - 1).y - 20, 20, 20, Snake.get(Snake.size() - 1).L_Sentido.get(0)));
            for (int i = 0; i < Snake.get(Snake.size() - 2).L_limite.size(); i++) {
                Snake.get(Snake.size() - 1).AnadirPosicion(Snake.get(Snake.size() - 2).L_Sentido.get(i), Snake.get(Snake.size() - 2).L_limite.get(i));
            }
        }
        if (Snake.get(Snake.size() - 1).L_Sentido.get(0) == 'o') {
            Snake.add(new ObjetosVisuales(Snake.get(Snake.size() - 1).x + 20, Snake.get(Snake.size() - 1).y, 20, 20, Snake.get(Snake.size() - 1).L_Sentido.get(0)));
            for (int i = 0; i < Snake.get(Snake.size() - 2).L_limite.size(); i++) {
                Snake.get(Snake.size() - 1).AnadirPosicion(Snake.get(Snake.size() - 2).L_Sentido.get(i), Snake.get(Snake.size() - 2).L_limite.get(i));
            }
        }
        if (Snake.get(Snake.size() - 1).L_Sentido.get(0) == 'e') {
            Snake.add(new ObjetosVisuales(Snake.get(Snake.size() - 1).x - 20, Snake.get(Snake.size() - 1).y, 20, 20, Snake.get(Snake.size() - 1).L_Sentido.get(0)));
            for (int i = 0; i < Snake.get(Snake.size() - 2).L_limite.size(); i++) {
                Snake.get(Snake.size() - 1).AnadirPosicion(Snake.get(Snake.size() - 2).L_Sentido.get(i), Snake.get(Snake.size() - 2).L_limite.get(i));
            }
        }
    }

    /**
     * Metodo que contiene todas las colisiones del juego
     */
    private void ListaColisiones() {
        
        for (int i = 0; i < Comida.size(); i++) {
            if (Snake.get(0).Colision(Comida.get(i).y, Comida.get(i).x, Comida.get(i).alto, Comida.get(i).ancho)) {//Comida
                CrecerCulebra();
                NumeroComida++;
                if (i == 0) {
                    Puntaje += 5;
                    GenerarComida();
                } else {
                    if (NumeroComida % 3 == 0 && NumeroComida != 0) {
                        Puntaje += 10;
                        Comida.remove(1);
                    }
                }
            }
            if (Comida.size() > 1) {
                if (i != 0 && Comida.get(i).LimiteTiempo(Tiempo, 1000)) {
                    Comida.remove(1);
                }
            }
        }
        for (int i = 0; i < Obstaculos.size(); i++) {
            if (Snake.get(0).Colision(Obstaculos.get(i).y, Obstaculos.get(i).x, Obstaculos.get(i).alto, Obstaculos.get(i).ancho)) {
                MuerteCulebra();
            }
        }

        if (!Obstaculos.isEmpty()) // si pasa el tiempo y no hay choque elimine el obstaculo
        {
            for (int i = 0; i < Obstaculos.size(); i++) {
                if (Obstaculos.get(i).LimiteTiempo(Tiempo, 2000)) {
                    Obstaculos.remove(i);
                }
            }
        }

        if (Snake.get(0).x < 45 || Snake.get(0).x > 738 || Snake.get(0).y < 45 || Snake.get(0).y > 535)//Salio del campo
        {
            MuerteCulebra();
        }
        for (int i = 3; i < Snake.size(); i++) {//choca con su cuerpo
            if (Snake.get(i).Colision((short) (Snake.get(0).y), (short) (Snake.get(0).x), (short) (Snake.get(0).ancho), (short) Snake.get(0).alto)) {
                MuerteCulebra();
            }
        }
        
    }

    /**
     * Detiene el movimiento de los objetos del juego
     */
    private void MuerteCulebra() {
       
        Obstaculos.clear();
        Comida.clear();
        if (Vidas > 0) {
            ActivadorMuerte = true;
            GuardarPuntaje();
        } else {
            FinalizoJuego = true;
        }
         
    }
    /**
     * Actualiza los puntajes del juego si alguno! se encuentra entre los 10 mejores
     */
    private void GuardarPuntaje() {
        boolean dentre = false;
       
        
        for (int i = 0; i < 10; i++) {
            if (Puntaje > Lista.ObjetoPuntos.Puntos.get(i)) {
                Lista.ObjetoPuntos.Nombres.add(i, Nombre);
                Lista.ObjetoPuntos.Puntos.add(i, (int) (Puntaje));
                dentre = true;
                break;
            }
        }
        if (dentre) {
            Lista.ObjetoPuntos.Nombres.remove(10);
            Lista.ObjetoPuntos.Puntos.remove(10);
        }
        try {
            Lista.GuardarRegistro();
        } catch (IOException ex) {
            Logger.getLogger(PanelJuego.class.getName()).log(Level.SEVERE, null, ex);
        }
         
    }
}

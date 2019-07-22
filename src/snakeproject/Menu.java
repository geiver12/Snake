package snakeproject;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * Carga el menu del juego y los distintos paneles
 *
 * @author Geiver Botello
 * @version 1.0 Feb/2017
 */
public class Menu extends Ventana {

    JButton NuevoJuego, Puntaje, Instrucciones, Salir;
    PanelJuego ContJuego;
    JPanel puntaje_instruccion;
    JLabel portada;
    Archivo MejoresPuntajes;

    /**
     * Constructor que carga todos los botones y la ventana
     *
     * @param nombre descripcion de la ventana
     * @param ancho ancho de la ventana
     * @param alto alto de la ventana
     */

    public Menu(String nombre, int ancho, int alto) {
        super(nombre, ancho, alto);
        
        
        
        portada  = new JLabel(new ImageIcon(getClass().getResource("/Imagenes/portada.jpg")));
        portada.setBounds(0, 0, 800, 600);
        add(portada);
        
        NuevoJuego = new JButton("Nuevo Juego");
        NuevoJuego.setBounds(250, 100, 300, 50);
        NuevoJuego.setBackground(Color.BLUE);
        NuevoJuego.setForeground(Color.white);
        NuevoJuego.setFont(new Font("arial", Font.BOLD, 18));
        
        
        MejoresPuntajes = new Archivo(null);
        try {
            File f = new File("Puntaje.obj");
            if (!f.exists()) {
                CrearFichero();
            }
            MejoresPuntajes.LeerRegistro();
        } catch (IOException ex) {
            Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
        }

        add(NuevoJuego);
        NuevoJuego.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String Nombre = JOptionPane.showInputDialog(null, "Ingrese Su Nick antes de comenzar");
                if (Nombre != null) {
                    if (Nombre.length() >= 2) {
                        VaciarVentana(Nombre, 1);
                    }
                }
            }
        });

        Puntaje = new JButton("Puntajes");
        Puntaje.setBounds(250, 200, 300, 50);
        Puntaje.setBackground(Color.BLUE);
        Puntaje.setForeground(Color.white);
        Puntaje.setFont(new Font("arial", Font.BOLD, 18));
        add(Puntaje);
        Puntaje.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                VaciarVentana("", 2);
            }
        });

        Instrucciones = new JButton("Instrucciones");
        Instrucciones.setBounds(250, 300, 300, 50);
        Instrucciones.setBackground(Color.BLUE);
        Instrucciones.setForeground(Color.white);
        Instrucciones.setFont(new Font("arial", Font.BOLD, 18));
        add(Instrucciones);

        Instrucciones.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                VaciarVentana("", 3);
            }
        });

        Salir = new JButton("Salir");
        Salir.setBounds(250, 400, 300, 50);
        Salir.setBackground(Color.BLUE);
        Salir.setForeground(Color.white);
        Salir.setFont(new Font("arial", Font.BOLD, 18));
        add(Salir);
        Salir.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        portada.updateUI();
        Puntaje.updateUI();
        NuevoJuego.updateUI();
        Salir.updateUI();
        Instrucciones.updateUI();
        
        
        
    }

    /**
     * Metodo para limpiar el panel anterior y agregar uno nuevo
     *
     * @param Nombre Descripcion del jugador
     * @param labor Numero del panel que debe mostrarse
     */
    public void VaciarVentana(String Nombre, int labor) {
         
        ActualizarBotones(false);

        if (labor == 1) {
            ContJuego = new PanelJuego((short) 800, (short) 600, Nombre, MejoresPuntajes);
            new Thread(ContJuego).start();
            add(ContJuego);
            ContJuego.requestFocus();
        }
        if (labor == 2 || labor == 3) {
            
            puntaje_instruccion=new JPanel();
            puntaje_instruccion.setSize(800,600);
            puntaje_instruccion.setLayout(null);
            add(puntaje_instruccion);
            portada.setVisible(false);
            
            puntaje_instruccion.addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent me) {
                puntaje_instruccion.setVisible(false);
               // portada.setVisible(true);
                ActualizarBotones(true); 
                
                }
            });
                
            if (labor == 2) {
                ArrayList<JLabel> obj = new ArrayList<JLabel>();
                for (int i = 0; i < MejoresPuntajes.ObjetoPuntos.Nombres.size(); i++) {
                    obj.add(new JLabel((i + 1) + "---" + MejoresPuntajes.ObjetoPuntos.Nombres.get(i) + ":::" + MejoresPuntajes.ObjetoPuntos.Puntos.get(i)));
                }

                JLabel l = new JLabel("10 Mejores Puntajes");
                l.setBackground(Color.WHITE);
                l.setBounds(200, 20, 450, 80);
                l.setFont(new Font("Serif", Font.BOLD, 50));
                puntaje_instruccion.add(l);

                for (int i = 0, y = 100; i < obj.size(); i++, y += 40) {
                    obj.get(i).setFont(new Font("Serif", Font.BOLD, 20));
                    obj.get(i).setForeground(Color.red);
                    obj.get(i).setBounds(250, y, 200, 30);
                    puntaje_instruccion.add(obj.get(i));
                }
                puntaje_instruccion.setBackground(Color.black);
            } else {
                //Instrucciones!! la idea escargar una imagen de 800 *600 en el panel y listo!!
                JLabel l1 = new JLabel();
                URL url = this.getClass().getResource("/Imagenes/inst.jpg");
                l1.setIcon(new ImageIcon(url));
                l1.setBounds(0, 0, 800, 600);
                puntaje_instruccion.add(l1);
            }
            repaint();
            puntaje_instruccion.updateUI();
            puntaje_instruccion.repaint();
        }
      
    }

    /**
     * Muestra o quita temporalmente los botones
     *
     * @param enable activa o desactiva botones
     */
    public void ActualizarBotones(boolean enable) {
        
        if(enable){
        portada  = new JLabel(new ImageIcon(getClass().getResource("/Imagenes/portada.jpg")));
        portada.setBounds(0, 0, 800, 600);
        add(portada);
        }
        this.NuevoJuego.setVisible(enable);
        this.Puntaje.setVisible(enable);
        this.Instrucciones.setVisible(enable);
        this.Salir.setVisible(enable);
        
        portada.updateUI();
        Puntaje.updateUI();
        NuevoJuego.updateUI();
        Salir.updateUI();
        Instrucciones.updateUI();
        
    }

    /**
     * Crea un archivo en caso de no existir
     */
    private void CrearFichero() {
        
        ArrayList<String> nombre = new ArrayList<String>();
        ArrayList<Integer> puntos = new ArrayList<Integer>();
        for (int i = 0; i < 10; i++) {
            nombre.add("*******");
            puntos.add(0);
        }
        Puntaje p = new Puntaje(nombre, puntos);
        Archivo a = new Archivo(p);
        try {
            a.GuardarRegistro();
        } catch (IOException ex) {
            Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

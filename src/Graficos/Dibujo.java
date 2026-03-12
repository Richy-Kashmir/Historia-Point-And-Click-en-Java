package Graficos;

import java.awt.AlphaComposite;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import Principal.ControlPrincipal;
import control.Raton;
import herramientas.CargadorRecursos;
import herramientas.Sonido;
import herramientas.TransformadorImagenes;


public class Dibujo extends Canvas {

    private static final long serialVersionUID = 1L;
    private BufferStrategy buffer;
    private Graphics graficos;
    private Raton raton;
    private BufferedImage imagenInicial;
    private BufferedImage imagenSecundaria;
    private BufferedImage imagenActual;
    private BufferedImage imagenOpciones;
    public boolean cambioRealizado = false;
	private long tiempoInicio;
	private Sonido musicaFondo;
	// Botones invisibles sobre el menú
	private int jugarX = 270, jugarY = 170, jugarAncho = 260, jugarAlto = 65;
	private int opcionesX = 270, opcionesY = 245, opcionesAncho = 260, opcionesAlto = 65;
	private int salirX = 270, salirY = 320, salirAncho = 260, salirAlto = 65;

	// Para efecto hover (resaltar al pasar el mouse)
	private boolean hoverJugar = false;
	private boolean hoverOpciones = false;
	private boolean hoverSalir = false;
	private JFrame ventana;
	private boolean enPantallaOpciones = false;
	private BufferedImage imagenLupa;
	private boolean enPantallaDelJuego = false;
	
	private int pantallaOficinaActual = 0;
	private Sonido musicaJuego;

	
	// - CLAUDE -
    // -----------------------------------------------------------------------
    // Aquí he agregado: zonas de clic para navegar entre pantallas de oficina
    // -----------------------------------------------------------------------

    // Zona ABAJO: franja inferior completa y delgada (Oficina1 <-> Oficina2)
	// OFICINA 1
	private int o1_abajoX = 5, o1_abajoY = 500, o1_abajoW = 800, o1_abajoH = 800;

	// OFICINA 2
	private int o2_abajoX = 5, o2_abajoY = 500, o2_abajoW = 800, o2_abajoH = 800;
	private int o2_derX = 550, o2_derY = 150, o2_derW = 100, o2_derH = 280;
	private int o2_izqX = 130, o2_izqY = 150, o2_izqW = 100, o2_izqH = 280;

	// OFICINA 3
	private int o3_izqX = 5, o3_izqY = 0, o3_izqW = 120, o3_izqH = 800;

	// OFICINA 4
	private int o4_derX = 700, o4_derY = 0, o4_derW = 800, o4_derH = 800;
	// OBJETO OCULTO EJEMPLO
	private int objeto1X = 420;
	private int objeto1Y = 310;
	private int objeto1W = 80;
	private int objeto1H = 80;

	private boolean hoverObjeto1 = false;
	private boolean objetoEncontrado = false;

    public Dibujo(int ancho, int alto, long tiempoInicio, JFrame ventana) {
        setPreferredSize(new Dimension(ancho, alto));
        this.tiempoInicio = tiempoInicio; // asignamos el tiempo de inicio para la duración de la presentación
        this.ventana = ventana; // asisgnamos la referencia a la ventana para poder cambiar el título después de la presentación
        TransformadorImagenes transformador = new TransformadorImagenes();
        
        imagenInicial = CargadorRecursos.cargarImagen("recursos/imagenes/Presentacion.jpg");
       
        imagenSecundaria = CargadorRecursos.cargarImagen("recursos/imagenes/Menu.png");
     
        imagenOpciones = CargadorRecursos.cargarImagen("recursos/imagenes/opciones.png");
        
	        	 // Escala la imagen a la mitad de su tamaño original
			imagenActual = imagenInicial; // Comienza con la imagen inicial
        
			
			imagenLupa = CargadorRecursos.cargarImagen("recursos/imagenes/lupa.png");
			
			if (imagenLupa != null) {

			    java.awt.Toolkit toolkit = java.awt.Toolkit.getDefaultToolkit(); // Obtiene el toolkit para crear un cursor personalizado

			    java.awt.Point puntoHotspot = new java.awt.Point(0, 0);

			    java.awt.Cursor cursorLupa = toolkit.createCustomCursor(
			            imagenLupa,
			            puntoHotspot,
			            "CursorLupa"
			    );

			    setCursor(cursorLupa);

			} else {
			    System.out.println("Error cargando la imagen de la lupa");
			}
			
			
		
			// Para hacer el cursor invisible, creamos una imagen transparente de 1x1 píxel y la usamos como cursor
			BufferedImage cursorInvisible = new BufferedImage(1,1,BufferedImage.TYPE_INT_ARGB);
			Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorInvisible,new Point(0,0),"blank"); 
			setCursor(blankCursor);
			

       
        musicaFondo = new Sonido("recursos/musica/Custodes Abyssi.wav"); // Carga la música de fondo
       
        
        musicaJuego = new Sonido("recursos/musica/Interator.wav");

        raton = new Raton(this); // Inicializa el objeto ratón para rastrear la posición del cursor

        // ---------------------------------------------------------------
        // Listener de clics
        // ---------------------------------------------------------------
        this.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (cambioRealizado) {
                    int mx = e.getX();
                    int my = e.getY();

                    // --- Botones del MENÚ (solo si NO estamos en el juego) ---
                    if (!enPantallaDelJuego) {

                        // Botón JUGAR
                        if (mx >= jugarX && mx <= jugarX + jugarAncho &&
                            my >= jugarY && my <= jugarY + jugarAlto) {
                            System.out.println("JUGAR presionado");

                            detenerMusica();
                            String rutaVideo = "recursos/Video/Intro.mp4";

                            ReproductorVideo panelVideo = new ReproductorVideo(rutaVideo, new Runnable() {
                                @Override
                                public void run() {
                                    SwingUtilities.invokeLater(new Runnable() {
                                        @Override
                                        public void run() {
                                            ventana.getContentPane().removeAll();
                                            Dibujo nuevoDibujo = new Dibujo(ventana.getWidth(), ventana.getHeight(),
                                                                            System.currentTimeMillis(), ventana);
                                            nuevoDibujo.cambiarAOficina1();
                                            ventana.getContentPane().add(nuevoDibujo);
                                            ventana.revalidate();
                                            ventana.repaint();

                                            ControlPrincipal.setDibujo(nuevoDibujo);

                                            SwingUtilities.invokeLater(() -> {
                                                nuevoDibujo.createBufferStrategy(3);
                                            });
                                        }
                                    });
                                }
                            });

                            ventana.getContentPane().removeAll();
                            ventana.getContentPane().add(panelVideo);
                            ventana.revalidate();
                            ventana.repaint();
                        }
                   
                        // Botón OPCIONES
                        if (mx >= opcionesX && mx <= opcionesX + opcionesAncho &&
                            my >= opcionesY && my <= opcionesY + opcionesAlto) {
                            System.out.println("OPCIONES presionado");
                            cambiarAImagenOpciones();
                        }

                        // Botón SALIR / VOLVER AL MENÚ
                        if (mx >= salirX && mx <= salirX + salirAncho &&
                            my >= salirY && my <= salirY + salirAlto) {
                            if (enPantallaOpciones) {
                                System.out.println("VOLVER AL MENÚ presionado");
                                enPantallaOpciones = false;
                                cambiarAImagenSecundaria();
                            } else {
                                System.out.println("SALIR presionado");
                                System.exit(0);
                            }
                        }
                    }
                    
                    // - CLAUDE -

                    // ---------------------------------------------------------------
                    // Aquí he agregado: navegación entre pantallas de oficina
                    // ---------------------------------------------------------------
                 // OFICINA 1
                    if (pantallaOficinaActual == 1) {

                        if (mx >= o1_abajoX && mx <= o1_abajoX + o1_abajoW &&
                            my >= o1_abajoY && my <= o1_abajoY + o1_abajoH) {

                            cambiarAOficina2();
                        }
                    }

                    // OFICINA 2
                    else if (pantallaOficinaActual == 2) {

                        if (mx >= o2_abajoX && mx <= o2_abajoX + o2_abajoW &&
                            my >= o2_abajoY && my <= o2_abajoY + o2_abajoH) {

                            cambiarAOficina1();
                        }

                        if (mx >= o2_derX && mx <= o2_derX + o2_derW &&
                            my >= o2_derY && my <= o2_derY + o2_derH) {

                            cambiarAOficina3();
                        }

                        if (mx >= o2_izqX && mx <= o2_izqX + o2_izqW &&
                            my >= o2_izqY && my <= o2_izqY + o2_izqH) {

                            cambiarAOficina4();
                        }
                    }

                    // OFICINA 3
                    else if (pantallaOficinaActual == 3) {

                        if (mx >= o3_izqX && mx <= o3_izqX + o3_izqW &&
                            my >= o3_izqY && my <= o3_izqY + o3_izqH) {

                            cambiarAOficina2();
                        }
                    }

                    // OFICINA 4
                    else if (pantallaOficinaActual == 4) {

                        if (mx >= o4_derX && mx <= o4_derX + o4_derW &&
                            my >= o4_derY && my <= o4_derY + o4_derH) {

                            cambiarAOficina2();
                        }
                    
                     // CLICK OBJETO OCULTO
                        if(enPantallaDelJuego && !objetoEncontrado){

                            if(mx >= objeto1X && mx <= objeto1X + objeto1W &&
                               my >= objeto1Y && my <= objeto1Y + objeto1H){

                                objetoEncontrado = true;
                                System.out.println("OBJETO ENCONTRADO");

                            }
                        }
                        
                        
                        
                    }
                }
            }
        });
        
        // - CLAUDE -

        // Listener de hover (solo para el menú)
        this.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            @Override
            public void mouseMoved(java.awt.event.MouseEvent e) {
                if (cambioRealizado && !enPantallaDelJuego) {
                    int mx = e.getX();
                    int my = e.getY();
                    hoverJugar    = (mx >= jugarX    && mx <= jugarX    + jugarAncho    && my >= jugarY    && my <= jugarY    + jugarAlto);
                    hoverOpciones = (mx >= opcionesX && mx <= opcionesX + opcionesAncho && my >= opcionesY && my <= opcionesY + opcionesAlto);
                    hoverSalir    = (mx >= salirX    && mx <= salirX    + salirAncho    && my >= salirY    && my <= salirY    + salirAlto);
                
            
             // HOVER OBJETO OCULTO (solo dentro del juego)
                if(enPantallaDelJuego && !objetoEncontrado){

                    hoverObjeto1 =
                        (mx >= objeto1X && mx <= objeto1X + objeto1W &&
                         my >= objeto1Y && my <= objeto1Y + objeto1H);
                	}
                }
            }
        });
    }

    
  // ------------------------------------------------------------------------------------------ //  
    
    
    // Métodos 
    
    
    public void actualizar() {
    	raton.actualizar(this); //
    	
	}
    
    
    // ------------------------------------------------------------------------------------------ //
    
    
    public void cambiarAImagenSecundaria() {
        imagenActual = imagenSecundaria;
        cambioRealizado = true; 
        enPantallaOpciones = false;
        
        if (musicaFondo != null) { // Reproduce la música de fondo en loop infinito
            musicaFondo.reproducir(true);  // true = loop infinito

        }
    }
    
    
    // ------------------------------------------------------------------------------------------ //
    
    // - CLAUDE -
    
    public void cambiarAOficina1() {
        imagenActual = CargadorRecursos.cargarImagen("recursos/imagenes/Oficina .jpeg");
        cambioRealizado = true;
        enPantallaDelJuego = true;
        pantallaOficinaActual = 1; // Aquí he agregado: marcamos en qué pantalla estamos

        // Aquí he agregado: detenemos Interator y arrancamos Corium
        detenerMusicaJuego();
        detenerMusica();
        musicaFondo = new Sonido("recursos/musica/Corium.wav");
        if (musicaFondo != null) {
            musicaFondo.reproducir(true);
        }
    }

    // Aquí he agregado: método para ir a Oficina2.png (vértebra de navegación)
    public void cambiarAOficina2() {
        imagenActual = CargadorRecursos.cargarImagen("recursos/imagenes/Oficina2.png");
        cambioRealizado = true;
        enPantallaDelJuego = true;
        pantallaOficinaActual = 2; // Aquí he agregado: marcamos en qué pantalla estamos

        // Aquí he agregado: detenemos Corium y arrancamos Interator
        detenerMusica();
        iniciarMusicaJuego();
    }

    // Aquí he agregado: método para ir a Oficina3.png
    public void cambiarAOficina3() {
        imagenActual = CargadorRecursos.cargarImagen("recursos/imagenes/Oficina3.png");
        cambioRealizado = true;
        enPantallaDelJuego = true;
        pantallaOficinaActual = 3; // Aquí he agregado: marcamos en qué pantalla estamos

        // Aquí he agregado: Interator continúa sin cortarse al cambiar entre Oficina2/3/4
        detenerMusica();
        iniciarMusicaJuego();
    }

    // Aquí he agregado: método para ir a Oficina4.png
    public void cambiarAOficina4() {
        imagenActual = CargadorRecursos.cargarImagen("recursos/imagenes/Oficina4.png");
        cambioRealizado = true;
        enPantallaDelJuego = true;
        pantallaOficinaActual = 4; // Aquí he agregado: marcamos en qué pantalla estamos

        // Aquí he agregado: Interator continúa sin cortarse al cambiar entre Oficina2/3/4
        detenerMusica();
        iniciarMusicaJuego();
    }
    
    // ------------------------------------------------------------------------------------------ //
    
    private void iniciarMusicaJuego() {
        if (musicaJuego == null) {
            musicaJuego = new Sonido("recursos/musica/Interator.wav");
        }
        if (musicaJuego != null && !musicaJuego.isReproduciendo()) {
            musicaJuego.reproducir(true);
        }
    }

    // Aquí he agregado: detiene la música de juego (Interator)
    private void detenerMusicaJuego() {
        if (musicaJuego != null) {
            musicaJuego.detener();
        }
        
        }
    
    // ------------------------------------------------------------------------------------------ //
    
    public void cambiarAImagenOpciones() {
		imagenActual = CargadorRecursos.cargarImagen("recursos/imagenes/opciones.png");
		cambioRealizado = true; // Para mantener el flujo del programa y permitir hover (clics en los botones invisibles)
		enPantallaOpciones = true; // En la pantalla de opciones, el botón "Salir" se convierte en "Volver al menú"
    }
    
    // ------------------------------------------------------------------------------------------ //
    
    public long getTiempoInicio() {
        return tiempoInicio;
    }
    
    // ------------------------------------------------------------------------------------------ //
    
    public void detenerMusica() {
        if (musicaFondo != null) {
            musicaFondo.detener();
        }
    }
   
    // -------------------------------------------------------------------------------- //
    
    public void cambiarAImagenOficina() {
        cambiarAOficina1();
    }
    
    // ------------------------------------------------------------------------------------------ //
    
    public void dibujar() {
    	if (!isDisplayable()) // Verifica si el canvas está listo para dibujar
    		return;
    	
    	try { // try-catch para evitar errores de dibujo si el canvas no está completamente inicializado
        buffer = getBufferStrategy(); // el buffer es lo que se va a mostrar en pantalla y se obtiene del canvas
        if (buffer == null) { // Si el buffer no existe, lo creamos
            createBufferStrategy(3);
            return;
        }

        graficos = buffer.getDrawGraphics();
        // primero el fondo
        graficos.setColor(Color.BLACK);
        // usar dimensiones dinámicas de la ventana/canvas
        int ancho = getWidth();
        int alto = getHeight();
        graficos.fillRect(0, 0, ancho, alto);

        // luego la imagen (escalada al tamaño actual de la ventana)
        if (imagenActual != null) {
            graficos.drawImage(imagenActual, 0, 0, ancho, alto, null);
        }
       
        // Si el cambio a la imagen secundaria ya se ha realizado, dibuja los botones invisibles y el efecto hover
   
        if (cambioRealizado && !enPantallaDelJuego) { // Solo dibuja los botones y el efecto hover si estamos en el menú (No en el juego)
            Graphics2D g2d = (Graphics2D) graficos; // Para efectos de transparencia y suavizado
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); // para redondear los bordes 

            // Brillo dorado al hacer hover sobre cada botón
            if (hoverJugar) {
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.25f)); // 25% de transparencia para el efecto de brillo
                g2d.setColor(new Color(255, 200, 50)); // Color para el menu hover (dorado)
                g2d.fillRoundRect(jugarX, jugarY, jugarAncho, jugarAlto, 15, 15); // El redondeado de bordes
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f)); // Restaurar opacidad completa para no afectar otros elementos
            }
            if (hoverOpciones) {
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.25f));
                g2d.setColor(new Color(255, 200, 50));
                g2d.fillRoundRect(opcionesX, opcionesY, opcionesAncho, opcionesAlto, 15, 15);
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
            }
            if (hoverSalir) {
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.25f));
                g2d.setColor(new Color(255, 200, 50));
                g2d.fillRoundRect(salirX, salirY, salirAncho, salirAlto, 15, 15);
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
            }
        }
        
       
     // ZONAS DE OFICINA (PARA DEBUG)
        if (enPantallaDelJuego) {

            Graphics2D g2d = (Graphics2D) graficos;
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.35f));
            g2d.setColor(new Color (255,255,255,100));
           
            

            if (pantallaOficinaActual == 1) {
                g2d.fillRect(o1_abajoX, o1_abajoY, o1_abajoW, o1_abajoH);
            }

            else if (pantallaOficinaActual == 2) {
                g2d.fillRect(o2_abajoX, o2_abajoY, o2_abajoW, o2_abajoH);
                g2d.fillRect(o2_derX, o2_derY, o2_derW, o2_derH);
                g2d.fillRect(o2_izqX, o2_izqY, o2_izqW, o2_izqH);
            }

            else if (pantallaOficinaActual == 3) {
                g2d.fillRect(o3_izqX, o3_izqY, o3_izqW, o3_izqH);
            }

            else if (pantallaOficinaActual == 4) {
                g2d.fillRect(o4_derX, o4_derY, o4_derW, o4_derH);
            }

            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        }
     // HOVER OBJETO OCULTO
        if(hoverObjeto1 && !objetoEncontrado){

            Graphics2D g2d = (Graphics2D) graficos;

            g2d.setComposite(AlphaComposite.getInstance(
                AlphaComposite.SRC_OVER, 0.35f));

            g2d.setColor(new Color(255,255,0));

            g2d.fillOval(objeto1X, objeto1Y, objeto1W, objeto1H);

            g2d.setComposite(AlphaComposite.getInstance(
                AlphaComposite.SRC_OVER, 1f));
        }
        
        

        
        raton.dibujar(graficos); // Dibuja la posición del ratón en la pantalla
        Point p = raton.getPosicion();
        graficos.drawImage(imagenLupa, (int)p.getX()-32, (int)p.getY()-32, 64, 64, null); // Dibuja la imagen de la lupa centrada en la posición del ratón
        graficos.dispose();
        buffer.show();
        
    	 } catch (Exception e) {
    	        return;
    }
}
}
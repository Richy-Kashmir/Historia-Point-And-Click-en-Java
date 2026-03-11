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
	private Sonido musicaOficina;
	private Sonido musicaPuerto;

	

    // ----------------------------------------------------------------------- //
    // Zonas de clic para navegar entre pantallas de oficina
    // ----------------------------------------------------------------------- //

    // Oficina 1 zona inferior  (Oficina1 <-> Oficina2)
	private int o1_abajoX = 5, o1_abajoY = 500, o1_abajoW = 800, o1_abajoH = 800;

    // Oficina 2 zona inferior (Oficina2 -> Caulquier otra oficina más el puerto1 / Cualquier otra oficina -> Oficina2)
	private int o2_abajoX = 5, o2_abajoY = 500, o2_abajoW = 800, o2_abajoH = 800;
	private int o2_derX = 550, o2_derY = 150, o2_derW = 100, o2_derH = 280;
	private int o2_izqX = 130, o2_izqY = 150, o2_izqW = 100, o2_izqH = 280;
	private int o2_arribaX = 360, o2_arribaY = 150, o2_arribaW = 80, o2_arribaH = 220; // Para ir al puerto1 

    // Oficina 3 zona izquierda (Oficina2 -> Oficina3 / Oficina3 -> Oficina2)
	private int o3_izqX = 5, o3_izqY = 0, o3_izqW = 120, o3_izqH = 800;
	
	// Ofina 4 zona derecha (Oficina2 -> Oficina4 / Oficina4 -> Oficina2)
	private int o4_derX = 700, o4_derY = 0, o4_derW = 800, o4_derH = 800;
	
	// Puerto 1 zona centro (Puerto1 -> Puerto2)
	private int p1_centroX = 200, p1_centroY = 180, p1_centroW = 400, p1_centroH = 240;
	
	// Puerto 1 zona inferior (Puerto1 -> Oficina2)
	private int p1_abajoX = 0,   p1_abajoY = 500, p1_abajoW = 800, p1_abajoH = 100;
	
	// Puerto 2 zona inferior (Puerto2 -> Puerto1)
	private int p2_abajoX = 0,   p2_abajoY = 500, p2_abajoW = 800, p2_abajoH = 100;
    

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
       
        
        musicaOficina = new Sonido("recursos/musica/Interator.wav");
        
        musicaPuerto  = new Sonido("recursos/musica/Resonare.wav");

        raton = new Raton(this); // Inicializa el objeto ratón para rastrear la posición del cursor

        // --------------------------------------------------------------- //
        // Listener de clics
        // --------------------------------------------------------------- //
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

                            
                            // Es la creación de un panel del video y se le pasa una función Runnable (Para ejecutar el video)
                            ReproductorVideo panelVideo = new ReproductorVideo(rutaVideo, new Runnable() {
                                @Override
                                public void run() {
                                	
                                	// invorelater para asegurarnos de que el cambio de pantalla se ejecute en el hilo de la interfaz gráfica
                                	
                                    SwingUtilities.invokeLater(new Runnable() {  
                                        public void run() {
                                            ventana.getContentPane().removeAll();
                                            Dibujo nuevoDibujo = new Dibujo(ventana.getWidth(), ventana.getHeight(),
                                                                            System.currentTimeMillis(), ventana); // currentTimeMillis para reiniciar el tiempo de inicio (para la duración de la presentación)
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

                            ventana.getContentPane().removeAll();  // Elimina el contenido actual (menú) para mostrar el video
                            ventana.getContentPane().add(panelVideo); // Agrega el panel del video a la ventana
                            ventana.revalidate();  // Refresca la ventana para mostrar el nuevo contenido (es decir el video)
                            ventana.repaint(); // para que se actualice la ventana y se muestre el video
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
                    

                    // --------------------------------------------------------------- //
                    // Navegación entre pantallas de oficina
                    // --------------------------------------------------------------- //
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
                        
                        if (mx >= o2_arribaX && mx <= o2_arribaX + o2_arribaW &&
                                my >= o2_arribaY && my <= o2_arribaY + o2_arribaH) {
                                System.out.println("Oficina2 -> Puerto1");
                                cambiarAPuerto1();
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
                    
                        
                    }
                    else if (pantallaOficinaActual == 5) {

                        // Centro -> Puerto2
                        if (mx >= p1_centroX && mx <= p1_centroX + p1_centroW &&
                            my >= p1_centroY && my <= p1_centroY + p1_centroH) {
                            System.out.println("Puerto1 -> Puerto2");
                            cambiarAPuerto2();
                        }

                        // Aquí he cambiado: abajo vuelve a Oficina2 (antes era Oficina1)
                        if (mx >= p1_abajoX && mx <= p1_abajoX + p1_abajoW &&
                            my >= p1_abajoY && my <= p1_abajoY + p1_abajoH) {
                            System.out.println("Puerto1 -> Oficina2");
                            cambiarAOficina2();
                        }
                    }

                    // Aquí he agregado: PUERTO 2
                    else if (pantallaOficinaActual == 6) {

                        // Abajo -> Puerto1
                        if (mx >= p2_abajoX && mx <= p2_abajoX + p2_abajoW &&
                            my >= p2_abajoY && my <= p2_abajoY + p2_abajoH) {
                            System.out.println("Puerto2 -> Puerto1");
                            cambiarAPuerto1();
                        }
                    }
                }
            }
        });
        

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
    
    // Escenarios Oficinas
    
    public void cambiarAOficina1() {
        imagenActual = CargadorRecursos.cargarImagen("recursos/imagenes/Oficina .jpeg");
        cambioRealizado = true;
        enPantallaDelJuego = true;
        pantallaOficinaActual = 1; // Aquí he agregado: marcamos en qué pantalla estamos

        // Se dentendra las cancion de Interator y arrancara Corium
        detenerMusicaOficina(); // Detiene la música de juego (Interator)
        detenerMusica(); // Detiene la música de fondo (Custodes Abyssi)
        musicaFondo = new Sonido("recursos/musica/Corium.wav"); // Carga la música de Corium para la oficina 1
        if (musicaFondo != null) {
            musicaFondo.reproducir(true);
        }
    }

   
    public void cambiarAOficina2() {
        imagenActual = CargadorRecursos.cargarImagen("recursos/imagenes/Oficina2.png");
        cambioRealizado = true;
        enPantallaDelJuego = true;
        pantallaOficinaActual = 2; 


        detenerMusica();
        detenerMusicaPuerto();
        iniciarMusicaOficina();
    }

    // Aquí he agregado: método para ir a Oficina3.png
    public void cambiarAOficina3() {
        imagenActual = CargadorRecursos.cargarImagen("recursos/imagenes/Oficina3.png");
        cambioRealizado = true;
        enPantallaDelJuego = true;
        pantallaOficinaActual = 3; 

        detenerMusica();
        iniciarMusicaOficina();
    }

   
    public void cambiarAOficina4() {
        imagenActual = CargadorRecursos.cargarImagen("recursos/imagenes/Oficina4.png");
        cambioRealizado = true;
        enPantallaDelJuego = true;
        pantallaOficinaActual = 4; 

       
        detenerMusica();
        iniciarMusicaOficina();
    }
    
    // ------------------------------------------------------------------------------------------ //
    
    // Escenarios Puertos 
    
    
     public void cambiarAPuerto1() {
		imagenActual = CargadorRecursos.cargarImagen("recursos/imagenes/Puerto1.jpg");
		cambioRealizado = true;
		enPantallaDelJuego = true;
		pantallaOficinaActual = 5; 

		detenerMusica();
		detenerMusicaOficina();
        iniciarMusicaPuerto();
	}

	 public void cambiarAPuerto2() {
		imagenActual = CargadorRecursos.cargarImagen("recursos/imagenes/Puerto2.jpg");
		cambioRealizado = true;
		enPantallaDelJuego = true;
		pantallaOficinaActual = 6; 

		detenerMusica();
		detenerMusicaOficina();
		iniciarMusicaPuerto();
	}
	 
    // ------------------------------------------------------------------------------------------ //
	 
	 // Musica de los escenarios
	 
    private void iniciarMusicaOficina() {
        if (musicaOficina == null) {
            musicaOficina = new Sonido("recursos/musica/Interator.wav");
        }
        if (musicaOficina != null && !musicaOficina.isReproduciendo()) {
            musicaOficina.reproducir(true);
        }
    }

    
    private void detenerMusicaOficina() {
        if (musicaOficina != null) {
            musicaOficina.detener();
            
            musicaOficina = null; // Libera recursos de la musica para que pueda volver a cargar la música
        }
        
        }
    
    
    private void iniciarMusicaPuerto() {
        if (musicaPuerto == null) {
            musicaPuerto = new Sonido("recursos/musica/Resonare.wav");
        }
        if (musicaPuerto != null && !musicaPuerto.isReproduciendo()) {
            musicaPuerto.reproducir(true);
        }
    }
    
    private void detenerMusicaPuerto() {
		if (musicaPuerto != null) {
			musicaPuerto.detener();
			musicaPuerto = null; // Libera recursos de la musica para que pueda volver a cargar la música
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
        
        
        // Las posiciones de clic para navegar entre pantallas de oficina.
        
        if (enPantallaDelJuego) {

            Graphics2D g2d = (Graphics2D) graficos;
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.35f)); // 35% de transparencia para el efecto de superposición	
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

            else if (pantallaOficinaActual == 5) {
                g2d.fillRect(p1_centroX, p1_centroY, p1_centroW, p1_centroH);
                g2d.fillRect(p1_abajoX,  p1_abajoY,  p1_abajoW,  p1_abajoH);
            }
            else if (pantallaOficinaActual == 6) {
                g2d.fillRect(p2_abajoX, p2_abajoY, p2_abajoW, p2_abajoH);
            }

            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
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
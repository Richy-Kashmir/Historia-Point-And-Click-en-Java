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

			    java.awt.Toolkit toolkit = java.awt.Toolkit.getDefaultToolkit();

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
        raton = new Raton(this); // Inicializa el objeto ratón para rastrear la posición del cursor
    
        // Agrega un MouseListener para detectar clics en los botones invisibles
        this.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) { // Detecta clics del raton
                if (cambioRealizado) {
                    int mx = e.getX(); // Obtiene la posición X del clic
                    int my = e.getY(); // Obtiene la posición Y del clic
                    if (mx >= jugarX && mx <= jugarX + jugarAncho && // Verifica si el clic está dentro del área del botón JUGAR
                        my >= jugarY && my <= jugarY + jugarAlto) { // Si el clic está dentro del área del botón JUGAR
                        System.out.println("JUGAR presionado");
                        // aquí va la acción de jugar
                        
                        
                        // Apartado para reproducir el video de introducción 
                        detenerMusica();
                        String rutaVideo = "recursos/Video/Intro.mp4";

                        ReproductorVideo panelVideo = new ReproductorVideo(rutaVideo, new Runnable() {
                           
                        	@Override
                            public void run() { // Para cuando el video termine
                                SwingUtilities.invokeLater(new Runnable() {
                                    @Override
                                    public void run() { // Cambia a la imagen de la oficina después de que el video termine
                                        ventana.getContentPane().removeAll();
                                        Dibujo nuevoDibujo = new Dibujo(ventana.getWidth(), ventana.getHeight(),
                                                                        System.currentTimeMillis(), ventana); // currentTimeMillis para reiniciar el tiempo 
                                        nuevoDibujo.cambiarAImagenOficina();
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
                    
                    // aquí va la acción de opciones
                    
                    if (mx >= opcionesX && mx <= opcionesX + opcionesAncho &&
                        my >= opcionesY && my <= opcionesY + opcionesAlto) {
                        System.out.println("OPCIONES presionado");
                        
                        cambiarAImagenOpciones();
    
                        
                        
                        
                        
                        // aquí va la acción de salir
                    }
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
            }
        });

        // Efecto hover al mover el mouse
        this.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            @Override
            public void mouseMoved(java.awt.event.MouseEvent e) {
                if (cambioRealizado) {
                    int mx = e.getX(); // Obtiene la posición X del raton
                    int my = e.getY(); 
                    
                    // Veifica si el raton está sobre cada botón y actualiza las variables de hover
                    hoverJugar   = (mx >= jugarX && mx <= jugarX + jugarAncho && my >= jugarY && my <= jugarY + jugarAlto); // 
                    hoverOpciones = (mx >= opcionesX && mx <= opcionesX + opcionesAncho && my >= opcionesY && my <= opcionesY + opcionesAlto);
                    hoverSalir   = (mx >= salirX && mx <= salirX + salirAncho && my >= salirY && my <= salirY + salirAlto);
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
    
    public void cambiarAImagenOficina() {
		imagenActual = CargadorRecursos.cargarImagen("recursos/imagenes/Oficina.JPEG");
		cambioRealizado = true;  // Para mantener el flujo del programa y permitir hover (clics en los botones invisibles)
		detenerMusica();  // Por si acaso
		musicaFondo = new Sonido("recursos/musica/Corium.wav"); 
		if (musicaFondo != null) {
			musicaFondo.reproducir(true);  // Loop infinito
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
   
    
    // ------------------------------------------------------------------------------------------ //
    
    public void dibujar() {
    	if (!isDisplayable()) // Verifica si el canvas está listo para dibujar
    		return;
        buffer = getBufferStrategy(); // el buffer es lo que se va a mostrar en pantalla y se obtiene del canvas
        if (buffer == null) {
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
   
        if (cambioRealizado) {
            Graphics2D g2d = (Graphics2D) graficos; // Para efectos de transparencia y suavizado
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); // para redondear los bordes 

            // Brillo dorado al hacer hover sobre cada botón
            if (hoverJugar) {
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.25f));
                g2d.setColor(new Color(255, 200, 50));
                g2d.fillRoundRect(jugarX, jugarY, jugarAncho, jugarAlto, 15, 15);
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
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
        
        
        
        raton.dibujar(graficos); // Dibuja la posición del ratón en la pantalla
        Point p = raton.getPosicion();
        graficos.drawImage(imagenLupa, (int)p.getX()-32, (int)p.getY()-32, 64, 64, null); // Dibuja la imagen de la lupa centrada en la posición del ratón
        graficos.dispose();
        buffer.show();
    }
}
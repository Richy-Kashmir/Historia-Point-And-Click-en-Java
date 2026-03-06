package Graficos;

import herramientas.TransformadorImagenes;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import control.Raton;
import herramientas.CargadorRecursos;
import herramientas.Sonido;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.AlphaComposite;


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
	private BufferedImage imagenLupa;
    

    public Dibujo(int ancho, int alto, long tiempoInicio) {
        setPreferredSize(new Dimension(ancho, alto));
        this.tiempoInicio = tiempoInicio;
        TransformadorImagenes transformador = new TransformadorImagenes();
        
        imagenInicial = CargadorRecursos.cargarImagen("recursos/imagenes/Presentacion.jpg");
       
        imagenSecundaria = CargadorRecursos.cargarImagen("recursos/imagenes/Menu.PNG");
     
        imagenOpciones = CargadorRecursos.cargarImagen("recursos/imagenes/opciones.PNG");
        
	        	 // Escala la imagen a la mitad de su tamaño original
		imagenActual = imagenInicial; // Comienza con la imagen inicial
        
		
			// RICHARD
			// Carga la imagen de la lupa para el cursor personalizado
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
		
		
		// RICHARD
		// Para hacer el cursor invisible, creamos una imagen transparente de 1x1 píxel y la usamos como cursor
		BufferedImage cursorInvisible = new BufferedImage(1,1,BufferedImage.TYPE_INT_ARGB);
		Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorInvisible,new Point(0,0),"blank");
		setCursor(blankCursor);
		
       
		// Inicializa el objeto Sonido para la música de fondo
		musicaFondo = new Sonido("recursos/musica/Custodes Abyssi.wav"); // Carga la música de fondo
        raton = new Raton(this); // Inicializa el objeto ratón para rastrear la posición del cursor
    
        // Agrega un MouseListener para detectar clics en los botones invisibles
        
        this.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (cambioRealizado) {
                   
                		int mx = e.getX();
                		int my = e.getY();
                    
                    if (mx >= jugarX && mx <= jugarX + jugarAncho &&
                        my >= jugarY && my <= jugarY + jugarAlto) {
                        System.out.println("JUGAR presionado");
                        // aquí va la acción de jugar
                    }
                    if (mx >= opcionesX && mx <= opcionesX + opcionesAncho &&
                        my >= opcionesY && my <= opcionesY + opcionesAlto) {
                        System.out.println("OPCIONES presionado");
                           
                        // aquí va la acción de opciones
                    }
                    if (mx >= salirX && mx <= salirX + salirAncho &&
                        my >= salirY && my <= salirY + salirAlto) {
                        System.out.println("SALIR presionado");
                        System.exit(0);
                        
                        
                    }
                }
            }
        });

        // Efecto hover al mover el mouse
        
        this.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            @Override
            public void mouseMoved(java.awt.event.MouseEvent e) {
                if (cambioRealizado) {
                    int mx = e.getX();
                    int my = e.getY();
                    hoverJugar   = (mx >= jugarX && mx <= jugarX + jugarAncho && my >= jugarY && my <= jugarY + jugarAlto);
                    hoverOpciones = (mx >= opcionesX && mx <= opcionesX + opcionesAncho && my >= opcionesY && my <= opcionesY + opcionesAlto);
                    hoverSalir   = (mx >= salirX && mx <= salirX + salirAncho && my >= salirY && my <= salirY + salirAlto);
                }
            }
        });
    }

   
    
    public void actualizar() {
    	raton.actualizar(this); //
    	
	}
    
    public void cambiarAImagenSecundaria() {
        imagenActual = imagenSecundaria;
        cambioRealizado = true;
        
        if (musicaFondo != null) { // Reproduce la música de fondo en loop infinito
            musicaFondo.reproducir(true);  // true = loop infinito

        }
    }
    
    public long getTiempoInicio() {
        return tiempoInicio;
    }
    
    
    public void detenerMusica() {
        if (musicaFondo != null) {
            musicaFondo.detener();
        }
    }
    
    
    public void dibujar() {
        buffer = getBufferStrategy();
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
            Graphics2D g2d = (Graphics2D) graficos;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

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
        
        
     raton.dibujar(graficos);

     // RICHARD
     // dibujar lupa
     Point p = raton.getPosicion();
     graficos.drawImage(imagenLupa, (int)p.getX()-32, (int)p.getY()-32, 64, 64, null);

     graficos.dispose();
     buffer.show();

    }
}
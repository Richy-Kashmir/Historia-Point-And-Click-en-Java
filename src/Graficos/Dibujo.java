package Graficos;

import java.awt.AlphaComposite;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

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
	
	// Imagenes de objetos	
	private BufferedImage imagenLupa;
	private BufferedImage imgLinterna;
	private BufferedImage imgGrabadora;
	private BufferedImage imgPistola;
	private BufferedImage imgDocumento;
	private BufferedImage imgHuella;
	private BufferedImage imgFoto;
	private BufferedImage imgGanzuas;
	private BufferedImage imgLlave;
	
	
	
	private boolean enPantallaDelJuego = false;
	
	private int pantallaOficinaActual = 0;
	private Sonido musicaOficina;
	private Sonido musicaPuerto;
	private Sonido musicaDemo;
	// Botón menú dentro del juego
	private int menuX = 20, menuY = 15, menuW = 100, menuH = 40;
	private boolean menuJuegoAbierto = false;

    // ----------------------------------------------------------------------- //
    // Zonas de clic para navegar entre pantallas de oficina
    // ----------------------------------------------------------------------- //

    // Oficina 1 zona inferior  (Oficina1 <-> Oficina2)
	private int o1_abajoX = 5, o1_abajoY = 500, o1_abajoW = 800, o1_abajoH = 500;

    // Oficina 2 zona inferior (Oficina2 -> Caulquier otra oficina más el puerto1 / Cualquier otra oficina -> Oficina2)
	private int o2_abajoX = 5, o2_abajoY = 500, o2_abajoW = 800, o2_abajoH = 800;
	private int o2_derX = 550, o2_derY = 150, o2_derW = 100, o2_derH = 280;
	private int o2_izqX = 130, o2_izqY = 150, o2_izqW = 100, o2_izqH = 280;
	private int o2_arribaX = 360, o2_arribaY = 150, o2_arribaW = 80, o2_arribaH = 220; // Para ir al puerto1 
	private int o3_izqX = 5, o3_izqY = 0, o3_izqW = 120, o3_izqH = 800; // Oficina 3 zona izquierda (Oficina2 -> Oficina3 / Oficina3 -> Oficina2)
	private int o4_derX = 800 - 50 - 20, o4_derY = 400 -25, o4_derW = 65, o4_derH = 65;// Ofina 4 zona derecha (Oficina2 -> Oficina4 / Oficina4 -> Oficina2)
	private int p1_centroX = 200, p1_centroY = 180, p1_centroW = 400, p1_centroH = 240; // Puerto 1 zona centro (Puerto1 -> Puerto2
	private int p1_abajoX = 0,   p1_abajoY = 500, p1_abajoW = 800, p1_abajoH = 100;// Puerto 1 zona inferior (Puerto1 -> Oficina2)
	private int p2_abajoX = 0,   p2_abajoY = 500, p2_abajoW = 800, p2_abajoH = 100; // Puerto 2 zona inferior (Puerto2 -> Puerto1)
	private int p2_demoX = 130, p2_demoY = 390, p2_demoW = 80, p2_demoH = 80; // Pantalla "final"
	
	// --------------------------------------------------
	// OBJETOS DEL JUEGO
	// --------------------------------------------------

	// OFICINA 1
	private int ganzuaX = 240, ganzuaY = 350, ganzuaW = 75, ganzuaH = 75;
	private int grabadoraX = 440, grabadoraY = 160, grabadoraW = 75, grabadoraH = 75;
	private int pistolaX = 640, pistolaY = 450, pistolaW = 75, pistolaH = 75;

	// OFICINA 3
	private int documentoX = 700, documentoY = 550, documentoW = 75, documentoH = 75;
	private int llaveX = 450, llaveY = 350, llaveW = 75, llaveH = 75;
	private int fotoX = 120, fotoY = 320, fotoW = 75, fotoH = 75;

	// OFICINA 4
	private int linternaX = 350, linternaY = 350, linternaW = 75, linternaH = 75;
	private int huellaX = 560, huellaY = 380, huellaW = 75, huellaH = 75;


	// OBJETOS ENCONTRADOS
	private boolean linternaEncontrada = false;
	private boolean grabadoraEncontrada = false;
	private boolean pistolaEncontrada = false;
	private boolean documentoEncontrado = false;
	private boolean huellaEncontrada = false;
	private boolean fotoEncontrada = false;
	private boolean ganzuasEncontradas = false;
	private boolean llaveEncontrada = false;
    
	
	// Obtención de objetos más mensaje
    private boolean mostrarMensaje = false;
    private String textoMensaje = "";
    private int mensajeX = 80, mensajeY = 190, mensajeW = 640, mensajeH = 130; // Área del mensaje (para poder cerrarlo al hacer click encima)

 // CONTADOR DE PRUEBAS
    private String totalPruebas = "?";
    private int pruebasEncontradas = 0;
    private boolean mensajeInicioMostrado = false ;
    
    
    public Dibujo(int ancho, int alto, long tiempoInicio, JFrame ventana) {
        setPreferredSize(new Dimension(ancho, alto));
        this.tiempoInicio = tiempoInicio; // asignamos el tiempo de inicio para la duración de la presentación
        this.ventana = ventana; // asisgnamos la referencia a la ventana para poder cambiar el título después de la presentación
        TransformadorImagenes transformador = new TransformadorImagenes();
        
        	imagenInicial = CargadorRecursos.cargarImagen("recursos/imagenes/Presentacion.jpg");
        	imagenSecundaria = CargadorRecursos.cargarImagen("recursos/imagenes/Menu.png");
        	imagenOpciones = CargadorRecursos.cargarImagen("recursos/imagenes/opciones.png");
        
	        imagenActual = imagenInicial; // Comienza con la imagen inicial
         
			imagenLupa 	  = CargadorRecursos.cargarImagen("recursos/imagenes/lupa.png");
			imgLinterna   = CargadorRecursos.cargarImagen("recursos/imagenes/Linterna.jpeg");
			imgGrabadora  = CargadorRecursos.cargarImagen("recursos/imagenes/Grabadora.jpeg");
			imgPistola    = CargadorRecursos.cargarImagen("recursos/imagenes/Pistola.jpeg");
			imgDocumento  = CargadorRecursos.cargarImagen("recursos/imagenes/Documentos.jpeg");
			imgHuella     = CargadorRecursos.cargarImagen("recursos/imagenes/Huellas.jpeg");
			imgFoto       = CargadorRecursos.cargarImagen("recursos/imagenes/Foto.jpeg");
			imgGanzuas    = CargadorRecursos.cargarImagen("recursos/imagenes/Ganzua.jpeg");
			imgLlave      = CargadorRecursos.cargarImagen("recursos/imagenes/Llaves.jpeg");
			
			
			
			
			
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

                    
                    if (mostrarMensaje) {
                        if (mx >= mensajeX && mx <= mensajeX + mensajeW &&
                            my >= mensajeY && my <= mensajeY + mensajeH) {
                            mostrarMensaje = false;
                        }
                        return; // Hasta no hacer click en el mensaje no se podrá hacer nada
                    }
                    
            
                    // --- Botones del MENÚ (solo si NO estamos en el juego) ---
                    if (!enPantallaDelJuego) {

                        // Botón JUGAR
                        if (!enPantallaOpciones && mx >= jugarX && mx <= jugarX + jugarAncho &&
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
                    
                       //  menú del juego (solo si estamos en el juego)
                        
                        if(mx >= menuX && mx <= menuX + menuW &&
                        		   my >= menuY && my <= menuY + menuH){

                        		    System.out.println("MENU ABIERTO");

                        		    enPantallaDelJuego = false;
                        		    cambiarAImagenSecundaria();
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
           
                            if (tieneTodasLasHerramientas()) {
                                System.out.println("Oficina2 -> Puerto1");
                                mostrarMensaje = false;
                                cambiarAPuerto1();
                            } else {
                                // Aquí he agregado: muestra el mensaje en pantalla
                                textoMensaje = "Antes de salir al puerto, necesito asegurarme de tener todas mis herramientas para ir tras mi hija. Debería revisar bien en cada rincon para encontrar lo que me falta.";
                                mostrarMensaje = true;
                                System.out.println(textoMensaje);
                            }
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

                        //  zona Demo DENTRO del bloque de Puerto2
                        if (mx >= p2_demoX && mx <= p2_demoX + p2_demoW &&
                            my >= p2_demoY && my <= p2_demoY + p2_demoH) {
                            if (ganzuasEncontradas) {
                                System.out.println("Puerto2 -> Demo");
                                cambiarADemo();
                            } else {
                                textoMensaje = "Necesito mi ganzúa para poder abrir esta puerta.";
                                mostrarMensaje = true;
                            }
                        }
                    }

                    // Aquí he movido: Demo en su propio else if de la cadena principal
                    else if (pantallaOficinaActual == 7) {
                        System.out.println("Demo -> Menú");
                        cambiarAMenuPrincipal();
                    }
                    
                    
                
                 // --------------------------------------------------
                 // OBJETOS OFICINA 1
                 // --------------------------------------------------
                 if(pantallaOficinaActual == 1){

                     if (!ganzuasEncontradas &&
                             mx >= ganzuaX && mx <= ganzuaX + ganzuaW &&
                             my >= ganzuaY && my <= ganzuaY + ganzuaH) {
                             if (llaveEncontrada) {
                                 ganzuasEncontradas = true;
                                 System.out.println("GANZUAS OBTENIDAS");
                             } else {
                                 textoMensaje = "Si no mal me equivoco aquí esta mi ganzúa, pero necesito la llave para poder acceder a ella";
                                 mostrarMensaje = true;
                             }
                         }
                     

                     if(!grabadoraEncontrada &&
                        mx >= grabadoraX && mx <= grabadoraX + grabadoraW &&
                        my >= grabadoraY && my <= grabadoraY + grabadoraH){

                         grabadoraEncontrada = true;
                         System.out.println("GRABADORA OBTENIDA");
                     }

                     if (!pistolaEncontrada &&
                             mx >= pistolaX && mx <= pistolaX + pistolaW &&
                             my >= pistolaY && my <= pistolaY + pistolaH) {
                             if (llaveEncontrada) {
                                 pistolaEncontrada = true;
                                 System.out.println("PISTOLA OBTENIDA");
                             } else {
                                 textoMensaje = "Aquí esta mi pistola, me sera util para defenderme, pero necesito la llave para poder acceder a ella";
                                 mostrarMensaje = true;
                             }
                         }
                 }


                 // --------------------------------------------------
                 // OBJETOS OFICINA 3
                 // --------------------------------------------------
                 if(pantallaOficinaActual == 3){

                     if(!documentoEncontrado &&
                        mx >= documentoX && mx <= documentoX + documentoW &&
                        my >= documentoY && my <= documentoY + documentoH){

                         documentoEncontrado = true;
                         System.out.println("DOCUMENTO OBTENIDO");
                     }

                     if(!llaveEncontrada &&
                        mx >= llaveX && mx <= llaveX + llaveW &&
                        my >= llaveY && my <= llaveY + llaveH){

                         llaveEncontrada = true;
                         System.out.println("LLAVE OBTENIDA");
                         
                         
                     }

                     if(!fotoEncontrada &&
                        mx >= fotoX && mx <= fotoX + fotoW &&
                        my >= fotoY && my <= fotoY + fotoH){

                         fotoEncontrada = true;
                         System.out.println("FOTO OBTENIDA");
                     }
                 }


                 // --------------------------------------------------
                 // OBJETOS OFICINA 4
                 // --------------------------------------------------
                 if(pantallaOficinaActual == 4){

                     if(!linternaEncontrada &&
                        mx >= linternaX && mx <= linternaX + linternaW &&
                        my >= linternaY && my <= linternaY + linternaH){

                    	 	linternaEncontrada = true;
                         System.out.println("LINTERNA OBTENIDA");
                     }

                     if(!huellaEncontrada &&
                        mx >= huellaX && mx <= huellaX + huellaW &&
                        my >= huellaY && my <= huellaY + huellaH){

                         huellaEncontrada = true;
                         System.out.println("HUELLA OBTENIDA");
                     }
                 }
                
                 if(enPantallaDelJuego){
                	     mx = e.getX();
                	     my = e.getY();

                	    // --- Abrir/Cerrar menú ---
                	    if(mx >= menuX && mx <= menuX + menuW &&
                	       my >= menuY && my <= menuY + menuH){
                	        menuJuegoAbierto = !menuJuegoAbierto; // alterna el menú
                	    }

                	    // --- Opciones del menú ---
                	    if(menuJuegoAbierto){
                	        // Reanudar juego
                	        int reanudarX = 20, reanudarY = 70, reanudarW = 150, reanudarH = 40;
                	        if(mx >= reanudarX && mx <= reanudarX + reanudarW &&
                	           my >= reanudarY && my <= reanudarY + reanudarH){
                	            menuJuegoAbierto = false; // cerrar menú y volver al juego
                	        }

                	        // Salir al menú principal
                	        int salirX = 20, salirY = 120, salirW = 150, salirH = 40;
                	        if(mx >= salirX && mx <= salirX + salirW &&
                	           my >= salirY && my <= salirY + salirH){
                	            menuJuegoAbierto = false;
                	            enPantallaDelJuego = false;
                	            cambiarAImagenSecundaria(); // vuelve al menú principal
                	        }
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
    	
    	actualizarContadorPruebas();
    	
	}
    
    
    private void actualizarContadorPruebas(){

        pruebasEncontradas = 0;

        if(linternaEncontrada) pruebasEncontradas++;
        if(grabadoraEncontrada) pruebasEncontradas++;
        if(pistolaEncontrada) pruebasEncontradas++;
        if(documentoEncontrado) pruebasEncontradas++;
        if(huellaEncontrada) pruebasEncontradas++;
        if(fotoEncontrada) pruebasEncontradas++;
        if(ganzuasEncontradas) pruebasEncontradas++;
        if(llaveEncontrada) pruebasEncontradas++;
    }
    
    // -------------------------------------------------------------------------------- //
    
public boolean tieneTodasLasHerramientas() {
		return linternaEncontrada && grabadoraEncontrada && pistolaEncontrada &&
		   documentoEncontrado && huellaEncontrada && fotoEncontrada &&
		   llaveEncontrada;
}
    
    // ------------------------------------------------------------------------------------------ //
    
public List<String> partirTextoEnLineas(Graphics2D g, String texto, int anchoMaximo) {
    List<String> lineas = new ArrayList<>(); // Lista para almacenar las líneas resultantes
    FontMetrics fm = g.getFontMetrics(); // Para medir el ancho del texto con la fuente actual

    String[] palabras = texto.split(" "); // Divide el texto en palabras 
    StringBuilder lineaActual = new StringBuilder(); // StringBuilder para construir la línea actual de texto

    for (String palabra : palabras) {
        String prueba = lineaActual.length() == 0 ? palabra : lineaActual + " " + palabra;  // Agrega la palabra a la línea actual para probar si cabe dentro del ancho máximo
        if (fm.stringWidth(prueba) <= anchoMaximo) {    
            lineaActual = new StringBuilder(prueba); 
        } else {
            if (lineaActual.length() > 0) {
                lineas.add(lineaActual.toString());
            }
            lineaActual = new StringBuilder(palabra);
        }
    }
    if (lineaActual.length() > 0) {
        lineas.add(lineaActual.toString());
    }
    return lineas;
}


 // ------------------------------------------------------------------------------------------ //
    
public void cambiarAMenuPrincipal() {
    imagenActual = imagenSecundaria;
    cambioRealizado    = true;
    enPantallaDelJuego = false;
    enPantallaOpciones = false;
    pantallaOficinaActual = 0;

    // Detener toda la música antes de arrancar la del menú
    detenerMusica();
    detenerMusicaOficina();
    detenerMusicaPuerto();
    detenerMusicaDemo();

    // Aquí he cambiado: siempre se crea una nueva instancia para garantizar que suene
    musicaFondo = new Sonido("recursos/musica/Custodes Abyssi.wav");
    if (musicaFondo != null) musicaFondo.reproducir(true);
}



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
      
        if (!mensajeInicioMostrado) {
        textoMensaje = "Según las notas del secuestro, todo indica que viene del puerto pesquero, será el primer lugar que iré, sin embargo necesito prepararme para ir a un lugar muy peligroso.";
        mostrarMensaje = true;
        
        mensajeInicioMostrado = true;
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
	 
	 public void cambiarADemo() {
	        imagenActual = CargadorRecursos.cargarImagen("recursos/imagenes/Demo.jpg");
	        cambioRealizado = true;
	        enPantallaDelJuego = true;
	        pantallaOficinaActual = 7;
	 
	        // Detener toda la música anterior y arrancar la canción de la Demo
	        detenerMusica();
	        detenerMusicaOficina();
	        detenerMusicaPuerto();
	        musicaDemo = new Sonido("recursos/musica/La Lista de Array.wav");
	        if (musicaDemo != null) musicaDemo.reproducir(true);
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
    
    
    public void iniciarMusicaPuerto() {
        if (musicaPuerto == null) {
            musicaPuerto = new Sonido("recursos/musica/Resonare.wav");
        }
        if (musicaPuerto != null && !musicaPuerto.isReproduciendo()) {
            musicaPuerto.reproducir(true);
        }
    }
    
    public void detenerMusicaPuerto() {
		if (musicaPuerto != null) {
			musicaPuerto.detener();
			musicaPuerto = null; // Libera recursos de la musica para que pueda volver a cargar la música
		}
	}
    
    private void detenerMusicaDemo() {
        if (musicaDemo != null) { musicaDemo.detener(); musicaDemo = null; }
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
        	
        	if (pantallaOficinaActual != 7) {
        	
        	
        	// --------------------------------------------------
        	// BARRA SUPERIOR DEL JUEGO (HUD)
        	// --------------------------------------------------

        	Graphics2D gHUD = (Graphics2D) graficos; // Para efectos de transparencia

        	gHUD.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.85f)); // 85% de opacidad para la barra superior del HUD 
        	gHUD.setColor(new Color(20,20,20));
        	gHUD.fillRect(0,0,getWidth(),70);

        	gHUD.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,1f)); // Restaurar opacidad completa para el menú y otros elementos del HUD
        	
        
        	
        	gHUD.setColor(new Color(120,120,120));
        	gHUD.fillRoundRect(menuX, menuY, menuW, menuH, 10, 10);

        	gHUD.setColor(Color.WHITE); // Color del texto del menú	
        	gHUD.drawString("MENU", menuX + 30, menuY + 25); // Texto del botón del menú
        	
        	gHUD.setColor(Color.WHITE);
        	gHUD.drawString("UTILES: " + pruebasEncontradas + " / " + totalPruebas, 700, 40);
        	
        	
        	
        	
        	// --------------------------------------------------
        	// MENU DEL JUEGO
        	// --------------------------------------------------

        	if(menuJuegoAbierto){
        	    Graphics2D gMenu = (Graphics2D) graficos;
        	    gMenu.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,0.9f));

        	    // Botón Reanudar
        	    gMenu.setColor(new Color(40,40,40));
        	    gMenu.fillRoundRect(20,70,150,40,10,10);
        	    gMenu.setColor(Color.WHITE);
        	    gMenu.drawString("REANUDAR JUEGO",30,95);

        	    // Botón Salir al menú principal
        	    gMenu.setColor(new Color(40,40,40));
        	    gMenu.fillRoundRect(20,120,150,40,10,10);
        	    gMenu.setColor(Color.WHITE);
        	    gMenu.drawString("SALIR AL MENÚ",30,145);
        	}
        	
        	// --------------------------------------------------
        	// INVENTARIO
        	// --------------------------------------------------

        	int invX = 200;
        	int invY = 10;
        	int invSize = 50;
        	int espacio = 10;

        	gHUD.setColor(new Color(70,70,70));

        	for(int i=0;i<8;i++){
        	    gHUD.fillRect(invX + i*(invSize+espacio), invY, invSize, invSize);
        	}

        	// OBJETOS RECOGIDOS

        	int slot = 0;
        	int tamaño = invSize; // tamaño del slot

        	if(linternaEncontrada){
        	    if(imgLinterna != null)
        	        gHUD.drawImage(imgLinterna, invX + slot*(invSize+espacio), invY, tamaño, tamaño, null);
        	    slot++;
        	}

        	if(grabadoraEncontrada){
        	    if(imgGrabadora != null)
        	        gHUD.drawImage(imgGrabadora, invX + slot*(invSize+espacio), invY, tamaño, tamaño, null);
        	    slot++;
        	}

        	if(pistolaEncontrada){
        	    if(imgPistola != null)
        	        gHUD.drawImage(imgPistola, invX + slot*(invSize+espacio), invY, tamaño, tamaño, null);
        	    slot++;
        	}

        	if(documentoEncontrado){
        	    if(imgDocumento != null)
        	        gHUD.drawImage(imgDocumento, invX + slot*(invSize+espacio), invY, tamaño, tamaño, null);
        	    slot++;
        	}

        	if(huellaEncontrada){
        	    if(imgHuella != null)
        	        gHUD.drawImage(imgHuella, invX + slot*(invSize+espacio), invY, tamaño, tamaño, null);
        	    slot++;
        	}

        	if(fotoEncontrada){
        	    if(imgFoto != null)
        	        gHUD.drawImage(imgFoto, invX + slot*(invSize+espacio), invY, tamaño, tamaño, null);
        	    slot++;
        	}

        	if(ganzuasEncontradas){
        	    if(imgGanzuas != null)
        	        gHUD.drawImage(imgGanzuas, invX + slot*(invSize+espacio), invY, tamaño, tamaño, null);
        	    slot++;
        	}

        	if(llaveEncontrada){
        	    if(imgLlave != null)
        	        gHUD.drawImage(imgLlave, invX + slot*(invSize+espacio), invY, tamaño, tamaño, null);
        	    slot++;
        	}
        	
        	}
        
        	// --------------------------------------------------
        	
        	Graphics2D g2d = (Graphics2D) graficos;
        	
        	Font fuenteOriginal = g2d.getFont();
        	g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f)); // opacidad completa para las flechas
        	
        	
        	g2d.setColor(Color.WHITE); // color de las flechas
        	g2d.setFont(new Font("Arial", Font.BOLD, 50));

        	// OFICINA 1 -> Abajo a Oficina2
        	if (pantallaOficinaActual == 1) {
        	    g2d.drawString("↓", o1_abajoX + o1_abajoW / 2 - 10, o1_abajoY + 40);
        	}

        	// OFICINA 2
        	else if (pantallaOficinaActual == 2) {
        	    g2d.drawString("↓", o2_abajoX + o2_abajoW / 2 - 10, o2_abajoY + 40); // Oficina1
        	    g2d.drawString("→", o2_derX + o2_derW / 2 - 10, o2_derY + o2_derH / 2); // Oficina3
        	    g2d.drawString("←", o2_izqX + o2_izqW / 2 - 10, o2_izqY + o2_izqH / 2); // Oficina4
        	    if (tieneTodasLasHerramientas()) {
        	        g2d.drawString("↑", o2_arribaX + o2_arribaW / 2 - 10, o2_arribaY + 30); // Puerto1
        	    }
        	}

        	// OFICINA 3 -> Izquierda a Oficina2
        	else if (pantallaOficinaActual == 3) {
        	    g2d.drawString("←", o3_izqX + o3_izqW / 2 - 10, o3_izqY + o3_izqH / 2);
        	}

        	// OFICINA 4 -> Derecha a Oficina2
        	else if (pantallaOficinaActual == 4) {
        	    g2d.drawString("→", o4_derX + o4_derW / 2 - 10, o4_derY + o4_derH / 2);
        	}

        	// PUERTO 1
        	else if (pantallaOficinaActual == 5) {
        	    g2d.drawString("↓", p1_abajoX + p1_abajoW / 2 - 10, p1_abajoY + 40); // Oficina2
        	    g2d.drawString("↑", p1_centroX + p1_centroW / 2 - 10, p1_centroY + 30); // Puerto2
        	}

        	// PUERTO 2
        	else if (pantallaOficinaActual == 6) {
        	    g2d.drawString("↓", p2_abajoX + p2_abajoW / 2 - 10, p2_abajoY + 40); // Puerto1
        	    g2d.drawString("→", p2_demoX + p2_demoW / 2 - 10, p2_demoY + 40); // Demo
        	}
         // --------------------------------------------------
         // HOVER OBJETOS
         // --------------------------------------------------
        	g2d.setFont(fuenteOriginal); 
        

         g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.30f));
         g2d.setColor(new Color(255,255,0));

         Point mouse = raton.getPosicion();

         int mx = (int) mouse.getX();
         int my = (int) mouse.getY();

         g2d.setColor(new Color(255, 200, 50));
         
         if(pantallaOficinaActual == 1){

             if(!ganzuasEncontradas &&
                mx >= ganzuaX && mx <= ganzuaX + ganzuaW &&
                my >= ganzuaY && my <= ganzuaY + ganzuaH){

                 g2d.fillOval(ganzuaX, ganzuaY, ganzuaW, ganzuaH);
             }

             if(!grabadoraEncontrada &&
                mx >= grabadoraX && mx <= grabadoraX + grabadoraW &&
                my >= grabadoraY && my <= grabadoraY + grabadoraH){

                 g2d.fillOval(grabadoraX, grabadoraY, grabadoraW, grabadoraH);
             }

             if(!pistolaEncontrada &&
                mx >= pistolaX && mx <= pistolaX + pistolaW &&
                my >= pistolaY && my <= pistolaY + pistolaH){

                 g2d.fillOval(pistolaX, pistolaY, pistolaW, pistolaH);
             }
         }
         
         
         if(pantallaOficinaActual == 3){

			 if(!documentoEncontrado &&
				mx >= documentoX && mx <= documentoX + documentoW &&
				my >= documentoY && my <= documentoY + documentoH){

				 g2d.fillOval(documentoX, documentoY, documentoW, documentoH);
			 }

			 if(!llaveEncontrada &&
				mx >= llaveX && mx <= llaveX + llaveW &&
				my >= llaveY && my <= llaveY + llaveH){

				 g2d.fillOval(llaveX, llaveY, llaveW, llaveH);
			 }

			 if(!fotoEncontrada &&
				mx >= fotoX && mx <= fotoX + fotoW &&
				my >= fotoY && my <= fotoY + fotoH){

				 g2d.fillOval(fotoX, fotoY, fotoW, fotoH);
			 }
		 }
         
         
         if(pantallaOficinaActual == 4){

			 if(!linternaEncontrada &&
				mx >= linternaX && mx <= linternaX + linternaW &&
				my >= linternaY && my <= linternaY + linternaH){

				 g2d.fillOval(linternaX, linternaY, linternaW, linternaH);
			 }

			 if(!huellaEncontrada &&
				mx >= huellaX && mx <= huellaX + huellaW &&
				my >= huellaY && my <= huellaY + huellaH){

				 g2d.fillOval(huellaX, huellaY, huellaW, huellaH);
			 }

			
		 }
         
         if (pantallaOficinaActual == 6) {

        	    if (mx >= p2_demoX && mx <= p2_demoX + p2_demoW &&
        	        my >= p2_demoY && my <= p2_demoY + p2_demoH) {

        	        g2d.fillOval(p2_demoX, p2_demoY, p2_demoW, p2_demoH);
        	    }
        	}
         
         
         g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,1f));
        
       
        
        }
        
        // --------------------------------------------------------------------------------------- //
        // Mensaje de pantalla 
        // --------------------------------------------------------------------------------------- //
        if (mostrarMensaje) {
            Graphics2D gMsg = (Graphics2D) graficos;
            gMsg.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            Font fuenteTexto = new Font("Arial", Font.ITALIC, 15);
            gMsg.setFont(fuenteTexto);

            // Margen interno del cuadro
            int margen         = 20;
            int anchoTexto     = mensajeW - margen * 2;
            int alturaLinea    = 22; // espacio entre líneas en px

            // Partir el texto en líneas que quepan en el cuadro
            List<String> lineas = partirTextoEnLineas(gMsg, textoMensaje, anchoTexto);

            // Altura dinámica: líneas * alturaLinea + espacio para "click para cerrar" + márgenes
            int alturaCuadro = margen + lineas.size() * alturaLinea + 30;

            // Fondo oscuro semitransparente
            gMsg.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.88f));
            gMsg.setColor(new Color(20, 20, 20));
            gMsg.fillRoundRect(mensajeX, mensajeY, mensajeW, alturaCuadro, 18, 18);

            // Borde dorado
            gMsg.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
            gMsg.setColor(new Color(200, 160, 30));
            gMsg.drawRoundRect(mensajeX, mensajeY, mensajeW, alturaCuadro, 18, 18);

            // Dibujar cada línea de texto
            gMsg.setColor(Color.WHITE);
            gMsg.setFont(fuenteTexto);
            for (int i = 0; i < lineas.size(); i++) {
                gMsg.drawString(lineas.get(i), mensajeX + margen, mensajeY + margen + (i + 1) * alturaLinea);
            }

            // Indicación para cerrar
            gMsg.setColor(new Color(160, 160, 160));
            gMsg.setFont(new Font("Arial", Font.PLAIN, 11));
            gMsg.drawString("[ Haz click aquí para cerrar el mensaje ]",
                            mensajeX + margen, mensajeY + alturaCuadro - 10);

            // Actualizar el área de click de cierre con la altura real del cuadro
            mensajeH = alturaCuadro + 20;
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
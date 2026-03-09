package Graficos;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.base.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.player.component.EmbeddedMediaPlayerComponent;

public class ReproductorVideo extends JPanel {

    private EmbeddedMediaPlayerComponent componente;
    private final Runnable cuandoTermina;
    private String rutaVideo;
    private boolean yaTermino = false;
    
    // --------------------------------------------------------------------------------- //
    
    private final MouseAdapter skipPorClic = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) { // Detecta cualquier clic para saltar el video
            System.out.println("Video saltado por clic");
            ejecutarCallback();
        }
        };
        
    // --------------------------------------------------------------------------------- //
        
        private final KeyAdapter skipPorTeclado = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) { // Detecta la tecla Enter para saltar el video
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    System.out.println("Video saltado por Enter");
                    ejecutarCallback();
                }
            }
        };

        //-------------------------------------------------------------------------------- //
        
    public ReproductorVideo(String rutaVideo, Runnable callbackTerminado) {
        super(new BorderLayout()); // Usa BorderLayout para que el video ocupe toda la pantalla
        setBackground(Color.BLACK);
        this.cuandoTermina = callbackTerminado;
        this.rutaVideo = rutaVideo;

        componente = new EmbeddedMediaPlayerComponent();
        add(componente, BorderLayout.CENTER);
        
        componente.addMouseListener(skipPorClic); // Añade el listener de clic al componente principal
        componente.videoSurfaceComponent().addMouseListener(skipPorClic); // Añade el listener de clic a la superficie de video para asegurar que se detecten los clics en toda el área del video
        componente.addKeyListener(skipPorTeclado);
        componente.videoSurfaceComponent().addKeyListener(skipPorTeclado);

        // Añadir listeners de ratón y teclado al panel para permitir saltar el video
        setFocusable(true);
        componente.videoSurfaceComponent().requestFocusInWindow();

        MediaPlayer mp = componente.mediaPlayer();

        mp.events().addMediaPlayerEventListener(new MediaPlayerEventAdapter() { // Escucha el evento de finalización del video
          
        	
        	@Override
            public void finished(MediaPlayer mediaPlayer) { // Cuando el video termina, ejecuta el callback
                SwingUtilities.invokeLater(() -> {
                    ejecutarCallback();
                });
            }
        });
    }



    // Método que se encarga de ejecutar el callback una sola vez
    private void ejecutarCallback() {
        if (!yaTermino) {
            yaTermino = true; // marcamos que ya terminó para no llamar dos veces
            if (cuandoTermina != null) {
                cuandoTermina.run();
            }
        }
    }

    @Override
    public void addNotify() {
        super.addNotify(); // Dejamos que el componente se adjunte primero
        SwingUtilities.invokeLater(() -> { // invokeLater asegura que el componente esté completamente inicializado antes de reproducir
            componente.mediaPlayer().media().play(rutaVideo); // Reemplaza con la ruta correcta del video
            // Pedir el foco para recibir eventos de teclado
            requestFocusInWindow();
        });
    }

    public void detener() {
        if (componente != null) { // Verificamos que el componente no sea nulo antes de intentar detenerlo
        	EmbeddedMediaPlayerComponent comp = componente;
            componente = null;
            try {
                comp.mediaPlayer().controls().stop();  // Detiene la reproducción del video
            } catch (Exception ignored) {}

            // 
            new Thread(() -> { // Usamos un hilo separado para liberar los recursos después de detener el video
                try {
                    Thread.sleep(200); // Pequeña espera para que vlcj termine de parar
                    comp.mediaPlayer().release();
                    comp.release();
                } catch (Exception ignored) {}
            }).start();
        }
            }

    @Override
    public void removeNotify() {
        detener();
        super.removeNotify(); // llama al método para eliminar el componente 
    }
}
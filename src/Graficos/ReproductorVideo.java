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

    public ReproductorVideo(String rutaVideo, Runnable callbackTerminado) {
        super(new BorderLayout()); // Usa BorderLayout para que el video ocupe toda la pantalla
        setBackground(Color.BLACK);
        this.cuandoTermina = callbackTerminado;
        this.rutaVideo = rutaVideo;

        componente = new EmbeddedMediaPlayerComponent();
        add(componente, BorderLayout.CENTER);

        // Añadir listeners de ratón y teclado al panel para permitir saltar el video
        setFocusable(true);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("Video saltado por clic");
                ejecutarCallback();
            }
        });

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    System.out.println("Video saltado por Enter");
                    ejecutarCallback();
                }
            }
        });

        MediaPlayer mp = componente.mediaPlayer();

        mp.events().addMediaPlayerEventListener(new MediaPlayerEventAdapter() { // Escucha eventos del reproductor
            @Override
            public void finished(MediaPlayer mediaPlayer) {
                // Ejecutar el callback en el hilo EDT
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
        if (componente != null) {
            componente.mediaPlayer().controls().stop(); // Detiene la reproducción
            componente.mediaPlayer().release(); // Libera los recursos del reproductor
            componente.release(); // Libera los recursos del componente
            componente = null;
        }
    }

    @Override
    public void removeNotify() {
        detener();
        super.removeNotify(); // llama al método para eliminar el componente 
    }
}
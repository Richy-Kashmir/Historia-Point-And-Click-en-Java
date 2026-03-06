package Graficos;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.base.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.player.component.EmbeddedMediaPlayerComponent;

public class ReproductorVideo extends JPanel {

    private EmbeddedMediaPlayerComponent componente;
    private final Runnable cuandoTermina;

    public ReproductorVideo(String rutaVideo, Runnable callbackTerminado) {
        super(new BorderLayout()); // Usa BorderLayout para que el video ocupe toda la pantalla
        setBackground(Color.BLACK);
        this.cuandoTermina = callbackTerminado;

        componente = new EmbeddedMediaPlayerComponent();
        add(componente, BorderLayout.CENTER); 

        MediaPlayer mp = componente.mediaPlayer(); 

        mp.events().addMediaPlayerEventListener(new MediaPlayerEventAdapter() { // Escucha eventos del reproductor
            @Override
            public void finished(MediaPlayer mediaPlayer) {
                SwingUtilities.invokeLater(() -> { // invoreLater para que se ejecute en el hilo de la interfaz gráfica
                    if (cuandoTermina != null) {
                        cuandoTermina.run();
                    }
                });
            }

            @Override
            public void error(MediaPlayer mediaPlayer) {
                System.err.println("Error reproduciendo video: " + mediaPlayer.media().info().mrl());
                if (cuandoTermina != null) cuandoTermina.run();
            }
        });

        mp.media().play(rutaVideo);  // Inicia automáticamente
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
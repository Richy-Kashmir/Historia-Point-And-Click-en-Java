package Principal;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.base.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.player.component.EmbeddedMediaPlayerComponent;

public class TestVlcjSimple {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Prueba rápida de vlcj");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);
            frame.setLocationRelativeTo(null);

            EmbeddedMediaPlayerComponent playerComponent = new EmbeddedMediaPlayerComponent();
            frame.getContentPane().add(playerComponent);
            frame.setVisible(true);

            String videoPath = "C:\\Users\\Richy\\Desktop\\Copia\\Portugal.mp4";  // CAMBIA ESTO

            System.out.println("Intentando reproducir: " + videoPath);
            boolean ok = playerComponent.mediaPlayer().media().play(videoPath);
            System.out.println("Play iniciado: " + ok);

            playerComponent.mediaPlayer().events().addMediaPlayerEventListener(new MediaPlayerEventAdapter() {
                @Override
                public void finished(MediaPlayer mediaPlayer) {
                    System.out.println("El video terminó.");
                }

                @Override
                public void error(MediaPlayer mediaPlayer) {
                    System.out.println("Error en reproducción.");
                }
            });
        });
    }
}
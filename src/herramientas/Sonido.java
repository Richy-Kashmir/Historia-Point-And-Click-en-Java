package herramientas;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Sonido {

    private Clip clip;

    public Sonido(String rutaArchivo) {
        try {
            File archivo = new File(rutaArchivo); 
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(archivo);
            clip = AudioSystem.getClip();
            clip.open(audioStream);

 
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(-12.0f); // -12 dB ≈ volumen medio (más bajo que antes para no saturar)

            System.out.println("Sonido cargado correctamente: " + rutaArchivo);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.err.println("Error al cargar el sonido: " + rutaArchivo);
            e.printStackTrace();
            clip = null;
        }
    }

    public void reproducir(boolean loop) {
        if (clip != null) {
            clip.setFramePosition(0); // Reinicia desde el principio
            if (loop) {
                clip.loop(Clip.LOOP_CONTINUOUSLY);
            } else {
                clip.start();
            }
            System.out.println("Reproduciendo: " + (loop ? "en loop" : "una vez"));
        }
    }

    public void detener() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
            clip.close();
            System.out.println("Música detenida.");
        }
    }
}
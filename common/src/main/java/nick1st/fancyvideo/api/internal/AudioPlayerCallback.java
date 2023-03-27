package nick1st.fancyvideo.api.internal;

import com.sun.jna.Pointer;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.base.callback.AudioCallbackAdapter;

import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine.Info;

public class AudioPlayerCallback extends AudioCallbackAdapter {

    private static final int BLOCK_SIZE = 4;

    private static final int SAMPLE_BITS = 16; // BLOCK_SIZE * 8 / channels ???

    private final AudioFormat audioFormat;

    private final Info info;

    private final SourceDataLine dataLine;

    public AudioPlayerCallback(String format, int rate, int channels) throws Exception {
        this.audioFormat = new AudioFormat(rate, SAMPLE_BITS, channels, true, false);
        this.info = new Info(SourceDataLine.class, audioFormat);
        this.dataLine = (SourceDataLine)AudioSystem.getLine(info);
        start();
    }

    private void start() throws Exception {
        System.out.println("start()");
        dataLine.open(audioFormat);
        dataLine.start();
    }

    private void stop() {
        System.out.println("stop()");
        dataLine.close();
    }

    @Override
    public void play(MediaPlayer mediaPlayer, Pointer samples, int sampleCount, long pts) {
        // There may be more efficient ways to do this...
        int bufferSize = sampleCount * BLOCK_SIZE;
        // You could process these samples in some way before playing them...
        byte[] data = samples.getByteArray(0, bufferSize);
        dataLine.write(data, 0, bufferSize);
    }

    @Override
    public void drain(MediaPlayer mediaPlayer) {
        System.out.println("drain()");
        dataLine.drain();
    }

}
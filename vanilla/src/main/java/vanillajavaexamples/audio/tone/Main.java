package vanillajavaexamples.audio.tone;

import java.io.File;

import com.jsyn.JSyn;
import com.jsyn.Synthesizer;
import com.jsyn.unitgen.SineOscillator;
import com.jsyn.util.WaveRecorder;

import lombok.SneakyThrows;


/**
 * https://github.com/philburk/jsyn/blob/master/examples/src/main/java/com/jsyn/examples/PlayTone.java
 * https://www.softsynth.com/jsyn/docs/usersguide.php#CapturingOutput
 * https://stackoverflow.com/questions/1932490/java-generating-sound
 *
 * Atualmente está aumentando a frequencia de mil em mil e salvando num wav
 */
public class Main {
  @SneakyThrows
  public static void main(String[] args) {

    // Create a context for the synthesizer.
    Synthesizer synth = JSyn.createSynthesizer();

    // Start synthesizer using default stereo output at 44100 Hz.
    synth.start();

    // Add a tone generator.
    SineOscillator oscillator = new SineOscillator();
    synth.add(oscillator);
    // Add a stereo audio output unit.

//    LineOut lineOut = new LineOut();
//    synth.add(lineOut);

    final var lineOut = new WaveRecorder(synth, new File("/tmp/tmp.wav"));

    // Connect the oscillator to both channels of the output.
    oscillator.output.connect(0, lineOut.getInput(), 0);
    oscillator.output.connect(0, lineOut.getInput(), 1);

    // Set the frequency and amplitude for the sine wave.
    oscillator.frequency.set(15000.0);
    oscillator.amplitude.set(0.8);

    new Thread(() -> {
      int i = 1000;
      while (true){
        oscillator.frequency.set(10_000 + (i += 1000));
        oscillator.amplitude.set(0.8);
        try {
          Thread.sleep(500);
        } catch (InterruptedException e) {}
      }
    }).start();

    // We only need to start the LineOut. It will pull data from the
    // oscillator.
    lineOut.start();

    // Sleep while the sound is generated in the background.
    try {
      double time = synth.getCurrentTime();
      // Sleep for a few seconds.
      synth.sleepUntil(time + 10.0);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    // Stop everything.
    synth.stop();
  }

}

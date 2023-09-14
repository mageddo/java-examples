package vanillajavaexamples.audio.tone;

import java.nio.file.Path;
import java.nio.file.Paths;

import com.jsyn.JSyn;
import com.jsyn.Synthesizer;
import com.jsyn.unitgen.SineOscillator;
import com.jsyn.util.WaveRecorder;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;


/**
 * Oscilates the frequency between 15k and 22k
 */
@Slf4j
public class FrequencyOscilatorGeneratorMain {


  public static final int FPS = 1000 / 60;

  public static void main(String[] args) {
    new FrequencyOscilatorGeneratorMain().generateFrequency(
        15_000, 20_000, 60, Paths.get("/tmp/tone.wav")
    );
  }

  @SneakyThrows
  public void generateFrequency(int from, int to, int durationInSecs, Path out){

    log.info("starting..");
    // Create a context for the synthesizer.
    Synthesizer synth = JSyn.createSynthesizer();

    // Start synthesizer using default stereo output at 44100 Hz.
    synth.start();

    // Add a tone generator.
    SineOscillator oscillator = new SineOscillator();
    synth.add(oscillator);
    // Add a stereo audio output unit.

    final var lineOut = new WaveRecorder(synth, out.toFile());

    // Connect the oscillator to both channels of the output.
    oscillator.output.connect(0, lineOut.getInput(), 0);
    oscillator.output.connect(0, lineOut.getInput(), 1);

    // Set the frequency and amplitude for the sine wave.
    oscillator.frequency.set(from);
    oscillator.amplitude.set(0.8);

    final var whenToStop = System.currentTimeMillis() + (durationInSecs * 1000);
    Thread t = new Thread(() -> {

      // o audio é formado por fatias de 5s, cada fatia oscila da frequencia minima até a máxima
      // aumentando a frequencia dentro desse tempo, depois comeca tudo denovo em uma outra
      // fatia
      int sliceDurationInSecs = Math.min(5, durationInSecs);
      int cycles = sliceDurationInSecs * FPS;
      int frequencyIncreaseByCycle = (to - from) / cycles;

      while (System.currentTimeMillis() < whenToStop) {

        int newFreq = from;
        for (int i = 0; i < cycles; i++) {

          oscillator.frequency.set(newFreq);
          newFreq += frequencyIncreaseByCycle;

          sleep(FPS);
        }
      }

      log.info("audio oscilation is done");

    });
    t.start();

    // We only need to start the LineOut. It will pull data from the
    // oscillator.
    lineOut.start();

    log.info("waiting for the oscilation generation ");
    // Sleep while the sound is generated in the background.
    double time = synth.getCurrentTime();
    // Sleep for a few seconds.
    synth.sleepUntil(time + (durationInSecs + 3));

    log.info("all done, stopping");

    // Stop everything.
    synth.stop();

  }

  private static void sleep(int millis) {
    try {
      Thread.sleep(millis);
    } catch (InterruptedException e) {}
  }

}

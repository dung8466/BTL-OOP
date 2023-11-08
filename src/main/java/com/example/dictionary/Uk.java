package com.example.dictionary;

import javax.speech.Central;
import javax.speech.synthesis.Synthesizer;
import javax.speech.synthesis.SynthesizerModeDesc;
import java.util.Locale;

public class Uk {
    public static void Speech(String Text) {
        try {

            System.setProperty("freetts.voices", "com.sun.speech.freetts.en.us" + ".cmu_us_kal.KevinVoiceDirectory");

            Central.registerEngineCentral("com.sun.speech.freetts" + ".jsapi.FreeTTSEngineCentral");


            Synthesizer synthesizer = Central.createSynthesizer(new SynthesizerModeDesc(Locale.US));

            synthesizer.allocate();

            synthesizer.resume();

            synthesizer.speakPlainText(
                    Text, null);
            synthesizer.waitEngineState(
                    Synthesizer.QUEUE_EMPTY);

            //synthesizer.deallocate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}

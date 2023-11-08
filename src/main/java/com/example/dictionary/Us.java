package com.example.dictionary;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class Us {
    private static Us us;

    public Us() {
    }

    public synchronized static Us getInstance() {

        if (us == null) {
            us = new Us();
        }
        return us;
    }

    public InputStream getAudio(String text, String languageOutput)
            throws IOException {
        URL url = new URL("http://translate.google.com/translate_tts?ie=UTF-8&tl=" + languageOutput + "&client=tw-ob&q="
                + text.replace(" ", "%20"));
        URLConnection urlConn = url.openConnection();
        urlConn.addRequestProperty("User-Agent",
                "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
        InputStream audioSrc = urlConn.getInputStream();
        return new BufferedInputStream(audioSrc);
    }

    public void play(InputStream sound) throws JavaLayerException {
        new Player(sound).play();
    }
}

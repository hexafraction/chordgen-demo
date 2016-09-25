package org.hackcooper.chordgen.backend;


import org.jfugue.player.Player;

import javax.sound.midi.*;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import static javax.sound.midi.Sequence.SMPTE_24;
import static javax.sound.midi.ShortMessage.NOTE_OFF;
import static javax.sound.midi.ShortMessage.NOTE_ON;

public class RiddleTesting {
    static Random r = new Random();
    public static void main(String[] args) throws IOException, InvalidMidiDataException, MidiUnavailableException, InterruptedException {
        System.out.println("Generating blank database. This can take a while.");
        RatingDatabase rdb = new RatingDatabase();
        System.out.println("Updating database with sample \"The Riddle\"");
        rdb.addScore(new ChordProgression(new Chord(Chord.Key.G, Chord.ChordType.Major),
                new Chord(Chord.Key.A, Chord.ChordType.Minor),
                new Chord(Chord.Key.G, Chord.ChordType.Major),
                new Chord(Chord.Key.C, Chord.ChordType.Major),
                new Chord(Chord.Key.D, Chord.ChordType.Major),
                new Chord(Chord.Key.A, Chord.ChordType.Minor),
                new Chord(Chord.Key.G, Chord.ChordType.Major),
                new Chord(Chord.Key.F, Chord.ChordType.Major),
                new Chord(Chord.Key.C, Chord.ChordType.Major),
                new Chord(Chord.Key.A, Chord.ChordType.Minor),
                new Chord(Chord.Key.G, Chord.ChordType.Major),
                new Chord(Chord.Key.C, Chord.ChordType.Major),
                new Chord(Chord.Key.D, Chord.ChordType.Major),
                new Chord(Chord.Key.A, Chord.ChordType.Minor),
                new Chord(Chord.Key.G, Chord.ChordType.Major),
                new Chord(Chord.Key.F, Chord.ChordType.Major),
                new Chord(Chord.Key.C, Chord.ChordType.Major),
                new Chord(Chord.Key.D, Chord.ChordType.Minor),
                new Chord(Chord.Key.C, Chord.ChordType.Major),
                new Chord(Chord.Key.F, Chord.ChordType.Major),
                new Chord(Chord.Key.G, Chord.ChordType.Major),
                new Chord(Chord.Key.A, Chord.ChordType.Minor)), 1.0, 1.9);
        //System.out.println("c");
        //rdb.saveAs(new File("G:/chorddb/test2.db"));
        System.out.println("Starting genetic fit");
        GeneticFitter gf = new GeneticFitter(64, rdb.getFitnessFunction());
        ChordProgression cp = gf.generate();
        System.out.println("DEBUG: Resulting progression: " + cp);
        Player player = new Player();
        Sequence sq = new Sequence(SMPTE_24, 8);
        Track trk = sq.createTrack();
        int pos = 240;
//        for (Chord c : cp.chords) {
//            ShortMessage sm1 = new ShortMessage();
//            sm1.setMessage(NOTE_ON, 60 + c.key.ordinal, 127);
//            ShortMessage sm2 = new ShortMessage();
//            sm2.setMessage(NOTE_ON, (c.type == Chord.ChordType.Minor ? 63 : 64) + c.key.ordinal, 127);
//            ShortMessage sm3 = new ShortMessage();
//            sm3.setMessage(NOTE_ON, 67 + c.key.ordinal, 127);
//
//            ShortMessage sm4 = new ShortMessage();
//            if(c.type== Chord.ChordType.Seventh)  {sm4.setMessage(NOTE_ON, 70 + c.key.ordinal, 127);
//
//                trk.add(new MidiEvent(sm4, pos));}
//            trk.add(new MidiEvent(sm1, pos));
//            trk.add(new MidiEvent(sm2, pos));
//            trk.add(new MidiEvent(sm3, pos));
//            ShortMessage smo1 = new ShortMessage();
//            smo1.setMessage(NOTE_OFF, 60 + c.key.ordinal, 127);
//            ShortMessage smo2 = new ShortMessage();
//            smo2.setMessage(NOTE_OFF, (c.type == Chord.ChordType.Minor ? 63 : 64) + c.key.ordinal, 127);
//            ShortMessage smo3 = new ShortMessage();
//
//            smo3.setMessage(NOTE_OFF, 67 + c.key.ordinal, 127);
//            ShortMessage smo4 = new ShortMessage();
//            if(c.type== Chord.ChordType.Seventh)  {smo4.setMessage(NOTE_OFF, 70 + c.key.ordinal, 127);
//
//                trk.add(new MidiEvent(smo4, pos+240));}
//            trk.add(new MidiEvent(smo1, pos+240));
//            trk.add(new MidiEvent(smo2, pos+240));
//            trk.add(new MidiEvent(smo3, pos+240));
//            pos+=240;
//        }
        ShortMessage sm0 = new ShortMessage();
        sm0.setMessage(NOTE_OFF, 60, 127);
        trk.add(new MidiEvent(sm0, 0));
        for (Chord c : cp.chords) {
            ShortMessage sm1 = new ShortMessage();
            sm1.setMessage(NOTE_ON, 60 + c.key.ordinal, 127);
            ShortMessage sm2 = new ShortMessage();
            sm2.setMessage(NOTE_ON, (c.type == Chord.ChordType.Minor ? 63 : 64) + c.key.ordinal, 127);
            ShortMessage sm3 = new ShortMessage();
            sm3.setMessage(NOTE_ON, 67 + c.key.ordinal, 127);

            ShortMessage sm4 = new ShortMessage();
            sm4.setMessage(NOTE_ON, 72 + c.key.ordinal, 127);

            trk.add(new MidiEvent(sm4, pos+0));
            trk.add(new MidiEvent(sm1, pos+0));
            trk.add(new MidiEvent(sm2, pos+0));
            trk.add(new MidiEvent(sm3, pos+0));
            ShortMessage smo1 = new ShortMessage();
            smo1.setMessage(NOTE_OFF, 60 + c.key.ordinal, 127);
            ShortMessage smo2 = new ShortMessage();
            smo2.setMessage(NOTE_OFF, (c.type == Chord.ChordType.Minor ? 63 : 64) + c.key.ordinal, 127);
            ShortMessage smo3 = new ShortMessage();

            smo3.setMessage(NOTE_OFF, 67 + c.key.ordinal, 127);
            ShortMessage smo4 = new ShortMessage();
            smo4.setMessage(NOTE_OFF, 72 + c.key.ordinal, 127);

            trk.add(new MidiEvent(smo4, pos + 120));
            trk.add(new MidiEvent(smo1, pos + 120));
            trk.add(new MidiEvent(smo2, pos + 120));
            trk.add(new MidiEvent(smo3, pos + 120));
            pos += 120;
        }
        ShortMessage sm1 = new ShortMessage();
        sm1.setMessage(NOTE_ON, 72, 127);
        //trk.add(new MidiEvent(sm1, pos, ));
        ShortMessage smz = new ShortMessage();
        smz.setMessage(NOTE_OFF, 60, 127);
        trk.add(new MidiEvent(smz, 100000));
        player.play(sq);
        Thread.sleep(1000);
        System.out.println("Finished with run.");
        //rdb.saveAs(new File("G:/chorddb/testdb.db"));
    }
}


package org.hackcooper.chordgen.backend;

import java.util.ArrayList;
import java.util.List;

public class ChordProgression extends ChordTuple {
    public ChordProgression(Chord... chords) {
        super(chords);
    }

    List<ChordTuple> getSingles() {
        List<ChordTuple> rVal = new ArrayList<>();
        for (int i = 0; i < chords.length; i++) {
            rVal.add(new ChordTuple(chords[i]));
        }
        return rVal;
    }

    List<ChordTuple> getDoubles() {
        List<ChordTuple> rVal = new ArrayList<>();
        for (int i = 0; i < chords.length - 1; i++) {
            rVal.add(new ChordTuple(chords[i], chords[i + 1]));
        }
        return rVal;
    }

    List<ChordTuple> getTriples() {
        List<ChordTuple> rVal = new ArrayList<>();
        for (int i = 0; i < chords.length - 2; i++) {
            rVal.add(new ChordTuple(chords[i], chords[i + 1], chords[i + 2]));
        }
        return rVal;
    }

    List<ChordTuple> getQuads() {
        List<ChordTuple> rVal = new ArrayList<>();
        for (int i = 0; i < chords.length - 3; i++) {
            rVal.add(new ChordTuple(chords[i], chords[i + 1], chords[i + 2], chords[i + 3]));
        }
        return rVal;
    }


}



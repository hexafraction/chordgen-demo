package org.hackcooper.chordgen.backend;

import java.util.Arrays;

public class ChordTuple implements StringSerializable {
    final Chord[] chords;
    public ChordTuple(Chord... chords) {
        if(chords==null || chords.length==0) {
            throw new IllegalArgumentException("Empty array of chords");
        } else {
            this.chords = chords;
        }
    }

    @Override
    public String toString() {
        return "ChordTuple{" +
                "chords=" + Arrays.toString(chords) +
                '}';
    }

    public static ChordTuple deserialize(String s){
        return new ChordTuple(Arrays.stream(s.split(",")).map(Chord::deserialize).toArray((i)->new Chord[i]));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ChordTuple that = (ChordTuple) o;

        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        return Arrays.equals(chords, that.chords);

    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(chords);
    }

    public Chord[] getChords() {

        return chords;
    }

    @Override
    public String serialize() {
        return Arrays.stream(chords).map(Chord::serialize).reduce((t, u)-> t+","+u).orElseThrow(() -> new IllegalArgumentException("Empty array of chords"));
    }
}

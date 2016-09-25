package org.hackcooper.chordgen.backend;

import java.util.*;

public class Chord implements StringSerializable {
    public static final int MAX_KEY = 12;
    public static final int MAX_TYPE = 3;
    Key key;
    ChordType type;
    @Override
    public String serialize() {
        return key.ordinal + "/" + type.ordinal;
    }
    static final Random r = new Random();

    @Override
    public String toString() {
        return "{"+key + type +
                '}';
    }

    public static Chord deserialize(String s) {
        String lSide = s.substring(0, s.indexOf('/'));
        String rSide = s.substring(s.indexOf('/')+1);
        return fromOrdinals(Integer.parseInt(lSide), Integer.parseInt(rSide));
    }

    enum Key {
        C(0), Cs(1), D(2), Ds(3), E(4), F(5), Fs(6), G(7), Gs(8), A(9), As(10), B(11);

        final int ordinal;

        Key(int ordinal) {
            this.ordinal = ordinal;
        }

        private static final Key[] fromOrdinal = {C, Cs, D, Ds, E, F, Fs, G, Gs, A, As, B};

        public static Key fromOrdinal(int ord) {
            return fromOrdinal[ord];
        }

    }

    enum ChordType {
        Major(0), Minor(1), Seventh(2);

        final int ordinal;

        ChordType(int ordinal) {
            this.ordinal = ordinal;
        }

        private static final ChordType[] fromOrdinal = {Major, Minor, Seventh};

        public static ChordType fromOrdinal(int ord) {
            return fromOrdinal[ord];
        }

    }

    public static Chord randomChord() {
        return Chord.fromOrdinals(r.nextInt(MAX_KEY), r.nextInt(MAX_TYPE));
    }

    public static Chord fromOrdinals(int key, int type){
        return new Chord(Key.fromOrdinal(key), ChordType.fromOrdinal(type));
    }

    public Chord(Key key, ChordType type) {
        this.key = key;
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Chord chord = (Chord) o;

        if (key != chord.key) return false;
        return type == chord.type;

    }


    private static ArrayList<Chord> allChords = new ArrayList<>();

    public static List<Chord> getAllChords() {
        return (allChords);
    }

    static {
        for(int i = 0; i < MAX_KEY; i++){
            for(int j = 0; j < MAX_TYPE; j++){
                allChords.add(fromOrdinals(i, j));
            }
        }
    }

    @Override
    public int hashCode() {
        int result = key != null ? key.hashCode() : 0;
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }
}

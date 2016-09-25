package org.hackcooper.chordgen.backend;

import java.io.*;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

import static org.hackcooper.chordgen.backend.Chord.Key.C;

public class RatingDatabase implements Map<ChordTuple, RatingDatabase.WeightedScore> {
    public FitnessFunction getFitnessFunction(){
        return cp -> {
            double demerit = 0;
            double total = 0;
            double totalWeight = 0;
            for(ChordTuple ctp : cp.getSingles()){
                totalWeight += get(ctp).weight;
                total += get(ctp).totalScore;
            }
            for(ChordTuple ctp : cp.getDoubles()){
                totalWeight += 2*get(ctp).weight;
                total += 2*get(ctp).totalScore;
            }
            for(ChordTuple ctp : cp.getTriples()){
                totalWeight += 4*get(ctp).weight;
                total += 4*get(ctp).totalScore;
            }
            for(ChordTuple ctp : cp.getQuads()){
                totalWeight += 8*get(ctp).weight;
                total += 8*get(ctp).totalScore;
            }
            if(cp.chords[cp.chords.length-1].key!=C) demerit+=0.1;
            //System.out.println(total/totalWeight);
            return total/totalWeight - demerit;
        };
    }

    public void addScore(ChordProgression cp, double score, double weight){
        for(ChordTuple ctp : cp.getSingles()){
            get(ctp).applyRating(score, weight);
        }
        for(ChordTuple ctp : cp.getDoubles()){
            get(ctp).applyRating(score, weight);
        }
        for(ChordTuple ctp : cp.getTriples()){
            get(ctp).applyRating(score, weight);
        }
        for(ChordTuple ctp : cp.getQuads()){
            get(ctp).applyRating(score, weight);
        }
    }

    LinkedHashMap<ChordTuple, WeightedScore> backingMap;


    @Override
    public int size() {
        return backingMap.size();
    }

    @Override
    public boolean isEmpty() {
        return backingMap.isEmpty();
    }

    @Override
    public WeightedScore get(Object key) {
        return backingMap.get(key);
    }

    @Override
    public boolean containsKey(Object key) {
        return backingMap.containsKey(key);
    }

    @Override
    public WeightedScore put(ChordTuple key, WeightedScore value) {
        return backingMap.put(key, value);
    }

    @Override
    public void putAll(Map<? extends ChordTuple, ? extends WeightedScore> m) {
        backingMap.putAll(m);
    }

    @Override
    public WeightedScore remove(Object key) {
        return backingMap.remove(key);
    }

    @Override
    public void clear() {
        backingMap.clear();
    }

    @Override
    public boolean containsValue(Object value) {
        return backingMap.containsValue(value);
    }

    @Override
    public Set<ChordTuple> keySet() {
        return backingMap.keySet();
    }

    @Override
    public Collection<WeightedScore> values() {
        return backingMap.values();
    }

    @Override
    public Set<Entry<ChordTuple, WeightedScore>> entrySet() {
        return backingMap.entrySet();
    }

    @Override
    public WeightedScore getOrDefault(Object key, WeightedScore defaultValue) {
        return backingMap.getOrDefault(key, defaultValue);
    }

    @Override
    public WeightedScore putIfAbsent(ChordTuple key, WeightedScore value) {
        return backingMap.putIfAbsent(key, value);
    }

    @Override
    public boolean remove(Object key, Object value) {
        return backingMap.remove(key, value);
    }

    @Override
    public boolean replace(ChordTuple key, WeightedScore oldValue, WeightedScore newValue) {
        return backingMap.replace(key, oldValue, newValue);
    }

    @Override
    public WeightedScore replace(ChordTuple key, WeightedScore value) {
        return backingMap.replace(key, value);
    }
    File f;

    public void save() throws IOException {
        FileOutputStream fos = new FileOutputStream(f);
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        DataOutputStream dos = new DataOutputStream(bos);
        for(Chord c : Chord.getAllChords()){
            WeightedScore ws = get(new ChordTuple(c));
            dos.writeDouble(ws.totalScore);
            dos.writeDouble(ws.weight);
        }
        for(Chord c : Chord.getAllChords()){
            for(Chord d : Chord.getAllChords()){
                WeightedScore ws = get(new ChordTuple(c, d));
                dos.writeDouble(ws.totalScore);
                dos.writeDouble(ws.weight);
            }
        }
        for(Chord c : Chord.getAllChords()){
            for(Chord d : Chord.getAllChords()){
                for(Chord e : Chord.getAllChords()){
                    WeightedScore ws = get(new ChordTuple(c, d, e));
                    dos.writeDouble(ws.totalScore);
                    dos.writeDouble(ws.weight);
                }
            }
        }
        for(Chord c : Chord.getAllChords()){
            for(Chord d : Chord.getAllChords()){
                for(Chord e : Chord.getAllChords()){
                    for(Chord f : Chord.getAllChords()){
                        WeightedScore ws = get(new ChordTuple(c, d, e, f));
                        dos.writeDouble(ws.totalScore);
                        dos.writeDouble(ws.weight);
                    }
                }
            }
        }
        dos.close();
        bos.close();
        fos.close();

    }

    public void saveAs(File f) throws IOException {
        this.f = f;
        save();
    }

    private static String serializeLine(Entry<ChordTuple, WeightedScore> entry) {
        return entry.getKey().serialize()+":"+entry.getValue().serialize();
    }

    public RatingDatabase() {
        backingMap = new LinkedHashMap<>();
        for(Chord c : Chord.getAllChords()){
            backingMap.put(new ChordTuple(c), new WeightedScore(0.5, 1));
        }
        for(Chord c : Chord.getAllChords()){
            for(Chord d : Chord.getAllChords()){
                backingMap.put(new ChordTuple(c, d), new WeightedScore(0.5, 1));
            }
        }
        for(Chord c : Chord.getAllChords()){
            for(Chord d : Chord.getAllChords()){
                for(Chord e : Chord.getAllChords()){
                    backingMap.put(new ChordTuple(c, d, e), new WeightedScore(0.5, 1));
                }
            }
        }
        for(Chord c : Chord.getAllChords()){
            for(Chord d : Chord.getAllChords()){
                for(Chord e : Chord.getAllChords()){
                    for(Chord f : Chord.getAllChords()){
                        backingMap.put(new ChordTuple(c, d, e, f), new WeightedScore(0.5, 1));
                    }
                }
            }
        }
        f = null;
    }

    public RatingDatabase(File f) throws IOException {
        backingMap = new LinkedHashMap<>();
        this.f = f;
        readEntries();
    }

    private void readEntries() throws IOException {
        FileInputStream fis = new FileInputStream(f);
        DataInputStream dis = new DataInputStream(fis);
        for(Chord c : Chord.getAllChords()){
            backingMap.put(new ChordTuple(c), new WeightedScore(dis.readDouble(), dis.readDouble()));
        }
        for(Chord c : Chord.getAllChords()){
            for(Chord d : Chord.getAllChords()){
                backingMap.put(new ChordTuple(c, d), new WeightedScore(dis.readDouble(), dis.readDouble()));
            }
        }
        for(Chord c : Chord.getAllChords()){
            for(Chord d : Chord.getAllChords()){
                for(Chord e : Chord.getAllChords()){
                    backingMap.put(new ChordTuple(c, d, e), new WeightedScore(dis.readDouble(), dis.readDouble()));
                }
            }
        }
        for(Chord c : Chord.getAllChords()){
            for(Chord d : Chord.getAllChords()){
                for(Chord e : Chord.getAllChords()){
                    for(Chord f : Chord.getAllChords()){
                        backingMap.put(new ChordTuple(c, d, e, f), new WeightedScore(dis.readDouble(), dis.readDouble()));
                    }
                }
            }
        }
        dis.close();
        fis.close();
    }

    private void addLine(String s) {
        String lSide = s.substring(0, s.indexOf(':'));
        String rSide = s.substring(s.indexOf(':')+1);
        ChordTuple ct = ChordTuple.deserialize(lSide);
        WeightedScore ws = WeightedScore.deserialize(rSide);
        backingMap.put(ct, ws);
    }

    public static class WeightedScore implements StringSerializable {
        // ALL SCORES OUT OF 1
        private double totalScore;
        private double weight;
        public static WeightedScore deserialize(String s){
            String[] segs = s.split("/");
            return new WeightedScore(Double.parseDouble(segs[0]), Double.parseDouble(segs[1]));
        }
        public void applyRating(double score, double weight){
            this.totalScore = (this.totalScore) + (score * weight);
            this.weight = this.weight + weight;
        }


        public WeightedScore(double totalScore, double weight) {
            this.totalScore = totalScore;
            this.weight = weight;
        }

        public static WeightedScore blank(){
            return new WeightedScore(0, 0);
        }

        @Override
        public String serialize() {
            return totalScore + "/" + weight;
        }
    }
}

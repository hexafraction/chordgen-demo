//package org.hackcooper.chordgen.backend;
//
//import org.apache.commons.lang.StringUtils;
//
//import javax.sound.midi.InvalidMidiDataException;
//import java.io.File;
//import java.io.IOException;
//import java.util.Arrays;
//import java.util.Scanner;
//import java.util.stream.IntStream;
//
//public class RatingTool {
//    public static void main(String[] args) throws IOException, InvalidMidiDataException {
//        Scanner sc = new Scanner(System.in);
//        RatingDatabase db;
//        String userHome = System.getProperty("user.home");
//        File dbf = new File(userHome, "Documents/concertfish.db");
//        if(!dbf.exists()){
//            System.out.print("Generating and saving a new db... ");
//            db = new RatingDatabase();
//            // repeated chords 3 and 4 in a row suck
//            for(Chord c : Chord.getAllChords()){
//                db.get(new ChordTuple(c,c,c)).applyRating(-10, 1);
//                db.get(new ChordTuple(c,c,c,c)).applyRating(-10, 1);
//
//            }
//            // penalize transitions between major and minor
//            for(int i = 0; i < 12; i++){
//                db.get(new ChordTuple(Chord.fromOrdinals(i, 0), Chord.fromOrdinals(i, 1))).applyRating(0, 3);
//                db.get(new ChordTuple(Chord.fromOrdinals(i, 1), Chord.fromOrdinals(i, 0))).applyRating(0, 3);
//                db.get(new ChordTuple(Chord.fromOrdinals(i, 2), Chord.fromOrdinals(i, 1))).applyRating(0, 3);
//                db.get(new ChordTuple(Chord.fromOrdinals(i, 1), Chord.fromOrdinals(i, 2))).applyRating(0, 3);
//
//                db.addScore(new ChordProgression(Chord.fromOrdinals(i, 1)), 0, 2);
//            }
//            //db.get(new ChordTuple(Chord.fromOrdinals(0, 0), Chord.fromOrdinals(7, 0), Chord.fromOrdinals(9, 0), Chord.fromOrdinals(0, 0))).applyRating(1, 8);
//            //db.addScoreLim(new ChordProgression(Chord.fromOrdinals(0, 0), Chord.fromOrdinals(7, 0), Chord.fromOrdinals(9, 0), Chord.fromOrdinals(5, 0)), 0.6, 8);
//            //db.get(new ChordTuple(Chord.fromOrdinals(9, 0), Chord.fromOrdinals(7, 0), Chord.fromOrdinals(5, 0), Chord.fromOrdinals(7, 0))).applyRating(1, 8);
//            //db.addScoreLim(new ChordProgression(Chord.fromOrdinals(9, 0), Chord.fromOrdinals(7, 0), Chord.fromOrdinals(5, 0), Chord.fromOrdinals(7, 0)), 0.6, 8);
//            //db.get(new ChordTuple(Chord.fromOrdinals(0, 0), Chord.fromOrdinals(5, 0), Chord.fromOrdinals(9, 0), Chord.fromOrdinals(7, 0))).applyRating(1, 8);
//            //db.addScoreLim(new ChordProgression(Chord.fromOrdinals(0, 0), Chord.fromOrdinals(5, 0), Chord.fromOrdinals(9, 0), Chord.fromOrdinals(7, 0)), 0.6, 8);
//            db.saveAs(dbf);
//            System.out.println("[DONE]");
//        } else {
//            System.out.print("Loading the db... ");
//            db = new RatingDatabase(dbf);
//            System.out.println("[DONE]");
//        }
//        int count = 0;
//        while(true) {
//            double maxw = db.values().stream().mapToDouble(RatingDatabase.WeightedScore::getWeight).max().getAsDouble();
//
//            System.out.println("Type a negative number at any prompt to quit.");
//            System.out.println("The highest weight in the database is "+maxw);
//            GeneticFitter fit;
//            if(count%3==0){
//                System.out.println("Penalizing things we've seen before");
//                fit = new GeneticFitter(8, db.getFitnessFunction(maxw/2));
//            }
//            else {
//                fit = new GeneticFitter(8, db.getFitnessFunction(maxw*2));
//            }
//            System.out.print("Generating a progression... ");
//            ChordProgression cp = fit.generate();
//            System.out.println("[DONE with score "+fit.getFf().getFitness(cp)+"]");
//            System.out.println(IntStream.range(1,9).mapToObj(Integer::toString).map(a -> StringUtils.rightPad(a, 10)).reduce(String::concat).get());
//            System.out.println(Arrays.stream(cp.chords).map(Chord::toString).map(a->a.replaceAll("^([A-G])s", "$1#")).map(a->StringUtils.rightPad(a, 10)).reduce(String::concat).get());
//            ChordPlayer.playChords(cp);
//            System.out.print("Please input an overall rating [0-10]: ");
//            double rating = sc.nextDouble()/10;
//            if(rating<0){
//                break;
//            }
//            db.addScore(cp, rating, 0.5);
//            System.out.print("Please input a rating for the first half (chords 1-4) [0-10]: ");
//            double fhRating = sc.nextDouble()/10;
//            if(fhRating<0){
//                break;
//            }
//            db.addScore(cp.subset(0, 4), fhRating, 1);
//            System.out.print("Please input a rating for the second half (chords 5-8) [0-10]: ");
//            double shRating = sc.nextDouble()/10;
//            if(shRating<0){
//                break;
//            }
//            db.addScore(cp.subset(4, 8), shRating, 1);
//            count++;
//            if(count % 5 == 0){
//                System.out.print("Saving database... ");
//                db.save();
//                System.out.println("[DONE]");
//            }
//        }
//        System.out.print("Saving database... ");
//        db.save();
//        System.out.println("[DONE]");
//
//    }
//}

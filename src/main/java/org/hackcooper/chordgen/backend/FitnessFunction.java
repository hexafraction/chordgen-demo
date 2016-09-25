package org.hackcooper.chordgen.backend;

public interface FitnessFunction {
    double getFitness(ChordProgression cp);

    static FitnessFunction multiply(FitnessFunction a, FitnessFunction b){
        return cp -> a.getFitness(cp) * b.getFitness(cp);
    }
}

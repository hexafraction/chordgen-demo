package org.hackcooper.chordgen.backend;


import org.jenetics.*;
import org.jenetics.engine.Codec;
import org.jenetics.engine.Engine;
import org.jenetics.engine.EvolutionResult;
import org.jenetics.util.Factory;

public class GeneticFitter {
    final Factory<Genotype<AnyGene<Chord>>> factory;
    private static final Codec<Chord, AnyGene<Chord>> CODEC = Codec.of(
            Genotype.of(AnyChromosome.of(Chord::randomChord)),
            gt -> gt.getGene().getAllele()
    );
    int generations;
    public GeneticFitter(int length, FitnessFunction fit) {
        factory = new Factory<Genotype<AnyGene<Chord>>>() {
            @Override
            public Genotype<AnyGene<Chord>> newInstance() {
                return Genotype.of(AnyChromosome.of(Chord::randomChord, length));
            }
        };
        this.ff = fit;
        this.generations = length*16;

    }


    private Double fitness(final Genotype<AnyGene<Chord>> gt){
        return ff.getFitness(new ChordProgression(gt.getChromosome(0).stream().map(Gene::getAllele).toArray((i)->new Chord[i])));
    }


    FitnessFunction ff;
    ChordProgression generate(){
        final Engine<AnyGene<Chord>, Double> engine = Engine
                .builder((gt)->fitness(gt), factory)
                .maximizing()
                .populationSize(200)
                //.offspringSelector(new ExponentialRankSelector<>(0.8))
                .alterers(new SinglePointCrossover<>(1), new Mutator<>(0.25))
                .build();
        Genotype<AnyGene<Chord>> rslt = engine.stream().limit(1000).collect(EvolutionResult.toBestGenotype());
        System.out.println("Final score: " + fitness(rslt));
        return new ChordProgression(rslt.getChromosome(0).stream().map(Gene::getAllele).toArray((i)->new Chord[i]));
    };

}

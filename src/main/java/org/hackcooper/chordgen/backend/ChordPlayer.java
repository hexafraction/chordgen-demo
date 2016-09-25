package org.hackcooper.chordgen.backend;

import javax.sound.midi.*;
import java.util.Stack;

public class ChordPlayer {
    MidiDevice.Info[] MidiDeviceInfos = MidiSystem.getMidiDeviceInfo();
    //find the suitable device number here, based on some criteria
    MidiDevice MidiOutDevice = MidiSystem.getMidiDevice(MidiDeviceInfos[3]);

    Synthesizer synth = MidiSystem.getSynthesizer();
    MidiChannel[] channels = synth.getChannels();
    Stack<Integer> notes = new Stack<>();

    public ChordPlayer() throws MidiUnavailableException {
    }

    void playMajor(int key) throws MidiUnavailableException, InvalidMidiDataException {
        ShortMessage m1 = new ShortMessage();
        ShortMessage m2 = new ShortMessage();
        ShortMessage m3 = new ShortMessage();
        // Play the note Middle C (60) moderately loud
        // (velocity = 93)on channel 4 (zero-based).
        channels[0].noteOn(60+key, 93);
        notes.push(60+key);

        channels[0].noteOn(64+key, 93);
        notes.push(64+key);

        channels[0].noteOn(67+key, 93);
        notes.push(67+key);

    }

    void playMinor(int key) throws MidiUnavailableException, InvalidMidiDataException {
        ShortMessage m1 = new ShortMessage();
        ShortMessage m2 = new ShortMessage();
        ShortMessage m3 = new ShortMessage();
        // Play the note Middle C (60) moderately loud
        // (velocity = 93)on channel 4 (zero-based).
        channels[0].noteOn(60+key, 93);
        notes.push(60+key);

        channels[0].noteOn(64+key, 93);
        notes.push(63+key);

        channels[0].noteOn(67+key, 93);
        notes.push(67+key);

    }
    void playSeventh(int key) throws MidiUnavailableException, InvalidMidiDataException {
        ShortMessage m1 = new ShortMessage();
        ShortMessage m2 = new ShortMessage();
        ShortMessage m3 = new ShortMessage();
        ShortMessage m4 = new ShortMessage();
        // Play the note Middle C (60) moderately loud
        // (velocity = 93)on channel 4 (zero-based).
        channels[0].noteOn(60+key, 93);
        notes.push(60+key);

        channels[0].noteOn(64+key, 93);
        notes.push(64+key);

        channels[0].noteOn(67+key, 93);
        notes.push(67+key);

        channels[0].noteOn(70+key, 93);
        notes.push(70+key);


    }
    void stop() throws InvalidMidiDataException {
        while(!notes.empty()){
            int note = notes.pop();
            ShortMessage m1 = new ShortMessage();


            m1.setMessage(ShortMessage.NOTE_OFF, 4, note, 93);


            channels[0].noteOff(note);
        }
    }
}

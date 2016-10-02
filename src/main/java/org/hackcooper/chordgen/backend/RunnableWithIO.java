package org.hackcooper.chordgen.backend;

import java.io.IOException;

@FunctionalInterface
public interface RunnableWithIO<T> {
    public void runWithIO() throws IOException;
}

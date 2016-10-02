package org.hackcooper.chordgen.backend;

import java.io.IOException;

@FunctionalInterface
public interface SupplierWithIO<T> {
    public T getWithIO() throws IOException;
}

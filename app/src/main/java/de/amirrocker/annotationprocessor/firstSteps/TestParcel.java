package de.amirrocker.annotationprocessor.firstSteps;

import de.amirrocker.library.AutoParcel;

//@AutoParcel // - not yet implemented
public class TestParcel {
    private final String name;
    private final Integer number;

    public TestParcel(String name, Integer number) {
        this.name = name;
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public Integer getNumber() {
        return number;
    }
}

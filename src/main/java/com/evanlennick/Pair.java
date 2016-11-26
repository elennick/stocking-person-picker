package com.evanlennick;

import java.util.Arrays;
import java.util.List;

public class Pair {

    private Person person1;

    private Person person2;

    public Pair(Person person1, Person person2) {
        this.person1 = person1;
        this.person2 = person2;
    }

    public Person getPerson1() {
        return person1;
    }

    public Person getPerson2() {
        return person2;
    }

    public List<Person> getPeople() {
        return Arrays.asList(person1, person2);
    }
}

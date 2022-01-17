package com.example.tracker3;

import java.util.ArrayList;
import java.util.UUID;

public class Research {
    private UUID id;
    private String name;

    public Research(UUID id, String name) {
        this.id = id;
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    //TODO remove dummy function. Create a proper list generator.
    public static ArrayList<Research> createResearchesList(int numContacts) {
        ArrayList<Research> contacts = new ArrayList<>();

        for (int i = 1; i <= numContacts; i++) {
            contacts.add(new Research(UUID.randomUUID(), "Pesquisa " + i));
        }

        return contacts;
    }
}

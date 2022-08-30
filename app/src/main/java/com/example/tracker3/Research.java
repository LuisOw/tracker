package com.example.tracker3;


public class Research {
    private int id;
    private String title;
    private String description;
    private String state;

    public Research(int id, String title, String description, String state) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.state = state;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getState() {
        return state;
    }

    @Override
    public String toString() {
        return "Research{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", state='" + state + '\'' +
                '}';
    }


    /*
    //TODO remove dummy function. Create a proper list generator.
    public static ArrayList<Research> createResearchesList(int numContacts) {
        ArrayList<Research> contacts = new ArrayList<>();

        for (int i = 1; i <= numContacts; i++) {
            contacts.add(new Research(UUID.randomUUID(), "Pesquisa " + i));
        }

        return contacts;
    }*/
}


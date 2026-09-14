package entity;

import utils.StringUtility;

import java.util.UUID;

public class User {
    private UUID id = UUID.randomUUID();
    private String name;

    public User(UUID pId, String pName) {

        if (StringUtility.isStringNullOrBlank(pName)) {
            throw new IllegalArgumentException("User must have a name, please fill name field");
        }
        name = pName;

        this.id = pId;

    }

    public User(String pName) {
        if (StringUtility.isStringNullOrBlank(pName)) {
            throw new IllegalArgumentException("User must have a name, please fill name field");
        }
        name = pName;
    }

    public void setName(String pName) {
        if (StringUtility.isStringNullOrBlank(pName)) {
            throw new IllegalArgumentException("User must have a name, please fill name field");
        }
        this.name = pName;
    }

    public String getName() {
        return name;
    }

    public UUID getId() {
        return id;
    }

    @Override
    public String toString() {

        return this.getName() + " with id: " + this.id;
    }


}
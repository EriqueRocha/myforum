package dev.erique.myforum.enums;

public enum Role {

    ADMIN("admin"),
    CLIENT("client"),
    USER("user");

    private String role;

    Role(String role) {
        this.role = role;
    }

    public String getRole(){
        return role;
    }

}

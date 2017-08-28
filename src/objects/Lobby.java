package objects;

public class Lobby {
    private String name;
    private String uuid;
    private String passwort;

    public Lobby(String name, String uuid, String passwort) {
        this.name = name;
        this.uuid = uuid;
        this.passwort = passwort;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getPasswort() {
        return passwort;
    }

    public void setPasswort(String passwort) {
        this.passwort = passwort;
    }
}

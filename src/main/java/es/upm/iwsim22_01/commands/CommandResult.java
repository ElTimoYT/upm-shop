package es.upm.iwsim22_01.commands;

public class CommandResult {
    private String path = "";
    private boolean result = false, initialized = false;

    public void addPath(String path) {
        this.path = String.format("%s %s", this.path, path);
    }

    public void success() {
        result = true;
    }

    public void initialize() {
        initialized = true;
    }

    public void hideMessage() {
        initialized = false;
    }

    public boolean canShowMessage() {
        return initialized;
    }

    public String getMessage() {
        return String.format("%s: %s", path.substring(1, path.length()), result ? "ok" : "fail");
    }
}

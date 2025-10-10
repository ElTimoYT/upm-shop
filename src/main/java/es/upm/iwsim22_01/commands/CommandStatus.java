package es.upm.iwsim22_01.commands;

public class CommandStatus {
    private boolean status;
    private String message;

    public CommandStatus(boolean status, String message) {
        this.status = status;
        this.message = message;
    }

    public CommandStatus(boolean status) {
        this.status = status;
    }

    public boolean getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}

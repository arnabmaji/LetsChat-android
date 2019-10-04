package io.github.arnabmaji19.letschat;

public class InstantMessage {

    private String author;
    private String message;

    public InstantMessage(String author, String message) {
        this.author = author;
        this.message = message;
    }

    public InstantMessage() {
    }

    public String getAuthor() {
        return author;
    }

    public String getMessage() {
        return message;
    }
}

package edu.school21.Spring.Printer;

import edu.school21.Spring.Renderer.Renderer;

public class PrinterWithPrefixImpl implements Printer {
    private final Renderer renderer;
    private String prefix;

    public PrinterWithPrefixImpl(Renderer renderer) {
        this.renderer = renderer;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public void print(String message) {
        String prefixedMessage = prefix + " " + message;
        renderer.render(prefixedMessage);
    }
}
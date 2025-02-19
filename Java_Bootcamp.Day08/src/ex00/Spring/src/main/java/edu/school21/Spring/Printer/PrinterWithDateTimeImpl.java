package edu.school21.Spring.Printer;

import edu.school21.Spring.Renderer.Renderer;
import java.time.LocalDateTime;

public class PrinterWithDateTimeImpl implements Printer {
    private final Renderer renderer;

    public PrinterWithDateTimeImpl(Renderer renderer) {
        this.renderer = renderer;
    }

    @Override
    public void print(String message) {
        String dateTimeMessage = LocalDateTime.now() + ": " + message;
        renderer.render(dateTimeMessage);
    }
}
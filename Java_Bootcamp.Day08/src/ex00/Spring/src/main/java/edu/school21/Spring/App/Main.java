package edu.school21.Spring.App;

import edu.school21.Spring.Printer.Printer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("context.xml");
        
        Printer printer = context.getBean("printerWithPrefix", Printer.class);
        printer.print("Hello!");

        printer = context.getBean("printerWithDateTime", Printer.class);
        printer.print("Hello!");
    }
}
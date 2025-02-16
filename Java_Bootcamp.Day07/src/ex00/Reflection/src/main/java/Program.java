import classes.User;
import classes.Car;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Scanner;

public class Program {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("Classes:");
        System.out.println("  - User");
        System.out.println("  - Car");
        System.out.println("---------------------");
        System.out.println("Enter class name:");
        System.out.print("-> ");
        
        String className = scanner.nextLine();
        
        if (className.equalsIgnoreCase("exit")) {
            System.out.println("Program finished!");
            return;
        }
        
        Class<?> selectedClass;
        
        try {
            selectedClass = Class.forName("classes." + className);
            System.out.println("---------------------");
            
            // Display fields
            System.out.println("fields:");
            for (Field field : selectedClass.getDeclaredFields()) {
                System.out.println("\t" + field.getType().getSimpleName() + " " + field.getName());
            }
            
            // Display methods
            System.out.println("methods:");
            for (Method method : selectedClass.getDeclaredMethods()) {
                if (!method.getName().equals("toString")) {
                    String params = String.join(", ",
                            Arrays.stream(method.getParameterTypes())
                                    .map(Class::getSimpleName)
                                    .toArray(String[]::new));
                    System.out.printf("\t%s %s(%s)%n",
                            method.getReturnType().getSimpleName(),
                            method.getName(),
                            params);
                }
            }
            
            // Create object
            System.out.println("---------------------");
            System.out.println("Let's create an object.");
            Object obj = null;
            
            if (className.equals("User")) {
                System.out.print("firstName:\n-> ");
                String firstName = scanner.nextLine();
                System.out.print("lastName:\n-> ");
                String lastName = scanner.nextLine();
                System.out.print("height:\n-> ");
                int height = Integer.parseInt(scanner.nextLine());
                
                Constructor<?> constructor = selectedClass.getConstructor(String.class, String.class, int.class);
                obj = constructor.newInstance(firstName, lastName, height);
            } else if (className.equals("Car")) {
                System.out.print("model:\n-> ");
                String model = scanner.nextLine();
                System.out.print("year:\n-> ");
                int year = Integer.parseInt(scanner.nextLine());
                System.out.print("price:\n-> ");
                double price = Double.parseDouble(scanner.nextLine());
                
                Constructor<?> constructor = selectedClass.getConstructor(String.class, int.class, double.class);
                obj = constructor.newInstance(model, year, price);
            }
            
            System.out.println("Object created: " + obj);
            
            // Change field value
            System.out.println("---------------------");
            System.out.println("Enter name of the field for changing:");
            System.out.print("-> ");
            String fieldName = scanner.nextLine();
            
            if (fieldName.contains("(")) {
                System.out.println("Enter name of the method for call:");
                System.out.print("-> ");
                String methodName = fieldName.substring(0, fieldName.indexOf("("));
                String paramType = fieldName.substring(fieldName.indexOf("(") + 1, fieldName.indexOf(")"));
                
                Method method = selectedClass.getDeclaredMethod(methodName, getParamClass(paramType));
                System.out.println("Enter " + paramType + " value:");
                System.out.print("-> ");
                String value = scanner.nextLine();
                
                Object result = method.invoke(obj, parseValue(value, paramType));
                if (result != null) {
                    System.out.println("Method returned:");
                    System.out.println(result);
                }
                return;
            }
            
            Field field = selectedClass.getDeclaredField(fieldName);
            field.setAccessible(true);
            
            System.out.println("Enter " + field.getType().getSimpleName() + " value:");
            System.out.print("-> ");
            String value = scanner.nextLine();
            
            if (field.getType() == String.class) {
                field.set(obj, value);
            } else if (field.getType() == int.class) {
                field.set(obj, Integer.parseInt(value));
            } else if (field.getType() == double.class) {
                field.set(obj, Double.parseDouble(value));
            }
            
            System.out.println("Object updated: " + obj);
            
            // Call method
            System.out.println("---------------------");
            System.out.println("Enter name of the method for call:");
            System.out.print("-> ");
            String methodName = scanner.nextLine();
            
            Method method = null;
            for (Method m : selectedClass.getDeclaredMethods()) {
                if (m.getName().equals(methodName.split("\\(")[0])) {
                    method = m;
                    break;
                }
            }
            
            if (method != null) {
                System.out.println("Enter " + method.getParameterTypes()[0].getSimpleName() + " value:");
                System.out.print("-> ");
                String methodParam = scanner.nextLine();
                Object result = null;
                
                if (method.getParameterTypes()[0] == int.class) {
                    result = method.invoke(obj, Integer.parseInt(methodParam));
                } else if (method.getParameterTypes()[0] == double.class) {
                    result = method.invoke(obj, Double.parseDouble(methodParam));
                }
                
                if (result != null) {
                    System.out.println("Method returned:");
                    System.out.println(result);
                }
            }
            
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    private static Class<?> getParamClass(String type) {
        switch (type) {
            case "int": return int.class;
            case "double": return double.class;
            case "String": return String.class;
            default: throw new IllegalArgumentException("Unsupported type: " + type);
        }
    }
    
    private static Object parseValue(String value, String type) {
        switch (type) {
            case "int": return Integer.parseInt(value);
            case "double": return Double.parseDouble(value);
            case "String": return value;
            default: throw new IllegalArgumentException("Unsupported type: " + type);
        }
    }
}

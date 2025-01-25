package edu.school21.models;

import java.util.Objects;

/**
 * Класс Product представляет собой модель товара с уникальным идентификатором, названием и ценой.
 */
public class Product {
    private Long identifier;
    private String name;
    private int price;

    /**
     * Конструктор для создания объекта Product с указанными параметрами.
     *
     * @param identifier уникальный идентификатор товара
     * @param name       название товара
     * @param price      цена товара
     */
    public Product(Long identifier, String name, int price) {
        this.identifier = identifier;
        this.name = name;
        this.price = price;
    }

    /**
     * Конструктор без параметров для создания объекта Product.
     */
    public Product() {
    }

    // Геттеры и сеттеры

    public Long getIdentifier() {
        return identifier;
    }

    public void setIdentifier(Long identifier) {
        this.identifier = identifier;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    // Переопределение методов equals и hashCode

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product)) return false;
        Product product = (Product) o;
        return price == product.price &&
                Objects.equals(identifier, product.identifier) &&
                Objects.equals(name, product.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identifier, name, price);
    }

    @Override
    public String toString() {
        return "Product{" +
                "identifier=" + identifier +
                ", name='" + name + '\'' +
                ", price=" + price +
                '}';
    }
}
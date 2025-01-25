package edu.school21.repositories;

import edu.school21.models.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ProductsRepositoryJdbcImplTest {
    private ProductsRepositoryJdbcImpl productsRepository;
    private DataSource dataSource;

    final List<Product> EXPECTED_FIND_ALL_PRODUCTS = new ArrayList<>();
    final Product EXPECTED_FIND_BY_ID_PRODUCT = new Product(1L, "Product A", 100);
    final Product EXPECTED_UPDATED_PRODUCT = new Product(1L, "Updated Product", 150);

    @BeforeEach
    public void init() {
        dataSource = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.HSQL)
                .addScript("schema.sql")
                .addScript("data.sql")
                .build();

        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        productsRepository = new ProductsRepositoryJdbcImpl(jdbcTemplate);

        // Подготовка данных для тестирования
        EXPECTED_FIND_ALL_PRODUCTS.add(new Product(1L, "Product A", 100));
        EXPECTED_FIND_ALL_PRODUCTS.add(new Product(2L, "Product B", 200));
        EXPECTED_FIND_ALL_PRODUCTS.add(new Product(3L, "Product C", 300));
        EXPECTED_FIND_ALL_PRODUCTS.add(new Product(4L, "Product D", 400));
        EXPECTED_FIND_ALL_PRODUCTS.add(new Product(5L, "Product E", 500));
    }

    @Test
    public void testFindAll() {
        List<Product> products = productsRepository.findAll();
        assertEquals(EXPECTED_FIND_ALL_PRODUCTS, products);
    }

    @Test
    public void testFindById() {
        Optional<Product> product = productsRepository.findById(1L);
        assertTrue(product.isPresent());
        assertEquals(EXPECTED_FIND_BY_ID_PRODUCT, product.get());
    }

    @Test
    public void testUpdate() {
        productsRepository.update(EXPECTED_UPDATED_PRODUCT);
        Optional<Product> updatedProduct = productsRepository.findById(1L);
        assertTrue(updatedProduct.isPresent());
        assertEquals(EXPECTED_UPDATED_PRODUCT, updatedProduct.get());
    }

    @Test
    public void testSave() {
        Product newProduct = new Product(6L, "Product F", 600);
        productsRepository.save(newProduct);
        Optional<Product> savedProduct = productsRepository.findById(6L);
        assertTrue(savedProduct.isPresent());
        assertEquals(newProduct, savedProduct.get());
    }

    @Test
    public void testDelete() {
        productsRepository.delete(1L);
        Optional<Product> deletedProduct = productsRepository.findById(1L);
        assertFalse(deletedProduct.isPresent());
    }
}

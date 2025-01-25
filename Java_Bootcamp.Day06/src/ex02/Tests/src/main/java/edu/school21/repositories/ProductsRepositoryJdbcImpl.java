package edu.school21.repositories;

import edu.school21.models.Product;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;
import java.util.Optional;

public class ProductsRepositoryJdbcImpl implements ProductsRepository {
    private final JdbcTemplate jdbcTemplate;

    public ProductsRepositoryJdbcImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Product> rowMapper = (rs, rowNum) -> new Product(
            rs.getLong("identifier"),
            rs.getString("name"),
            rs.getInt("price")
    );

    @Override
    public List<Product> findAll() {
        return jdbcTemplate.query("SELECT * FROM product", rowMapper);
    }

    @Override
    public Optional<Product> findById(Long id) {
        return jdbcTemplate.query("SELECT * FROM product WHERE identifier = ?",
                rowMapper, id).stream().findFirst();
    }

    @Override
    public void update(Product product) {
        jdbcTemplate.update("UPDATE product SET name = ?, price = ? WHERE identifier = ?",
                product.getName(), product.getPrice(), product.getIdentifier());
    }

    @Override
    public void save(Product product) {
        jdbcTemplate.update("INSERT INTO product (identifier, name, price) VALUES (?, ?, ?)",
                product.getIdentifier(), product.getName(), product.getPrice());
    }

    @Override
    public void delete(Long id) {
        jdbcTemplate.update("DELETE FROM product WHERE identifier = ?", id);
    }
}

package edu.school21.orm.manager;

import edu.school21.orm.annotations.OrmColumn;
import edu.school21.orm.annotations.OrmColumnId;
import edu.school21.orm.annotations.OrmEntity;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OrmManager {
    private final DataSource dataSource;

    public OrmManager(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void init(Class<?>... entityClasses) {
        for (Class<?> entityClass : entityClasses) {
            if (!entityClass.isAnnotationPresent(OrmEntity.class)) {
                continue;
            }

            String tableName = entityClass.getAnnotation(OrmEntity.class).table();
            dropTableIfExists(tableName);
            createTable(entityClass);
        }
    }

    private void dropTableIfExists(String tableName) {
        String sql = "DROP TABLE IF EXISTS " + tableName;
        System.out.println("Executing SQL: " + sql);
        
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to drop table: " + tableName, e);
        }
    }

    private void createTable(Class<?> entityClass) {
        OrmEntity entityAnnotation = entityClass.getAnnotation(OrmEntity.class);
        String tableName = entityAnnotation.table();
        int defaultLength = entityAnnotation.length();
        StringBuilder sql = new StringBuilder("CREATE TABLE ")
                .append(tableName)
                .append(" (");

        Field[] fields = entityClass.getDeclaredFields();
        List<String> columns = new ArrayList<>();

        for (Field field : fields) {
            if (field.isAnnotationPresent(OrmColumnId.class)) {
                columns.add(field.getName() + " BIGSERIAL PRIMARY KEY");
            } else if (field.isAnnotationPresent(OrmColumn.class)) {
                OrmColumn column = field.getAnnotation(OrmColumn.class);
                // Используем имя из OrmColumn, а длину из OrmEntity
                String columnDefinition = column.name() + " " + getSqlType(field.getType(), defaultLength);
                columns.add(columnDefinition);
            }
        }

        sql.append(String.join(", ", columns)).append(")");
        System.out.println("Executing SQL: " + sql);

        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql.toString());
        } catch (SQLException e) {
            throw new RuntimeException("Failed to create table: " + tableName, e);
        }
    }

    public void save(Object entity) {
        Class<?> entityClass = entity.getClass();
        if (!entityClass.isAnnotationPresent(OrmEntity.class)) {
            throw new IllegalArgumentException("Class is not an ORM entity");
        }

        String tableName = entityClass.getAnnotation(OrmEntity.class).table();
        List<String> columnNames = new ArrayList<>();
        List<Object> values = new ArrayList<>();

        for (Field field : entityClass.getDeclaredFields()) {
            if (field.isAnnotationPresent(OrmColumnId.class)) {
                continue;
            }
            if (field.isAnnotationPresent(OrmColumn.class)) {
                field.setAccessible(true);
                try {
                    Object value = field.get(entity);
                    if (value != null) {
                        columnNames.add(field.getAnnotation(OrmColumn.class).name());
                        values.add(value);
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Failed to access field: " + field.getName(), e);
                }
            }
        }

        String sql = String.format("INSERT INTO %s (%s) VALUES (%s) RETURNING id",
                tableName,
                String.join(", ", columnNames),
                String.join(", ", Collections.nCopies(values.size(), "?")));

        System.out.println("Executing SQL: " + sql);

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            for (int i = 0; i < values.size(); i++) {
                statement.setObject(i + 1, values.get(i));
            }

            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                Field idField = getIdField(entityClass);
                idField.setAccessible(true);
                idField.set(entity, rs.getLong(1));
            }
        } catch (SQLException | IllegalAccessException e) {
            throw new RuntimeException("Failed to save entity", e);
        }
    }

    public void update(Object entity) {
        Class<?> entityClass = entity.getClass();
        if (!entityClass.isAnnotationPresent(OrmEntity.class)) {
            throw new IllegalArgumentException("Class is not an ORM entity");
        }

        String tableName = entityClass.getAnnotation(OrmEntity.class).table();
        List<String> setColumns = new ArrayList<>();
        List<Object> values = new ArrayList<>();
        Long id = null;

        for (Field field : entityClass.getDeclaredFields()) {
            field.setAccessible(true);
            try {
                if (field.isAnnotationPresent(OrmColumnId.class)) {
                    id = (Long) field.get(entity);
                    if (id == null) {
                        throw new IllegalArgumentException("ID cannot be null for update operation");
                    }
                } else if (field.isAnnotationPresent(OrmColumn.class)) {
                    setColumns.add(field.getAnnotation(OrmColumn.class).name() + " = ?");
                    values.add(field.get(entity));
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Failed to access field: " + field.getName(), e);
            }
        }

        values.add(id); // Add ID for WHERE clause

        String sql = String.format("UPDATE %s SET %s WHERE id = ?",
                tableName,
                String.join(", ", setColumns));

        System.out.println("Executing SQL: " + sql);

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            for (int i = 0; i < values.size(); i++) {
                statement.setObject(i + 1, values.get(i));
            }

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update entity", e);
        }
    }

    public <T> T findById(Long id, Class<T> entityClass) {
        if (!entityClass.isAnnotationPresent(OrmEntity.class)) {
            throw new IllegalArgumentException("Class is not an ORM entity");
        }

        String tableName = entityClass.getAnnotation(OrmEntity.class).table();
        String sql = "SELECT * FROM " + tableName + " WHERE id = ?";

        System.out.println("Executing SQL: " + sql);

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setLong(1, id);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                T entity = entityClass.newInstance();
                for (Field field : entityClass.getDeclaredFields()) {
                    field.setAccessible(true);
                    if (field.isAnnotationPresent(OrmColumnId.class)) {
                        field.set(entity, rs.getLong("id"));
                    } else if (field.isAnnotationPresent(OrmColumn.class)) {
                        String columnName = field.getAnnotation(OrmColumn.class).name();
                        Object value = rs.getObject(columnName, field.getType());
                        field.set(entity, value);
                    }
                }
                return entity;
            }
            return null;
        } catch (SQLException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Failed to find entity by ID", e);
        }
    }

    private String getSqlType(Class<?> type, int length) {
        if (type == String.class) {
            return "VARCHAR(" + length + ")";
        } else if (type == Integer.class) {
            return "INTEGER";
        } else if (type == Long.class) {
            return "BIGINT";
        } else if (type == Double.class) {
            return "DOUBLE PRECISION";
        } else if (type == Boolean.class) {
            return "BOOLEAN";
        }
        throw new IllegalArgumentException("Unsupported type: " + type.getName());
    }

    private Field getIdField(Class<?> entityClass) {
        for (Field field : entityClass.getDeclaredFields()) {
            if (field.isAnnotationPresent(OrmColumnId.class)) {
                return field;
            }
        }
        throw new IllegalArgumentException("No ID field found in entity class");
    }
}

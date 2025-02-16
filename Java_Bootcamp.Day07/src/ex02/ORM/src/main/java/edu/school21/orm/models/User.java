package edu.school21.orm.models;

import edu.school21.orm.annotations.OrmColumn;
import edu.school21.orm.annotations.OrmColumnId;
import edu.school21.orm.annotations.OrmEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@OrmEntity(table = "simple_user", length = 10)
public class User {
    @OrmColumnId
    private Long id;

    @OrmColumn(name = "first_name")
    private String firstName;

    @OrmColumn(name = "last_name")
    private String lastName;

    @OrmColumn(name = "age")
    private Integer age;

    public User(String firstName, String lastName, Integer age) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
    }
}

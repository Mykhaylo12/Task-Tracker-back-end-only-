package com.example.demo.model;

import lombok.Data;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "name")
    private String name;
    @ManyToMany(mappedBy = "roles",fetch = FetchType.LAZY)
    private List<User>users;

    @Override
    public String toString() {
        return "Role{" + "id=" + id +", name='" + name + '\'' + '}';
    }
}

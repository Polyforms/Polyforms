package org.polyforms.repository.integration;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class MockEntity {
    @Id
    private Long id;
    private String code;
    private String name;

    protected MockEntity() {
    }

    public MockEntity(final Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(final String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }
}

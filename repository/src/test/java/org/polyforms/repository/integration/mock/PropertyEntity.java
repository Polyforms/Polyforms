package org.polyforms.repository.integration.mock;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class PropertyEntity {
    private Long id;

    protected PropertyEntity() {
    }

    public PropertyEntity(final Long id) {
        this.id = id;
    }

    @Id
    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }
}

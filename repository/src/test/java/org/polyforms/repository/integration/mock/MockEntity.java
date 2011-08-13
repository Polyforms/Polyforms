package org.polyforms.repository.integration.mock;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity
@NamedQueries({
        @NamedQuery(name = "MockEntity.findByName", query = "select m from MockEntity m where m.name = ?1"),
        @NamedQuery(name = "MockEntity.loadByName", query = "select m from MockEntity m where m.name = ?1"),
        @NamedQuery(name = "MockEntity.loadByCodeAndName", query = "select m from MockEntity m where m.code = ?1 and m.name = ?2 "),
        @NamedQuery(name = "MockEntity.getByName", query = "select m from MockEntity m where m.name = ?1"),
        @NamedQuery(name = "MockEntity.getByCodeAndName", query = "select m from MockEntity m where m.code = :code and m.name = :name "),
        @NamedQuery(name = "MockEntity.updateNameByCode", query = "update MockEntity m set m.name = :name where m.code = :code"),
        @NamedQuery(name = "MockEntity.removeByCode", query = "delete From MockEntity m where m.code = :code") })
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

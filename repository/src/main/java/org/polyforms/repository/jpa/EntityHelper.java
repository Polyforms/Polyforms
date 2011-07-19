package org.polyforms.repository.jpa;

/**
 * Helper for resolving identifier of entity which are persisted by JPA framework.
 * 
 * @author Kuisong Tong
 * @since 1.0
 */
public interface EntityHelper {
    /**
     * Check specific class whether it is an entity class.
     * 
     * @param candidate the class to check
     * 
     * @return true if the candidate is an entity class
     */
    boolean isEntity(Class<?> candidate);

    /**
     * Get class of identifier from an entity class.
     * 
     * @param entityClass the entity class
     * 
     * @return Identity class of the entity
     * @throws IllegalArgumentException if it is not an class of entity
     */
    Class<?> getIdentifierClass(Class<?> entityClass);

    /**
     * Get name of identifier.
     * 
     * @param entityClass the entity class
     * 
     * @return Identity name of the entity
     * @throws IllegalArgumentException if it is not an class of entity
     */
    String getIdentifierName(Class<?> entityClass);

    /**
     * Get identifier of entity instance.
     * 
     * @param entity the entity instance
     * 
     * @return Identity value of the entity instance
     * @throws IllegalArgumentException if it is not an instance of entity
     */
    Object getIdentifierValue(Object entity);
}

package org.polyforms.repository.integration.hibernate;

import org.polyforms.repository.integration.ReadOnlyRepositoryIT;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration("hibernate-context.xml")
public class HibernateReadOnlyRepositoryIT extends ReadOnlyRepositoryIT {
}

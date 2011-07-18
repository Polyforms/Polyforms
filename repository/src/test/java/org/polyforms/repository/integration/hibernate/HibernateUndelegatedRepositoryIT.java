package org.polyforms.repository.integration.hibernate;

import org.polyforms.repository.integration.UndelegatedRepositoryIT;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration("hibernate-context.xml")
public class HibernateUndelegatedRepositoryIT extends UndelegatedRepositoryIT {
}

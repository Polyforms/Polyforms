package org.polyforms.repository.integration.openjpa;

import org.polyforms.repository.integration.ReadOnlyRepositoryIT;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration("openJpa-context.xml")
public class OpenJpaReadOnlyRepositoryIT extends ReadOnlyRepositoryIT {
}

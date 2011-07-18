package org.polyforms.repository.integration.eclipselink;

import org.polyforms.repository.integration.ReadOnlyRepositoryIT;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration("eclipseLink-context.xml")
public class EclipseLinkReadOnlyRepositoryIT extends ReadOnlyRepositoryIT {
}

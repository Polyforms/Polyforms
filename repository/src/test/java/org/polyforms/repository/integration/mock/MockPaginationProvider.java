package org.polyforms.repository.integration.mock;

import org.polyforms.repository.jpa.PaginationProvider;

public class MockPaginationProvider implements PaginationProvider {
    public int getFirstResult() {
        return 1;
    }

    public int getMaxResults() {
        return 1;
    }
}

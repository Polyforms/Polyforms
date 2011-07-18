package org.polyforms.event.integration;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.polyforms.event.annotation.NoOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@ContextConfiguration("ComponentScannerTest-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public final class NoOperationAdvisorTest {
    @Autowired
    private NoOperationService noopService;

    @Test
    public void noOperation() {
        noopService.noOperation();
    }

    @Component
    public static interface NoOperationService {
        @NoOperation
        void noOperation();
    }
}

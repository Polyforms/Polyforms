package org.polyforms.repository.jpa.querydsl;

import com.mysema.query.types.path.EntityPathBase;
import com.mysema.query.types.path.StringPath;

@SuppressWarnings("serial")
public class QMockEntity extends EntityPathBase<MockEntity> {
    public final StringPath code = createString("code");

    public static final QMockEntity mockEntity = new QMockEntity("mockEntity");

    public QMockEntity(final String variable) {
        super(MockEntity.class, variable);
    }
}

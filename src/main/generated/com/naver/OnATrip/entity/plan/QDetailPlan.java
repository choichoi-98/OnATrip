package com.naver.OnATrip.entity.plan;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QDetailPlan is a Querydsl query type for DetailPlan
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QDetailPlan extends EntityPathBase<DetailPlan> {

    private static final long serialVersionUID = 1210336358L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QDetailPlan detailPlan = new QDetailPlan("detailPlan");

    public final StringPath country = createString("country");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> memberId = createNumber("memberId", Long.class);

    public final DatePath<java.time.LocalDate> perDate = createDate("perDate", java.time.LocalDate.class);

    public final com.naver.OnATrip.entity.QPlan plan;

    public QDetailPlan(String variable) {
        this(DetailPlan.class, forVariable(variable), INITS);
    }

    public QDetailPlan(Path<? extends DetailPlan> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QDetailPlan(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QDetailPlan(PathMetadata metadata, PathInits inits) {
        this(DetailPlan.class, metadata, inits);
    }

    public QDetailPlan(Class<? extends DetailPlan> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.plan = inits.isInitialized("plan") ? new com.naver.OnATrip.entity.QPlan(forProperty("plan")) : null;
    }

}


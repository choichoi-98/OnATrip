package com.naver.OnATrip.entity.plan;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QDetailPlan is a Querydsl query type for DetailPlan
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QDetailPlan extends EntityPathBase<DetailPlan> {

    private static final long serialVersionUID = 1210336358L;

    public static final QDetailPlan detailPlan = new QDetailPlan("detailPlan");

    public final StringPath country = createString("country");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath perDate = createString("perDate");

    public final NumberPath<Long> planId = createNumber("planId", Long.class);

    public QDetailPlan(String variable) {
        super(DetailPlan.class, forVariable(variable));
    }

    public QDetailPlan(Path<? extends DetailPlan> path) {
        super(path.getType(), path.getMetadata());
    }

    public QDetailPlan(PathMetadata metadata) {
        super(DetailPlan.class, metadata);
    }

}


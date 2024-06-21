package com.naver.OnATrip.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QPlan is a Querydsl query type for Plan
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPlan extends EntityPathBase<Plan> {

    private static final long serialVersionUID = 85021794L;

    public static final QPlan plan = new QPlan("plan");

    public final StringPath country = createString("country");

    public final StringPath endDate = createString("endDate");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Long> mateId = createNumber("mateId", Long.class);

    public final NumberPath<Long> memberId = createNumber("memberId", Long.class);

    public final StringPath startDate = createString("startDate");

    public QPlan(String variable) {
        super(Plan.class, forVariable(variable));
    }

    public QPlan(Path<? extends Plan> path) {
        super(path.getType(), path.getMetadata());
    }

    public QPlan(PathMetadata metadata) {
        super(Plan.class, metadata);
    }

}


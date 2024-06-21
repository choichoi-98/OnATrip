package com.naver.OnATrip.entity.pay;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSubscribe is a Querydsl query type for Subscribe
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSubscribe extends EntityPathBase<Subscribe> {

    private static final long serialVersionUID = -1338005909L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSubscribe subscribe = new QSubscribe("subscribe");

    public final StringPath endDate = createString("endDate");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.naver.OnATrip.entity.QMember member;

    public final EnumPath<SubscribeRenewal> renewal = createEnum("renewal", SubscribeRenewal.class);

    public final StringPath startDate = createString("startDate");

    public final EnumPath<SubscribeStatus> status = createEnum("status", SubscribeStatus.class);

    public QSubscribe(String variable) {
        this(Subscribe.class, forVariable(variable), INITS);
    }

    public QSubscribe(Path<? extends Subscribe> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSubscribe(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSubscribe(PathMetadata metadata, PathInits inits) {
        this(Subscribe.class, metadata, inits);
    }

    public QSubscribe(Class<? extends Subscribe> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new com.naver.OnATrip.entity.QMember(forProperty("member")) : null;
    }

}


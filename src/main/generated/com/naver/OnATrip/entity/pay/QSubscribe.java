package com.naver.OnATrip.entity.pay;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QSubscribe is a Querydsl query type for Subscribe
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSubscribe extends EntityPathBase<Subscribe> {

    private static final long serialVersionUID = -1338005909L;

    public static final QSubscribe subscribe = new QSubscribe("subscribe");

    public final StringPath endDate = createString("endDate");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Integer> itemPeriod = createNumber("itemPeriod", Integer.class);

    public final StringPath memberId = createString("memberId");

    public final EnumPath<SubscribeRenewal> renewal = createEnum("renewal", SubscribeRenewal.class);

    public final StringPath startDate = createString("startDate");

    public final EnumPath<SubscribeStatus> status = createEnum("status", SubscribeStatus.class);

    public QSubscribe(String variable) {
        super(Subscribe.class, forVariable(variable));
    }

    public QSubscribe(Path<? extends Subscribe> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSubscribe(PathMetadata metadata) {
        super(Subscribe.class, metadata);
    }

}


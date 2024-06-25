package com.naver.OnATrip.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * Qlocation is a Querydsl query type for location
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class Qlocation extends EntityPathBase<location> {

    private static final long serialVersionUID = -1209483186L;

    public static final Qlocation location = new Qlocation("location");

    public final StringPath city = createString("city");

    public final StringPath country = createString("country");

    public final StringPath description = createString("description");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final ArrayPath<byte[], Byte> image = createArray("image", byte[].class);

    public Qlocation(String variable) {
        super(location.class, forVariable(variable));
    }

    public Qlocation(Path<? extends location> path) {
        super(path.getType(), path.getMetadata());
    }

    public Qlocation(PathMetadata metadata) {
        super(location.class, metadata);
    }

}


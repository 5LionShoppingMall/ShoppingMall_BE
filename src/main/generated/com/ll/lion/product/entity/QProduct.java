package com.ll.lion.product.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QProduct is a Querydsl query type for Product
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QProduct extends EntityPathBase<Product> {

    private static final long serialVersionUID = 1432148308L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QProduct product = new QProduct("product");

    public final com.ll.lion.common.entity.QDateEntity _super = new com.ll.lion.common.entity.QDateEntity(this);

    public final ListPath<CartItem, QCartItem> cartItems = this.<CartItem, QCartItem>createList("cartItems", CartItem.class, QCartItem.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath description = createString("description");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final ListPath<com.ll.lion.common.entity.Image, com.ll.lion.common.entity.QImage> images = this.<com.ll.lion.common.entity.Image, com.ll.lion.common.entity.QImage>createList("images", com.ll.lion.common.entity.Image.class, com.ll.lion.common.entity.QImage.class, PathInits.DIRECT2);

    public final ListPath<com.ll.lion.community.like.entity.Like, com.ll.lion.community.like.entity.QLike> likes = this.<com.ll.lion.community.like.entity.Like, com.ll.lion.community.like.entity.QLike>createList("likes", com.ll.lion.community.like.entity.Like.class, com.ll.lion.community.like.entity.QLike.class, PathInits.DIRECT2);

    public final NumberPath<Long> price = createNumber("price", Long.class);

    public final com.ll.lion.user.entity.QUser seller;

    public final EnumPath<ProductStatus> status = createEnum("status", ProductStatus.class);

    public final StringPath title = createString("title");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QProduct(String variable) {
        this(Product.class, forVariable(variable), INITS);
    }

    public QProduct(Path<? extends Product> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QProduct(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QProduct(PathMetadata metadata, PathInits inits) {
        this(Product.class, metadata, inits);
    }

    public QProduct(Class<? extends Product> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.seller = inits.isInitialized("seller") ? new com.ll.lion.user.entity.QUser(forProperty("seller"), inits.get("seller")) : null;
    }

}


package com.ll.lion.user.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUser is a Querydsl query type for User
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUser extends EntityPathBase<User> {

    private static final long serialVersionUID = 913421028L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUser user = new QUser("user");

    public final StringPath address = createString("address");

    public final com.ll.lion.product.entity.QCart cart;

    public final ListPath<com.ll.lion.community.comment.entity.Comment, com.ll.lion.community.comment.entity.QComment> comments = this.<com.ll.lion.community.comment.entity.Comment, com.ll.lion.community.comment.entity.QComment>createList("comments", com.ll.lion.community.comment.entity.Comment.class, com.ll.lion.community.comment.entity.QComment.class, PathInits.DIRECT2);

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final StringPath email = createString("email");

    public final BooleanPath emailVerified = createBoolean("emailVerified");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final ListPath<com.ll.lion.community.like.entity.Like, com.ll.lion.community.like.entity.QLike> likes = this.<com.ll.lion.community.like.entity.Like, com.ll.lion.community.like.entity.QLike>createList("likes", com.ll.lion.community.like.entity.Like.class, com.ll.lion.community.like.entity.QLike.class, PathInits.DIRECT2);

    public final StringPath nickname = createString("nickname");

    public final StringPath password = createString("password");

    public final StringPath phoneNumber = createString("phoneNumber");

    public final ListPath<com.ll.lion.community.post.entity.Post, com.ll.lion.community.post.entity.QPost> posts = this.<com.ll.lion.community.post.entity.Post, com.ll.lion.community.post.entity.QPost>createList("posts", com.ll.lion.community.post.entity.Post.class, com.ll.lion.community.post.entity.QPost.class, PathInits.DIRECT2);

    public final ListPath<com.ll.lion.product.entity.Product, com.ll.lion.product.entity.QProduct> products = this.<com.ll.lion.product.entity.Product, com.ll.lion.product.entity.QProduct>createList("products", com.ll.lion.product.entity.Product.class, com.ll.lion.product.entity.QProduct.class, PathInits.DIRECT2);

    public final StringPath profilePhotoUrl = createString("profilePhotoUrl");

    public final EnumPath<SocialProvider> provider = createEnum("provider", SocialProvider.class);

    public final StringPath providerId = createString("providerId");

    public final QRefreshToken refreshToken;

    public final StringPath role = createString("role");

    public final DateTimePath<java.time.LocalDateTime> updatedAt = createDateTime("updatedAt", java.time.LocalDateTime.class);

    public QUser(String variable) {
        this(User.class, forVariable(variable), INITS);
    }

    public QUser(Path<? extends User> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUser(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUser(PathMetadata metadata, PathInits inits) {
        this(User.class, metadata, inits);
    }

    public QUser(Class<? extends User> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.cart = inits.isInitialized("cart") ? new com.ll.lion.product.entity.QCart(forProperty("cart"), inits.get("cart")) : null;
        this.refreshToken = inits.isInitialized("refreshToken") ? new QRefreshToken(forProperty("refreshToken")) : null;
    }

}


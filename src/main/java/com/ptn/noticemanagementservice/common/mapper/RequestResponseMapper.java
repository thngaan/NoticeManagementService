package com.ptn.noticemanagementservice.common.mapper;

import org.mapstruct.IterableMapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Qualifier;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Collection;
import java.util.List;

/**
 * Abstract mapper maps Entity to DTO and vice versa
 *
 * @param <S> source
 * @param <T> target
 */
public interface RequestResponseMapper<R, S, T> {

    @Simple
    T toDto(S entity);

    @SimpleList
    @IterableMapping(qualifiedBy = Simple.class)
    List<T> toDtoList(Collection<S> entities);

    @Simple
    S toEntity(R request);

    @SimpleList
    @IterableMapping(qualifiedBy = Simple.class)
    List<S> toEntityList(Collection<R> entities);

    @Simple
    @IterableMapping(qualifiedBy = Simple.class)
    void updateEntity(R request, @MappingTarget S entity);

    @Qualifier
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.CLASS)
    @interface Simple {
    }

    @Qualifier
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.CLASS)
    @interface SimpleList {
    }

}

package com.apimybarber.domain.entity.mappers;

public interface EntityMapper<V, E> {
    E toEntity(V vo, E entity);
}

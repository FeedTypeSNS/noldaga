package com.noldaga.domain.Mapper;

import java.util.List;

public interface GenericMapper<E, D> {
    D toDto(E entity);
    E toEntity(D dto);
    List<E> toEntityList(List<D> dtoList);
    List<D> toDtoList(List<E> entityList);
}

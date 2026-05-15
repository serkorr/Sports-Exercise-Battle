package org.sports.exercise.battle.application.common;

public interface DTOConverter<TEntity, TDto> {
    TDto toDTO(TEntity entity);
}
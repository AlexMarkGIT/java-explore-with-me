package ru.practicum.statistic.model;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.dto.HitDto;

@Mapper(componentModel = "spring")
public interface HitMapper {

    @Mapping(target = "timestamp", source = "timestamp", dateFormat = "yyyy-MM-dd HH:mm:ss")
    Hit toEntity(HitDto hitDto);

}

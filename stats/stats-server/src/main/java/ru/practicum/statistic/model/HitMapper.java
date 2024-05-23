package ru.practicum.statistic.model;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.dto.HitDto;
import ru.practicum.statistic.utils.Pattern;

@Mapper(componentModel = "spring")
public interface HitMapper {

    @Mapping(target = "timestamp", source = "timestamp", dateFormat = Pattern.DATETIME)
    Hit toEntity(HitDto hitDto);

}

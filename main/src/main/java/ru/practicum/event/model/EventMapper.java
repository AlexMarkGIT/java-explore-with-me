package ru.practicum.event.model;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.practicum.event.dto.*;

import java.util.List;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface EventMapper {

    @Mapping(target = "lat", source = "location.lat")
    @Mapping(target = "lon", source = "location.lon")
    @Mapping(target = "category", ignore = true)
    Event toNewEntity(EventNewDto eventNewDto);

    @Mapping(target = "lat", source = "location.lat")
    @Mapping(target = "lon", source = "location.lon")
    @Mapping(target = "category", ignore = true)
    void updateEventFromDtoByUser(EventUpdateUserDto eventUpdateUserDto,
                                  @MappingTarget Event event);

    @Mapping(target = "lat", source = "location.lat")
    @Mapping(target = "lon", source = "location.lon")
    @Mapping(target = "category", ignore = true)
    void updateEventFromDtoByAdmin(EventUpdateAdminDto eventUpdateAdminDto,
                                    @MappingTarget Event event);

    @Mapping(target = "location",
            expression = "java(new ru.practicum.event.dto.Location(event.getLat(), event.getLon()))")
    EventDto toDto(Event event);

    List<EventDto> toDtoList(List<Event> events);

    EventShortDto toShortDto(Event event);

    List<EventShortDto> toShortDtoList(List<Event> events);

}

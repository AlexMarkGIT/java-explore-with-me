package ru.practicum.request.model;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.request.dto.RequestDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RequestMapper {

    @Mapping(target = "created", dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Mapping(target = "event", source = "event.id")
    @Mapping(target = "requester", source = "requester.id")
    RequestDto toDto(Request request);

    List<RequestDto> toDtoList(List<Request> requests);


}

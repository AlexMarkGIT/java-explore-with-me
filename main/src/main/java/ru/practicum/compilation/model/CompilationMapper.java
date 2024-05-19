package ru.practicum.compilation.model;

import org.mapstruct.Mapper;
import ru.practicum.compilation.dto.CompilationDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CompilationMapper {
    CompilationDto toDto(Compilation compilation);
    List<CompilationDto> toDtoList(List<Compilation> compilations);
}

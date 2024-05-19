package ru.practicum.compilation;

import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.CompilationNewDto;
import ru.practicum.compilation.dto.CompilationUpdateDto;

import java.util.List;

public interface CompilationService {
    CompilationDto create(CompilationNewDto dto);

    CompilationDto update(Long compId, CompilationUpdateDto dto);

    CompilationDto get(Long comId);

    List<CompilationDto> getAll(Boolean pinned, Integer from, Integer size);

    void delete(Long compId);

}

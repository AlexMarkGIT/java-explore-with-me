package ru.practicum.compilation;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.CompilationNewDto;
import ru.practicum.compilation.dto.CompilationUpdateDto;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.compilation.model.CompilationMapper;
import ru.practicum.event.EventRepository;
import ru.practicum.event.model.Event;
import ru.practicum.exception.NotFoundException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {

    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;
    private final CompilationMapper compilationMapper;

    @Override
    public CompilationDto create(CompilationNewDto dto) {

        Compilation compilation = new Compilation();

        if (dto.getEvents() != null && !dto.getEvents().isEmpty()) {
            compilation.setEvents(new HashSet<>(eventRepository
                    .findAllById(dto.getEvents())));
        } else {
            compilation.setEvents(new HashSet<>());
        }

        if (dto.getPinned() == null) {
            compilation.setPinned(false);
        } else {
            compilation.setPinned(dto.getPinned());
        }

        compilation.setTitle(dto.getTitle());


        return compilationMapper.toDto(compilationRepository.save(compilation));
    }

    @Override
    public CompilationDto update(Long compId, CompilationUpdateDto dto) {

        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("подборка не найдена"));

        if (dto.getEvents() != null && !dto.getEvents().isEmpty()) {
            compilation.setEvents(new HashSet<>(eventRepository
                    .findAllById(dto.getEvents())));
        }

        if (dto.getPinned() != null) {
            compilation.setPinned(dto.getPinned());
        }

        if (dto.getTitle() != null) {
            compilation.setTitle(dto.getTitle());
        }

        return compilationMapper.toDto(compilationRepository.save(compilation));
    }

    @Override
    public CompilationDto get(Long compId) {

        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("такой подборки не существует"));

        return compilationMapper.toDto(compilation);
    }

    @Override
    public List<CompilationDto> getAll(Boolean pinned, Integer from, Integer size) {

        Pageable pageable = PageRequest.of(from / size, size);

        if (pinned == null) {
            List<Compilation> compilations = compilationRepository.findAll(pageable).toList();
            return compilationMapper.toDtoList(compilations);
        } else {
            List<Compilation> compilations = compilationRepository.findAllByPinned(pinned, pageable).toList();
            return compilationMapper.toDtoList(compilations);
        }

    }


    @Override
    public void delete(Long compId) {
        Compilation compilation = compilationRepository.findById(compId)
                        .orElseThrow(() -> new NotFoundException("такой подборки не существует"));
        compilationRepository.deleteById(compilation.getId());
    }


}

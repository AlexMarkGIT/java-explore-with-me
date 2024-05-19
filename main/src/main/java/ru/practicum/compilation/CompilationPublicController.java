package ru.practicum.compilation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilation.dto.CompilationDto;

import java.util.List;

@RestController
@RequestMapping("/compilations")
@Validated
@RequiredArgsConstructor
@Slf4j
public class CompilationPublicController {

    private final CompilationService compilationService;

    @GetMapping("/{compId}")
    public CompilationDto getCompilation(@PathVariable Long compId) {
        log.info("\n\nполучение подборки\n");
        return compilationService.get(compId);
    }

    @GetMapping
    public List<CompilationDto> getCompilations(@RequestParam(required = false) Boolean pinned,
                                                @RequestParam(required = false, defaultValue = "0") Integer from,
                                                @RequestParam(required = false, defaultValue = "10") Integer size) {
        log.info("\n\nполучение всех подборок\n");
        return compilationService.getAll(pinned, from, size);
    }
}

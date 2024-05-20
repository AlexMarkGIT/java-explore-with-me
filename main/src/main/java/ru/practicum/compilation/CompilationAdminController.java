package ru.practicum.compilation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.CompilationNewDto;
import ru.practicum.compilation.dto.CompilationUpdateDto;

import javax.validation.Valid;

@RestController
@RequestMapping("/admin/compilations")
@Validated
@RequiredArgsConstructor
@Slf4j
public class CompilationAdminController {

    private final CompilationService compilationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto create(@RequestBody @Valid CompilationNewDto dto) {
        log.info("\n\nсоздание подборки\n");
        return compilationService.create(dto);
    }

    @PatchMapping("/{compId}")
    public CompilationDto update(@PathVariable Long compId,
                                 @Valid @RequestBody CompilationUpdateDto dto) {
        log.info("\n\nобновление подборки\n");
        return compilationService.update(compId, dto);
    }

    @DeleteMapping("/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilation(@PathVariable Long compId) {
        log.info("\n\nудаление подборки\n");
        compilationService.delete(compId);
    }

}

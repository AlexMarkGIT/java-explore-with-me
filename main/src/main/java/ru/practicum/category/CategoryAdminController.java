package ru.practicum.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.CategoryNewDto;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/admin/categories")
@Validated
@RequiredArgsConstructor
@Slf4j
public class CategoryAdminController {

    private final CategoryService categoryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto create(@RequestBody @Valid CategoryNewDto categoryNewDto) {
        log.info("\n\nсоздание категории\n");
        return categoryService.create(categoryNewDto);
    }

    @PatchMapping("/{catId}")
    public CategoryDto update(@PathVariable Long catId,
                              @RequestBody @Valid CategoryDto categoryDto) {
        log.info("\n\nобновление категории\n");
        return categoryService.update(catId, categoryDto);
    }

    @DeleteMapping("/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long catId) {
        log.info("\n\nудаление категории\n");
        categoryService.delete(catId);
    }


}

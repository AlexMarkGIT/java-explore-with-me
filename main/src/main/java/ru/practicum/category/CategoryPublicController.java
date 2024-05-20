package ru.practicum.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.category.dto.CategoryDto;

import java.util.List;

@RestController
@RequestMapping(path = "/categories")
@RequiredArgsConstructor
@Slf4j
public class CategoryPublicController {

    private final CategoryService categoryService;

    @GetMapping
    public List<CategoryDto> getAll(@RequestParam(required = false, defaultValue = "0") Integer from,
                                    @RequestParam(required = false, defaultValue = "10") Integer size) {
        log.info("\n\nполучение всех категории\n");
        return categoryService.getAll(from, size);
    }

    @GetMapping(path = "/{catId}")
    public CategoryDto get(@PathVariable Long catId) {
        log.info("\n\nполучение категории\n");
        return categoryService.get(catId);
    }

}

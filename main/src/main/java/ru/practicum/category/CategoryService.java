package ru.practicum.category;

import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.CategoryNewDto;

import java.util.List;

public interface CategoryService {

    CategoryDto create(CategoryNewDto categoryNewDto);
    CategoryDto update(Long catId, CategoryDto categoryDto);
    List<CategoryDto> getAll(Integer from, Integer size);
    CategoryDto get(Long catId);
    void delete(Long catId);
}

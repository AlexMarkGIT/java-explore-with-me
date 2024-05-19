package ru.practicum.category.model;

import org.mapstruct.Mapper;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.CategoryNewDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    Category toEntity(CategoryNewDto categoryNewDto);
    CategoryDto toDto(Category category);
    List<CategoryDto> toDtoList(List<Category> categories);
}

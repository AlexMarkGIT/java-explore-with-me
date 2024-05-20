package ru.practicum.category;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.CategoryNewDto;
import ru.practicum.category.model.Category;
import ru.practicum.category.model.CategoryMapper;
import ru.practicum.event.EventRepository;
import ru.practicum.exception.AlreadyExistException;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final EventRepository eventRepository;

    @Override
    public CategoryDto create(CategoryNewDto categoryNewDto) {
        if (categoryRepository.existsByName(categoryNewDto.getName())) {
            throw new AlreadyExistException("категория с таким именем уже существует");
        }
        Category category = categoryRepository.save(categoryMapper.toEntity(categoryNewDto));
        return categoryMapper.toDto(category);
    }

    @Override
    public CategoryDto update(Long catId, CategoryDto categoryDto) {

        Category category = categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException("категории с таким id не существует"));

        if (!category.getName().equals(categoryDto.getName())) {
            if (!categoryRepository.existsByName(categoryDto.getName())) {
                category.setName(categoryDto.getName());
            } else {
                throw new AlreadyExistException("категория с таким именем уже существует");
            }
        }

        return categoryMapper.toDto(categoryRepository.save(category));
    }

    @Override
    public List<CategoryDto> getAll(Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from / size, size);
        List<Category> categories = categoryRepository.findAll(pageable).toList();
        return categoryMapper.toDtoList(categories);
    }

    @Override
    public CategoryDto get(Long catId) {
        Category category = categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException("такой категории не существует"));
        return categoryMapper.toDto(category);
    }

    @Override
    public void delete(Long catId) {

        if (eventRepository.existsByCategoryId(catId)) {
            throw new ConflictException("не удалено: в базе есть мероприятия с этой категорией");
        }

        categoryRepository.deleteById(catId);
    }
}

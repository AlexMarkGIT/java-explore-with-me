package ru.practicum.statistic;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.dto.HitDto;
import ru.practicum.dto.StatsDto;
import ru.practicum.statistic.model.HitMapper;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {

    private final StatsRepository statsRepository;
    private final HitMapper mapper;

    @Override
    public void saveHit(HitDto hitDto) {
        statsRepository.save(mapper.toEntity(hitDto));
    }

    @Override
    public List<StatsDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {

        if (uris.isEmpty()) {
            if (unique) {
                return statsRepository.findAllUniqueStats(start, end);
            } else {
                return statsRepository.findAllNotUniqueStats(start, end);
            }
        } else {
            if (unique) {
                return statsRepository.findAllUniqueStatsByUris(start, end, uris);
            } else {
                return statsRepository.findAllNotUniqueStatsByUris(start, end, uris);
            }
        }
    }
}

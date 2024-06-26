package ru.practicum.statistic;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.dto.StatsDto;
import ru.practicum.statistic.model.Hit;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsRepository extends JpaRepository<Hit, Long> {

    @Query("select new ru.practicum.dto.StatsDto(h.app, h.uri, count(distinct h.ip)) " +
            "from Hit as h " +
            "where h.timestamp between ?1 and ?2 " +
            "group by h.app, h.uri " +
            "order by count(distinct h.ip) desc")
    List<StatsDto> findAllUniqueStats(LocalDateTime start, LocalDateTime end);

    @Query("select new ru.practicum.dto.StatsDto(h.app, h.uri, count(h)) " +
            "from Hit as h " +
            "where h.timestamp between ?1 and ?2 " +
            "group by h.app, h.uri " +
            "order by count(h) desc")
    List<StatsDto> findAllNotUniqueStats(LocalDateTime start, LocalDateTime end);

    @Query("select new ru.practicum.dto.StatsDto(h.app, h.uri, count(distinct h.ip)) " +
            "from Hit as h " +
            "where h.timestamp between ?1 and ?2 and h.uri in ?3 " +
            "group by h.app, h.uri " +
            "order by count(distinct h.ip) desc")
    List<StatsDto> findAllUniqueStatsByUris(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("select new ru.practicum.dto.StatsDto(h.app, h.uri, count(h)) " +
            "from Hit as h " +
            "where h.timestamp between ?1 and ?2 and h.uri in ?3 " +
            "group by h.app, h.uri " +
            "order by count(h) desc")
    List<StatsDto> findAllNotUniqueStatsByUris(LocalDateTime start, LocalDateTime end, List<String> uris);



}

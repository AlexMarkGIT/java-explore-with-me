package ru.practicum.event;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import ru.practicum.event.enums.EventState;
import ru.practicum.event.model.Event;
import ru.practicum.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    Boolean existsByCategoryId(Long catId);
    Optional<Event> findEventByIdAndInitiator(Long eventId, User user);
    List<Event> findAllByInitiator(User user, Pageable pageable);
    Optional<Event> findByIdAndState(Long eventId, EventState eventState);
    @Query("select e from Event e " +
            "where ((?1 is null) " +
            "or ((lower(e.annotation) like concat('%', lower(?1), '%')) " +
            "or (lower(e.description) like concat('%', lower(?1), '%')))) " +
            "and (?2 is null or e.category.id in ?2) " +
            "and (?3 is null or e.paid = ?3) " +
            "and (?4 is null or e.eventDate > ?4) " +
            "and (?5 is null or e.eventDate < ?5) " +
            "and (?6 = false " +
            "or ((?6 = true " +
            "and e.participantLimit > " +
            "(select count(*) from Request as r where r.event.id = e.id ))) " +
            "or (e.participantLimit > 0 ))")
    List<Event> getAllEventsByParams(String text,
                              List<Long> categories,
                              Boolean paid,
                              LocalDateTime rangeStart,
                              LocalDateTime rangeEnd,
                              Boolean onlyAvailable,
                              Pageable pageable);

    @Query("select e from Event e " +
            "where (?1 is null or e.initiator.id in ?1) " +
            "and (?2 is null or e.state in ?2) " +
            "and (?3 is null or e.category.id in ?3) " +
            "and (?4 is null or e.eventDate > ?4) " +
            "and (?5 is null or e.eventDate < ?5)")
    List<Event> getAllEventsByParamsByAdmin(List<Long> users,
                                            String states,
                                            List<Long> categories,
                                            LocalDateTime rangeStart,
                                            LocalDateTime rangeEnd,
                                            Pageable pageable);


}

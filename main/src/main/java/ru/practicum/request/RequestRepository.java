package ru.practicum.request;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.event.model.Event;
import ru.practicum.request.model.Request;
import ru.practicum.user.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {
    Boolean existsByEventAndRequester(Event event, User requester);
    Optional<Request> findByRequesterIdAndId(Long userId, Long requestId);
    List<Request> findAllByRequesterId(Long userId);
    List<Request> findAllByEvent(Event event);
    List<Request> findByEventIdAndIdIn(Long eventId, List<Long> ids);

}

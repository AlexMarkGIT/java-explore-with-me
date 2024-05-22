package ru.practicum.comment.model;

import lombok.*;
import ru.practicum.comment.enums.CommentStatus;
import ru.practicum.event.model.Event;
import ru.practicum.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "TEXT", length = 2000)
    private String content;
    private LocalDateTime created;
    private LocalDateTime published;
    @Enumerated(EnumType.STRING)
    private CommentStatus status;
    @ManyToOne
    @JoinColumn(name = "commentator_id", referencedColumnName = "id")
    private User commentator;
    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;
}

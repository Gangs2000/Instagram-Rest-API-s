package org.instagram.layers.Models;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;

import javax.persistence.Id;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Component
@Document
public class Comments {
    @Id
    private int _id;
    private int postId;
    private String comment;
    private String commentedBy;
    @JsonFormat(pattern = "dd-MM-yyyy hh:mm a", shape = JsonFormat.Shape.STRING)
    private LocalDateTime commentedAt;
}

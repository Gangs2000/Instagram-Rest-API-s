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
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Component
@Document
public class Posts {
    @Id
    private int _id;
    private String accountId;
    private String postTitle;
    private String postDescription;
    private List<String> postTags;
    private List<String> likes;
    private List<Comments> comments;
    private long shares;
    @JsonFormat(pattern = "dd-MM-yyyy hh:mm a",shape = JsonFormat.Shape.STRING)
    private LocalDateTime uploadedAt;
}

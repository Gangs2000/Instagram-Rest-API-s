package org.instagram.layers.Models;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;

import javax.persistence.Id;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Component
@Document
public class Accounts {
    @Id
    private String _id;
    private String userName;
    private String password;
    private String mobileNumber;
    private String gender;
    private List<String> followers;
    private List<String> following;
    private String accountType;
    private List<String> pendingRequest;
    private List<Integer> postIds;
    @JsonFormat(pattern = "dd-MM-yyyy",shape = JsonFormat.Shape.STRING)
    private LocalDate createdAt;
}

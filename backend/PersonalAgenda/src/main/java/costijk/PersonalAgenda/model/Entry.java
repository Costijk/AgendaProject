package costijk.PersonalAgenda.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Document(collection = "entries")
@CompoundIndex(name = "user_creation_idx", def = "{'userId' : 1, 'createdAt' : -1}")
public class Entry {

    @Id
    private String id;

    private String userId;

    private String title;

    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt;


    private List<Page> pages;
}

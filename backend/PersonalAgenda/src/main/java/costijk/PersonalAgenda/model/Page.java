package costijk.PersonalAgenda.model;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
public class Page {

    private String pageId;

    private int pageNumber;

    @Field("content")
    private String content;
}

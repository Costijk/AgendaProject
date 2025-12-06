package costijk.PersonalAgenda.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageDTO {

    private String pageId;

    private int pageNumber;

    private String content;
}

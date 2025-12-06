package costijk.PersonalAgenda.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EntryCreationDTO {


    private String title;


    private List<PageDTO> pages;
}

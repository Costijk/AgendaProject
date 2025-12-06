package costijk.PersonalAgenda.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EntryResponseDTO {
    private String id;

    private String title;


    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    private List<PageDTO> pages;
}

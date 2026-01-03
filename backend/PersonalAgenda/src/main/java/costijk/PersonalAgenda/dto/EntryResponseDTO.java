package costijk.PersonalAgenda.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class EntryResponseDTO {
    private String id;
    private String title;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String content;
    private List<PageDTO> pages;
}
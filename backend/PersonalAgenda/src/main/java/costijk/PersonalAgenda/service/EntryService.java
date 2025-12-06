package costijk.PersonalAgenda.service;

import costijk.PersonalAgenda.dto.PageDTO;
import costijk.PersonalAgenda.dto.EntryCreationDTO;
import costijk.PersonalAgenda.dto.EntryResponseDTO;
import costijk.PersonalAgenda.model.Entry;
import costijk.PersonalAgenda.repository.EntryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EntryService {

    private final EntryRepository entryRepository;

    public EntryResponseDTO createEntry(EntryCreationDTO creationDTO, String currentUserId) {

        Entry entry = convertToEntity(creationDTO,  currentUserId);
        Entry savedEntry = entryRepository.save(entry);
        return convertToResponseDTO(savedEntry);
    }

    private Entry convertToEntity(EntryCreationDTO creationDTO, String currentUserId) {
        Entry entry = new Entry();

        entry.setUserId(currentUserId);
        entry.setTitle(creationDTO.getTitle());
        entry.setCreatedAt(LocalDateTime.now());
        entry.setUpdatedAt(LocalDateTime.now());

        entry.setPages(creationDTO.getPages().stream().map(pageDto -> {
            costijk.PersonalAgenda.model.Page page = new costijk.PersonalAgenda.model.Page();

            page.setPageNumber(pageDto.getPageNumber());
            page.setContent(pageDto.getContent());
            return page;
        }).collect(Collectors.toList()));

        return entry;
    }

        private EntryResponseDTO convertToResponseDTO (Entry entry){
            EntryResponseDTO response = new EntryResponseDTO();
            response.setId(entry.getId());
            response.setTitle(entry.getTitle());
            response.setCreatedAt(entry.getCreatedAt());
            response.setUpdatedAt(entry.getUpdatedAt());


            response.setPages(entry.getPages().stream()
                    .map(page -> new costijk.PersonalAgenda.dto.PageDTO(
                            page.getPageId(),
                            page.getPageNumber(),
                            page.getContent()))
                    .collect(Collectors.toList()));

            return response;
        }
}


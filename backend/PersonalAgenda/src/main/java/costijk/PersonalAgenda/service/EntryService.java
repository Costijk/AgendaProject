package costijk.PersonalAgenda.service;

import costijk.PersonalAgenda.dto.PageDTO;
import costijk.PersonalAgenda.dto.EntryCreationDTO;
import costijk.PersonalAgenda.dto.EntryResponseDTO;
import costijk.PersonalAgenda.dto.NoteListDTO;
import costijk.PersonalAgenda.model.Entry;
import costijk.PersonalAgenda.model.User;
import costijk.PersonalAgenda.repository.EntryRepository;
import costijk.PersonalAgenda.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EntryService {

    private final EntryRepository entryRepository;
    private final UserRepository userRepository;


    private String getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("Utilizator neautentificat!");
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof costijk.PersonalAgenda.model.User) {
            return ((costijk.PersonalAgenda.model.User) principal).getId();
        }

        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilizatorul nu a fost găsit"))
                .getId();
    }


    public EntryResponseDTO createEntry(EntryCreationDTO creationDTO) {
        String userId = getCurrentUserId();
        Entry entry = convertToEntity(creationDTO, userId);
        Entry savedEntry = entryRepository.save(entry);
        return convertToResponseDTO(savedEntry);
    }

    public org.springframework.data.domain.Page<NoteListDTO> getMyEntriesPaginated(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        org.springframework.data.domain.Page<Entry> entriesPage = entryRepository.findByUserId(getCurrentUserId(), pageable);

        return entriesPage.map(entry -> {
            NoteListDTO dto = new NoteListDTO();
            dto.setId(entry.getId());
            dto.setTitle(entry.getTitle());
            dto.setCreatedAt(entry.getCreatedAt());
            return dto;
        });
    }

    private Entry convertToEntity(EntryCreationDTO creationDTO, String currentUserId) {
        Entry entry = new Entry();
        entry.setUserId(currentUserId);
        entry.setTitle(creationDTO.getTitle());
        entry.setCreatedAt(LocalDateTime.now());

        if (creationDTO.getPages() != null) {
            entry.setPages(creationDTO.getPages().stream().map(pageDto -> {
                costijk.PersonalAgenda.model.Page page = new costijk.PersonalAgenda.model.Page();
                page.setPageNumber(pageDto.getPageNumber());
                page.setContent(pageDto.getContent());
                return page;
            }).collect(Collectors.toList()));
        }
        return entry;
    }

    private EntryResponseDTO convertToResponseDTO(Entry entry) {
        EntryResponseDTO response = new EntryResponseDTO();
        response.setId(entry.getId());
        response.setTitle(entry.getTitle());
        response.setCreatedAt(entry.getCreatedAt());

        if (entry.getPages() != null) {
            response.setPages(entry.getPages().stream()
                    .map(page -> new costijk.PersonalAgenda.dto.PageDTO(
                            page.getPageId(),
                            page.getPageNumber(),
                            page.getContent()))
                    .collect(Collectors.toList()));
        }
        return response;
    }
}
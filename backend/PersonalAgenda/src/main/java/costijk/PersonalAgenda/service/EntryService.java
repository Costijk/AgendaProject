package costijk.PersonalAgenda.service;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import costijk.PersonalAgenda.dto.EntryCreationDTO;
import costijk.PersonalAgenda.dto.EntryResponseDTO;
import costijk.PersonalAgenda.dto.NoteListDTO;
import costijk.PersonalAgenda.model.Entry;
import costijk.PersonalAgenda.repository.EntryRepository;
import costijk.PersonalAgenda.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EntryService {

    private final EntryRepository entryRepository;
    private final UserRepository userRepository;


    private String getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("User not authenticated!");
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof costijk.PersonalAgenda.model.User) {
            return ((costijk.PersonalAgenda.model.User) principal).getId();
        }

        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"))
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
            dto.setUpdatedAt(entry.getUpdatedAt());
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

    private EntryResponseDTO mapToResponseDTO(Entry entry) {
        EntryResponseDTO dto = new EntryResponseDTO();
        dto.setId(entry.getId());
        dto.setTitle(entry.getTitle());

        if (entry.getPages() != null && !entry.getPages().isEmpty()) {
            dto.setContent(entry.getPages().get(0).getContent());
        }
        return dto;
    }

    private EntryResponseDTO convertToResponseDTO(Entry entry) {
        EntryResponseDTO response = new EntryResponseDTO();
        response.setId(entry.getId());
        response.setTitle(entry.getTitle());
        response.setCreatedAt(entry.getCreatedAt());
        response.setUpdatedAt(entry.getUpdatedAt());


        if (entry.getPages() != null && !entry.getPages().isEmpty()) {
            String combinedContent = entry.getPages().stream()
                    .map(page -> page.getContent())
                    .collect(Collectors.joining("\n---\n"));
            response.setContent(combinedContent);
            
            response.setPages(entry.getPages().stream().map(page -> {
                costijk.PersonalAgenda.dto.PageDTO pageDTO = new costijk.PersonalAgenda.dto.PageDTO();
                pageDTO.setPageNumber(page.getPageNumber());
                pageDTO.setContent(page.getContent());
                return pageDTO;
            }).collect(Collectors.toList()));
        }
        return response;
    }

    public EntryResponseDTO getEntryById(String id) {
        Entry entry = entryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Entry not found"));


        return convertToResponseDTO(entry);
    }

    public EntryResponseDTO updateEntry(String id, EntryCreationDTO updateDTO) {
        Entry entry = entryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Entry not found"));

        String currentUserId = getCurrentUserId();
        if (!entry.getUserId().equals(currentUserId)) {
            throw new RuntimeException("You don't have permission to modify this entry");
        }

        entry.setTitle(updateDTO.getTitle());
        entry.setUpdatedAt(LocalDateTime.now());
        
        if (updateDTO.getPages() != null) {
            entry.setPages(updateDTO.getPages().stream().map(pageDto -> {
                costijk.PersonalAgenda.model.Page page = new costijk.PersonalAgenda.model.Page();
                page.setPageNumber(pageDto.getPageNumber());
                page.setContent(pageDto.getContent());
                return page;
            }).collect(Collectors.toList()));
        }

        Entry updatedEntry = entryRepository.save(entry);
        return convertToResponseDTO(updatedEntry);
    }

    public void deleteEntry(String id) {
        Entry entry = entryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Entry not found"));

        String currentUserId = getCurrentUserId();
        if (!entry.getUserId().equals(currentUserId)) {
            throw new RuntimeException("You don't have permission to delete this entry");
        }

        entryRepository.delete(entry);
    }
}
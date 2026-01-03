package costijk.PersonalAgenda.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import costijk.PersonalAgenda.dto.EntryCreationDTO;
import costijk.PersonalAgenda.dto.EntryResponseDTO;
import costijk.PersonalAgenda.dto.NoteListDTO;
import costijk.PersonalAgenda.service.EntryService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/entries")
@RequiredArgsConstructor
public class EntryController {

    private final EntryService entryService;

    @PostMapping
    public ResponseEntity<EntryResponseDTO> createEntry(@RequestBody EntryCreationDTO dto) {
        return new ResponseEntity<>(entryService.createEntry(dto), HttpStatus.CREATED);
    }

    @GetMapping("/list")
    public ResponseEntity<Page<NoteListDTO>> getEntriesList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(entryService.getMyEntriesPaginated(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntryResponseDTO> getEntryById(@PathVariable String id) {

        return ResponseEntity.ok(entryService.getEntryById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntryResponseDTO> updateEntry(@PathVariable String id, @RequestBody EntryCreationDTO dto) {
        return ResponseEntity.ok(entryService.updateEntry(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEntry(@PathVariable String id) {
        entryService.deleteEntry(id);
        return ResponseEntity.noContent().build();
    }
}

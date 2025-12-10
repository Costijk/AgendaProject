package costijk.PersonalAgenda.controller;

import costijk.PersonalAgenda.dto.EntryCreationDTO;
import costijk.PersonalAgenda.dto.EntryResponseDTO;
import costijk.PersonalAgenda.service.EntryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/entries")
@RequiredArgsConstructor
public class EntryController {

    private final EntryService entryService;

    @PostMapping
    public ResponseEntity<EntryResponseDTO> createEntry(
            @RequestBody EntryCreationDTO creationDTO,
            @RequestHeader(name = "X-User-Id", required = false) String testUserId
    ) {


        String userId = (testUserId != null && !testUserId.isEmpty()) ? testUserId : "default_test_user_id";
        EntryResponseDTO response = entryService.createEntry(creationDTO, userId);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}

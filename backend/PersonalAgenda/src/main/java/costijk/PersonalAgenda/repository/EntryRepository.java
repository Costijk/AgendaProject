package costijk.PersonalAgenda.repository;

import costijk.PersonalAgenda.model.Entry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface EntryRepository extends MongoRepository<Entry, String> {

    Optional<Entry> findByIdAndUserId(String id, String userId);

    Page<Entry> findByUserId(String userId, Pageable pageable);
}
package dev.bomboclat.service;

import dev.bomboclat.model.SymptomEntry;
import dev.bomboclat.model.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class SymptomService {

    @Transactional
    public SymptomEntry createEntry(User user, SymptomEntry entry) {
        entry.user = user;
        entry.persist();
        return entry;
    }

    public List<SymptomEntry> listEntries(User user) {
        return SymptomEntry.list("user", user);
    }

    @Transactional
    public SymptomEntry updateEntry(UUID id, SymptomEntry updated, User user) {
        SymptomEntry existing = SymptomEntry.findById(id);
        if (existing == null || !existing.user.equals(user)) {
            return null;
        }
        existing.timestamp = updated.timestamp;
        existing.painLevel = updated.painLevel;
        existing.stiffnessLevel = updated.stiffnessLevel;
        existing.notes = updated.notes;
        return existing;
    }

    @Transactional
    public boolean softDelete(UUID id, User user) {
        SymptomEntry entry = SymptomEntry.findById(id);
        if (entry == null || !entry.user.equals(user)) {
            return false;
        }
        entry.deletedAt = LocalDateTime.now();
        return true;
    }
}

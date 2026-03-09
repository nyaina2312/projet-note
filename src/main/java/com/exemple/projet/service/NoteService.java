package com.exemple.projet.service;

import java.math.BigDecimal;
import java.util.List;
import com.exemple.projet.model.Note;

public interface NoteService {
    BigDecimal calculerNoteFinale(Integer idCandidat, Integer idMatiere);
    Note saveNote(Note note);
    List<Note> findAllNotes();
}

import java.util.*;
import java.util.Map.Entry;

public class EventResolver {

    public static Event resolveEvent(Event localEvent, Event remoteEvent) {

        String resolvedEventName = combineLocalAndRemoteEventName(localEvent, remoteEvent);

        // Make a map from the Local Event Note ID to the Note itself
        Map<Integer, Note> localEventNotes = new HashMap<>();

        for (Note note : localEvent.notes()) {
            getIdsInEventNotes(localEventNotes, note);
        }

        // Make a map from the Remote Event ID of the note to the note itself
        Map<Integer, Note> remoteEventNotes = new HashMap<>();

        for (Note note : remoteEvent.notes()) {
            getIdsInEventNotes(remoteEventNotes, note);
        }

        // Create new List for Resolved Event Notes
        List<Note> resolvedEventNotes = new LinkedList<>();

        for (Entry<Integer, Note> localEventNoteEntry : localEventNotes.entrySet()) {

            Note localEventNote = localEventNoteEntry.getValue();
            Note resolvedEventNote = new Note(localEventNote.getId(), localEventNote.getTimestamp(),
                    localEventNote.getText());
            Note conflictingRemoteEventNote = remoteEventNotes.get(localEventNoteEntry.getKey());

            ifConflictingIdInEventNotes(localEventNote, resolvedEventNote, conflictingRemoteEventNote);

            resolvedEventNotes.add(resolvedEventNote);
        }

        // Establish which are new Remote Event Notes
        Set<Integer> newRemoteEventNoteIds = remoteEventNotes.keySet();
        newRemoteEventNoteIds.removeAll(localEventNotes.keySet());

        // Non-conflicting Notes added to Resolved Event Notes
        for (Integer noteId : newRemoteEventNoteIds) {

            // Copy instead of re-using object to avoid unpredictable/wrong results
            Note newRemoteEventNote = remoteEventNotes.get(noteId);
            Note newRemoteEventNoteCopy = new Note(newRemoteEventNote.getId(), newRemoteEventNote.getTimestamp(),
                    newRemoteEventNote.getText());
            resolvedEventNotes.add(newRemoteEventNoteCopy);
        }

        return new Event(resolvedEventName, resolvedEventNotes.toArray(Note[]::new));
    }

    private static String combineLocalAndRemoteEventName(Event localEvent, Event remoteEvent) {

        String resolvedEventName = localEvent.name();

        // Combine Name from Local Event and Remote Event to create Name for Resolved Event
        if (!localEvent.name().equals(remoteEvent.name())) {
            resolvedEventName += " / " + remoteEvent.name();
        }
        return resolvedEventName;
    }

    private static void getIdsInEventNotes(Map<Integer, Note> localEventNotes, Note note) {
        localEventNotes.put(note.getId(), note);
    }

    private static void ifConflictingIdInEventNotes(Note localEventNote, Note resolvedEventNote,
                                                    Note conflictingRemoteEventNote) {

        // If conflicting Event Note ID exists...
        if (conflictingRemoteEventNote != null) {

            // Replace Local Event Timestamp with conflicting Remote Event Timestamp
            resolvedEventNote.setTimestamp(conflictingRemoteEventNote.getTimestamp());
            resolvedEventNote.setText(localEventNote.getText() + " / " + conflictingRemoteEventNote.getText());
        }
    }
}

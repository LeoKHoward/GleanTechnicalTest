import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EventResolverTest {

    private final String EXPECTED_LOCAL_EVENT_NAME = "Name 1";
    private final String EXPECTED_REMOTE_EVENT_NAME = "Name 2";
    private final String EXPECTED_RESOLVED_EVENT_NAME = "Name 1 / Name 2";
    private final String EXPECTED_RESOLVED_EVENT_TEXT = "A / B";

    @Test
    public void testNoConflictingIdsOneNoteInEachEvent() {

        // Happy path with no conflicts in Local or Remote Event
        // Two Notes (one from Local Event and one from Remote Event) added to Resolved Event

        // GIVEN
        Note[] localNotes = {new Note(1, 3200, "A")};
        Event localEvent = new Event(EXPECTED_LOCAL_EVENT_NAME, localNotes);

        Note[] remoteNotes = {new Note(2, 5000, "B")};
        Event remoteEvent = new Event(EXPECTED_REMOTE_EVENT_NAME, remoteNotes);

        // WHEN
        Event resolvedEvent = EventResolver.resolveEvent(localEvent, remoteEvent);

        // THEN
        assertEquals(EXPECTED_RESOLVED_EVENT_NAME, resolvedEvent.name());
        assertEquals(2, resolvedEvent.notes().length);
        assertEquals(1, resolvedEvent.notes()[0].getId());
        assertEquals(2, resolvedEvent.notes()[1].getId());
        assertEquals(3200, resolvedEvent.notes()[0].getTimestamp());
        assertEquals(5000, resolvedEvent.notes()[1].getTimestamp());
        assertEquals("A", resolvedEvent.notes()[0].getText());
        assertEquals("B", resolvedEvent.notes()[1].getText());
    }

    @Test
    public void testNoConflictingIdsTwoNotesInLocalEventOneNoteInRemoteEvent() {

        // Happy path with no conflicts in Local or Remote Event
        // Three Notes (two from Local Event and one from Remote Event) added to Resolved Event

        // GIVEN
        Note[] localNotes = {new Note(1, 3200, "A"),
                new Note(2, 5000, "B")};
        Event localEvent = new Event(EXPECTED_LOCAL_EVENT_NAME, localNotes);

        Note[] remoteNotes = {new Note(3, 7000, "C")};
        Event remoteEvent = new Event(EXPECTED_REMOTE_EVENT_NAME, remoteNotes);

        // WHEN
        Event resolvedEvent = EventResolver.resolveEvent(localEvent, remoteEvent);

        // THEN
        assertEquals(EXPECTED_RESOLVED_EVENT_NAME, resolvedEvent.name());
        assertEquals(3, resolvedEvent.notes().length);
        assertEquals(1, resolvedEvent.notes()[0].getId());
        assertEquals(2, resolvedEvent.notes()[1].getId());
        assertEquals(3, resolvedEvent.notes()[2].getId());
        assertEquals(3200, resolvedEvent.notes()[0].getTimestamp());
        assertEquals(5000, resolvedEvent.notes()[1].getTimestamp());
        assertEquals(7000, resolvedEvent.notes()[2].getTimestamp());
        assertEquals("A", resolvedEvent.notes()[0].getText());
        assertEquals("B", resolvedEvent.notes()[1].getText());
        assertEquals("C", resolvedEvent.notes()[2].getText());
    }

    @Test
    public void testNoConflictingIdsOneNoteInLocalEventTwoNotesInRemoteEvent() {

        // Happy path with no conflicts in Local or Remote Event
        // Three Notes (one from Local Event and two from Remote Event) added to Resolved Event

        // GIVEN
        Note[] localNotes = {new Note(1, 3200, "A")};
        Event localEvent = new Event(EXPECTED_LOCAL_EVENT_NAME, localNotes);

        Note[] remoteNotes = {new Note(2, 5000, "B"),
                new Note(3, 7000, "C")};
        Event remoteEvent = new Event(EXPECTED_REMOTE_EVENT_NAME, remoteNotes);

        // WHEN
        Event resolvedEvent = EventResolver.resolveEvent(localEvent, remoteEvent);

        // THEN
        assertEquals(EXPECTED_RESOLVED_EVENT_NAME, resolvedEvent.name());
        assertEquals(3, resolvedEvent.notes().length);
        assertEquals(1, resolvedEvent.notes()[0].getId());
        assertEquals(2, resolvedEvent.notes()[1].getId());
        assertEquals(3, resolvedEvent.notes()[2].getId());
        assertEquals(3200, resolvedEvent.notes()[0].getTimestamp());
        assertEquals(5000, resolvedEvent.notes()[1].getTimestamp());
        assertEquals(7000, resolvedEvent.notes()[2].getTimestamp());
        assertEquals("A", resolvedEvent.notes()[0].getText());
        assertEquals("B", resolvedEvent.notes()[1].getText());
        assertEquals("C", resolvedEvent.notes()[2].getText());
    }

    @Test
    public void testNoConflictingIdsAndTwoNotesInEachEvent() {

        // Happy path with no conflicts in Local or Remote Event
        // Four Notes (two from Local Event and two from Remote Event) added to Resolved Event

        // GIVEN
        Note[] localNotes = {new Note(1, 3200, "A"),
                new Note(2, 5000, "B")};
        Event localEvent = new Event(EXPECTED_LOCAL_EVENT_NAME, localNotes);

        Note[] remoteNotes = {new Note(3, 7000, "C"),
                new Note(4, 9000, "D")};
        Event remoteEvent = new Event(EXPECTED_REMOTE_EVENT_NAME, remoteNotes);

        // WHEN
        Event resolvedEvent = EventResolver.resolveEvent(localEvent, remoteEvent);

        // THEN
        assertEquals(EXPECTED_RESOLVED_EVENT_NAME, resolvedEvent.name());
        assertEquals(4, resolvedEvent.notes().length);
        assertEquals(1, resolvedEvent.notes()[0].getId());
        assertEquals(2, resolvedEvent.notes()[1].getId());
        assertEquals(3, resolvedEvent.notes()[2].getId());
        assertEquals(4, resolvedEvent.notes()[3].getId());
        assertEquals(3200, resolvedEvent.notes()[0].getTimestamp());
        assertEquals(5000, resolvedEvent.notes()[1].getTimestamp());
        assertEquals(7000, resolvedEvent.notes()[2].getTimestamp());
        assertEquals(9000, resolvedEvent.notes()[3].getTimestamp());
        assertEquals("A", resolvedEvent.notes()[0].getText());
        assertEquals("B", resolvedEvent.notes()[1].getText());
        assertEquals("C", resolvedEvent.notes()[2].getText());
        assertEquals("D", resolvedEvent.notes()[3].getText());
    }

    @Test
    public void testLocalAndRemoteEventNameTheSameOutputsCorrectValues() {

        // Conflicting Name in Local and Remote Event
        // Resolved Event Name does not combine anything

        // GIVEN
        Note[] localNotes = {new Note(1, 3200, "A")};
        Event localEvent = new Event(EXPECTED_LOCAL_EVENT_NAME, localNotes);

        Note[] remoteNotes = {new Note(2, 5000, "B")};
        Event remoteEvent = new Event(EXPECTED_LOCAL_EVENT_NAME, remoteNotes);

        // WHEN
        Event resolvedEvent = EventResolver.resolveEvent(localEvent, remoteEvent);

        // THEN
        assertEquals("Name 1", resolvedEvent.name());
        assertEquals(2, resolvedEvent.notes().length);
        assertEquals(1, resolvedEvent.notes()[0].getId());
        assertEquals(2, resolvedEvent.notes()[1].getId());
        assertEquals(3200, resolvedEvent.notes()[0].getTimestamp());
        assertEquals(5000, resolvedEvent.notes()[1].getTimestamp());
        assertEquals("A", resolvedEvent.notes()[0].getText());
        assertEquals("B", resolvedEvent.notes()[1].getText());
    }

    @Test
    public void testOneConflictingIdOutputsCorrectValues() {

        // Conflicting ID in Local and Remote Event
        // Local and Remote Event Names combined to make Resolved Event Name
        // One Note value in Resolved Event with ID, Remote Event Timestamp and combined Text

        // GIVEN
        Note[] localNotes = {new Note(1, 3200, "A")};
        Event localEvent = new Event(EXPECTED_LOCAL_EVENT_NAME, localNotes);

        Note[] remoteNotes = {new Note(1, 4321, "B")};
        Event remoteEvent = new Event(EXPECTED_REMOTE_EVENT_NAME, remoteNotes);

        // WHEN
        Event resolvedEvent = EventResolver.resolveEvent(localEvent, remoteEvent);

        // THEN
        assertEquals(EXPECTED_RESOLVED_EVENT_NAME, resolvedEvent.name());
        assertEquals(1, resolvedEvent.notes().length);
        assertEquals(1, resolvedEvent.notes()[0].getId());
        assertEquals(4321, resolvedEvent.notes()[0].getTimestamp());
        assertEquals(EXPECTED_RESOLVED_EVENT_TEXT, resolvedEvent.notes()[0].getText());
    }

    @Test
    public void testOneConflictingIdAndOneNonConflictingOutputsCorrectValues() {

        // Conflicting ID in Local and Remote Event and non-conflicting ID
        // Local and Remote Event Names combined to make Resolved Event Name
        // Two Note values in Resolved Event with ID, Remote Event Timestamp and combined Text
        // Non-conflicting Local Event Note added to Resolved Event

        // GIVEN
        Note[] localNotes = {new Note(1, 3200, "A"),
                new Note(2, 5000, "C")};
        Event localEvent = new Event(EXPECTED_LOCAL_EVENT_NAME, localNotes);

        Note[] remoteNotes = {new Note(1, 7000, "B")};
        Event remoteEvent = new Event(EXPECTED_REMOTE_EVENT_NAME, remoteNotes);

        // WHEN
        Event resolvedEvent = EventResolver.resolveEvent(localEvent, remoteEvent);

        // THEN
        assertEquals(EXPECTED_RESOLVED_EVENT_NAME, resolvedEvent.name());
        assertEquals(2, resolvedEvent.notes().length);
        assertEquals(1, resolvedEvent.notes()[0].getId());
        assertEquals(2, resolvedEvent.notes()[1].getId());
        assertEquals(7000, resolvedEvent.notes()[0].getTimestamp());
        assertEquals(5000, resolvedEvent.notes()[1].getTimestamp());
        assertEquals(EXPECTED_RESOLVED_EVENT_TEXT, resolvedEvent.notes()[0].getText());
        assertEquals("C", resolvedEvent.notes()[1].getText());
    }

    @Test
    public void testOneConflictingIdAndTwoNonConflictingOutputsCorrectValues() {

        // Conflicting ID in Local and Remote Event and two non-conflicting IDs
        // Local and Remote Event Names combined to make Resolved Event Name
        // Three Note values in Resolved Event with ID, Remote Event Timestamp and combined Text
        // Non-conflicting Notes from both Events added to Resolved Event as well

        // GIVEN
        Note[] localNotes = {new Note(1, 3200, "A"),
                new Note(2, 5600, "C")};
        Event localEvent = new Event(EXPECTED_LOCAL_EVENT_NAME, localNotes);

        Note[] remoteNotes = {new Note(1, 4321, "B"),
                new Note(3, 7000, "D")};
        Event remoteEvent = new Event(EXPECTED_REMOTE_EVENT_NAME, remoteNotes);

        // WHEN
        Event resolvedEvent = EventResolver.resolveEvent(localEvent, remoteEvent);

        // THEN
        assertEquals(EXPECTED_RESOLVED_EVENT_NAME, resolvedEvent.name());
        assertEquals(3, resolvedEvent.notes().length);
        assertEquals(1, resolvedEvent.notes()[0].getId());
        assertEquals(2, resolvedEvent.notes()[1].getId());
        assertEquals(3, resolvedEvent.notes()[2].getId());
        assertEquals(4321, resolvedEvent.notes()[0].getTimestamp());
        assertEquals(5600, resolvedEvent.notes()[1].getTimestamp());
        assertEquals(7000, resolvedEvent.notes()[2].getTimestamp());
        assertEquals(EXPECTED_RESOLVED_EVENT_TEXT, resolvedEvent.notes()[0].getText());
        assertEquals("C", resolvedEvent.notes()[1].getText());
        assertEquals("D", resolvedEvent.notes()[2].getText());
    }

    @Test
    public void testTwoConflictingIdsOutputsCorrectValues() {

        // Two pairs of conflicting IDs in Local and Remote Event
        // Local and Remote Event Names combined to make Resolved Event Name
        // Two Note values in Resolved Event with ID, Remote Event Timestamp and combined Text

        // GIVEN
        Note[] localNotes = {new Note(1, 3200, "A"),
                new Note(2, 5600, "C")};
        Event localEvent = new Event(EXPECTED_LOCAL_EVENT_NAME, localNotes);

        Note[] remoteNotes = {new Note(1, 4321, "B"),
                new Note(2, 7000, "D")};
        Event remoteEvent = new Event(EXPECTED_REMOTE_EVENT_NAME, remoteNotes);

        // WHEN
        Event resolvedEvent = EventResolver.resolveEvent(localEvent, remoteEvent);

        // THEN
        assertEquals(EXPECTED_RESOLVED_EVENT_NAME, resolvedEvent.name());
        assertEquals(2, resolvedEvent.notes().length);
        assertEquals(1, resolvedEvent.notes()[0].getId());
        assertEquals(2, resolvedEvent.notes()[1].getId());
        assertEquals(4321, resolvedEvent.notes()[0].getTimestamp());
        assertEquals(7000, resolvedEvent.notes()[1].getTimestamp());
        assertEquals(EXPECTED_RESOLVED_EVENT_TEXT, resolvedEvent.notes()[0].getText());
        assertEquals("C / D", resolvedEvent.notes()[1].getText());
    }

}
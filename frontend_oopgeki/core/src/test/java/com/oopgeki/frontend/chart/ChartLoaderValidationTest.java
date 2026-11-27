package com.oopgeki.frontend.chart;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ChartLoaderValidationTest {
    @Test
    void validatesMinimalChart() {
        String json = validJson();
        ChartLoader.parse(json, "test");
    }

    @Test
    void rejectsMissingNoteLane(){
        String json = validJson().replace("\"notes\": []", "\"notes\": [ { \"beat\": 4.0 } ]");
        assertFails(json, "lane");
    }

    @Test
    void rejectsNegativeNoteBeat(){
        String json = validJson().replace("\"notes\": []", "\"notes\": [ { \"beat\": -1.0, \"lane\": \"LEFT\" } ]");
        assertFails(json, "beat must be finite");
    }

    @Test
    void rejectsDuplicateNote(){
        String json = validJson().replace("\"notes\": []", "\"notes\": [ { \"beat\": 4.0, \"lane\": \"LEFT\" }, { \"beat\": 4.0, \"lane\": \"LEFT\" } ]");
        assertFails(json, "duplicate note");
    }

    @Test
    void rejectsNullNote(){
        String json = validJson().replace("\"notes\": []", "\"notes\": [ null ]");
        assertFails(json, "notes[0] is null");
    }

    @Test
    void rejectsNegativeBulletBeat(){
        String json = validJson().replace("\"bullets\": []", "\"bullets\": [ { \"beat\": -1.0, \"x\": 240 } ]");
        assertFails(json, "beat must be finite");
    }

    @Test
    void rejectsZeroBulletSpeed(){
        String json = validJson().replace("\"bullets\": []", "\"bullets\": [ { \"beat\": 8.0, \"x\": 240, \"speed\": 0 } ]");
        assertFails(json, "speed");
    }

    @Test
    void rejectsNegativeBulletDamage(){
        String json = validJson().replace("\"bullets\": []", "\"bullets\": [ { \"beat\": 8.0, \"x\": 240, \"damage\": -1 } ]");
        assertFails(json, "damage");
    }

    @Test
    void rejectsNullBullet(){
        String json = validJson().replace("\"bullets\": []", "\"bullets\": [ null ]");
        assertFails(json, "bullets[0] is null");
    }

    @Test
    void acceptsBulletWithoutOptionalFields(){
        String json = validJson().replace("\"bullets\": []", "\"bullets\": [ { \"beat\": 8.0, \"x\": 240 } ]");
        ChartLoader.parse(json, "test");
    }

    @Test
    void rejectsZeroBPM(){
        String json = validJson().replace("\"bpm\": 120", "\"bpm\": 0");
        assertFails(json, "BPM");
    }

    @Test
    void rejectsUnsupportedFormatVersion(){
        String json = validJson().replace("\"formatVersion\": 1", "\"formatVersion\": 2");
        assertFails(json, "formatVersion");
    }

    @Test
    void rejectsBlankTitle(){
        String json = validJson().replace("\"title\": \"Test Chart\"", "\"title\": \"\"");
        assertFails(json, "title");
    }

    @Test
    void rejectsBlankAudio(){
        String json = validJson().replace("\"audio\": \"audio/test.ogg\"", "\"audio\": \"\"");
        assertFails(json, "audio");
    }

    @Test
    void rejectsNegativeOffset(){
        String json = validJson().replace("\"offset\": 0.5", "\"offset\": -1");
        assertFails(json, "offset");
    }

    @Test
    void rejectsZeroNoteSpeed(){
        String json = validJson().replace("\"noteSpeed\": 520", "\"noteSpeed\": 0");
        assertFails(json, "note speed");
    }

    @Test
    void rejectsZeroDefaultBulletSpeed(){
        String json = validJson().replace("\"bulletSpeed\": 220", "\"bulletSpeed\": 0");
        assertFails(json, "bullet speed");
    }

    @Test
    void rejectsNegativeDefaultBulletDamage(){
        String json = validJson().replace("\"bulletDamage\": 10", "\"bulletDamage\": -1");
        assertFails(json, "bullet damage");
    }

    @Test
    void rejectsNullNotesArray(){
        String json = validJson().replace("\"notes\": []", "\"notes\": null");
        assertFails(json, "notes do not exist");
    }

    @Test
    void rejectsNullBulletsArray(){
        String json = validJson().replace("\"bullets\": []", "\"bullets\": null");
        assertFails(json, "bullets do not exist");
    }

    @Test
    void acceptsZeroOffset(){
        String json = validJson().replace("\"offset\": 0.5", "\"offset\": 0");
        ChartLoader.parse(json, "test");
    }

    @Test
    void collectsMultipleProblems(){
        String json = validJson()
                .replace("\"title\": \"Test Chart\"", "\"title\": \"\"")
                .replace("\"bpm\": 120", "\"bpm\": 0");

        ChartException exception = assertFails(json, "BPM");
        assertTrue(exception.getMessage().contains("title"), exception.getMessage());
        assertTrue(exception.getProblems().size >= 2, exception.getMessage());
    }

    private static String validJson() {
        return """
        {
          "formatVersion": 1,
          "title": "Test Chart",
          "audio": "audio/test.ogg",
          "bpm": 120,
          "offset": 0.5,
          "noteSpeed": 520,
          "bulletSpeed": 220,
          "bulletDamage": 10,
          "notes": [],
          "bullets": []
        }
        """;
    }
    private static ChartException assertFails(String json, String expectedMessagePart) {
        ChartException exception = assertThrows(
            ChartException.class,
            () -> ChartLoader.parse(json, "test")
        );

        assertTrue(
            exception.getMessage().contains(expectedMessagePart),
            exception.getMessage()
        );

        return exception;
    }
}

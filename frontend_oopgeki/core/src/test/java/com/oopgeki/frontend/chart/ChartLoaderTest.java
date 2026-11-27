package com.oopgeki.frontend.chart;

import org.junit.jupiter.api.Test;

import static com.oopgeki.frontend.chart.Lane.LEFT;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ChartLoaderTest {

    @Test
    void parsesMinimalChart() {
        String json = """
            {
              "formatVersion": 1,
              "title": "Test Chart",
              "audio": "audio/test.ogg",
              "bpm": 120,
              "offset": 0.5,
              "notes": [
                { "beat": 4.0, "lane": "LEFT" }
              ],
              "bullets": [
                { "beat": 8.0, "x": 240 }
              ]
            }
            """;

        Chart chart = ChartLoader.parse(json, "test");

        assertEquals("Test Chart", chart.title);
        assertEquals(120f, chart.bpm, 0.0001f);
        assertEquals(0.5f, chart.offset, 0.0001f);
        assertEquals(1, chart.notes.size);
        assertEquals(4.0f, chart.notes.first().beat, 0.0001f);
        assertEquals(LEFT, chart.notes.first().lane);
        assertEquals(1, chart.bullets.size);
        assertEquals(8.0f, chart.bullets.get(0).beat, 0.0001f);
        assertEquals(240f, chart.bullets.get(0).x, 0.0001f);
        assertEquals(220f, chart.bullets.get(0).speed, 0.0001f);
        assertEquals(10, chart.bullets.get(0).damage.intValue());
        assertEquals(1, chart.formatVersion);
        assertEquals("Normal", chart.difficulty);
        assertEquals("Unknown", chart.artist);
        assertEquals("audio/test.ogg", chart.audio);
        assertEquals(520f, chart.noteSpeed, 0.0001f);
        assertEquals(220f, chart.bulletSpeed,  0.0001f);
        assertEquals(10, chart.bulletDamage);
    }

    @Test
    void sortsNotesAndBulletsByBeat(){
        String json = """
            {
              "formatVersion": 1,
              "title": "Sorted",
              "audio": "audio/test.ogg",
              "bpm": 120,
              "notes": [
                { "beat": 8.0, "lane": "LEFT" },
                { "beat": 2.0, "lane": "RIGHT" },
                { "beat": 5.0, "lane": "LEFT" }
              ],
              "bullets": [
                { "beat": 9.0, "x": 100 },
                { "beat": 1.0, "x": 50 }
              ]
            }
            """;

        Chart chart = ChartLoader.parse(json, "test");

        assertEquals(2.0f, chart.notes.get(0).beat, 0.0001f);
        assertEquals(5.0f, chart.notes.get(1).beat, 0.0001f);
        assertEquals(8.0f, chart.notes.get(2).beat, 0.0001f);
        assertEquals(1.0f, chart.bullets.get(0).beat, 0.0001f);
        assertEquals(9.0f, chart.bullets.get(1).beat, 0.0001f);
    }

    @Test
    void resolvesOmittedBulletDefaults(){
        String json = """
            {
              "formatVersion": 1,
              "title": "Defaults",
              "audio": "audio/test.ogg",
              "bpm": 120,
              "notes": [],
              "bullets": [
                { "beat": 1.0, "x": 10 },
                { "beat": 2.0, "x": 20, "speed": 100, "damage": 3 }
              ]
            }
            """;

        Chart chart = ChartLoader.parse(json, "test");

        assertEquals(220f, chart.bullets.get(0).speed, 0.0001f);
        assertEquals(10, chart.bullets.get(0).damage.intValue());
        assertEquals(100f, chart.bullets.get(1).speed, 0.0001f);
        assertEquals(3, chart.bullets.get(1).damage.intValue());
    }

    @Test
    void resolvesOnlyOmittedBulletFields(){
        String json = """
            {
              "formatVersion": 1,
              "title": "Partial",
              "audio": "audio/test.ogg",
              "bpm": 120,
              "notes": [],
              "bullets": [
                { "beat": 1.0, "x": 10, "speed": 100 },
                { "beat": 2.0, "x": 20, "damage": 3 }
              ]
            }
            """;

        Chart chart = ChartLoader.parse(json, "test");

        assertEquals(100f, chart.bullets.get(0).speed, 0.0001f);
        assertEquals(10, chart.bullets.get(0).damage.intValue());
        assertEquals(220f, chart.bullets.get(1).speed, 0.0001f);
        assertEquals(3, chart.bullets.get(1).damage.intValue());
    }
}

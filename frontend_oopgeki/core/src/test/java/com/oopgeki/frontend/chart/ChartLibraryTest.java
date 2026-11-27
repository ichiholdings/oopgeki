package com.oopgeki.frontend.chart;

import com.badlogic.gdx.files.FileHandle;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ChartLibraryTest {

    @TempDir
    Path root;

    @Test
    void loadsChartsListedInManifest() throws IOException {
        writeManifest("charts/First.json", "libgdx.png", "charts/Second.json");
        writeChart("charts/First.json", "First");
        writeChart("charts/Second.json", "Second");

        ChartLibrary library = discover();

        assertEquals(2, library.charts.size);
        assertEquals(0, library.errors.size);
    }

    @Test
    void sortsChartsCaseInsensitively() throws IOException {
        writeManifest("charts/B.json", "charts/A.json");
        writeChart("charts/B.json", "Banana");
        writeChart("charts/A.json", "apple");

        ChartLibrary library = discover();

        assertEquals("apple", library.charts.get(0).title);
        assertEquals("Banana", library.charts.get(1).title);
    }

    @Test
    void fallsBackToDirectoryListingWhenManifestIsAbsent() throws IOException {
        writeChart("charts/One.json", "One");
        writeChart("charts/Two.json", "Two");
        writeFile("charts/readme.txt", "not a chart");
        writeChart("charts/nested/Three.json", "Three");

        ChartLibrary library = discover();

        assertEquals(2, library.charts.size);
        assertEquals(0, library.errors.size);
    }

    @Test
    void collectsBadChartsWithoutAborting() throws IOException {
        writeManifest("charts/bad.json", "charts/good.json");
        writeFile("charts/bad.json", """
            {
              "formatVersion": 1,
              "title": "Bad",
              "audio": "audio/test.ogg",
              "bpm": 0,
              "notes": [],
              "bullets": []
            }
            """);
        writeChart("charts/good.json", "Good");

        ChartLibrary library = discover();

        assertEquals(1, library.charts.size);
        assertEquals("Good", library.charts.get(0).title);
        assertEquals(1, library.errors.size);
        assertTrue(library.errors.get(0).getSource().endsWith("charts/bad.json"));
        assertTrue(library.errors.get(0).getMessage().contains("invalid BPM"));
    }

    @Test
    void returnsEmptyLibraryWhenManifestListsNoCharts() throws IOException {
        writeManifest("libgdx.png");

        ChartLibrary library = discover();

        assertEquals(0, library.charts.size);
        assertEquals(0, library.errors.size);
    }

    @Test
    void doesNotFallBackWhenManifestExists() throws IOException {
        writeManifest("libgdx.png");
        writeChart("charts/One.json", "One");

        ChartLibrary library = discover();

        assertEquals(0, library.charts.size);
        assertEquals(0, library.errors.size);
    }

    @Test
    void throwsWhenNoDiscoverySourceExists() {
        ChartException exception = assertThrows(ChartException.class, () -> discover());

        assertTrue(exception.getMessage().contains("chart library"));
        assertTrue(exception.getMessage().contains("no chart manifest"));
    }

    private ChartLibrary discover() {
        return ChartLibrary.discover(new FileHandle(root.toFile()));
    }

    private void writeManifest(String... lines) throws IOException {
        Files.writeString(root.resolve("assets.txt"), String.join("\n", lines));
    }

    private void writeChart(String relativePath, String title) throws IOException {
        writeFile(relativePath, """
            {
              "formatVersion": 1,
              "title": "%s",
              "audio": "audio/test.ogg",
              "bpm": 120,
              "notes": [],
              "bullets": []
            }
            """.formatted(title));
    }

    private void writeFile(String relativePath, String content) throws IOException {
        Path file = root.resolve(relativePath);
        Files.createDirectories(file.getParent());
        Files.writeString(file, content);
    }
}

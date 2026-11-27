package com.oopgeki.frontend.chart;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.SerializationException;
import java.util.Comparator;

public class ChartLoader {
    private static final Comparator<ChartEvent> BY_BEAT = (a, b) -> Float.compare(a.beat, b.beat);

    private static void sortEvents(Chart chart){
        chart.notes.sort(BY_BEAT);
        chart.bullets.sort(BY_BEAT);
    }

    public static Chart parse(String jsonText, String sourceName) {
        Json parser = new Json();
        parser.setIgnoreUnknownFields(true);
        Chart chart;
        try {
            chart = parser.fromJson(Chart.class, jsonText);
        } catch (SerializationException e) {
            Array<String> problems = new Array<>(1);
            problems.add("invalid JSON: " + e.getMessage());
            throw new ChartException(sourceName, problems);
        }

        if (chart == null){
            Array<String> problem = new Array<>(1);
            problem.add("chart is null");
            throw new ChartException(sourceName, problem);
        }
        Array<String> problems = new Array<>();
        validate(chart, problems);
        if (problems.size > 0){
            throw new ChartException(sourceName, problems);
        }
        sortEvents(chart);
        resolveBulletDefaults(chart);
        return chart;
    }

    public static Chart load(FileHandle file) {
        String json = file.readString("UTF-8");
        return parse(json, file.path());
    }

    private static void resolveBulletDefaults(Chart chart){
        for (int i = 0; i < chart.bullets.size; i++){
            BulletEvent bullet = chart.bullets.get(i);
            if (bullet.speed == null){
                bullet.speed = chart.bulletSpeed;
            }
            if (bullet.damage == null){
                bullet.damage = chart.bulletDamage;
            }
        }
    }

    private static void validate(Chart chart, Array<String> problems){
        validateMetadata(chart, problems);
        validateNotes(chart, problems);
        validateBullets(chart, problems);
    }
    private static void validateMetadata(Chart chart, Array<String> problems){
        if (chart.formatVersion != 1){
            problems.add("formatVersion is not 1");
        }
        if (chart.title == null || chart.title.isBlank()){
            problems.add("invalid chart title");
        }
        if (chart.audio == null || chart.audio.isBlank()){
            problems.add("invalid audio source");
        }
        if (!Float.isFinite(chart.bpm) || chart.bpm <= 0){
            problems.add("invalid BPM");
        }
        if (!Float.isFinite(chart.offset) || chart.offset < 0){
            problems.add("invalid offset");
        }
        if (!Float.isFinite(chart.noteSpeed) || chart.noteSpeed <= 0){
            problems.add("invalid note speed");
        }
        if (!Float.isFinite(chart.bulletSpeed) || chart.bulletSpeed <= 0){
            problems.add("invalid bullet speed");
        }
        if (chart.bulletDamage < 0){
            problems.add("bullet damage is less than 0");
        }
        if (chart.notes == null){
            problems.add("notes do not exist");
        }
        if (chart.bullets == null){
            problems.add("bullets do not exist");
        }
    }
    private static void validateNotes(Chart chart, Array<String> problems){
        if (chart.notes == null){
            return;
        }

        for (int i = 0; i < chart.notes.size; i++){
            NoteEvent note = chart.notes.get(i);

            if (note == null){
                problems.add("notes[" + i + "] is null");
                continue;
            }
            if (!Float.isFinite(note.beat) || note.beat < 0){
                problems.add("notes[" + i + "].beat must be finite and >= 0");
            }
            if (note.lane == null){
                problems.add("notes[" + i + "].lane is required");
            }
        }
        checkForDupeNotes(chart, problems);
    }

    private static void checkForDupeNotes(Chart chart, Array<String> problems){
        for (int i = 0; i < chart.notes.size; i++){
            NoteEvent note = chart.notes.get(i);
            if (note == null || note.lane == null){
                continue;
            }

            for (int j = i+1; j < chart.notes.size; j++){
                NoteEvent other = chart.notes.get(j);
                if (other == null){
                    continue;
                }

                if (note.lane == other.lane && note.beat == other.beat){
                    problems.add("duplicate note at beat " + note.beat + " in lane " + note.lane);
                    break;
                }
            }
        }
    }
    private static void validateBullets(Chart chart, Array<String> problems){
        if (chart.bullets == null){
            return;
        }

        for (int i = 0; i < chart.bullets.size; i++){
            BulletEvent bullet = chart.bullets.get(i);

            if (bullet == null){
                problems.add("bullets[" + i + "] is null");
                continue;
            }
            if (!Float.isFinite(bullet.beat) || bullet.beat < 0){
                problems.add("bullets[" + i + "].beat must be finite and >= 0");
            }
            if (!Float.isFinite(bullet.x)){
                problems.add("bullets[" + i + "].x must be finite");
            }
            if (bullet.speed != null && (!Float.isFinite(bullet.speed) || bullet.speed <= 0)){
                problems.add("bullets[" + i + "].speed must be finite and > 0");
            }
            if (bullet.damage != null && bullet.damage < 0){
                problems.add("bullets[" + i + "].damage must be >= 0");
            }
        }
    }
}

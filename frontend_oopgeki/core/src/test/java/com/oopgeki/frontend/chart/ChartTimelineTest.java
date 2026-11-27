package com.oopgeki.frontend.chart;

import com.badlogic.gdx.utils.Array;
import org.junit.jupiter.api.Test;

import static com.oopgeki.frontend.chart.Lane.LEFT;
import static com.oopgeki.frontend.chart.Lane.RIGHT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotSame;

class ChartTimelineTest {

    @Test
    void mergesNotesAndBulletsByBeat(){
        Chart chart = chart(120f, 0.5f);
        chart.notes.add(note(4f, LEFT));
        chart.notes.add(note(6f, RIGHT));
        chart.bullets.add(bullet(2f, 100f));
        chart.bullets.add(bullet(8f, 200f));

        Array<ChartEvent> timeline = chart.timeline();

        assertEquals(4, timeline.size);
        assertEquals(2f, timeline.get(0).beat, 0.0001f);
        assertInstanceOf(BulletEvent.class, timeline.get(0));
        assertEquals(4f, timeline.get(1).beat, 0.0001f);
        assertInstanceOf(NoteEvent.class, timeline.get(1));
        assertEquals(6f, timeline.get(2).beat, 0.0001f);
        assertInstanceOf(NoteEvent.class, timeline.get(2));
        assertEquals(8f, timeline.get(3).beat, 0.0001f);
        assertInstanceOf(BulletEvent.class, timeline.get(3));
    }

    @Test
    void putsNotesBeforeBulletsAtTheSameBeat(){
        Chart chart = chart(120f, 0f);
        chart.notes.add(note(4f, LEFT));
        chart.bullets.add(bullet(4f, 100f));

        Array<ChartEvent> timeline = chart.timeline();

        assertEquals(2, timeline.size);
        NoteEvent first = assertInstanceOf(NoteEvent.class, timeline.get(0));
        assertEquals(LEFT, first.lane);
        assertInstanceOf(BulletEvent.class, timeline.get(1));
    }

    @Test
    void returnsEmptyTimelineForEmptyChart(){
        Chart chart = chart(120f, 0f);

        assertEquals(0, chart.timeline().size);
    }

    @Test
    void treatsNullArraysAsEmpty(){
        Chart chart = chart(120f, 0f);
        chart.notes = null;
        chart.bullets = null;

        assertEquals(0, chart.timeline().size);
        assertEquals(0f, chart.durationBeats(), 0.0001f);
        assertEquals(0f, chart.durationSeconds(), 0.0001f);
    }

    @Test
    void returnsAFreshArrayOnEveryCall(){
        Chart chart = chart(120f, 0f);
        chart.notes.add(note(4f, LEFT));

        Array<ChartEvent> first = chart.timeline();
        Array<ChartEvent> second = chart.timeline();

        assertNotSame(first, second);
        first.clear();

        assertEquals(1, second.size);
        assertEquals(1, chart.notes.size);
    }

    @Test
    void durationBeatsUsesLastEventAcrossBothTypes(){
        Chart chart = chart(120f, 0.5f);
        chart.notes.add(note(4f, LEFT));
        chart.bullets.add(bullet(8f, 100f));

        assertEquals(8f, chart.durationBeats(), 0.0001f);
    }

    @Test
    void durationSecondsIncludesOffset(){
        Chart chart = chart(120f, 0.5f);
        chart.notes.add(note(4f, LEFT));
        chart.bullets.add(bullet(8f, 100f));

        assertEquals(4.5f, chart.durationSeconds(), 0.0001f);
    }

    @Test
    void durationSecondsForEventAtBeatZeroIsOffset(){
        Chart chart = chart(120f, 0.5f);
        chart.notes.add(note(0f, LEFT));

        assertEquals(0f, chart.durationBeats(), 0.0001f);
        assertEquals(0.5f, chart.durationSeconds(), 0.0001f);
    }

    @Test
    void durationIsZeroForEmptyChart(){
        Chart chart = chart(120f, 0.5f);

        assertEquals(0f, chart.durationBeats(), 0.0001f);
        assertEquals(0f, chart.durationSeconds(), 0.0001f);
    }

    @Test
    void durationBeatsUsesMaximumEvenWhenUnsorted(){
        Chart chart = chart(120f, 0f);
        chart.notes.add(note(9f, LEFT));
        chart.notes.add(note(3f, RIGHT));

        assertEquals(9f, chart.durationBeats(), 0.0001f);
    }

    private static Chart chart(float bpm, float offset){
        Chart chart = new Chart();
        chart.bpm = bpm;
        chart.offset = offset;
        return chart;
    }

    private static NoteEvent note(float beat, Lane lane){
        NoteEvent note = new NoteEvent();
        note.beat = beat;
        note.lane = lane;
        return note;
    }

    private static BulletEvent bullet(float beat, float x){
        BulletEvent bullet = new BulletEvent();
        bullet.beat = beat;
        bullet.x = x;
        return bullet;
    }
}

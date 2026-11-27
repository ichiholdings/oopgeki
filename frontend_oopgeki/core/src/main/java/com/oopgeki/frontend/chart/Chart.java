package com.oopgeki.frontend.chart;

import com.badlogic.gdx.utils.Array;

public class Chart {

    public int formatVersion = 0;
    public String title = null;
    public String artist = "Unknown";
    public String audio = null;
    public String difficulty = "Normal";
    public float bpm = 0f;
    public float offset = 0f;
    public float noteSpeed = 520f;
    public float bulletSpeed = 220f;
    public int bulletDamage = 10;

    public Array<NoteEvent> notes = new Array<>();
    public Array<BulletEvent> bullets = new Array<>();

    public float secondsOf(float beat){
        return offset + beat * (60f / bpm);
    }

    public Array<ChartEvent> timeline(){
        int noteCount = notes == null ? 0 : notes.size;
        int bulletCount = bullets == null ? 0 : bullets.size;
        Array<ChartEvent> result = new Array<ChartEvent>(noteCount + bulletCount);

        int i = 0, j = 0;

        while (i < noteCount && j < bulletCount){
            if (notes.get(i).beat <= bullets.get(j).beat){
                result.add(notes.get(i));
                i++;
            } else{
                result.add(bullets.get(j));
                j++;
            }
        }

        while (i < noteCount){
            result.add(notes.get(i));
            i++;
        }

        while (j < bulletCount){
            result.add(bullets.get(j));
            j++;
        }

        return result;
    }

    public float durationBeats(){
        float max = 0;
        if (notes != null){
            for (NoteEvent note : notes){
                max = Math.max(max, note.beat);
            }
        }
        if (bullets != null){
            for (BulletEvent bullet : bullets){
                max = Math.max(max, bullet.beat);
            }
        }
        return max;
    }

    public float durationSeconds(){
        boolean hasEvents = (notes != null && notes.size > 0) || (bullets != null && bullets.size > 0);
        if (!hasEvents){
            return 0;
        }
        return secondsOf(durationBeats());
    }
}

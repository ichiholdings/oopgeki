package com.oopgeki.frontend.chart;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ChartTimingTest {

    @Test
    void secondsOfRespectsBpmAndOffset() {
        Chart chart = new Chart();
        chart.bpm = 120f;
        chart.offset = 0.5f;

        assertEquals(0.5f, chart.secondsOf(0f), 0.0001f);
        assertEquals(1.0f, chart.secondsOf(1f), 0.0001f);
        assertEquals(1.5f, chart.secondsOf(2f), 0.0001f);
    }
}

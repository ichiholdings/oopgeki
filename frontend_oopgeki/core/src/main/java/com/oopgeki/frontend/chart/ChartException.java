package com.oopgeki.frontend.chart;

import com.badlogic.gdx.utils.Array;

public class ChartException extends RuntimeException{
    private final String source;
    private final Array<String> problems;

    public ChartException(String source, Array<String> problems){
        super(buildMessage(source, problems));
        this.source = source;
        this.problems = problems;
    }

    private static String buildMessage(String source, Array<String> problems){
        StringBuilder message = new StringBuilder(source + ":");
        for (var i : problems){
            message.append("\n - ").append(i);
        }
        return message.toString();
    }

    public String getSource() {
        return source;
    }

    public Array<String> getProblems() {
        return problems;
    }
}

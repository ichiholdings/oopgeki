package com.oopgeki.frontend.chart;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;

public class ChartLibrary {
    public final Array<Chart> charts = new Array<>();
    public final Array<ChartException> errors = new Array<>();

    public static ChartLibrary discover(){
        return discover(Gdx.files.internal(""));
    }

    static ChartLibrary discover(FileHandle root){
        ChartLibrary lib = new ChartLibrary();

        if (root.child("assets.txt").exists()){
            String manifest = root.child("assets.txt").readString("UTF-8");
            for (String rawLine : manifest.split("\\R")) {
                String line = rawLine.trim();
                if (line.contentEquals("")) continue;
                if (line.startsWith("charts/") && line.endsWith(".json")) loadOneChart(root.child(line), lib);
            }
        } else if (root.child("charts").exists() && root.child("charts").isDirectory()){
            for (FileHandle file : root.child("charts").list()){
                if (!file.isDirectory() && file.name().endsWith(".json")) loadOneChart(file, lib);
            }
        } else {
            Array<String> problem = new Array<>(1);
            problem.add("no chart manifest or charts directory found");
            throw new ChartException("chart library", problem);
        }
        lib.charts.sort((first, second) -> String.CASE_INSENSITIVE_ORDER.compare(first.title, second.title));
        return lib;
    }

    private static void loadOneChart(FileHandle file, ChartLibrary lib) {
        try {
            lib.charts.add(ChartLoader.load(file));
        } catch (ChartException e){
            lib.errors.add(e);
        }
    }
}

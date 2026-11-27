# Chart format (version 1)

A chart is a hand-written JSON file that describes when notes and bullets appear in a song. The game loads every chart in `assets/charts/` at startup through `ChartLibrary`. No Java changes are needed to add a song.

- Audio goes in `assets/audio/`.
- Charts go in `assets/charts/`.
- Keep other `.json` files out of `assets/charts/`; discovery treats every `.json` there as a chart.
- The JSON schema for editor validation is [chart.schema.json](chart.schema.json).

## Example

```json
{
  "formatVersion": 1,
  "title": "Sample Song",
  "artist": "Unknown",
  "audio": "audio/sample.ogg",
  "bpm": 150,
  "offset": 0.0,
  "difficulty": "Normal",

  "noteSpeed": 520,
  "bulletSpeed": 220,
  "bulletDamage": 10,

  "notes": [
    { "beat": 8.0, "lane": "LEFT" },
    { "beat": 8.5, "lane": "RIGHT" },
    { "beat": 9.0, "lane": "LEFT" },
    { "beat": 9.0, "lane": "RIGHT" }
  ],

  "bullets": [
    { "beat": 12.0, "x": 120 },
    { "beat": 12.0, "x": 240 },
    { "beat": 12.0, "x": 360 },
    { "beat": 12.5, "x": 180, "speed": 260 }
  ]
}
```

## Top-level fields

| Field | Type | Required | Default | Notes |
|---|---|---|---|---|
| `formatVersion` | integer | yes | - | Must be `1`. |
| `title` | string | yes | - | Display name. Must not be blank. |
| `artist` | string | no | `"Unknown"` | Display name. |
| `audio` | string | yes | - | Path under `assets/`, for example `audio/song.ogg`. |
| `bpm` | number | yes | - | Quarter-note beats per minute, `> 0`. Constant for the whole chart. |
| `offset` | number | no | `0` | Seconds into the audio where beat 0 occurs, `>= 0`. |
| `difficulty` | string | no | `"Normal"` | Display name. Not interpreted by the loader. |
| `noteSpeed` | number | no | `520` | Suggested note fall speed, pixels per second. Gameplay may override. |
| `bulletSpeed` | number | no | `220` | Default bullet speed, pixels per second. |
| `bulletDamage` | integer | no | `10` | Default bullet damage. |
| `notes` | array | no | `[]` | Note events. May be omitted, but may not be `null`. |
| `bullets` | array | no | `[]` | Bullet events. May be omitted, but may not be `null`. |

Unknown fields are ignored by the loader. The schema rejects them anyway, so a typo like `"lanes"` shows up in the editor instead of failing silently.

## Note events

| Field | Type | Required | Notes |
|---|---|---|---|
| `beat` | number | yes | `>= 0`; fractional values allowed. |
| `lane` | string | yes | `"LEFT"` or `"RIGHT"`, uppercase. |

- `LEFT` maps to the `S` key; `RIGHT` maps to the `D` key.
- `beat` is when the note should reach the hit line.
- Two notes in the same lane at the same beat are invalid, because one key press cannot hit both. The same beat in different lanes is valid and is how chords are written.
- No `beat` ordering is required in the file; the loader sorts by beat after validation.

## Bullet events

| Field | Type | Required | Notes |
|---|---|---|---|
| `beat` | number | yes | `>= 0`; fractional values allowed. |
| `x` | number | yes | Horizontal center in game coordinates. Fixed for the bullet's lifetime. |
| `speed` | number | no | `> 0`; defaults to the chart's `bulletSpeed`. |
| `damage` | integer | no | `>= 0`; defaults to the chart's `bulletDamage`. |

- `beat` is when the bullet crosses the player's row (the danger line), not when it spawns. Gameplay derives the spawn time from `speed` and its own screen constants.
- Bullets fall straight down at fixed `x`; the player dodges with the arrow keys.
- Multiple bullets at the same beat with different `x` create walls with gaps.
- `x` is not validated against the screen width, because screen constants belong to gameplay. Gameplay is expected to clamp or reject out-of-range values.

## Timing

```text
seconds(beat) = offset + beat * (60 / bpm)
```

At 150 BPM one beat is 0.4 seconds. With `offset: 0.0`, beat 8 lands at 3.2 seconds. With `offset: 0.5`, the same beat lands at 3.7 seconds.

- `offset` is the audio time at which beat 0 occurs. Increase it if the first beat is late relative to the music.
- Leave the first few beats empty so events have time to travel on screen before they must be hit. At 150 BPM, starting at beat 8 gives roughly 3.2 seconds of lead-in.
- Because BPM is constant in version 1, beat order is also time order.

## Spawn timing (gameplay integration)

The chart stores hit times. Gameplay converts them to spawn times using its own screen constants and speeds.

Notes:

```text
noteHitTime   = chart.secondsOf(note.beat)
noteSpawnTime = noteHitTime - (noteSpawnY - hitLineY) / noteSpeed
```

Bullets:

```text
bulletHitTime   = chart.secondsOf(bullet.beat)
bulletSpawnTime = bulletHitTime - (bulletSpawnY - playerY) / bullet.speed
```

`noteSpawnY`, `hitLineY`, `bulletSpawnY`, and `playerY` are gameplay constants. This split keeps every event on the same musical grid while letting gameplay choose its own layout.

## Adding a chart

1. Put the audio file in `frontend_oopgeki/assets/audio/`.
2. Put the JSON file in `frontend_oopgeki/assets/charts/`.
3. Rebuild or run the game.

`assets/assets.txt` is generated by Gradle's `generateAssetList` task and is the primary discovery source. When running from an IDE without Gradle and `assets.txt` is missing, discovery falls back to listing `assets/charts/` directly.

A chart that fails validation is skipped and reported through `ChartLibrary.errors`; it does not prevent other charts from loading.

## Validation errors

The loader collects every problem in a file and reports them together with the file path. The messages are:

| Message | Meaning |
|---|---|
| `formatVersion is not 1` | Set `"formatVersion": 1`. |
| `invalid chart title` | `title` is missing or blank. |
| `invalid audio source` | `audio` is missing or blank. |
| `invalid BPM` | `bpm` is missing, zero, negative, or not a number. |
| `invalid offset` | `offset` is negative or not a number. |
| `invalid note speed` | `noteSpeed` is zero, negative, or not a number. |
| `invalid bullet speed` | `bulletSpeed` is zero, negative, or not a number. |
| `bullet damage is less than 0` | `bulletDamage` is negative. |
| `notes do not exist` | `notes` is explicitly `null`; omit it or use `[]` instead. |
| `bullets do not exist` | `bullets` is explicitly `null`; omit it or use `[]` instead. |
| `notes[i] is null` / `bullets[i] is null` | The array contains a `null` entry. |
| `notes[i].beat must be finite and >= 0` | Fix the note's `beat`. |
| `notes[i].lane is required` | `lane` is missing. An unrecognized value such as `"left"` is reported as `invalid JSON` instead. |
| `bullets[i].beat must be finite and >= 0` | Fix the bullet's `beat`. |
| `bullets[i].x must be finite` | `x` is missing or not a number. |
| `bullets[i].speed must be finite and > 0` | `speed` is present but invalid; omit it to use `bulletSpeed`. |
| `bullets[i].damage must be >= 0` | `damage` is present but negative; omit it to use `bulletDamage`. |
| `duplicate note at beat <beat> in lane <lane>` | Two notes share a beat and lane. |
| `invalid JSON: ...` | The file does not parse; check commas, quotes, and brackets. |
| `chart is null` | The file contains `null` instead of an object. |

Indices refer to the order in the file, before sorting.

## Editor setup

The schema is inert until an editor is told where it is.

VS Code, workspace-wide: create `.vscode/settings.json` in `frontend_oopgeki/`:

```json
{
  "json.schemas": [
    {
      "fileMatch": ["/assets/charts/*.json"],
      "url": "./docs/chart.schema.json"
    }
  ]
}
```

Per file, editor-agnostic: add `$schema` as the first key of the chart. The loader ignores unknown fields, so it is harmless.

```json
{
  "$schema": "../../docs/chart.schema.json",
  "formatVersion": 1
}
```

IntelliJ IDEA can map the schema under Settings → Languages & Frameworks → Schemas and DTDs → JSON Schemas.

## Limitations in version 1

- No BPM changes or timing points inside a chart.
- No hold notes.
- No aimed, spread, or patterned bullets; bullets fall straight down at fixed `x`.
- No per-note timing offsets; every event is on the beat grid.
- The loader does not check that the `audio` file exists.

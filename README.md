# OOP-Geki (オプゲキ)

OOP-Geki adalah game rhythm yang mencampur elemen dari rhythm game (menekan not berdasarkan irama lagu) dengan bullet hell (menghindari bullet). Pemain akan ditempatkan di dalam sebuah course di mana akan jatuh not musik yang sesuai dengan lagu yang sedang bermain. Pemain harus menekan tombol yang sesuai untuk menekan note tersebut dengan irama yang sesuai dengan lagu sembari menghindari bullet dari Boss di bagian atas play area. 

Pemain akan memiliki HP yang akan berkurang jika kena bullet tersebut. Evaluasi dari performa pemain akan ditentukan berdasarkan score: not-not yang ditekan pemain yang sesuai dengan irama lagu dan berapa kali dia terkena oleh bullet saat bermain.

# Frontend
A [libGDX](https://libgdx.com/) project generated with [gdx-liftoff](https://github.com/libgdx/gdx-liftoff).

This project was generated with a template including simple application launchers and an `ApplicationAdapter` extension that draws libGDX logo.

## Modules 

- `core`: Main module with the application logic shared by all platforms.
- `lwjgl3`: Primary desktop platform using LWJGL3; was called 'desktop' in older docs.

## Gradle

This project uses [Gradle](https://gradle.org/) to manage dependencies.
The Gradle wrapper was included, so you can run Gradle tasks using `gradlew.bat` or `./gradlew` commands.
Useful Gradle tasks and flags:

- `--continue`: when using this flag, errors will not stop the tasks from running.
- `--daemon`: thanks to this flag, Gradle daemon will be used to run chosen tasks.
- `--offline`: when using this flag, cached dependency archives will be used.
- `--refresh-dependencies`: this flag forces validation of all dependencies. Useful for snapshot versions.
- `build`: builds sources and archives of every project.
- `cleanEclipse`: removes Eclipse project data.
- `cleanIdea`: removes IntelliJ project data.
- `clean`: removes `build` folders, which store compiled classes and built archives.
- `eclipse`: generates Eclipse project data.
- `idea`: generates IntelliJ project data.
- `lwjgl3:jar`: builds application's runnable jar, which can be found at `lwjgl3/build/libs`.
- `lwjgl3:run`: starts the application.
- `test`: runs unit tests (if any).

Note that most tasks that are not specific to a single project can be run with `name:` prefix, where the `name` should be replaced with the ID of a specific project.
For example, `core:clean` removes `build` folder only from the `core` project.

# Food Additives

An offline Android reference for food additives (E numbers). It lists 531 additives in nine
ranges, each with a short description and whether it is authorised as a food additive in the
European Union. The app is in English and Russian, has no ads, needs no network access and
collects no data.

- **Package:** `com.yrybiak.foodadditives`
- **Minimum Android version:** 6.0 (API 23)
- **Languages:** English (default), Russian

## History

The first version was written in 2014 as a learning project and published on Google Play.
Version 2.0 (2026) is a full rework: current toolchain, Material 3 design, English added, the
content rewritten and fact-checked, and the list extended from 191 to 531 additives.

## Content

- **Articles** are in `app/src/main/res/raw/additives_e*.json` (English) and
  `app/src/main/res/raw-ru/` (Russian): one file per range, each a list of
  `{"code", "name", "text"}`. Both languages must list the same codes in the same order; a
  unit test checks this.
- **EU status** is in `app/src/main/res/raw/eu_authorised.json`: the codes on the EU list of
  authorised food additives (Annex II, Part B of Regulation (EC) No 1333/2008), checked against
  the text consolidated in April 2025 plus the November 2026 authorisation of E424. Later
  amendments have to be added by hand.
- **Sources.** Every article was checked against sources such as EFSA opinions, the EU
  regulations, the Codex Alimentarius list of additive numbers, JECFA evaluations and the
  articles on individual substances in Wikipedia. Claims that could not be verified were
  removed. The per-entry record of changes and sources is in [`review/`](review/STATUS.md).

The descriptions are general information, not medical or regulatory advice.

## Building

Requirements: JDK 17 or newer and the Android SDK (platform 37). Android Studio provides both.

```sh
./gradlew :app:assembleDebug        # debug APK in app/build/outputs/apk/debug/
./gradlew :app:testDebugUnitTest    # unit tests, including the data checks
./gradlew :app:lintDebug            # Android lint
```

A release build (`./gradlew :app:bundleRelease`) is minified with R8. It is signed only when
signing details are provided; see [docs/RELEASING.md](docs/RELEASING.md).

## Project layout

```
app/src/main/java/com/yrybiak/foodadditives/
    MainActivity            list of categories
    AdditiveListActivity    additives of one category
    DetailActivity          one additive, or the About page
    Category, EuStatus      data loading
    ThemeSettings           light/dark choice
app/src/main/res/raw*/      the articles and the EU list
app/src/test/               data validation tests
design/                     source artwork for the launcher icon
distribution/whatsnew/      "What's new" texts for Google Play
review/                     record of the 2026 content review
.github/workflows/          CI and the Google Play release pipeline
```

## Releasing

Pushing a tag such as `v2.0.0` builds a signed bundle and uploads it to the internal testing
track on Google Play. The one-time setup and the release steps are in
[docs/RELEASING.md](docs/RELEASING.md).

## Privacy

The app has no network access, requests no access to user data and collects nothing. See
[PRIVACY.md](PRIVACY.md).

## License

Copyright 2014–2026 Yaroslav Rybiak.

Licensed under the Apache License, Version 2.0; see [LICENSE](LICENSE). The software is
provided "as is", without warranties or conditions of any kind.

## Credits

- Category and status icons: [Material Symbols](https://fonts.google.com/icons) by Google,
  Apache License 2.0.
- Author: Yaroslav Rybiak.

# Releasing to Google Play

The pipeline in `.github/workflows/release.yml` builds a signed app bundle and uploads it to
Google Play. It needs a one-time setup, described first. None of the secrets below belong in
the repository; `.gitignore` already excludes the usual file names.

## One-time setup

### 1. Create the app in Play Console

1. In [Play Console](https://play.google.com/console), create an app with the package name
   `com.yrybiak.foodadditives`. The package name cannot be changed after the first upload.
2. Fill in the store listing, content rating, target audience and the Data safety form. The
   app collects no data and has no network access. For the privacy policy URL, use the
   address of [`PRIVACY.md`](../PRIVACY.md) on GitHub.
3. Store graphics: the 512 px icon can be exported from `design/icon_cupcake_1024.png`. A
   1024×500 feature graphic and at least two phone screenshots are also required.

### 2. Create an upload key

New apps use Play App Signing: Google holds the key that signs what users install, and you
sign uploads with your own *upload key*. If the upload key is ever lost, Google can reset it,
so losing it no longer means losing the app.

```sh
keytool -genkeypair -v \
  -keystore upload.jks -alias upload \
  -keyalg RSA -keysize 2048 -validity 10000
```

Keep `upload.jks` and its passwords somewhere safe and backed up, outside this repository.

### 3. Do the first upload by hand

The Play API cannot create an app's first release. Build a signed bundle locally and upload it
in Play Console under *Testing > Internal testing*.

Create `keystore.properties` in the project root (it is git-ignored):

```properties
storeFile=/absolute/path/to/upload.jks
storePassword=...
keyAlias=upload
keyPassword=...
```

Then build:

```sh
./gradlew :app:bundleRelease
# result: app/build/outputs/bundle/release/app-release.aab
```

### 4. Create a service account for the pipeline

1. In [Google Cloud Console](https://console.cloud.google.com/), create a project (or pick
   one) and enable the **Google Play Android Developer API**.
2. Create a service account in that project and create a JSON key for it. Download the file.
3. In Play Console, open *Users and permissions*, invite the service account's e-mail address,
   and give it permission for this app to release to testing tracks (and to production, if the
   pipeline should be able to do that).

It can take a few hours before a new service account is accepted by the API.

### 5. Add the secrets to GitHub

In the repository, open *Settings > Environments*, create an environment named `play-store`,
and add these secrets to it:

| Secret | Value |
|---|---|
| `UPLOAD_KEYSTORE_BASE64` | The keystore, base64-encoded: `base64 -i upload.jks` (macOS) or `base64 -w0 upload.jks` (Linux) |
| `UPLOAD_KEYSTORE_PASSWORD` | Keystore password |
| `UPLOAD_KEY_ALIAS` | Key alias (`upload` if you used the command above) |
| `UPLOAD_KEY_PASSWORD` | Key password |
| `PLAY_SERVICE_ACCOUNT_JSON` | The whole content of the service account's JSON key file |

Optional but recommended: add yourself as a *required reviewer* on the `play-store`
environment. Every release then waits for your approval before it can read the secrets.

## Making a release

1. Update the "What's new" texts in `distribution/whatsnew/` (`whatsnew-en-US`,
   `whatsnew-ru-RU`; 500 characters at most each).
2. Commit, then tag and push:

   ```sh
   git tag v2.0.0
   git push origin v2.0.0
   ```

   The workflow runs the tests and lint, builds and signs the bundle, and uploads it to the
   **internal testing** track. The version name comes from the tag (`v2.0.0` gives `2.0.0`).
3. To release to another track, either promote the build in Play Console, or start the
   workflow by hand: *Actions > Release to Google Play > Run workflow*, and choose the track.

### Version codes

Google Play requires a higher version code for every upload. The pipeline sets it to
`100 + the workflow's run number`, so it grows on its own. The default in `app/build.gradle`
(used for local builds) does not need to be changed for releases made by the pipeline, but a
bundle uploaded by hand must have a code higher than any uploaded before.

### Draft apps

Until an app's first release has been reviewed and published, Play accepts API uploads only as
drafts. If the upload step fails with a message about draft apps, run the workflow by hand
with *status* set to `draft` and finish the rollout in Play Console.

## What the pipeline does not do

- It does not create the app, fill in the store listing, or upload screenshots.
- It does not change the EU list or the articles; those are edited in the repository.
- The CI workflow (`ci.yml`) runs tests, lint and a debug build on every push and pull request
  and needs no secrets.

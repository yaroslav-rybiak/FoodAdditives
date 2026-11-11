# Content review: work in progress

Paused on 2026-11-11. This branch (`content-review`) holds the half-finished content review.
`master` does not have any of it. Delete this `review/` folder before merging to `master`.

## Decisions already made

- Show EU status only (no UK/US), as data, not in the article text.
- Fact-check with a stronger model (Opus agents with web access).
- Claims that cannot be verified are deleted, not softened.

## Done

- **EU status source.** `review/eu/eu2020b.tsv` is Annex II Part B of Regulation (EC)
  No 1333/2008 as it stood on 2020-12-31, parsed from legislation.gov.uk (EUR-Lex blocks
  automated access and the Commission's additives API did not respond). Amendments since
  then that were verified one by one:
  - E171 removed (Regulation (EU) 2022/63)
  - E483 removed (Regulation (EU) 2023/2379, from April 2024)
  - E246 added (Regulation (EU) 2022/1037)
  - E267 added (Regulation (EU) 2023/2086)
  - E424 added (reported as Regulation (EU) 2026/2298, from 8 November 2026)

  Not verified: that these are the only amendments since 2020 affecting codes in the app.
- **App side** (builds, lint clean): `res/raw/eu_authorised.json` (332 codes), `EuStatus`,
  a status line on each article, a disclaimer on the About page, and a test that every code
  on the EU list has an article.
- **Review inputs.** `review/in/<chunk>.json`, 17 chunks covering 529 entries: the current
  English and Russian text, the EU flag and the official EU name. Six entries are new and
  have no text yet: E456 and E1205 to E1209 (EU-authorised, missing from the app).
- **Brief for the reviewers.** `review/BRIEF.md`.

## Not done

- **The fact-check itself.** 17 agents were launched and stopped before any wrote output.
  Nothing in `res/raw` or `res/raw-ru` has been edited by the review yet, so the articles
  still contain approval-status sentences and unverified claims.
- **Merge.** Reviewer output goes to `out/<chunk>.json` and `out/<chunk>.log.json` (see the
  brief). It then has to be merged back into `res/raw/additives_*.json` and
  `res/raw-ru/additives_*.json`, in input order, with the six new entries included.
- **Tests currently fail on this branch** (`./gradlew test`): `eu_authorised.json` lists the
  six new codes, which get their articles only when the review output is merged.
- **Final check.** After the merge: run the tests, look at the app in both languages, and
  read a sample of entries plus every entry the reviewers marked as unresolved.

## To resume

1. Copy `review/` to a scratch location (the brief expects `/tmp/fa-review/`, with `in/`,
   `out/` and `work/`).
2. Relaunch one reviewer per chunk with the prompt pattern in "Reviewer prompt" below.
   Chunks can be run a few at a time to spread the cost.
3. Merge, test, sample, then merge the branch to `master` without `review/`.

## Reviewer prompt

> Fact-check and edit a chunk of food-additive reference articles (English and Russian) for
> an Android app. Read the full brief first and follow it exactly: /tmp/fa-review/BRIEF.md
> Your chunk is: `<chunk>`. Input: /tmp/fa-review/in/`<chunk>`.json. Outputs:
> /tmp/fa-review/out/`<chunk>`.json and /tmp/fa-review/out/`<chunk>`.log.json. Scratch
> directory: /tmp/fa-review/work/`<chunk>`/. Unverifiable claims are deleted, not softened;
> EU approval status is shown by the app separately, so remove status statements from the
> prose. Do not edit any file outside /tmp/fa-review/.

Chunk-specific notes to add to the prompt:

- `e100_2`: E171 is not authorised since 2022; verify the reason before stating it.
- `e200_2`: E246 and E267 were added to the EU list after 2020.
- `e300_2`: the identity of E388 and the functions of E342, E353, E354, E375, E389, E390,
  E391 were guessed in the first draft.
- `e400_1`: E424 was authorised in November 2026; the notes on E407, E412 and E425 were
  written from memory.
- `e400_2`: E456 is new, write it from sources.
- `e400_3`: E483 was removed from the EU list in 2024.
- `e500_1`: the identity of E540 is disputed (dicalcium ferrocyanide or dicalcium
  diphosphate); settle it from the Codex INS list.
- `e600_1`: verify or delete the gout note on E627 and E631; make E636's source tree agree
  in both languages.
- `e700_1`: antibiotics, none is a food additive.
- `e900_1`: the functions of E905, E905b, E906, E910, E916 to E919 were guessed; E912 was
  removed from the EU list in 2014.
- `e900_2`: sweetness ratios need sources; keep the polyol and aspartame label warnings if
  verified.
- `e1000_1`: E1205 to E1209 are new, write them from sources.
- `e1000_2`: drop "approved in 2010" on E1521; the functions of E1452, E1503, E1519 were
  guessed.

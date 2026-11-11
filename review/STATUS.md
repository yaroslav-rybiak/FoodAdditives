# Content review: work in progress

Paused on 2026-11-11. This branch (`content-review`) holds the half-finished content review.
`master` does not have any of it. Delete this `review/` folder before merging to `master`.

## Progress (updated 2026-11-11, second pause)

- Reviewed and saved in `review/out/`: 8 of 17 chunks, 267 of 529 entries (E100 to E472b):
  `e100_1 e100_2 e200_1 e200_2 e300_1 e300_2 e400_1 e400_2`.
- Not started: `e400_3 e500_1 e500_2 e600_1 e700_1 e900_1 e900_2 e1000_1 e1000_2`.
- Run two chunks at a time. Reviewers must checkpoint: append each finished entry to
  `out/<chunk>.jsonl` and `out/<chunk>.log.jsonl`, and skip codes already there on a rerun.
  Agents that wrote output only at the end lost everything when interrupted.
- Tips that worked: reuse `work/<chunk>/` as a source cache; Wikipedia `action=raw` when the
  API rate-limits; legislation.gov.uk for EU legal texts; OpenAlex for EFSA abstracts; Codex
  CXG 36-1989 for functional classes (Wikipedia's E-number table has wrong classes).
- The app data in `res/raw*` is still unchanged; nothing is merged yet.

## Queued: second look by the strongest model (owner asked for this, not yet run)

Give the items the reviewers marked unresolved to one Fable agent, after all chunks are done
(or sooner for the finished ones). The full list is the non-empty `unresolved` fields in
`review/out/*.log.json`. Known so far:

- E317, E318: renamed potassium / calcium erythorbate; confirm against the Codex INS list.
- E424: confirm the EU authorisation (reported as Regulation (EU) 2026/2298, from
  8 November 2026); it rests on one web search result.
- E391: English Wikipedia says phytic acid is allowed in the EU; the 2020 EU list does not
  have it. Confirm `eu_authorised` false.
- E106: same substance as E101(ii) in the EU; decide how to word it.
- E152, E153, E164: English and Russian Wikipedia disagree on identity or name.
- E201, E213, E230 to E233: regulatory history that could not be sourced.
- About 20 Russian names with no Russian source (see logs: E140, E141, E209, E242, E243,
  E262, E266, E267, E283, E353, E380, E388, E392, E407a, E423, E427, E428, E456, E463a, E468).
- Entries cut to one or two sentences for lack of sources: E344, E370, E411, E430, E431,
  E434, E472b, E328.
- E123, E127, E443: US history kept in the text; confirm that fits the policy.

## Status corrections found during the review

My first parse of the EU list missed its footnotes. Corrected in `eu_authorised.json`
(now 328 codes), but `review/in/*.json` still carries the old values for these four:

- E556 and E559: "authorised until 31 January 2014"; E558: "authorised until 31 May 2013"
  (aluminium-containing additives, Regulation (EU) No 380/2012). Not authorised now.
- E161g canthaxanthin: on the list but "not authorised in the food categories listed in
  Part D and E". Treated as not authorised as a food additive.

The texts of these four must be checked against the corrected status in the final second look.

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

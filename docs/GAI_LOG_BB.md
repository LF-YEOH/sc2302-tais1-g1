# GAI Log: Belgutei Bayar (Part D, RestaurantUI)

| Date | Tool | What I asked for | How I used the output |
|---|---|---|---|
| 25 Oct 2026 | Claude | Asked some help implementing in TODO (D) method bodies in boundary/RestaurantUI.java from the skeleton comments and docs/team-briefs.md, and suggest a throwaway test. | Reviewed every method against its skeleton comment, ran the throwaway test (bad input such as "abc" and "9" is re-asked) and both replay files in `runs/`, then committed the file. |

## What I checked myself
- Method names, parameters and return types are unchanged from the skeleton.
- The UI never checks game rules; VICTORY/DEFEAT comes from `Scoreboard.isVictory()`.
- Money uses `Money.format(...)`; the average shows one decimal place, or "N/A" before anyone pays.
- `readInt` echoes replay lines, re-asks on bad input and returns `min` at the end of a replay file.

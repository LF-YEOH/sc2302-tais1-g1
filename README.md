# Restaurant Rush

SC2002 group assignment: a turn-based restaurant management game in Java,
built in three stages (tags `stage-1`, `stage-2`, `stage-3`).

## Build and run

Requires a JDK (Java 17 or newer). From the repository root:

| Windows (PowerShell) | Mac / Linux |
|---|---|
| `.\build.bat` | `./build.sh` |
| `.\build.bat --replay runs\stage1-victory.txt` | `./build.sh --replay runs/stage1-victory.txt` |

The first line plays with the keyboard; the second plays back a saved run.

## Working on this project

### Structure (Boundary-Control-Entity)

| Package | Contains | Rule |
|---|---|---|
| `restaurant.boundary` | `RestaurantUI` | Input and output only. Checks input format, never game rules. |
| `restaurant.control` | `GameController`, `ArrivalSchedule`, `WaiterTask` | Runs the turns, owns the lists of customers and tables. |
| `restaurant.entity` | Customer, Order, Table, Menu, staff, Scoreboard, ... | Data and the rules that belong to it. Never prints or reads input. |

The design is in `docs/design-stage1.md`; design decisions are in `docs/DECISIONS.md`.

### Rules for everyone

1. **Only edit the files you own** (see the "Owner:" line at the top of each
   class). Need a change in someone else's file? Ask the lead first.
2. **Do not change method names, parameters or return types** in the
   skeleton. Other people's code calls them. Ask the lead if one seems wrong.
3. **Follow the comment above each method exactly.** It says what the method
   must do, what it may assume, and when it must throw `InvalidActionException`.
4. **Stage 1 code contains only Stage 1 features.** No VIP, Critic, Host,
   ComboMeal or Happy Hour code, classes or comments until that stage starts.
5. **Comment the purpose of every class and method** (already done in the
   skeleton; keep it accurate if you add private helper methods).
6. **Use only Java covered in the course** (ArrayList, enums, exceptions,
   interfaces, enhanced for, etc.). No Queue/LinkedList, records or Optional.
7. **Record any AI help you use for your GAI declaration.**

### Git workflow

```
git checkout main
git pull
git checkout -b feature/<your-part>        # e.g. feature/customer
# ... write code, build, test ...
git add <your files>
git commit -m "Implement Customer state changes"
git push -u origin feature/<your-part>
```

Then open a **Pull Request** into `main` on GitHub. The lead reviews and
merges it. Commit in small steps with clear messages; the history is marked.

### Testing your part on its own

Make a throwaway class, e.g. `src/restaurant/CustomerCheck.java`, with a
`main` method that creates objects and prints results. Run it with
`javac -d out (all files)` then `java -cp out restaurant.CustomerCheck`.
**Do not commit it.** Delete it before your pull request.

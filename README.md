# Assignment 1 - Web Browser Navigation System

## Omar Hamza, OMH230001, CS 3345.002

---

## Deliverables

- All `.java` files compile without errors and include clear, modular code comments.
- Driver (`Main.java`) demonstrates all required operations.
- This README explains design choices, test cases, and asymptotic complexities.

---

## Project Structure

```
src/
    BrowserLinkedList.java     // Doubly-linked list (head/tail)
    BrowserArrayList.java      // Circular array (resizable) for queue
    BrowserStack.java          // Stack backed by BrowserLinkedList
    BrowserQueue.java          // Queue backed by BrowserArrayList
    StackIterator.java         // Iterator (top -> bottom) for stacks
    BrowserNavigation.java     // High-level browser ops
    Main.java                  // CLI driver
```

_All files use `package src;` at the top._

---

## How to Compile & Run

```bash
# from project root
javac -d out src/*.java
java -cp out src.Main

# optional: feed commands from a file
java -cp out src.Main < commands.txt
```

**Recognized commands:**

```
visit <url>    | back      | forward
history        | clear     | save
restore        | quit
```

---

## Implementation Summary

### Data Structures

- **Back Stack / Forward Stack**: `BrowserStack<T>` -> `BrowserLinkedList<T>`

  - `addFirst/removeFirst` on a doubly-linked list for O(1) push/pop.
  - `StackIterator` iterates top -> bottom (delegates to list iterator from `head`).

- **History Queue**: `BrowserQueue<T>` -> `BrowserArrayList<T>` (circular buffer)
  - Tracks `head` and `size`; tail index = `(head + size) % capacity`.
  - Amortized O(1) enqueue/dequeue; resizes by doubling.

### Browser Operations (`BrowserNavigation`)

- `visitWebsite(url)`: enqueue to history; push current page to back; clear forward.
- `goBack()`: push current to forward; pop from back as new current (throws `EmptyStackException` if empty).
- `goForward()`: push current to back; pop from forward as new current (throws `EmptyStackException` if empty).
- `showHistory()`: iterate queue using its iterator (circular order preserved).
- `clearHistory()`: empties the queue.
- `closeBrowser() / restoreLastSession()`: **text-based serialization**
  - Writes sections `#CURRENT`, `#BACK`, `#FWD`, `#HISTORY` to `session_data.txt`.
  - Reads back and repopulates structures. Human-readable and grader-friendly.

---

### BrowserNavigation

```java
String visitWebsite(String url);
String goBack();                 // throws EmptyStackException when back is empty
String goForward();              // throws EmptyStackException when forward is empty
String showHistory();            // returns newline-separated list or "No browsing history available."
String clearHistory();           // returns confirmation
String closeBrowser();           // writes session_data.txt
String restoreLastSession();     // reads session_data.txt (or returns "No previous session found.")
String getCurrentPage();
```

### BrowserStack<T>

```java
void push(T v); T pop(); T peek();
boolean isEmpty(); int size();
Iterator<T> iterator();          // top -> bottom
```

### BrowserQueue<T>

```java
void enqueue(T v); T dequeue(); boolean isEmpty(); int size();
Iterator<T> iterator();          // front -> back in logical order
```

---

## Time Complexity (Upper Bounds)

| Operation                               | Complexity                                |
| --------------------------------------- | ----------------------------------------- |
| Stack `push`, `pop`, `peek`, `isEmpty`  | O(1)                                      |
| Queue `enqueue`, `dequeue`, `peek`      | O(1) amort. (resize O(n))                 |
| `visitWebsite` (incl. clearing forward) | O(n_fwd) to clear forward; otherwise O(1) |
| `goBack`, `goForward`                   | O(1)                                      |
| `showHistory` (iterate/print)           | O(n_hist)                                 |
| `clearHistory`                          | O(n_hist) (nulling elements)              |
| `closeBrowser` (save all)               | O(n_back + n_fwd + n_hist)                |
| `restoreLastSession`                    | O(n_back + n_fwd + n_hist)                |

_Notes:_

- Queue operations are amortized O(1) due to occasional resize.
- `visitWebsite` clears the forward stack when branching a new path -> O(n_fwd).
- Clearing history sets array slots to `null` -> O(n_hist).

---

## Test Plan (Representative Cases)

1. **Basic Navigation**

   - `visit a`, `visit b`, `visit c` -> `getCurrentPage()==c`, history `a,b,c`.

2. **Back/Forward**

   - From above: `back` -> page `b`; `forward` -> page `c`.
   - After `back` then `visit d`: forward stack must be cleared (no `forward` allowed).

3. **Empty Stack Errors**

   - On fresh start: `back` or `forward` throws `EmptyStackException`.
   - Driver catches and prints specified messages.

4. **History Iteration**

   - `history` prints all visited URLs in insertion order using custom iterator.

5. **Clear History**

   - After multiple visits, `clear` -> `history` shows “No browsing history available.”

6. **Save/Restore**

   - `save` to write `session_data.txt`, restart program, `restore` -> same current page, stacks, and history.

7. **Edge Inputs**
   - `visit` with empty string or whitespace handled gracefully (still sets as current or reject via simple check if desired).
   - Long URLs and duplicates allowed (no dedupe by design).

---

## Design Choices & Rationale

- **Linked list for stacks**: true O(1) push/pop at head without capacity concerns.
- **Circular array for queue**: avoids shifting; contiguous cache-friendly storage; amortized O(1).
- **Text serialization**: human-readable, easy to debug/grade; stable across minor code changes.
- **Iterators**: conform to `Iterable<T>`/`Iterator<T>` so `for-each` works on stacks and queue.

---

## Optional Enhancements

- **Bounded history** (cap size; drop oldest on overflow).
- **De-duplicate consecutive visits** (don’t enqueue same URL twice in a row).
- **Timestamps** in history (store `url|epochMillis`).
- **Undo clear** (persist last snapshot).

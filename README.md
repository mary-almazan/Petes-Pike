# Pete's Pike
Pete's Pike is a logic puzzle game where characters slide in straight lines until hitting another piece or board boundary. The objective is to guide Pete (P) to the mountaintop (T) using numbered goats (0-8) as stopping points.

## Requirements

- Java 21+
- Maven 3.6+
- JavaFX 21

## Installation

```bash
git clone <repository-url>
cd petespike
mvn clean compile
```

## Usage

**GUI Version:**
```bash
mvn javafx:run
```

**CLI Version:**
```bash
mvn exec:java
```

## Game Controls

### GUI
- Click to select pieces (Pete or goats)
- Use directional buttons to move selected piece
- Access hints, reset, and solve functions via control panel

### CLI
- `move <row> <col> <direction>` - Move piece
- `hint` - Get suggested move
- `reset` - Restart puzzle
- `new <filename>` - Load puzzle
- `help` - Show all commands

## Project Structure

```
src/main/java/
├── petespike/model/     # Game logic and data structures
├── petespike/view/      # GUI and CLI interfaces
└── backtracker/         # Puzzle solving algorithm
```

## Key Features

- Dual interface (GUI/CLI)
- Automatic puzzle solver using backtracking
- Multiple difficulty levels
- Custom puzzle file support
- Move validation and hint system

## Puzzle Format

Puzzle files contain a grid where:
- `P` = Pete
- `T` = Mountaintop
- `0-8` = Numbered goats
- `-` = Empty space

## Testing

```bash
mvn test
```

## Technical Details

- **Architecture:** Model-View separation
- **Algorithms:** Backtracking for solving
- **Patterns:** Observer pattern for state management
- **Build System:** Maven with JavaFX plugin

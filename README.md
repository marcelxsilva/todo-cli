# Simple ToDo List CLI in Kotlin

## Quick Installation

### Option 1: Download pre-compiled binary

You can download the latest version of the application directly from the [Releases](https://github.com/marcelxsilva/todo-cli/releases) page:

- For Linux: Download `todo-linux.tar.gz`
- For macOS: Download `todo-macos.tar.gz`

After downloading, extract the file and follow these steps:
```bash
# Extract the file
tar -xzf todo-[your-system].tar.gz

# Copy all files to a directory in PATH
sudo cp -r todo/* /usr/local/bin/

# Restart the terminal and now you can use the command
todo --help
```

### Option 2: Build from source code

If you prefer to build from scratch, please refer to the compilation instructions below.

- JDK 17 or higher
- Gradle 7.0 or higher


### Build

```bash
./gradlew clean build
```

The JAR file will be generated in `build/libs/com.todo.list-1.0-SNAPSHOT.jar`.

### Manual Installation

If you compiled the code manually, follow these steps to install it:

1. Copy the generated JAR to `/usr/local/bin`:

```bash
sudo cp build/libs/com.todo.list-*.jar /usr/local/bin/todo.jar
```

2. Create an execution script or use one that already exists in this repository:

```bash
### Create the script file
cat > todo << 'EOF' 
#!/bin/sh
java --enable-native-access=ALL-UNNAMED -jar /usr/local/bin/todo.jar  "$@"
```

### Give execution permission
```bash
chmod +x todo
```

### Move to PATH
```bash
sudo mv todo /usr/local/bin/
```


### Available Commands

- `todo add <name>` - Add a new task
- `todo ls [id]` - List all tasks or a specific one by ID
- `todo update <id> [--name <new_name>] [--done|--no-done] [--deny|--no-deny]` - Update a task
- `todo rm <id>` - Remove a task

### Usage Examples

```bash
# Add a new task
todo add "Drink water"

# List all tasks
todo ls

# List a specific task
todo ls 1

# Mark a task as completed
todo update 1 --done

# Mark a task as pending
todo update 1 --deny

# Update a task name
todo update 1 --name "Drink more water"

# Remove a task
todo rm 1
```

## Data storage

The application stores your tasks in a SQLite database located at:

```
~/.todo/todo.db
```

Where `~` represents the home directory of your user. This is a standard location for storing application data on Unix-like systems (Linux and macOS).
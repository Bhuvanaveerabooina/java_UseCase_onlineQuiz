# Online Quiz & Assessment System

An interactive quiz system built with Core Java and Swing GUI.

## Features

- **Admin Panel**: Create multiple mocks, add and view questions for each mock
- **Mock Selection**: Users can select which mock/test to attempt
- **User Quiz**: Take timed multiple-choice quizzes
- **Auto Scoring**: Automatic score calculation
- **Result Summary**: Performance analysis with percentage and feedback
- **Timer**: 5-minute timer for quizzes
- **Navigation**: Easy switching between roles and mock tests

## How to Run

1. Compile the project:
   ```
   javac -d . src/main/java/com/example/quiz/*.java
   ```

2. Run the launcher:
   ```
   java -cp . com.example.quiz.Launcher
   ```

3. Select role: Admin or User

### Admin
- Create/Select a mock (e.g., "Java Basics", "Python Advanced", "DSA")
- Add questions with text, options, and correct answer index
- View existing questions in the selected mock
- Each mock is stored in a separate file (mock_<name>.txt)

### User
- Select from available mocks to attempt
- Take the quiz with 5-minute timer
- View results after completion with score, percentage, and performance level

## Project Structure

- `src/main/java/com/example/quiz/`: Source code
  - `Question.java`: Question model
  - `Quiz.java`: Quiz logic with timer
  - `QuizData.java`: Data persistence using text files
  - `Launcher.java`: Role selection
  - `AdminMain.java`: Admin GUI with mock management
  - `Main.java`: User quiz GUI
  - `MockSelection.java`: Mock/test selection interface

## Technologies Used

- Core Java (OOP concepts)
- Swing for GUI
- File handling for data storage (one file per mock)
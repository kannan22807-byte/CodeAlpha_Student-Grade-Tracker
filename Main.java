import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Entry point for the Student Grade Tracker application.
 */
public class Main {
    private static final String DATA_FILE = "students.txt";
    private static final Scanner scanner = new Scanner(System.in);
    private static final StudentManager manager = new StudentManager();

    public static void main(String[] args) {
        boolean running = true;
        while (running) {
            printMenu();
            int choice = readMenuChoice();
            switch (choice) {
                case 1 -> addStudent();
                case 2 -> viewAllStudents();
                case 3 -> searchStudentById();
                case 4 -> displayStatistics();
                case 5 -> saveRecordsToFile();
                case 6 -> loadRecordsFromFile();
                case 7 -> {
                    System.out.println("Exiting application. Goodbye!");
                    running = false;
                }
                default -> System.out.println("Please choose a valid option from 1 to 7.");
            }
            System.out.println();
        }
        scanner.close();
    }

    private static void printMenu() {
        System.out.println("=== Student Grade Tracker ===");
        System.out.println("1. Add Student");
        System.out.println("2. View All Students");
        System.out.println("3. Search Student by ID");
        System.out.println("4. Display Statistics");
        System.out.println("5. Save Records to File");
        System.out.println("6. Load Records from File");
        System.out.println("7. Exit");
        System.out.print("Choose an option: ");
    }

    private static int readMenuChoice() {
        try {
            int choice = Integer.parseInt(scanner.nextLine().trim());
            return choice;
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static void addStudent() {
        System.out.println("--- Add Student ---");
        try {
            System.out.print("Student ID: ");
            String id = scanner.nextLine().trim();
            if (id.isEmpty()) {
                System.out.println("ID cannot be empty.");
                return;
            }
            if (!manager.isStudentIdUnique(id)) {
                System.out.println("A student with this ID already exists.");
                return;
            }

            System.out.print("Student Name: ");
            String name = scanner.nextLine().trim();
            if (name.isEmpty()) {
                System.out.println("Name cannot be empty.");
                return;
            }

            int mark1 = readMark("Subject 1 mark");
            int mark2 = readMark("Subject 2 mark");
            int mark3 = readMark("Subject 3 mark");
            if (mark1 < 0 || mark2 < 0 || mark3 < 0) {
                return;
            }

            Student student = new Student(id, name, mark1, mark2, mark3);
            manager.addStudent(student);
            System.out.println("Student added successfully.");
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter correct values.");
            scanner.nextLine();
        }
    }

    private static int readMark(String prompt) {
        System.out.print(prompt + " (0-100): ");
        String line = scanner.nextLine().trim();
        try {
            int mark = Integer.parseInt(line);
            if (mark < 0 || mark > 100) {
                System.out.println("Mark must be between 0 and 100.");
                return -1;
            }
            return mark;
        } catch (NumberFormatException e) {
            System.out.println("Invalid number format.");
            return -1;
        }
    }

    private static void viewAllStudents() {
        System.out.println("--- All Students ---");
        if (manager.getStudents().isEmpty()) {
            System.out.println("No student records available.");
            return;
        }
        System.out.printf("%-10s %-20s %-8s %-8s %-8s %-8s %-6s%n", "ID", "Name", "Mark1", "Mark2", "Mark3", "Average", "Grade");
        for (Student student : manager.getStudents()) {
            System.out.printf(
                    "%-10s %-20s %-8d %-8d %-8d %-8.2f %-6s%n",
                    student.getId(),
                    student.getName(),
                    student.getMarks()[0],
                    student.getMarks()[1],
                    student.getMarks()[2],
                    student.getAverage(),
                    student.getGrade());
        }
    }

    private static void searchStudentById() {
        System.out.println("--- Search Student by ID ---");
        System.out.print("Enter student ID: ");
        String id = scanner.nextLine().trim();
        if (id.isEmpty()) {
            System.out.println("ID cannot be empty.");
            return;
        }
        Student student = manager.findStudentById(id);
        if (student == null) {
            System.out.println("Student not found.");
            return;
        }
        System.out.printf("ID: %s%nName: %s%nMarks: %d, %d, %d%nAverage: %.2f%nGrade: %s%n",
                student.getId(),
                student.getName(),
                student.getMarks()[0],
                student.getMarks()[1],
                student.getMarks()[2],
                student.getAverage(),
                student.getGrade());
    }

    private static void displayStatistics() {
        System.out.println("--- Class Statistics ---");
        if (manager.getStudents().isEmpty()) {
            System.out.println("No student records available to calculate statistics.");
            return;
        }
        System.out.printf("Highest average score: %.2f%n", manager.getHighestAverage());
        System.out.printf("Lowest average score: %.2f%n", manager.getLowestAverage());
        System.out.printf("Overall class average: %.2f%n", manager.getClassAverage());
    }

    private static void saveRecordsToFile() {
        try {
            manager.saveToFile(DATA_FILE);
            System.out.println("Records saved to " + DATA_FILE);
        } catch (IOException e) {
            System.out.println("Unable to save records: " + e.getMessage());
        }
    }

    private static void loadRecordsFromFile() {
        try {
            manager.loadFromFile(DATA_FILE);
            System.out.println("Records loaded from " + DATA_FILE);
        } catch (IOException e) {
            System.out.println("Unable to load records: " + e.getMessage());
        }
    }
}

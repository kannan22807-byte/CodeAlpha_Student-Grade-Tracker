import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages a list of students and provides methods for statistics and file operations.
 */
public class StudentManager {
    private List<Student> students;

    public StudentManager() {
        this.students = new ArrayList<>();
    }

    public void addStudent(Student student) {
        students.add(student);
    }

    public List<Student> getStudents() {
        return new ArrayList<>(students);
    }

    public Student findStudentById(String id) {
        for (Student student : students) {
            if (student.getId().equalsIgnoreCase(id)) {
                return student;
            }
        }
        return null;
    }

    public double getClassAverage() {
        if (students.isEmpty()) {
            return 0.0;
        }
        double total = 0;
        for (Student student : students) {
            total += student.getAverage();
        }
        return total / students.size();
    }

    public double getHighestAverage() {
        if (students.isEmpty()) {
            return 0.0;
        }
        double highest = Double.MIN_VALUE;
        for (Student student : students) {
            highest = Math.max(highest, student.getAverage());
        }
        return highest;
    }

    public double getLowestAverage() {
        if (students.isEmpty()) {
            return 0.0;
        }
        double lowest = Double.MAX_VALUE;
        for (Student student : students) {
            lowest = Math.min(lowest, student.getAverage());
        }
        return lowest;
    }

    public void saveToFile(String fileName) throws IOException {
        File file = new File(fileName);
        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
            for (Student student : students) {
                writer.println(student.toCsvString());
            }
        }
    }

    public void loadFromFile(String fileName) throws IOException {
        File file = new File(fileName);
        if (!file.exists()) {
            throw new IOException("File not found: " + fileName);
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            students.clear();
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                try {
                    Student student = Student.fromCsvString(line);
                    students.add(student);
                } catch (IllegalArgumentException e) {
                    System.err.println("Skipping invalid record: " + line);
                }
            }
        }
    }

    public boolean isStudentIdUnique(String id) {
        return findStudentById(id) == null;
    }
}

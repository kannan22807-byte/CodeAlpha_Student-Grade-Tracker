import java.util.Arrays;

/**
 * Represents a student record with ID, name, and marks for three subjects.
 */
public class Student {
    private String id;
    private String name;
    private int[] marks;

    public Student(String id, String name, int mark1, int mark2, int mark3) {
        this.id = id;
        this.name = name;
        this.marks = new int[] {mark1, mark2, mark3};
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int[] getMarks() {
        return Arrays.copyOf(marks, marks.length);
    }

    public int getTotal() {
        int total = 0;
        for (int mark : marks) {
            total += mark;
        }
        return total;
    }

    public double getAverage() {
        return getTotal() / (double) marks.length;
    }

    public String getGrade() {
        double average = getAverage();
        if (average >= 90) {
            return "A";
        } else if (average >= 80) {
            return "B";
        } else if (average >= 70) {
            return "C";
        } else if (average >= 60) {
            return "D";
        }
        return "F";
    }

    public String toCsvString() {
        return String.format("%s,%s,%d,%d,%d", id, name, marks[0], marks[1], marks[2]);
    }

    public static Student fromCsvString(String csvLine) throws IllegalArgumentException {
        String[] parts = csvLine.split(",");
        if (parts.length != 5) {
            throw new IllegalArgumentException("Invalid record format: " + csvLine);
        }
        String id = parts[0].trim();
        String name = parts[1].trim();
        int mark1 = Integer.parseInt(parts[2].trim());
        int mark2 = Integer.parseInt(parts[3].trim());
        int mark3 = Integer.parseInt(parts[4].trim());
        return new Student(id, name, mark1, mark2, mark3);
    }
}

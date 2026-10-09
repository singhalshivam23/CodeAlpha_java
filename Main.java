import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    static class Student {
        private final String name;
        private final String rollNumber;
        private final ArrayList<Double> marks;

        Student(String name, String rollNumber, ArrayList<Double> marks) {
            this.name = name;
            this.rollNumber = rollNumber;
            this.marks = marks;
        }

        String getName() { return name; }
        String getRollNumber() { return rollNumber; }
        ArrayList<Double> getMarks() { return marks; }

        double average() {
            if (marks.isEmpty()) return 0;
            double total = 0;
            for (double mark : marks) total += mark;
            return total / marks.size();
        }

        double highest() {
            double result = marks.get(0);
            for (double mark : marks) if (mark > result) result = mark;
            return result;
        }

        double lowest() {
            double result = marks.get(0);
            for (double mark : marks) if (mark < result) result = mark;
            return result;
        }

        String grade() {
            double avg = average();
            if (avg >= 90) return "A+";
            if (avg >= 80) return "A";
            if (avg >= 70) return "B";
            if (avg >= 60) return "C";
            if (avg >= 50) return "D";
            return "F";
        }
    }

    static final Scanner sc = new Scanner(System.in);
    static final ArrayList<Student> students = new ArrayList<>();

    public static void main(String[] args) {
        while (true) {
            System.out.println("\\n=== STUDENT GRADE TRACKER ===");
            System.out.println("1. Add student");
            System.out.println("2. View all student reports");
            System.out.println("3. Search student by roll number");
            System.out.println("4. Exit");
            System.out.print("Choose an option: ");
            int choice = readInt();

            switch (choice) {
                case 1 -> addStudent();
                case 2 -> showAll();
                case 3 -> searchStudent();
                case 4 -> {
                    System.out.println("Thank you for using Student Grade Tracker.");
                    return;
                }
                default -> System.out.println("Invalid option. Choose 1-4.");
            }
        }
    }

    static void addStudent() {
        System.out.print("Student name: ");
        String name = sc.nextLine().trim();
        System.out.print("Roll number: ");
        String roll = sc.nextLine().trim();

        if (name.isEmpty() || roll.isEmpty()) {
            System.out.println("Name and roll number cannot be empty.");
            return;
        }
        for (Student s : students) {
            if (s.getRollNumber().equalsIgnoreCase(roll)) {
                System.out.println("A student with this roll number already exists.");
                return;
            }
        }

        int count;
        do {
            System.out.print("Number of subjects (1-10): ");
            count = readInt();
        } while (count < 1 || count > 10);

        ArrayList<Double> marks = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            double mark;
            do {
                System.out.print("Marks for subject " + i + " (0-100): ");
                mark = readDouble();
                if (mark < 0 || mark > 100) System.out.println("Enter marks between 0 and 100.");
            } while (mark < 0 || mark > 100);
            marks.add(mark);
        }

        students.add(new Student(name, roll, marks));
        System.out.println("Student added successfully.");
    }

    static void showAll() {
        if (students.isEmpty()) {
            System.out.println("No student records yet.");
            return;
        }
        System.out.println("\\n--- STUDENT SUMMARY REPORT ---");
        for (Student s : students) printReport(s);
    }

    static void searchStudent() {
        System.out.print("Enter roll number: ");
        String roll = sc.nextLine().trim();
        for (Student s : students) {
            if (s.getRollNumber().equalsIgnoreCase(roll)) {
                printReport(s);
                return;
            }
        }
        System.out.println("Student not found.");
    }

    static void printReport(Student s) {
        System.out.println("\\nName: " + s.getName());
        System.out.println("Roll number: " + s.getRollNumber());
        System.out.println("Marks: " + s.getMarks());
        System.out.printf("Average: %.2f%n", s.average());
        System.out.printf("Highest: %.2f | Lowest: %.2f%n", s.highest(), s.lowest());
        System.out.println("Grade: " + s.grade());
    }

    static int readInt() {
        while (!sc.hasNextInt()) {
            System.out.print("Please enter a whole number: ");
            sc.next();
        }
        int value = sc.nextInt();
        sc.nextLine();
        return value;
    }

    static double readDouble() {
        while (!sc.hasNextDouble()) {
            System.out.print("Please enter a valid number: ");
            sc.next();
        }
        double value = sc.nextDouble();
        sc.nextLine();
        return value;
    }
}

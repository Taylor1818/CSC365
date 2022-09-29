import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class schoolsearch {
    public static void main(String[] args) throws Exception {
        beginPrompt(read("/Users/Taylor/Documents/GitHub/CSC365/Lab1/src/students.txt"));
    }

    private static List<Student> read(String file) {
        try {
            Scanner inputFile = new Scanner(new File(file));
            List<Student> students = new ArrayList<>();

            while (inputFile.hasNext()) {
                String line = inputFile.nextLine();
                String[] parts = line.split(",");
                students.add(new Student(parts[0], parts[1], Integer.parseInt(parts[2]), Integer.parseInt(parts[3]),
                        Integer.parseInt(parts[4]), Float.parseFloat(parts[5]), parts[6], parts[7]));
            }
            inputFile.close();

            return students;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(0);
            return null;
        }
    }

    private static void beginPrompt(List<Student> allStudents) {
        Scanner keyboard = new Scanner(System.in);
        boolean cont = true;
        while (cont) {
            System.out.println("Enter a query: ");
            cont = analyzeQuery(keyboard.nextLine().split(" "), allStudents);
        }
    }

    private static boolean analyzeQuery(String[] query, List<Student> allStudents) {
        try {
            if (query.length > 0 && query.length < 4) {
                if (query[0].equalsIgnoreCase("S:") || query[0].equalsIgnoreCase("Student:")) {
                    student(allStudents, query[1], query.length == 3 ? query[2] : null);
                } else if (query[0].equalsIgnoreCase("T:") || query[0].equalsIgnoreCase("Teacher:")) {
                    teacher(allStudents, query[1]);
                } else if (query[0].equalsIgnoreCase("B:") || query[0].equalsIgnoreCase("Bus:")) {
                    bus(allStudents, query[1]);
                } else if (query[0].equalsIgnoreCase("G:") || query[0].equalsIgnoreCase("Grade:")) {
                    grade(allStudents, Integer.parseInt(query[1]), query.length == 3 ? query[2] : null);
                } else if (query[0].equalsIgnoreCase("A:") || query[0].equalsIgnoreCase("Average:")) {
                    average(allStudents, Integer.parseInt(query[1]));
                } else if (query[0].equalsIgnoreCase("I") || query[0].equalsIgnoreCase("Info")) {
                    info(allStudents);
                } else if (query[0].equalsIgnoreCase("Q") || query[0].equalsIgnoreCase("Quit")) {
                    System.out.println("Exiting...");
                    return false;
                } else
                    System.out.println("Invalid Query, Please Re-Enter.");
            } else
                System.out.println("Invalid Query, Please Re-Enter.");
        } catch (Exception e) {
            System.out.println("Invalid Query, Please Re-Enter.");
        }

        return true;
    }

    private static void student(List<Student> allStudents, String lastname, String bus) {
        if (bus != null) {
            if ((bus.equalsIgnoreCase("B") || bus.equalsIgnoreCase("Bus"))) {
                allStudents.stream().filter(student -> student.getStLastName().equalsIgnoreCase(lastname))
                        .forEach(student -> System.out.println(student.getStLastName()
                                + " " + student.getStFirstName() + " " + student.getGrade() + " "
                                + student.getClassroom()
                                + " " + student.gettLastName() + " " + student.gettFirstName()));
            } else
                throw new IllegalArgumentException();
        } else {
            allStudents.stream().filter(student -> student.getStLastName().equalsIgnoreCase(lastname))
                    .forEach(student -> System.out.println(student.getStLastName()
                            + " " + student.getStFirstName() + " " + student.getBus()));
        }
    }

    private static void teacher(List<Student> allStudents, String teacherLastname) {
        allStudents.stream().filter(student -> student.gettLastName().equalsIgnoreCase(teacherLastname))
                .forEach(student -> System.out.println(student.getStLastName() + " " + student.getStFirstName()));
    }

    private static void bus(List<Student> allStudents, String bus) {
        allStudents.stream().filter(student -> student.getBus() == Integer.parseInt(bus))
                .forEach(student -> System.out.println(student.getStLastName() + " " + student.getStFirstName() + " "
                        + student.getGrade() + " " + student.getClassroom()));
    }

    private static void grade(List<Student> allStudents, int grade, String specifier) {
        List<Student> gradeTargets = allStudents.stream().filter(student -> student.getGrade() == grade)
                .collect(Collectors.toList());
        if (specifier == null) {
            gradeTargets
                    .forEach(student -> System.out.println(student.getStLastName() + " " + student.getStFirstName()));
        } else {
            Student target = null;
            if (specifier.equalsIgnoreCase("H") || specifier.equalsIgnoreCase("High")) {
                target = Collections.max(gradeTargets, Comparator.comparing(Student::getGpa));
            } else if (specifier.equalsIgnoreCase("L") || specifier.equalsIgnoreCase("Low")) {
                target = Collections.min(gradeTargets, Comparator.comparing(Student::getGpa));
            } else
                throw new IllegalArgumentException();

            if (target != null)
                System.out.println(target.getStLastName() + " " + target.getStFirstName() + " " + target.getGpa() + " "
                        + target.gettLastName() + " " + target.gettFirstName() + " " + target.getBus());
        }
    }

    private static void average(List<Student> allStudents, int gradeTarget) {
        System.out.println(gradeTarget + " " + allStudents.stream().filter(student -> student.getGrade() == gradeTarget)
                .mapToDouble(Student::getGpa).average());
    }

    private static void info(List<Student> allStudents) {
        int[] counts = new int[7];
        allStudents.forEach(student -> counts[student.getGrade()]++);
        for (int i = 0; i < counts.length; i++)
            System.out.println(i + ": " + counts[i]);
    }

}

class Student {
    private String StLastName;
    private String StFirstName;
    private int Grade;
    private int Classroom;
    private int Bus;
    private Float GPA;
    private String TLastName;
    private String TFirstName;

    Student(String StLastName, String StFirstName, int Grade,
            int Classroom, int Bus, Float GPA, String TLastName, String TFirstName) {
        this.StLastName = StLastName;
        this.StFirstName = StFirstName;
        this.Grade = Grade;
        this.Classroom = Classroom;
        this.Bus = Bus;
        this.GPA = GPA;
        this.TLastName = TLastName;
        this.TFirstName = TFirstName;
    }

    public String getStLastName() {
        return StLastName;
    }

    public String getStFirstName() {
        return StFirstName;
    }

    public int getGrade() {
        return Grade;
    }

    public int getClassroom() {
        return Classroom;
    }

    public int getBus() {
        return Bus;
    }

    public float getGpa() {
        return GPA;
    }

    public String gettLastName() {
        return TLastName;
    }

    public String gettFirstName() {
        return TFirstName;
    }

    @Override
    public String toString() {
        return "Lastname: " + this.StLastName + ", Firstname: " + this.StFirstName + ", Grade: " + this.Grade
                + ", Classroom: " + this.Classroom + ", Bus: " + this.Bus + ", GPA: " + this.GPA +
                ", tLastName: " + TLastName + ", tFirstName: " + this.TFirstName;
    }
}

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

    private static void beginPrompt(List<Student> students) {
        Scanner input = new Scanner(System.in);
        boolean cont = true;
        while (cont) {
            System.out.print("Enter a command: ");
            cont = analyzecommand(input.nextLine().split(" "), students);
        }
    }

    private static boolean analyzecommand(String[] command, List<Student> students) {
        try {
            if (command.length > 0 && command.length < 4) {
                if (command[0].equals("S:") || command[0].equals("Student:")) {
                    student(students, command[1], command.length == 3 ? command[2] : null);
                } else if (command[0].equals("T:") || command[0].equals("Teacher:")) {
                    teacher(students, command[1]);
                } else if (command[0].equals("B:") || command[0].equals("Bus:")) {
                    bus(students, command[1]);
                } else if (command[0].equals("G:") || command[0].equals("Grade:")) {
                    grade(students, Integer.parseInt(command[1]), command.length == 3 ? command[2] : null);
                } else if (command[0].equals("A:") || command[0].equals("Average:")) {
                    average(students, Integer.parseInt(command[1]));
                } else if (command[0].equals("I") || command[0].equals("Info")) {
                    info(students);
                } else if (command[0].equals("Q") || command[0].equals("Quit")) {
                    System.out.println("Exiting...");
                    return false;
                } else
                    System.out.println("Please enter a valid input.");
            } else
                System.out.println("Please enter a valid input.");
        } catch (Exception e) {
            System.out.println("Please enter a valid input.");
        }

        return true;
    }

    private static void student(List<Student> students, String lastname, String bus) {
        if (bus != null) {
            if ((bus.equals("B") || bus.equals("Bus"))) {
                students.stream().filter(student -> student.getStLastName().equals(lastname))
                    .forEach(student -> System.out.println(student.getStLastName()
                            + " " + student.getStFirstName() + " " + student.getBus()));
            } else
                throw new IllegalArgumentException();
        } else {
            students.stream().filter(student -> student.getStLastName().equals(lastname))
                        .forEach(student -> System.out.println(student.getStLastName()
                                + " " + student.getStFirstName() + " " + student.getGrade() + " "
                                + student.getClassroom()
                                + " " + student.gettLastName() + " " + student.gettFirstName()));
        }
    }

    private static void teacher(List<Student> students, String teacherLastname) {
        students.stream().filter(student -> student.gettLastName().equals(teacherLastname))
                .forEach(student -> System.out.println(student.getStLastName() + " " + student.getStFirstName()));
    }

    private static void bus(List<Student> students, String bus) {
        students.stream().filter(student -> student.getBus() == Integer.parseInt(bus))
                .forEach(student -> System.out.println(student.getStLastName() + " " + student.getStFirstName() + " "
                        + student.getGrade() + " " + student.getClassroom()));
    }

    private static void grade(List<Student> students, int grade, String specifier) {
        List<Student> gradeTargets = students.stream().filter(student -> student.getGrade() == grade)
                .collect(Collectors.toList());
        if (specifier == null) {
            gradeTargets
                    .forEach(student -> System.out.println(student.getStLastName() + " " + student.getStFirstName()));
        } else {
            Student target = null;
            if (specifier.equals("H") || specifier.equals("High")) {
                target = Collections.max(gradeTargets, Comparator.comparing(Student::getGpa));
            } else if (specifier.equals("L") || specifier.equals("Low")) {
                target = Collections.min(gradeTargets, Comparator.comparing(Student::getGpa));
            } else
                throw new IllegalArgumentException();

            if (target != null)
                System.out.println(target.getStLastName() + " " + target.getStFirstName() + " " + target.getGpa() + " "
                        + target.gettLastName() + " " + target.gettFirstName() + " " + target.getBus());
        }
    }

    private static void average(List<Student> students, int gradeTarget) {
        System.out.println(gradeTarget + " " + students.stream().filter(student -> student.getGrade() == gradeTarget)
                .mapToDouble(Student::getGpa).average());
    }

    private static void info(List<Student> students) {
        int[] counts = new int[7];
        students.forEach(student -> counts[student.getGrade()]++);
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
}

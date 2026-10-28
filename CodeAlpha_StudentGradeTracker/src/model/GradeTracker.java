package model;

import java.util.ArrayList;
import java.util.List;

public class GradeTracker
{
  private final List<Student> students = new ArrayList<>();

  public void addStudent(Student student)
  {
    if (student == null)
    {
      throw new IllegalArgumentException("Student cannot be null");
    }

    for (Student existing : students)
    {
      if (existing.getStudentId().equals(student.getStudentId()))
      {
        throw new IllegalArgumentException(
            "Student ID " + student.getStudentId() + " already exists");
      }
    }

    students.add(student);
  }

  public List<Student> getStudents()
  {
    return new ArrayList<>(students);
  }

  public double getAverageScore()
  {
    ensureStudentsExist();
    return students.stream()
        .mapToDouble(Student::getScore)
        .average()
        .orElseThrow();
  }

  public double getHighestScore()
  {
    ensureStudentsExist();
    return students.stream()
        .mapToDouble(Student::getScore)
        .max()
        .orElseThrow();
  }

  public double getLowestScore()
  {
    ensureStudentsExist();
    return students.stream()
        .mapToDouble(Student::getScore)
        .min()
        .orElseThrow();
  }

  private void ensureStudentsExist()
  {
    if (students.isEmpty())
    {
      throw new IllegalStateException("No student scores are available");
    }
  }
}
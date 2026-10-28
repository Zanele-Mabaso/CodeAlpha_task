package model;

public class Student
{
  //Fields for student name and score
  private String name;
  private double score;
  private String studentId;

  //Constructor calls the two setters so validation always runs
  public Student(String name, double score,String studentId)
  {
    setName(name);      
    setScore(score);
    setStudentId(studentId);

  }

  //Getters to return the name and score of the student
  public String getName()
  {
    return name;
  }

  public double getScore()
  {
    return score;
  }
  public String getStudentId()
  {
     return studentId;
  }

  //Setters with validation
  public void setName(String name)
  {
    if (name == null || name.isBlank())      
    {
      throw new IllegalArgumentException("Name cannot be null or empty");
    }
    String trimmedName = name.trim();
    if (!trimmedName.matches("[\\p{L}]+(?:[ '-][\\p{L}]+)*"))
    {
      throw new IllegalArgumentException(
          "Name can contain letters, spaces, hyphens, and apostrophes only");
    }
    this.name = trimmedName;
  }

  public void setScore(double score)
  {
    if (score < 0 || score > 100)
    {
      throw new IllegalArgumentException("Score must be between 0 and 100");
    }
    this.score = score;                     
  }
  public void setStudentId(String studentId)
  {
    if (studentId == null || studentId.isBlank())
    {
      throw new IllegalArgumentException("Student ID cannot be null or empty");
    }

    String trimmedId = studentId.trim();
    if (trimmedId.length() != 7)
    {
      throw new IllegalArgumentException("Student ID must be exactly 7 characters");
    }

    this.studentId = trimmedId;
  }
}

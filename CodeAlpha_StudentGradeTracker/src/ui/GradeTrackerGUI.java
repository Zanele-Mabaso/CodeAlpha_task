package ui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.GradeTracker;
import model.Student;
import java.util.List;
import java.util.Locale;

public class GradeTrackerGUI extends Application
{
  private final GradeTracker tracker = new GradeTracker();

  private TextField nameField;
  private TextField idField;
  private TextField scoreField;
  private Label messageLabel;
  private TextArea listArea;

  @Override
  public void start(Stage stage)
  {
    nameField = new TextField();
    nameField.setPromptText("Name");

    idField = new TextField();
    idField.setPromptText("Student ID (7 characters)");

    scoreField = new TextField();
    scoreField.setPromptText("Score (0-100)");

    Button addButton = new Button("Add student");
    addButton.setStyle(
        "-fx-background-color: white; " +
        "-fx-text-fill: #0077A8; " +
        "-fx-font-weight: bold; " +
        "-fx-background-radius: 6; " +
        "-fx-padding: 8 16;");
    addButton.setOnAction(e -> handleAdd());

    messageLabel = new Label();
    messageLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");

    listArea = new TextArea();
    listArea.setEditable(false);
    listArea.setText("No students added yet.");
    listArea.setStyle(
        "-fx-control-inner-background: white; " +
        "-fx-text-fill: #163B4D; " +
        "-fx-background-radius: 6;");

    String fieldStyle =
        "-fx-background-color: white; " +
        "-fx-text-fill: #163B4D; " +
        "-fx-prompt-text-fill: #6B8793; " +
        "-fx-background-radius: 6;";
    nameField.setStyle(fieldStyle);
    idField.setStyle(fieldStyle);
    scoreField.setStyle(fieldStyle);

    VBox root = new VBox(10, nameField, idField, scoreField,
                         addButton, messageLabel, listArea);
    root.setPadding(new Insets(15));
    root.setStyle("-fx-background-color: #00BFFF;");

    stage.setScene(new Scene(root, 400, 450));
    stage.setTitle("Student Grade Tracker");
    stage.show();
  }

  private void handleAdd()
  {
    try
    {
      String name = nameField.getText();
      String id = idField.getText();
      double score = Double.parseDouble(scoreField.getText().trim());

      Student student = new Student(name, score, id);
      tracker.addStudent(student);

      messageLabel.setText("Added " + student.getName());
      clearFields();
      refreshList();
    }
    catch (NumberFormatException ex)
    {
      messageLabel.setText("Score must be a number, for example 85 or 72.5");
    }
    catch (IllegalArgumentException ex)
    {
      messageLabel.setText(ex.getMessage());
    }
  }

  private void clearFields()
  {
    nameField.clear();
    idField.clear();
    scoreField.clear();
    nameField.requestFocus();
  }

  private void refreshList()
  {
    StringBuilder sb = new StringBuilder();
    List<Student> students = tracker.getStudents();

    if (students.isEmpty())
    {
      listArea.setText("No students added yet.");
      return;
    }

    sb.append("Student grades\n");
    for (Student s : students)
    {
      sb.append(s.getStudentId())
        .append("   ")
        .append(s.getName())
        .append("   ")
        .append(String.format(Locale.ROOT, "%.2f", s.getScore()))
        .append("\n");
    }

    sb.append("\nSummary\n")
      .append("Students: ")
      .append(students.size())
      .append("\nAverage score: ")
      .append(String.format(Locale.ROOT, "%.2f", tracker.getAverageScore()))
      .append("\nHighest score: ")
      .append(String.format(Locale.ROOT, "%.2f", tracker.getHighestScore()))
      .append("\nLowest score: ")
      .append(String.format(Locale.ROOT, "%.2f", tracker.getLowestScore()));

    listArea.setText(sb.toString());
  }
}
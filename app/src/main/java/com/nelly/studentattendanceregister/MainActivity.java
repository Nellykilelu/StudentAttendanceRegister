
package com.nelly.studentattendanceregister;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EditText studentNameInput;
    private Button addStudentButton;
    private ListView studentList;
    private TextView attendanceSummary;
    private TextView emptyMessage;

    private final ArrayList<Student> students = new ArrayList<>();
    private StudentAdapter adapter;

    private static final String PRESENT = "Present";
    private static final String ABSENT = "Absent";

    private static final int GREEN = Color.rgb(76, 175, 80);
    private static final int RED = Color.rgb(239, 83, 80);

    private static class Student {
        private final String name;
        private String status = ABSENT;

        Student(String name) {
            this.name = name;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        studentNameInput = findViewById(R.id.studentNameInput);
        addStudentButton = findViewById(R.id.addStudentButton);
        studentList = findViewById(R.id.studentList);
        attendanceSummary = findViewById(R.id.attendanceSummary);
        emptyMessage = findViewById(R.id.emptyMessage);

        adapter = new StudentAdapter();
        studentList.setAdapter(adapter);

        addStudentButton.setOnClickListener(v -> addStudent());

        updateSummary();
    }

    private void addStudent() {
        String name = studentNameInput.getText()
                .toString().trim();

        if (name.isEmpty()) {
            studentNameInput.setError("Enter a student's name");
            Toast.makeText(this,
                    "Please enter a student's name",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        students.add(new Student(name));
        studentNameInput.setText("");

        adapter.notifyDataSetChanged();
        updateSummary();

        Toast.makeText(this,
                "Student added successfully",
                Toast.LENGTH_SHORT).show();
    }

    private void updateSummary() {
        int present = 0;
        int absent = 0;

        for (Student student : students) {
            if (student.status.equals(PRESENT)) {
                present++;
            } else {
                absent++;
            }
        }

        attendanceSummary.setText(
                "Total: " + students.size()
                        + "  |  Present: " + present
                        + "  |  Absent: " + absent
        );

        emptyMessage.setVisibility(
                students.isEmpty() ? View.VISIBLE : View.GONE
        );
    }

    private void updateAttendance(Student student, String status) {
        student.status = status;
        adapter.notifyDataSetChanged();
        updateSummary();
    }

    private int dp(int value) {
        return (int) (value * getResources()
                .getDisplayMetrics().density + 0.5f);
    }

    private class StudentAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return students.size();
        }

        @Override
        public Student getItem(int position) {
            return students.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View row,
                            ViewGroup parent) {

            Student student = getItem(position);

            LinearLayout layout = new LinearLayout(
                    MainActivity.this);
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.setPadding(dp(12), dp(10), dp(12), dp(10));
            layout.setBackgroundColor(Color.parseColor("#1E1E1E"));

            TextView nameView = new TextView(MainActivity.this);
            nameView.setText(student.name);
            nameView.setTextSize(18);
            nameView.setTextColor(Color.WHITE);
            nameView.setTypeface(null, Typeface.BOLD);

            TextView statusView = new TextView(MainActivity.this);
            statusView.setText("Status: " + student.status);
            statusView.setTextSize(14);
            statusView.setPadding(0, dp(4), 0, dp(4));

            int statusColor = student.status.equals(PRESENT)
                    ? GREEN : RED;
            statusView.setTextColor(statusColor);

            LinearLayout buttons = new LinearLayout(
                    MainActivity.this);
            buttons.setOrientation(LinearLayout.HORIZONTAL);
            buttons.setGravity(Gravity.CENTER_VERTICAL);

            Button presentButton = new Button(MainActivity.this);
            presentButton.setText("✓ Present");
            presentButton.setTextSize(12);
            presentButton.setAllCaps(false);
            presentButton.setBackgroundTintList(
                    ColorStateList.valueOf(GREEN));
            presentButton.setTextColor(Color.WHITE);

            Button absentButton = new Button(MainActivity.this);
            absentButton.setText("✗ Absent");
            absentButton.setTextSize(12);
            absentButton.setAllCaps(false);
            absentButton.setBackgroundTintList(
                    ColorStateList.valueOf(RED));
            absentButton.setTextColor(Color.WHITE);

            LinearLayout.LayoutParams buttonParams =
                    new LinearLayout.LayoutParams(
                            0,
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            1);

            buttonParams.setMargins(
                    dp(3), dp(3), dp(3), dp(3));

            buttons.addView(presentButton, buttonParams);
            buttons.addView(absentButton, buttonParams);

            presentButton.setOnClickListener(v ->
                    updateAttendance(student, PRESENT));

            absentButton.setOnClickListener(v ->
                    updateAttendance(student, ABSENT));

            layout.addView(nameView);
            layout.addView(statusView);
            layout.addView(buttons);

            return layout;
        }
    }
}
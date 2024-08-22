package com.example.taskmanager;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ArrayList<TaskModel> arrayTaskList = new ArrayList<>();
    RecyclerTaskAdapter adapter;
    DatePickerDialog datePickerDialog;
    RecyclerView recyclerView;
    View emptyStateLayout;
    Button dateButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        ImageView backBtn = findViewById(R.id.toolbar_back_btn);
        ImageView menuBtn = findViewById(R.id.menu_icon);
        TextView toolBarTitle = findViewById(R.id.toolbar_title);
        FloatingActionButton btnOpenAddTaskDialogBox = findViewById(R.id.addTaskDialogBox);

        recyclerView = findViewById(R.id.recyclerTaskList);
        emptyStateLayout = findViewById(R.id.empty_state_layout);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));


//        arrayTaskList.add(new TaskModel("Task1", "Task Description", "AUG 24 2024", true));
//        arrayTaskList.add(new TaskModel("Task2", "Task Description", "MAY 28 2024", true));
//        arrayTaskList.add(new TaskModel("Task3", "Task Description", "SEPT 21 2024", true));
//        arrayTaskList.add(new TaskModel("Task4", "Task Description", "OCT 21 2024", true));
//        arrayTaskList.add(new TaskModel("Task5", "Task Description", "NOV 21 2024", true));
//        arrayTaskList.add(new TaskModel("Task6", "Task Description", "DEC 21 2024", true));
//        arrayTaskList.add(new TaskModel("Task7", "Task Description", "JAN 21 2024", true));
//        arrayTaskList.add(new TaskModel("Task8", "Task Description", "FEB 21 2024", true));
//        arrayTaskList.add(new TaskModel("Task9", "Task Description", "MAR 21 2024", true));
//        arrayTaskList.add(new TaskModel("Task10", "Task Description", "APR 21 2024", true));
//        arrayTaskList.add(new TaskModel("Task11", "Task Description", "JUN 21 2024", true));
//        arrayTaskList.add(new TaskModel("Task12", "Task Description", "JUL 21 2024", true));

        adapter = new RecyclerTaskAdapter(this, arrayTaskList);
        recyclerView.setAdapter(adapter);

        checkIfEmpty(arrayTaskList);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "You clicked on Back Button", Toast.LENGTH_SHORT).show();
            }
        });

        menuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "You clicked on Menu Button", Toast.LENGTH_SHORT).show();
            }
        });

        toolBarTitle.setText(R.string.app_name);

        btnOpenAddTaskDialogBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(MainActivity.this);
                initDatePicker();
                dialog.setContentView(R.layout.task_add_popup_layout);
                dateButton = dialog.findViewById(R.id.btnDueDatePicker);
                TextView txtPopUpTitle = dialog.findViewById(R.id.txtPopUpTitle);
                EditText edtTitle = dialog.findViewById(R.id.edtPopUpCardTitle);
                EditText edtDescription = dialog.findViewById(R.id.edtPopUpCardDescription);
                CheckBox chkStatus = dialog.findViewById(R.id.chkStatusBox);
                Button btnTaskAction = dialog.findViewById(R.id.btnTaskAction);

                dateButton.setText(getTodayDate());

                dateButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openDatePicker(view);
                    }
                });

                btnTaskAction.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String title = "";
                        String description = "";
                        String dueDate = "";
                        boolean status;
                        if (!edtTitle.getText().toString().isEmpty()) {
                            title = edtTitle.getText().toString();
                        } else {
                            Toast.makeText(MainActivity.this, "Please! Enter Task Title", Toast.LENGTH_SHORT).show();
                        }

                        if (!edtDescription.getText().toString().isEmpty()) {
                            description = edtDescription.getText().toString();
                        } else {
                            Toast.makeText(MainActivity.this, "Please! Enter Task Description", Toast.LENGTH_SHORT).show();
                        }
                        if (!dateButton.getText().toString().isEmpty()) {
                            dueDate = dateButton.getText().toString();
                        } else {
                            Toast.makeText(MainActivity.this, "Please! Enter Due Date", Toast.LENGTH_SHORT).show();
                        }
                        status = chkStatus.isChecked();

                        if (!title.isEmpty() && !description.isEmpty() && !dueDate.isEmpty()) {
                            arrayTaskList.add(new TaskModel(title, description, dueDate, status));
                            adapter.notifyItemInserted(arrayTaskList.size() - 1);
                            recyclerView.scrollToPosition(arrayTaskList.size() - 1);
                            dialog.dismiss();
                            checkIfEmpty(arrayTaskList);
                        }
                    }
                });
                dialog.show();
            }
        });
    }

    public void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = makeDateString(day, month, year);
                dateButton.setText(getTodayDate());
                dateButton.setText(date);
            }
        };
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 10000);
    }

    private String getTodayDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        month = month + 1;
        return makeDateString(day, month, year);
    }

    private String makeDateString(int day, int month, int year) {
        return getMonthFormat(month) + " " + day + " " + year;
    }

    private String getMonthFormat(int month) {
        if (month == 1)
            return "JAN";
        if (month == 2)
            return "FEB";
        if (month == 3)
            return "MAR";
        if (month == 4)
            return "APR";
        if (month == 5)
            return "MAY";
        if (month == 6)
            return "JUN";
        if (month == 7)
            return "JUL";
        if (month == 8)
            return "AUG";
        if (month == 9)
            return "SEPT";
        if (month == 10)
            return "OCT";
        if (month == 11)
            return "NOV";
        if (month == 12)
            return "DEC";
        return "DEC";
    }

    public void openDatePicker(View view) {
        datePickerDialog.show();
    }

    private void checkIfEmpty(ArrayList<TaskModel> items) {
        if (items.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyStateLayout.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyStateLayout.setVisibility(View.GONE);
        }
    }
}
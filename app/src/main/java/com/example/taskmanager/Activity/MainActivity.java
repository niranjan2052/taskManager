package com.example.taskmanager.Activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskmanager.Adapter.RecyclerTaskAdapter;
import com.example.taskmanager.Model.TaskModel;
import com.example.taskmanager.Helper.MyDBHelper;
import com.example.taskmanager.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    ArrayList<TaskModel> arrayTaskList = new ArrayList<>();
    RecyclerTaskAdapter adapter;
    DatePickerDialog datePickerDialog;
    RecyclerView recyclerView;
    View emptyStateLayout;
    Button dateButton;

    Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;


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

        MyDBHelper dbHelper = new MyDBHelper(this);

//        dbHelper.addTask("Title1","Description1", "AUG 28 2024",1);
//        dbHelper.addTask("Title2","Description2", "AUG 28 2025",0);
//        dbHelper.addTask("Title3","Description3", "AUG 28 2026",1);


//        TaskModel model = new TaskModel();
//
//        model.taskId = 9;
//        model.title = "Title1 updated ta";
//        model.description = "Updated Description";
//        model.dueDate = "SEPT 10 2024";
//        model.status = true;
//
//        dbHelper.updateTask(model);


//        dbHelper.deleteTask(2);
//
//        ArrayList<TaskModel> arrTask = dbHelper.fetchTask();
//
//        for (int i = 0; i < arrTask.size(); i++)
//            Log.d("TASK INFO", "TITLE: " + arrTask.get(i).title + " Description " + arrTask.get(i).description + "Due Date " + arrTask.get(i).dueDate + " Status " + arrTask.get(i).status);

        toolbar = findViewById(R.id.toolbar_layout);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigationView);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int itemId = item.getItemId();

                if (itemId == R.id.menu_home) {
                    item.setChecked(true);
//                    Toast.makeText(MainActivity.this, "Home Page Selected", Toast.LENGTH_SHORT).show();
                    drawerLayout.closeDrawers();
                    return true;
                } else if (itemId == R.id.menu_logout) {
//                    item.setChecked(true);
//                    Toast.makeText(MainActivity.this, "LogOut Selected", Toast.LENGTH_SHORT).show();
                    SharedPreferences preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("remember", "false");
                    editor.apply();
                    drawerLayout.closeDrawers();
                    Intent iLogin = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(iLogin);
                    finish();
                    return true;
                } else if (itemId == R.id.menu_exit) {
                    finishAffinity();
                    return true;
                }
                return false;
            }
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

        arrayTaskList = dbHelper.fetchTask();
        adapter = new RecyclerTaskAdapter(this, arrayTaskList);
        recyclerView.setAdapter(adapter);

        checkIfEmpty(arrayTaskList);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        menuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Menu Btn
                drawerLayout.openDrawer(GravityCompat.END);
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
                            int statusInInt;
                            arrayTaskList.add(new TaskModel(title, description, dueDate, status));
                            if (status)
                                statusInInt = 1;
                            else
                                statusInInt = 0;
                            dbHelper.addTask(title, description, dueDate, statusInInt);
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

    public void checkIfEmpty(ArrayList<TaskModel> items) {
        if (items.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyStateLayout.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyStateLayout.setVisibility(View.GONE);
        }
    }
}
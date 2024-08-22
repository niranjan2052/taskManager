package com.example.taskmanager;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class RecyclerTaskAdapter extends RecyclerView.Adapter<RecyclerTaskAdapter.ViewHolder> {

    Context context;
    ArrayList<TaskModel> arrayTasks;
    DatePickerDialog datePickerDialog;
    Button datePickerButton;

    RecyclerTaskAdapter(Context context, ArrayList<TaskModel> arrayTasks) {
        this.context = context;
        this.arrayTasks = arrayTasks;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.recycler_task_row_layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        try {
            SimpleDateFormat dates = new SimpleDateFormat("MMM dd yyyy");
            String CurrentDate = dates.format(new Date());
            String DueDate = arrayTasks.get(position).dueDate;
            Date date1;
            Date date2;
            date1 = dates.parse(CurrentDate);
            date2 = dates.parse(DueDate);
            long difference = Math.abs(date1.getTime() - date2.getTime());
            long differenceDates = difference / (24 * 60 * 60 * 1000);
            String daysDifference = Long.toString(differenceDates);
            holder.daysLeft.setText(String.format("%s Days left", daysDifference));
            holder.txtTitle.setText(arrayTasks.get(position).title);
            holder.dueDate.setText(arrayTasks.get(position).dueDate);
        } catch (Exception exception) {
            Toast.makeText(context, "Unable to Find Difference", Toast.LENGTH_SHORT).show();
        }

        //Code to edit Task
        holder.LLTaskRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.task_add_popup_layout);
                initDatePicker();
                datePickerButton = dialog.findViewById(R.id.btnDueDatePicker);
                TextView txtPopUpTitle = dialog.findViewById(R.id.txtPopUpTitle);
                EditText edtTitle = dialog.findViewById(R.id.edtPopUpCardTitle);
                EditText edtDescription = dialog.findViewById(R.id.edtPopUpCardDescription);
                CheckBox chkStatus = dialog.findViewById(R.id.chkStatusBox);
                Button btnTaskAction = dialog.findViewById(R.id.btnTaskAction);

                txtPopUpTitle.setText("Update Task");
                btnTaskAction.setText("Update Task");

                datePickerButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        datePickerDialog.show();
                    }
                });

                edtTitle.setText(arrayTasks.get(holder.getAdapterPosition()).title);
                edtDescription.setText(arrayTasks.get(holder.getAdapterPosition()).description);
                datePickerButton.setText(arrayTasks.get(holder.getAdapterPosition()).dueDate);
                chkStatus.setChecked(arrayTasks.get(holder.getAdapterPosition()).status);

                //Update Task
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
                            Toast.makeText(context, "Please! Enter Task Title", Toast.LENGTH_SHORT).show();
                        }

                        if (!edtDescription.getText().toString().isEmpty()) {
                            description = edtDescription.getText().toString();
                        } else {
                            Toast.makeText(context, "Please! Enter Task Description", Toast.LENGTH_SHORT).show();
                        }
                        if (!datePickerButton.getText().toString().isEmpty()) {
                            dueDate = datePickerButton.getText().toString();
                        } else {
                            Toast.makeText(context, "Please! Enter Due Date", Toast.LENGTH_SHORT).show();
                        }
                        status = chkStatus.isChecked();

                        arrayTasks.set(holder.getAdapterPosition(), new TaskModel(title, description, dueDate, status));
                        notifyItemChanged(holder.getAdapterPosition());
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
        //Code to Delete Task
        holder.LLTaskRow.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context, AlertDialog.THEME_HOLO_LIGHT)
                        .setTitle("Delete Contact")
                        .setMessage("Are you sure want to delete")
                        .setIcon(R.drawable.baseline_delete_24)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                arrayTasks.remove(holder.getAdapterPosition());
                                notifyItemRemoved(holder.getAdapterPosition());
                                MainActivity activity = (MainActivity) context;
                                activity.checkIfEmpty(arrayTasks);
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                builder.show();
                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayTasks.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitle, dueDate, daysLeft;
        View emptyLayout;
        LinearLayout LLTaskRow;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txtRecyclerCardTitle);
            dueDate = itemView.findViewById(R.id.txtRecyclerCardDueDays);
            daysLeft = itemView.findViewById(R.id.txtRecyclerCardDaysLeft);
            emptyLayout = itemView.findViewById(R.id.empty_state_layout);
            LLTaskRow = itemView.findViewById(R.id.LLTaskRow);

        }
    }

//    private void checkIfEmpty(ArrayList<TaskModel> items) {
//        if (items.isEmpty()) {
//            recyclerView.setVisibility(View.GONE);
//            emptyStateLayout.setVisibility(View.VISIBLE);
//        } else {
//            recyclerView.setVisibility(View.VISIBLE);
//            emptyStateLayout.setVisibility(View.GONE);
//        }
//    }

    public void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = makeDateString(day, month, year);
                datePickerButton.setText(getTodayDate());
                datePickerButton.setText(date);
            }
        };
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(context, style, dateSetListener, year, month, day);
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
}

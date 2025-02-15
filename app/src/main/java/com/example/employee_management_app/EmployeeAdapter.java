package com.example.employee_management_app;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.List;

public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.EmployeeViewHolder> {
    private List<Employee> employees;
    private Context context;
    public EmployeeAdapter(List<Employee> employees) {
        this.employees = employees;
    }

    @NonNull
    @Override
    public EmployeeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_employee, parent, false);
        return new EmployeeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EmployeeViewHolder holder, int position) {
        Employee employee = employees.get(position);
        holder.tvName.setText(employee.getName());
        holder.tvEmail.setText(employee.getEmail());
        holder.tvDepartment.setText(employee.getDepartment());
        holder.tvPhone.setText(employee.getPhone());
        holder.itemView.setOnClickListener(v -> {
            showDeleteDialog(employee);
        });
    }
    private void showDeleteDialog(Employee employee) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context, R.style.MaterialAlertDialog_Rounded)
                .setTitle("Delete Employee")
                .setMessage("Are you sure you want to delete " + employee.getName() + "?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    deleteEmployee(employee);
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(dialogInterface -> {
            Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);

            positiveButton.setTextColor(ContextCompat.getColor(context, R.color.red));
            negativeButton.setTextColor(ContextCompat.getColor(context, R.color.gray));
        });

        dialog.show();
    }

    private void deleteEmployee(Employee employee) {
        ApiService.getInstance().deleteEmployee(employee.getId(), new ApiCallback<Void>() {
            @Override
            public void onSuccess(Void result) {
                ((Activity) context).runOnUiThread(() -> {
                    int position = employees.indexOf(employee);
                    employees.remove(position);
                    notifyItemRemoved(position);
                    Toast.makeText(context, "Employee deleted successfully", Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onError(Exception e) {
                ((Activity) context).runOnUiThread(() -> {
                    Toast.makeText(context, "Error deleting employee: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return employees.size();
    }

    static class EmployeeViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvEmail, tvDepartment, tvPhone;

        public EmployeeViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvEmail = itemView.findViewById(R.id.tvEmail);
            tvDepartment = itemView.findViewById(R.id.tvDepartment);
            tvPhone = itemView.findViewById(R.id.tvPhone);
        }
    }
}
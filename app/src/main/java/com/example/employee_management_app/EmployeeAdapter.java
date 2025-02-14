package com.example.employee_management_app;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.EmployeeViewHolder> {
    private List<Employee> employees;

    public EmployeeAdapter(List<Employee> employees) {
        this.employees = employees;
    }

    @NonNull
    @Override
    public EmployeeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
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
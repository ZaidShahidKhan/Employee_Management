package com.example.employee_management_app;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class EmployeeListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private EmployeeAdapter adapter;
    private List<Employee> employees = new ArrayList<>();
    private List<Employee> filteredEmployees = new ArrayList<>();
    private TextView tvCount;
    private EditText etSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_list);

        // Initialize views
        recyclerView = findViewById(R.id.recyclerView);
        tvCount = findViewById(R.id.tvCount);
        etSearch = findViewById(R.id.etSearch);
        FloatingActionButton fabAdd = findViewById(R.id.fabAdd);

        // Setup RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new EmployeeAdapter(filteredEmployees);
        recyclerView.setAdapter(adapter);

        // Setup search functionality
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterEmployees(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Setup FAB click listener
        fabAdd.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddEmployeeActivity.class);
            startActivity(intent);
        });

        // Setup back button
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        // Load employees
        loadEmployees();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadEmployees(); // Refresh list when returning to this screen
    }

    private void loadEmployees() {
        ApiService.getInstance().getAllEmployees(new ApiCallback<List<Employee>>() {
            @Override
            public void onSuccess(List<Employee> result) {
                runOnUiThread(() -> {
                    employees = result;
                    filterEmployees(etSearch.getText().toString());
                });
            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(() -> {
                    Toast.makeText(EmployeeListActivity.this,
                            "Error loading employees: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    private void filterEmployees(String query) {
        filteredEmployees.clear();
        if (query.isEmpty()) {
            filteredEmployees.addAll(employees);
        } else {
            String lowerQuery = query.toLowerCase();
            for (Employee employee : employees) {
                if (employee.getName().toLowerCase().contains(lowerQuery) ||
                        employee.getEmail().toLowerCase().contains(lowerQuery) ||
                        employee.getDepartment().toLowerCase().contains(lowerQuery)) {
                    filteredEmployees.add(employee);
                }
            }
        }
        adapter.notifyDataSetChanged();
        tvCount.setText(filteredEmployees.size() + " Total");
    }
}
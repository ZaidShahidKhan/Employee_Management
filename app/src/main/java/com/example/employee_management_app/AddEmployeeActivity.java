package com.example.employee_management_app;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class AddEmployeeActivity extends AppCompatActivity {
    private EditText etName, etEmail, etPhone;
    private Spinner spinnerDepartment;
    private CardView btnSave;
    private ImageView btnBack;
    private CardView cardProfilePic;
    private boolean isLoading = false;

    // Sample departments - you can modify this list as needed
    private final String[] departments = {
            "Engineering",
            "Human Resources",
            "Marketing",
            "Sales",
            "Finance",
            "Operations"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_employee);


        // Initialize views
        initializeViews();

        // Setup department spinner
        setupDepartmentSpinner();

        // Setup click listeners
        setupClickListeners();
    }

    private void initializeViews() {
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        spinnerDepartment = findViewById(R.id.spinnerDepartment);
        btnSave = findViewById(R.id.btnSave);
        btnBack = findViewById(R.id.btnBack);
        cardProfilePic = findViewById(R.id.cardProfilePic);
    }

    private void setupDepartmentSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                departments
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDepartment.setAdapter(adapter);
    }

    private void setupClickListeners() {
        btnBack.setOnClickListener(v -> finish());

        cardProfilePic.setOnClickListener(v -> {
            // TODO: Implement profile picture selection
            Toast.makeText(this, "Profile picture feature coming soon!", Toast.LENGTH_SHORT).show();
        });

        btnSave.setOnClickListener(v -> {
            if (!isLoading) {
                validateAndSaveEmployee();
            }
        });
    }

    private void validateAndSaveEmployee() {
        // Get values from form
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String department = spinnerDepartment.getSelectedItem().toString();

        // Validate inputs
        if (TextUtils.isEmpty(name)) {
            etName.setError("Name is required");
            return;
        }

        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Email is required");
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Please enter a valid email address");
            return;
        }

        if (TextUtils.isEmpty(phone)) {
            etPhone.setError("Phone number is required");
            return;
        }

        // Create employee object
        Employee employee = new Employee(name, email, phone, department);

        // Show loading state
        setLoadingState(true);

        // Save employee
        ApiService.getInstance().addEmployee(employee, new ApiCallback<Employee>() {
            @Override
            public void onSuccess(Employee result) {
                runOnUiThread(() -> {
                    setLoadingState(false);
                    Toast.makeText(AddEmployeeActivity.this,
                            "Employee added successfully", Toast.LENGTH_SHORT).show();
                    finish();
                });
            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(() -> {
                    setLoadingState(false);
                    Toast.makeText(AddEmployeeActivity.this,
                            "Error adding employee: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    private void setLoadingState(boolean loading) {
        isLoading = loading;
        btnSave.setEnabled(!loading);
        // You could also add a progress indicator here
    }
}
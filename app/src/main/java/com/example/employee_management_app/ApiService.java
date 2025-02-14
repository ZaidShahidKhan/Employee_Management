package com.example.employee_management_app;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ApiService {
    private static final String TAG = "ApiService";
//    private static final String BASE_URL = "https://employee-server-szkq.onrender.com/api";
private static final String BASE_URL = "https://employeeserver-production.up.railway.app/api";
    private static ApiService instance;

    private ApiService() {}

    public static synchronized ApiService getInstance() {
        if (instance == null) {
            instance = new ApiService();
        }
        return instance;
    }

    public void addEmployee(Employee employee, ApiCallback<Employee> callback) {
        new Thread(() -> {
            HttpURLConnection conn = null;
            try {
                URL url = new URL(BASE_URL + "/employees");
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);

                // Convert employee to JSON
                JSONObject jsonEmployee = new JSONObject();
                jsonEmployee.put("name", employee.getName());
                jsonEmployee.put("email", employee.getEmail());
                jsonEmployee.put("phone", employee.getPhone());
                jsonEmployee.put("department", employee.getDepartment());

                String jsonString = jsonEmployee.toString();
                Log.d(TAG, "Sending request to: " + url);
                Log.d(TAG, "Request body: " + jsonString);

                // Send data
                try (OutputStream os = conn.getOutputStream()) {
                    byte[] input = jsonString.getBytes("utf-8");
                    os.write(input, 0, input.length);
                }

                // Read response
                int responseCode = conn.getResponseCode();
                Log.d(TAG, "Response code: " + responseCode);

                if (responseCode == HttpURLConnection.HTTP_CREATED) {
                    BufferedReader br = new BufferedReader(
                            new InputStreamReader(conn.getInputStream(), "utf-8"));
                    StringBuilder response = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }

                    String responseBody = response.toString();
                    Log.d(TAG, "Response body: " + responseBody);

                    JSONObject jsonResponse = new JSONObject(responseBody);
                    employee.setId(jsonResponse.getInt("id"));
                    callback.onSuccess(employee);
                } else {
                    // Read error stream
                    InputStream errorStream = conn.getErrorStream();
                    if (errorStream != null) {
                        BufferedReader br = new BufferedReader(
                                new InputStreamReader(errorStream, "utf-8"));
                        StringBuilder errorResponse = new StringBuilder();
                        String errorLine;
                        while ((errorLine = br.readLine()) != null) {
                            errorResponse.append(errorLine.trim());
                        }
                        Log.e(TAG, "Error response: " + errorResponse.toString());
                    }
                    throw new Exception("Server returned code: " + responseCode);
                }

            } catch (Exception e) {
                Log.e(TAG, "Error in addEmployee", e);
                callback.onError(e);
            } finally {
                if (conn != null) {
                    conn.disconnect();
                }
            }
        }).start();
    }


    // Get All Employees
    public void getAllEmployees(ApiCallback<List<Employee>> callback) {
        new Thread(() -> {
            try {
                URL url = new URL(BASE_URL + "/employees");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                JSONArray jsonArray = new JSONArray(response.toString());
                List<Employee> employees = new ArrayList<>();

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    Employee employee = new Employee();
                    employee.setId(jsonObject.getInt("id"));
                    employee.setName(jsonObject.getString("name"));
                    employee.setEmail(jsonObject.getString("email"));
                    employee.setPhone(jsonObject.getString("phone"));
                    employee.setDepartment(jsonObject.getString("department"));
                    employees.add(employee);
                }

                callback.onSuccess(employees);

            } catch (Exception e) {
                callback.onError(e);
            }
        }).start();
    }
}
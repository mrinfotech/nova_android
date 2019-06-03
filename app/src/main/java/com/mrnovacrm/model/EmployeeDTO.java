package com.mrnovacrm.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by android on 29-03-2018.
 */

public class EmployeeDTO {

    String status;
    String message;
    String uni_code;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<EmployeeListDTO> getEmployeeListDTO() {
        return employeeListDTO;
    }

    public void setEmployeeListDTO(List<EmployeeListDTO> employeeListDTO) {
        this.employeeListDTO = employeeListDTO;
    }

    @SerializedName("employees")
    @Expose
    List<EmployeeListDTO> employeeListDTO;

    public String getUni_code() {
        return uni_code;
    }

    public void setUni_code(String uni_code) {
        this.uni_code = uni_code;
    }
}

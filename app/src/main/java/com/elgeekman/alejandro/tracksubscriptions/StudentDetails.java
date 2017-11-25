package com.elgeekman.alejandro.tracksubscriptions;

/**
 * Created by Juned on 7/25/2017.
 */

public class StudentDetails {


    private String name;
    private String phoneNumber;
    private String color;
    private String moneda;
    private String fecha;
    private String visitas;



    public StudentDetails() {
        // This is default constructor.
    }

    public String getStudentName() {

        return name;
    }

    public void setStudentName(String name) {

        this.name = name;
    }

    public String getStudentPhoneNumber() {

        return phoneNumber;
    }

    public void setStudentPhoneNumber(String phonenumber) {

        this.phoneNumber = phonenumber;
    }

    public String getColor() {

        return color;
    }

    public void setColor(String color) {

        this.color = color;
    }
    public String getMoneda() {

        return moneda;
    }

    public void setMoneda(String moneda) {

        this.moneda = moneda;
    }
    public String getFecha() {

        return fecha;
    }

    public void setFecha(String fecha) {

        this.fecha = fecha;
    }

    public String getVisitas() {

        return visitas;
    }

    public void setVisitas(String fecha) {

        this.visitas = visitas;
    }


}

package edu.uph.ii;

import jakarta.servlet.ServletException;

public class Main {
    public static void main(String[] args) throws ServletException {
        WypiszOsoby app = new WypiszOsoby();
        app.init();
    }
}
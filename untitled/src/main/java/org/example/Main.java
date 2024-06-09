package org.example;

public class Main {
    public static void main(String[] args) {
        String var = "%id:redstone%";
        int id =var.indexOf("%id");
        System.out.println(id, var.indexOf("%", id+1));
    }
}
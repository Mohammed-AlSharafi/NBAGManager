package com.group.NBAGManager.model;

public class Player {

    //used for creating and adding player to db.players
    public Player(int id, String firstName, String lastName, int age, double height, double weight, String position, double points, double rebounds, double assists, double steals, double blocks) {
        this.playerId = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.height = height;
        this.weight = weight;
        this.position = position;
        this.points = points;
        this.rebounds = rebounds;
        this.assists = assists;
        this.steals = steals;
        this.blocks = blocks;
    }

    //used as an enclosure for player(s) coming from db.players
    public Player(String firstName, String lastName, int age, double height, double weight, String position, double points, double rebounds, double assists, double steals, double blocks) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.height = height;
        this.weight = weight;
        this.position = position;
        this.points = points;
        this.rebounds = rebounds;
        this.assists = assists;
        this.steals = steals;
        this.blocks = blocks;
    }

    //used as an enclosure for player(s) coming from db.teams
    public Player(int playerId, String firstName, String lastName, int age, double height, double weight, String position, double salary, double points, double rebounds, double assists, double steals, double blocks) {
        this.playerId = playerId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.height = height;
        this.weight = weight;
        this.position = position;
        Salary = salary;
        this.points = points;
        this.rebounds = rebounds;
        this.assists = assists;
        this.steals = steals;
        this.blocks = blocks;
    }

    private int playerId;
    private String firstName;
    private String lastName;
    private int age;
    private double height;
    private double weight;
    private String position;
    private double Salary;
    private double points;
    private double rebounds;
    private double assists;
    private double steals;
    private double blocks;

    public int getPlayerId() {
        return playerId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public double getSalary() {
        return Salary;
    }

    public void setSalary(double salary) {
        Salary = salary;
    }

    public double getPoints() {
        return points;
    }

    public void setPoints(double points) {
        this.points = points;
    }

    public double getRebounds() {
        return rebounds;
    }

    public void setRebounds(double rebounds) {
        this.rebounds = rebounds;
    }

    public double getAssists() {
        return assists;
    }

    public void setAssists(double assists) {
        this.assists = assists;
    }

    public double getSteals() {
        return steals;
    }

    public void setSteals(double steals) {
        this.steals = steals;
    }

    public double getBlocks() {
        return blocks;
    }

    public void setBlocks(double blocks) {
        this.blocks = blocks;
    }

    @Override
    public String toString() {
        String name = firstName + " " + lastName;

        String sb = String.format("%-25s %-9s %-12d %-12s %-8.2f %n", name, "Age:", age, "Points:", points) +
                String.format("%-25s %-9s %-12.2f %-12s %-8.2f %n", "", "Height:", height, "Rebounds:", rebounds) +
                String.format("%-25s %-9s %-12.2f %-12s %-8.2f %n", "", "Weight:", weight, "Assists:", assists) +
                String.format("%-25s %-9s %-12s %-12s %-8.2f %n", "", "Position:", position, "Steals:", steals) +
                String.format("%-25s %-9s %-12.2f %-12s %-8.2f %n", "", "Salary:", Salary, "Blocks:", blocks);

        return sb;
    }
}

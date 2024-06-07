package com.group.NBAGManager.model;

import java.time.LocalDateTime;

public class Player implements Comparable<Player> {
    //used for creating and adding player to db.players
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
        this.compositeScore = calculatePerformanceScore();
    }

    //used as an enclosure for player(s) coming from db.players
    public Player(int playerId, String firstName, String lastName, int age, double height, double weight,
                  String position, double points, double rebounds, double assists, double steals, double blocks,
                  double compositeScore)
    {
        this.playerId = playerId;
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
        this.compositeScore = compositeScore;
    }

    //used as an enclosure for player(s) coming from db.teams
    public Player(int playerId, String firstName, String lastName, int age, double height, double weight,
                  String position, double salary, double points, double rebounds, double assists, double steals,
                  double blocks, double compositeScore, boolean isInjured, LocalDateTime injuryDateTime,
                  String injuryDescription, boolean isContractRenewQueued) {

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
        this.compositeScore = compositeScore;
        this.isInjured = isInjured;
        this.injuryDateTime = injuryDateTime;
        this.injuryDescription = injuryDescription;
        this.isContractRenewQueued = isContractRenewQueued;
    }

    //instance variables
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
    private double compositeScore;
    private boolean isInjured;
    private LocalDateTime injuryDateTime;
    private String injuryDescription;
    private boolean isContractRenewQueued;

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

    public String getFullName() {
        return firstName + " " + lastName;
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

    public double getCompositeScore() {
        return compositeScore;
    }

    public void setCompositeScore(double compositeScore) {
        this.compositeScore = compositeScore;
    }

    public boolean isInjured() {
        return isInjured;
    }

    public void setInjured(boolean injured) {
        isInjured = injured;
    }

    public LocalDateTime getInjuryDateTime() {
        return injuryDateTime;
    }

    public void setInjuryDateTime(LocalDateTime injuryDateTime) {
        this.injuryDateTime = injuryDateTime;
    }

    public String getInjuryDescription() {
        return injuryDescription;
    }

    public void setInjuryDescription(String injuryDescription) {
        this.injuryDescription = injuryDescription;
    }

    public boolean isContractRenewQueued() {
        return isContractRenewQueued;
    }

    public void setContractRenewQueued(boolean contractRenewQueued) {
        isContractRenewQueued = contractRenewQueued;
    }

    // Method to calculate composite performance score
    public double calculatePerformanceScore() {
        // Define weights for different criteria based on player's position
        double pointsWeight = 1.7;
        double reboundsWeight = (position.equals("Center") || position.equals("Forward")) ? 3 : 1.0;
        double stealsWeight = (position.equals("Guard")) ? 3 : 1.0;
        double assistsWeight = (position.equals("Guard")) ? 3 : 1.0;
        double blocksWeight = (position.equals("Center") || position.equals("Forward")) ? 3 : 1.0;

        // Calculate composite performance score
        return points * pointsWeight + rebounds * reboundsWeight + steals * stealsWeight + assists * assistsWeight + blocks * blocksWeight;
    }

    //temporary toString to debug

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

    @Override
    public int compareTo(Player o) {
        return Integer.compare(playerId, o.getPlayerId());
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Player player){
            return playerId == player.getPlayerId();
        }
        return false;
    }
}

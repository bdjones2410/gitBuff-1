package com.company;

import jodd.json.JsonSerializer;
import spark.ModelAndView;
import spark.Session;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {

    public static void createTables(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.execute("CREATE TABLE IF NOT EXISTS users (id IDENTITY(1,1) , name VARCHAR , " +
                "password VARCHAR , url VARCHAR)");
        stmt.execute("CREATE TABLE IF NOT EXISTS arms (id IDENTITY(1,1) , arm VARCHAR)");
        stmt.execute("CREATE TABLE IF NOT EXISTS legs (id IDENTITY(1,1) , leg VARCHAR)");
        stmt.execute("CREATE TABLE IF NOT EXISTS cardios (id IDENTITY(1,1) , cardio VARCHAR)");
        stmt.execute("CREATE TABLE IF NOT EXISTS cores (id IDENTITY(1,1) , core VARCHAR)");

    }

    public static void insertUser (Connection conn , String name, String password , String url) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO users VALUES (NULL , ? , ? , ?)");
        stmt.setString(1, name);
        stmt.setString(2, password);
        stmt.setString(3 , "https://slack-imgs.com/?url=https%3A%2F%2" + //generic pic url
                "Fwww.placecage.com%2Fc%2F200%2F300&width=200&height=300");
        stmt.execute();
    }

    public static User selectUser (Connection conn , String name) throws SQLException {
        User user = null;
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE name = ?");
        stmt.setString(1, name);
        ResultSet results = stmt.executeQuery();
        if (results.next()){
            user = new User();
            user.id = results.getInt("id");
            user.password = results.getString("password");
        }
        return user;
    }

    public static void insertArm(Connection conn) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO arms VALUES (NULL, ?)");
        stmt.setString(1, "arm1");
        stmt.execute();
        stmt.setString(1, "arm2");
        stmt.execute();
        stmt.setString(1, "arm3");
        stmt.execute();
        stmt.setString(1, "arm4");
        stmt.execute();
        stmt.setString(1, "arm5");
        stmt.execute();
    }

    public static void insertLeg(Connection conn) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO legs VALUES (NULL, ?)");
        stmt.setString(1, "leg1");
        stmt.execute();
        stmt.setString(1, "leg2");
        stmt.execute();
        stmt.setString(1, "leg3");
        stmt.execute();
        stmt.setString(1, "leg4");
        stmt.execute();
        stmt.setString(1, "leg5");
        stmt.execute();
    }

    public static void insertCardio(Connection conn) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO cardios VALUES (NULL, ?)");
        stmt.setString(1, "cardio1");
        stmt.execute();
        stmt.setString(1, "cardio2");
        stmt.execute();
        stmt.setString(1, "cardio3");
        stmt.execute();
        stmt.setString(1, "cardio4");
        stmt.execute();
        stmt.setString(1, "cardio5");
        stmt.execute();
    }

    public static void insertCore(Connection conn) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO cores VALUES (NULL, ?)");
        stmt.setString(1, "core1");
        stmt.execute();
        stmt.setString(1, "core2");
        stmt.execute();
        stmt.setString(1, "core3");
        stmt.execute();
        stmt.setString(1, "core4");
        stmt.execute();
        stmt.setString(1, "core5");
        stmt.execute();
    }

    public static Workout createWorkout(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        ArmWorkout armName = null;
        LegWorkout legName = null;
        CardioWorkout cardioName = null;
        CoreWorkout coreName = null;

        insertArm(conn);

        ResultSet resultsArm = stmt.executeQuery("SELECT * FROM arms ORDER BY RAND() LIMIT 1");
        if(resultsArm.next()) {
            armName = new ArmWorkout();
            armName.id = resultsArm.getInt("id");
            armName.arm = resultsArm.getString("arm");
        }

        insertLeg(conn);

        ResultSet resultsLeg = stmt.executeQuery("SELECT * FROM legs ORDER BY RAND() LIMIT 1");
        if(resultsLeg.next()) {
            legName = new LegWorkout();
            legName.id = resultsLeg.getInt("id");
            legName.leg = resultsLeg.getString("leg");
        }

        insertCardio(conn);

        ResultSet resultsCardio = stmt.executeQuery("SELECT * FROM cardios ORDER BY RAND() LIMIT 1");
        if(resultsCardio.next()) {
            cardioName = new CardioWorkout();
            cardioName.id = resultsCardio.getInt("id");
            cardioName.cardio = resultsCardio.getString("cardio");
        }

        insertCore(conn);

        ResultSet resultsCore = stmt.executeQuery("SELECT * FROM cores ORDER BY RAND() LIMIT 1");
        if(resultsCore.next()) {
            coreName = new CoreWorkout();
            coreName.id = resultsCore.getInt("id");
            coreName.core = resultsCore.getString("core");
        }


        Workout workout = new Workout();
        workout.armWorkout = armName;
        workout.legWorkout = legName;
        workout.cardioWorkout = cardioName;
        workout.coreWorkout = coreName;
        return workout;
    }


    public static void main(String[] args) throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:h2:./main");

        createTables(conn);

        if (selectUser(conn, "alice") == null) {
            insertUser(conn, "alice", "1245", "");
        }


        Spark.externalStaticFileLocation(".");
        Spark.init();


        Spark.post(
                "/login",
                ((request, response) -> {
                    String username = request.queryParams("username");
                    String password = request.queryParams("password");
                    String url = request.queryParams("url");

                    if (username.isEmpty() || password.isEmpty()) {
                        Spark.halt(403);
                    }

                    User user = selectUser(conn, username);
                    if (user == null) {
                        insertUser(conn, username, password, url);
                    }
                    else if (!password.equals(user.password)) {
                        Spark.halt(403);
                    }
                    return "";
                })
        );

        Spark.get("/randomWorkout", (request, response) -> {
            Workout workout = createWorkout(conn);
            JsonSerializer serializer = new JsonSerializer();
            return serializer.serialize(workout);
        });

    }
}

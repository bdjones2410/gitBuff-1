package com.company;

import jodd.json.JsonSerializer;
import spark.Session;
import spark.Spark;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Main {
    static LocalDateTime lastWorkoutTime = null;
    static Workout lastWorkout = null;

    public static void createTables(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.execute("CREATE TABLE IF NOT EXISTS users (id IDENTITY(1,1) , name VARCHAR , " +
                "password VARCHAR , url VARCHAR)");
        stmt.execute("CREATE TABLE IF NOT EXISTS arms (id IDENTITY(1,1) , arm VARCHAR)");
        stmt.execute("CREATE TABLE IF NOT EXISTS legs (id IDENTITY(1,1) , leg VARCHAR)");
        stmt.execute("CREATE TABLE IF NOT EXISTS cardios (id IDENTITY(1,1) , cardio VARCHAR)");
        stmt.execute("CREATE TABLE IF NOT EXISTS cores (id IDENTITY(1,1) , core VARCHAR)");
        stmt.execute("CREATE TABLE IF NOT EXISTS notes (id IDENTITY(1,1) , user_id INT, " +
                "note VARCHAR , note_date TIMESTAMP)");
        stmt.execute("CREATE TABLE IF NOT EXISTS quotes (id IDENTITY (1,1), quote VARCHAR)");
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

    public static void insertNote(Connection conn , int userId , String text , LocalDateTime noteDate) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO notes Values (NULL , ? , ? , ?)");
        stmt.setInt(1, userId);
        stmt.setString(2, text);
        stmt.setTimestamp(3, Timestamp.valueOf(noteDate));
        stmt.execute();
    }

    public static void deleteNote (Connection conn , int id) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM notes WHERE id = ?");
        stmt.setInt(1, id);
        stmt.execute();

    }

    public static void insertQuote(Connection conn) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO quotes VALUES (NULL, ?)");
        stmt.setString(1, "Success is most often achieved by those who don't know that failure is inevitable -- Coco Chanel");
        stmt.execute();
        stmt.setString(1, "Things work out best for those who make the best of how things work out -- John Wooden");
        stmt.execute();
        stmt.setString(1, "Courage is grace under pressure -- Ernest Hemingway");
        stmt.execute();
        stmt.setString(1, "If you are not willing to risk the usual, you will have to settle for the ordinary -- Jim Rohn");
        stmt.execute();
        stmt.setString(1, "Learn from yesterday, live for today, hope for tomorrow. The important thing is not to stop questioning -- Albert Einstein");
        stmt.execute();
        stmt.setString(1, "All our dreams can come true if we have the courage to pursue them -- Walt Disney");
        stmt.execute();
        stmt.setString(1, "It does not matter how slowly you go, so long as you do not stop -- Confucius");
        stmt.execute();
        stmt.setString(1, "Success is walking from failure to failure with no loss of enthusiasm -- Winston Churchill");
        stmt.execute();
        stmt.setString(1, "Someone is sitting in the shade today because someone planted a tree a long time ago -- Warren Buffett");
        stmt.execute();
        stmt.setString(1, "Don't cry because it's over, smile because it happened -- Dr. Seuss");
        stmt.execute();
    }

    public static void insertArm(Connection conn) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO arms VALUES (NULL, ?)");
        stmt.setString(1, "Curls");
        stmt.execute();
        stmt.setString(1, "Bench press");
        stmt.execute();
        stmt.setString(1, "Push ups ");
        stmt.execute();
        stmt.setString(1, "Pull ups");
        stmt.execute();
        stmt.setString(1, "Dips");
        stmt.execute();
    }

    public static void insertLeg(Connection conn) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO legs VALUES (NULL, ?)");
        stmt.setString(1, "Squats");
        stmt.execute();
        stmt.setString(1, "Lunges");
        stmt.execute();
        stmt.setString(1, "Box jumps");
        stmt.execute();
        stmt.setString(1, "Step ups");
        stmt.execute();
        stmt.setString(1, "Calf raises");
        stmt.execute();
    }

    public static void insertCardio(Connection conn) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO cardios VALUES (NULL, ?)");
        stmt.setString(1, "Run");
        stmt.execute();
        stmt.setString(1, "Bike");
        stmt.execute();
        stmt.setString(1, "Swim");
        stmt.execute();
        stmt.setString(1, "Row");
        stmt.execute();
        stmt.setString(1, "Jump rope");
        stmt.execute();
    }

    public static void insertCore(Connection conn) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO cores VALUES (NULL, ?)");
        stmt.setString(1, "Sit ups");
        stmt.execute();
        stmt.setString(1, "Crunches");
        stmt.execute();
        stmt.setString(1, "V ups ");
        stmt.execute();
        stmt.setString(1, "Plank");
        stmt.execute();
        stmt.setString(1, "Flutter kick");
        stmt.execute();
    }

    public static Quote createQuote(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        Quote quote = null;
        insertQuote(conn);

        ResultSet resultsQuote = stmt.executeQuery("SELECT * FROM quotes ORDER BY RAND() LIMIT 1");
        if(resultsQuote.next()) {
            quote = new Quote();
            quote.id =resultsQuote.getInt("id");
            quote.quote = resultsQuote.getString("quote");
        }
        return quote;
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

    public static ArrayList<Note> selectNotes(Connection conn) throws SQLException {
        ArrayList<Note> countries = new ArrayList();
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM notes");
        ResultSet results = stmt.executeQuery();
        while (results.next()) {
            Note note = new Note();
            note.id = results.getInt("id");
            note.text = results.getString("note");
            note.noteDate = results.getTimestamp("note_date").toLocalDateTime();
            countries.add(note);
        }
        return countries;
    }

    //**************************************************************************************************************//


    public static void main(String[] args) throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:h2:./main");

        createTables(conn);

        if (selectUser(conn, "alice") == null) {
            insertUser(conn, "alice", "1245", "");
        }

        insertNote(conn, 0, "This is a note", LocalDateTime.now());


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
                    } else if (!password.equals(user.password)) {
                        Spark.halt(403);
                    }

                    Session session = request.session();
                    session.attribute("username", username);

                    return "";
                })
        );

        Spark.get("/randomWorkout",
                (request, response) -> {
                    if (lastWorkoutTime == null || lastWorkoutTime.isBefore(LocalDateTime.now().minusSeconds(10))){
                        lastWorkoutTime = LocalDateTime.now();
                        lastWorkout = createWorkout(conn);
                    }
                    JsonSerializer serializer = new JsonSerializer();
                    return serializer.serialize(lastWorkout);
                });

        Spark.post(
                "/create-note",
                ((request, response) -> {
                    Session session = request.session();
                    String username = session.attribute("username");
                    User me = selectUser(conn, username);
                    int userId = me.id;

                    String note = request.queryParams("note");
                    LocalDateTime noteDate = LocalDateTime.now();
                    try {
                        insertNote(conn, userId , note , noteDate );
                    }catch (Exception e){
                        System.out.println("failure to create note");

                    }
                    return "";
                })
        );

        Spark.get("/notes",
                (request, response) -> {
                        JsonSerializer serializer = new JsonSerializer();
                        String json = serializer.serialize(selectNotes(conn));
                        return json;
                });

        Spark.post(
                "/delete-note",
                ((request, response) -> {
                    String id = request.queryParams("noteId");
                    try {
                        int idNum = Integer.valueOf(id);
                        deleteNote(conn, idNum);
                    }catch (Exception e){

                    }
                    return "";
                })
        );

        Spark.get("/random-quote",
                (request, response) -> {
                    JsonSerializer serializer = new JsonSerializer();
                    String json = serializer.serialize(createQuote(conn));
                    return serializer.serialize(json);
                });
        }

}

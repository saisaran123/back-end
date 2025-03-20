package Visualizer;

import java.sql.*;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.util.Scanner;

public class Executer {
    public Connection connect;
    public final Scanner sc;
    public String role ;
    public Connection parent;
    public String user ;
    String actions = "";
    LocalDateTime login;
    LocalDateTime logout;
    public Executer(String role,Connection parent,String username) {
        sc = new Scanner(System.in);
        this.role = role;
        this.parent = parent;
        this.user = username;
    }

    public void start() {
        login = LocalDateTime.now();
        if(role.equals("admin")){
            while (true) {
                System.out.println();
                System.out.println("1. Establish Connection");
                System.out.println("2. change role of users ?");
                System.out.println("3. show history");
                System.out.println("4. logout");
                System.out.println("5. show mostly used querry");
                System.out.print("Choose an option: ");
                int choice = sc.nextInt();
                sc.nextLine();

                if (choice == 1) {
                    establishConnection();
                    if (connect != null) {
                        handleDatabases();
                        break;
                    }}
                else if (choice == 2) {
                    changeRoll();
                } else if (choice == 3) {
                    showHistory();
                }
                else if (choice == 4) {
                    logout = LocalDateTime.now();
                    addHistory(login,logout);
                    System.out.println("Exiting...");
                    break;
                }
                else if (choice == 5){
                    showMostUsedQuerry();
                }
                else {
                    System.out.println("Invalid choice. Try again.");
                }
            }
        }
        else {
            while (true) {
                System.out.println("1. Establish Connection");
                System.out.println("2. logout");
                System.out.println("3. show history");
                System.out.print("Choose an option: ");
                int choice = sc.nextInt();
                sc.nextLine();

                if (choice == 1) {
                    establishConnection();
                    if (connect != null) {
                        handleDatabases();
                        break;
                    }
                } else if (choice == 2) {
                    logout = LocalDateTime.now();
                    addHistory(login,logout);
                    System.out.println("Exiting...");
                    break;
                }
                else if (choice == 3) {
                    showHistory();
                }else {
                    System.out.println("Invalid choice. Try again.");
                }
            }
        }
    }

    public void showMostUsedQuerry(){
        String query = "SELECT count(queries) as times_used,queries FROM query group by queries order by count(queries) desc limit 3;";
        try (Statement cursor = parent.createStatement();
             ResultSet rs = cursor.executeQuery(query)) {
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            ArrayList<String> headings = new ArrayList<>();
            ArrayList<ArrayList<String>> content = new ArrayList<>();
            ArrayList<Integer> maxWidth = new ArrayList<>();

            for (int i = 1; i <= columnCount; i++) {
                String columnName = metaData.getColumnName(i);
                headings.add(columnName);
                maxWidth.add(columnName.length());
            }

            while (rs.next()) {
                ArrayList<String> row = new ArrayList<>();
                for (int i = 1; i <= columnCount; i++) {
                    String value = rs.getString(i);
                    if(value == null){
                        value = "null";
                    }
                    row.add(value);
                    if (maxWidth.get(i - 1) < value.length()) {
                        maxWidth.set(i - 1, value.length());
                    }
                }
                content.add(row);
            }

            new DisplayTable(content, headings, maxWidth, "History");
            System.out.println();
        } catch (SQLException e) {
            System.out.println("Error fetching history: " + e.getMessage());
        }
    }

    public void showHistory() {
        String query = "";
        if(role.equals("admin")){query = "SELECT * FROM history;";}
        else{
            query = "select * from history where username = '"+user+"';";}
        try (Statement cursor = parent.createStatement();
             ResultSet rs = cursor.executeQuery(query)) {
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            ArrayList<String> headings = new ArrayList<>();
            ArrayList<ArrayList<String>> content = new ArrayList<>();
            ArrayList<Integer> maxWidth = new ArrayList<>();

            for (int i = 1; i <= columnCount; i++) {
                String columnName = metaData.getColumnName(i);
                headings.add(columnName);
                maxWidth.add(columnName.length());
            }

            while (rs.next()) {
                ArrayList<String> row = new ArrayList<>();
                for (int i = 1; i <= columnCount; i++) {
                    String value = rs.getString(i);
                    if(value == null){
                        value = "null";
                    }
                    row.add(value);
                    if (maxWidth.get(i - 1) < value.length()) {
                        maxWidth.set(i - 1, value.length());
                    }
                }
                content.add(row);
            }

            new DisplayTable(content, headings, maxWidth, "History");
            System.out.println();
        } catch (SQLException e) {
            System.out.println("Error fetching history: " + e.getMessage());
        }
    }

    public void addQuerry(String param){
        if(actions.equals("")){return;}
        String query = "INSERT INTO query(queries) VALUES ( ? );";
        try (PreparedStatement stmt = parent.prepareStatement(query)) {
            stmt.setString(1, param);
            stmt.execute();
        } catch (SQLException e){
            System.out.println("Error logging action: " + e.getMessage());
        }
    }

    public void addHistory(LocalDateTime login,LocalDateTime logout) {
        if(actions.equals("")){actions = "no action performed";}
        String query = "INSERT INTO history (username, action_performed ,login_time ,logout_time) VALUES ( ? , ? , ? , ? );";
        try (PreparedStatement stmt = parent.prepareStatement(query)) {
            stmt.setString(1, user);
            stmt.setString(2, actions);
            stmt.setString(3,login.toString());
            stmt.setString(4,logout.toString());
            stmt.execute();
        } catch (SQLException e) {
            System.out.println("Error logging action: " + e.getMessage());
        }
    }

    public void changeRoll() {
        String query = "select id, username , role from login";
        try (Statement cursor = parent.createStatement();
             ResultSet rs = cursor.executeQuery(query)) {
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            ArrayList<String> heading = new ArrayList<>();
            ArrayList<ArrayList<String>> content = new ArrayList<>();
            ArrayList<Integer> maxWidth = new ArrayList<>();
            for (int i = 1; i <= columnCount; i++) {
                String name = metaData.getColumnName(i);
                heading.add(name);
                maxWidth.add(name.length());
            }
            while (rs.next()) {
                ArrayList<String> temp = new ArrayList<>();
                for (int i = 1; i <= columnCount; i++) {
                    String clname = rs.getString(i);
                    if (clname == null) clname = "NULL";
                    if(clname == null){
                        clname = "null";
                    }
                    temp.add(clname);
                    if(maxWidth.get(i-1) < clname.length()){
                        maxWidth.set(i-1,clname.length());

                    }
                }
                content.add(temp);
            }
            new DisplayTable(content,heading,maxWidth,metaData.getTableName(1));
            System.out.println();
            System.out.print("enter id of the user you want to change :");
            String value = sc.nextLine();
            update("role","admin","id",value,"login",parent);
        } catch (SQLException e) {
            System.out.println("error "+ e.getMessage());
        }
    }

    public void establishConnection() {
        System.out.print("Enter host (e.g., localhost): ");
        String host = sc.nextLine();
        System.out.print("Enter user: ");
        String user = sc.nextLine();
        System.out.print("Enter password: ");
        String password = sc.nextLine();

        String url = "jdbc:mysql://" + host + ":3306/";
        try {
            connect = DriverManager.getConnection(url, user, password);
            System.out.println("Connection established successfully.");
        } catch (SQLException e) {
            System.out.println("Error establishing connection: " + e.getMessage());
        }
    }

    public void handleDatabases() {
        try (Statement cursor = connect.createStatement()) {
            ResultSet rs = cursor.executeQuery("SHOW DATABASES;");
            ArrayList<String> db = new ArrayList<>();
            int count = 1;
            System.out.println();
            System.out.println(count +" : back to connection");
            count++;
            System.out.println("Available Databases:");
            while (rs.next()) {
                String dbname = rs.getString(1);
                System.out.println(count +" : " +dbname );
                count++;
                db.add(dbname);
            }

            System.out.print("Select a database: ");
            int database = sc.nextInt();
            if (database == 1) {
                start();
                return;
            }

            cursor.execute("USE " + db.get(database - 2));
            System.out.println("Using database: " + db.get(database - 2));

            while (true) {
                System.out.println("\nOptions:");
                System.out.println("1. Create Table");
                System.out.println("2. Show Tables");
                System.out.println("3. Back to Main Menu");
                System.out.print("Choose an option: ");
                int choice = sc.nextInt();
                sc.nextLine();

                switch (choice) {
                    case 1:
                        createTable(cursor);
                        break;
                    case 2:
                        handleTables(cursor);
                        break;
                    case 3:
                        handleDatabases();
                        return;
                    default:
                        System.out.println("Invalid choice. Try again.");
                }
            }
        }
        catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void createTable(Statement cursor) {
        System.out.print("Enter table name: ");
        String tableName = sc.nextLine();
        System.out.print("Enter table structure (e.g., id INT, name VARCHAR(50)): ");
        String structure = sc.nextLine();

        String query = "CREATE TABLE " + tableName + " (" + structure + ");";
        actions += query;
        addQuerry(query);
        try {
            cursor.execute(query);
            System.out.println("Table " + tableName + " created successfully.");
        } catch (SQLException e) {
            System.out.println("Error creating table: " + e.getMessage());
        }
    }

    public void handleTables(Statement cursor) {
        try {
            ResultSet rs = cursor.executeQuery("SHOW TABLES;");
            ArrayList<String> tb = new ArrayList<>();
            int count = 1;
            System.out.println();
            System.out.println(count +" : back to database selection");
            count++;
            System.out.println("Available Tables:");
            while (rs.next()) {
                String tableName = rs.getString(1);
                System.out.println(count  +" : "+  tableName);
                tb.add(tableName);
                count++;
            }

            System.out.print("Select a table: ");
            int tablenum = sc.nextInt();
            if (tablenum == 1) {
                return;
            }
            String table = tb.get(tablenum - 2);

            while (true) {
                System.out.println("\nOptions:");
                System.out.println("1. Describe Table");
                System.out.println("2. Show Data in Table");
                System.out.println("3. Insert Data");
                System.out.println("4. update table");
                System.out.println("5. alter table");
                System.out.println("6. Drop Table");
                System.out.println("7. Back to table Selection");
                System.out.print("Choose an option: ");
                int choice = sc.nextInt();
                sc.nextLine();

                switch (choice) {
                    case 1:
                        describeTable(cursor, table);
                        break;
                    case 2:
                        showDataInTable(cursor, table);
                        break;
                    case 3:
                        insertData(cursor, table);
                        break;
                    case 4:
                        updateTable(cursor,table);
                        break;
                    case 5:
                        alterTable(cursor, table);
                        break;
                    case 6:
                        dropTable(cursor, table);
                        handleTables(cursor);
                        return;
                    case 7:
                        handleTables(cursor);
                        return;
                    default:
                        System.out.println("Invalid choice.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void describeTable(Statement cursor, String table) {
        try {
            ResultSet rs = cursor.executeQuery("DESCRIBE " + table + ";");
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            ArrayList<String> heading = new ArrayList<>();
            ArrayList<ArrayList<String>> content = new ArrayList<>();
            ArrayList<Integer> maxWidth = new ArrayList<>();
            for (int i = 1; i <= columnCount; i++) {
                String name = metaData.getColumnName(i);
                heading.add(name);
                maxWidth.add(name.length());
            }
            while (rs.next()) {
                ArrayList<String> temp = new ArrayList<>();
                for (int i = 1; i <= columnCount; i++) {
                    String clname = rs.getString(i);
                    if (clname == null) clname = "NULL";
                    temp.add(clname);
                    if(maxWidth.get(i-1) < clname.length()){
                        maxWidth.set(i-1,clname.length());

                    }
                }
                content.add(temp);
            }
            new DisplayTable(content,heading,maxWidth,metaData.getTableName(1));
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void showDataInTable(Statement cursor, String table) {
        try {
            ResultSet rs = cursor.executeQuery("SELECT * FROM " + table + ";");
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            ArrayList<String> heading = new ArrayList<>();
            ArrayList<ArrayList<String>> content = new ArrayList<>();
            ArrayList<Integer> maxWidth = new ArrayList<>();
            for (int i = 1; i <= columnCount; i++) {
                String name = metaData.getColumnName(i);
                heading.add(name);
                maxWidth.add(name.length());
            }
            while (rs.next()) {
                ArrayList<String> temp = new ArrayList<>();
                for (int i = 1; i <= columnCount; i++) {
                    String clname = rs.getString(i);
                    if(clname == null){
                        clname = "null";
                    }
                    temp.add(clname);
                    if(maxWidth.get(i-1) < clname.length()){
                        maxWidth.set(i-1,clname.length());
                    }
                }
                content.add(temp);
            }
            new DisplayTable(content,heading,maxWidth,metaData.getTableName(1));
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void insertData(Statement cursor, String table) {
        System.out.print("Enter values to insert (comma-separated): ");
        String values = sc.nextLine();
        String query = "INSERT INTO " + table + " VALUES (" + values + ");";
        actions += query;
        addQuerry(query);
        try {
            boolean rows = cursor.execute(query);
            System.out.println("row inserted.");
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void alterTable(Statement cursor, String table) {
        System.out.println("\nOptions for altering table:");
        System.out.println("1. Add Column");
        System.out.println("2. Modify Column");
        System.out.println("3. Drop Column");
        System.out.println("4. Rename Column");
        System.out.println("5. back to menu");
        System.out.print("Choose an option: ");
        int choice = sc.nextInt();
        sc.nextLine();

        try {
            switch (choice) {
                case 1:
                    System.out.print("Enter new column name: ");
                    String newColumn = sc.nextLine();
                    System.out.print("Enter data type for new column (e.g., VARCHAR(50), INT): ");
                    String dataType = sc.nextLine();
                    cursor.execute("ALTER TABLE " + table + " ADD COLUMN " + newColumn + " " + dataType + ";");
                    System.out.println("Column " + newColumn + " added to table " + table + ".");
                    actions += "ALTER TABLE " + table + " ADD COLUMN " + newColumn + " " + dataType + ";";
                    addQuerry("ALTER TABLE " + table + " ADD COLUMN " + newColumn + " " + dataType + ";");
                    break;

                case 2:
                    System.out.println();
                    describeTable(cursor,table);
                    System.out.println();
                    System.out.print("Enter column name to modify: ");
                    String columnToModify = sc.nextLine();
                    System.out.print("Enter new data type (e.g., VARCHAR(100), INT): ");
                    String newDataType = sc.nextLine();
                    cursor.execute("ALTER TABLE " + table + " MODIFY COLUMN " + columnToModify + " " + newDataType + ";");
                    System.out.println("Column " + columnToModify + " modified in table " + table + ".");
                    actions +="ALTER TABLE " + table + " MODIFY COLUMN " + columnToModify + " " + newDataType + ";";
                    addQuerry("ALTER TABLE " + table + " MODIFY COLUMN " + columnToModify + " " + newDataType + ";");
                    break;

                case 3:
                    System.out.println();
                    describeTable(cursor,table);
                    System.out.println();
                    System.out.print("Enter column name to drop: ");
                    String columnToDrop = sc.nextLine();
                    cursor.execute("ALTER TABLE " + table + " DROP COLUMN " + columnToDrop + ";");
                    System.out.println("Column " + columnToDrop + " dropped from table " + table + ".");
                    actions +="ALTER TABLE " + table + " DROP COLUMN " + columnToDrop + ";";
                    addQuerry("ALTER TABLE " + table + " DROP COLUMN " + columnToDrop + ";");
                    break;
                case 4:
                    System.out.println();
                    describeTable(cursor,table);
                    System.out.println();
                    System.out.print("Enter current column name: ");
                    String currentColumn = sc.nextLine();
                    System.out.print("Enter new column name: ");
                    String newColumnName = sc.nextLine();
                    cursor.execute("ALTER TABLE " + table + " RENAME COLUMN " + currentColumn + " TO " + newColumnName + ";");
                    System.out.println("Column " + currentColumn + " renamed to " + newColumnName + " in table " + table + ".");
                    actions += "ALTER TABLE " + table + " RENAME COLUMN " + currentColumn + " TO " + newColumnName + ";";
                    addQuerry("ALTER TABLE " + table + " RENAME COLUMN " + currentColumn + " TO " + newColumnName + ";");
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        } catch (SQLException e) {
            System.out.println("Error altering table: " + e.getMessage());
        }
    }

    public void updateTable(Statement cursor, String table) {
        System.out.println();
        showDataInTable(cursor,table);
        System.out.println();
        System.out.print("Enter the column to update: ");
        String columnToUpdate = sc.nextLine();
        System.out.print("Enter the new value for the column: ");
        String newValue = sc.nextLine();
        System.out.print("Enter the condition column: ");
        String conditionColumn = sc.nextLine();
        System.out.print("Enter the condition value: ");
        String conditionValue = sc.nextLine();
        update(columnToUpdate,newValue,conditionColumn,conditionValue,table,connect);
    }

    public void update(String columnToUpdate,String newValue,String conditionColumn,String conditionValue,String table,Connection connect){
        String query = "UPDATE " + table + " SET " + columnToUpdate + " = ? WHERE " + conditionColumn + " = ?";
        try(PreparedStatement pstmt = connect.prepareStatement(query);) {
            pstmt.setString(1, newValue);
            pstmt.setString(2, conditionValue);
            boolean rows = pstmt.execute();
            System.out.println(" row updated in table " + table + ".");
            actions +="Updated table: " + table + ", column: " + columnToUpdate + ", new value: " + newValue + ", condition: " + conditionColumn + " = " + conditionValue;
            addQuerry("Updated table: " + table + ", column: " + columnToUpdate + ", new value: " + newValue + ", condition: " + conditionColumn + " = " + conditionValue);

        } catch (SQLException e) {
            System.out.println("Error updating table: " + e.getMessage());
        }
    }

    public void dropTable(Statement cursor, String table) {
        System.out.print("Are you sure you want to drop the table " + table + "? (yes/no): ");
        String confirmation = sc.nextLine();
        if ("yes".equalsIgnoreCase(confirmation)) {
            try {
                cursor.execute("DROP TABLE " + table + ";");
                System.out.println("Table " + table + " dropped successfully.");
                actions += "DROP TABLE " + table + ";";
                addQuerry("DROP TABLE " + table + ";");
            } catch (SQLException e) {
                System.out.println("Error: " + e.getMessage());
            }
        } else {
            System.out.println("Drop table operation cancelled.");
        }
    }
}
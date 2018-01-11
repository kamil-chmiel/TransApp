package com.example.kamil.transapp;

import android.os.StrictMode;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DatabaseHandler {

    private static java.sql.Connection connection;
    private static DatabaseHandler instance = null;
    private static Statement s;

    public static DatabaseHandler getInstance() {
        if(instance == null) {
            instance = new DatabaseHandler();
        }
        return instance;
    }


    public static boolean checkDriver(String driver) {
        // LADOWANIE STEROWNIKA
        System.out.print("Sprawdzanie sterownika:");
        try {
            Class.forName(driver).newInstance();
            return true;
        } catch (Exception e) {
            System.out.println("Blad przy ladowaniu sterownika bazy!");
            return false;
        }
    }

    public static Connection connectToDatabase(String kindOfDatabase, String adress,
                                               String dataBaseName, String userName, String password) throws SQLException {
        String baza = kindOfDatabase + adress + "/" + dataBaseName;
        java.sql.Connection connection = null;
            connection = DriverManager.getConnection(baza, userName, password);

        return connection;
    }

    private static Statement createStatement(Connection connection) {
        try {
            return connection.createStatement();
        } catch (SQLException e) {
            System.out.println("Błąd createStatement! " + e.getMessage() + ": " + e.getErrorCode());
            System.exit(3);
        }
        return null;
    }

    private static void closeConnection(Connection connection, Statement s) {
        try {
            s.close();
            connection.close();
        } catch (SQLException e) {
            System.out
                    .println("Bląd przy zamykaniu polączenia z bazą! " + e.getMessage() + ": " + e.getErrorCode());;
            System.exit(4);
        }
        //System.out.print(" zamknięcie OK");
    }

    private static ResultSet executeQuery(Statement s, String sql) {
        try {
            return s.executeQuery(sql);
        } catch (SQLException e) {
            System.out.println("Zapytanie nie wykonane! " + e.getMessage() + ": " + e.getErrorCode());
        }
        return null;
    }
    private static int executeUpdate(Statement s, String sql) {
        try {
            return s.executeUpdate(sql);
        } catch (SQLException e) {
            System.out.println("Zapytanie nie wykonane! " + e.getMessage() + ": " + e.getErrorCode());
        }
        return -1;
    }

    /**
     * Wyświetla dane uzyskane zapytaniem select
     * @param r - wynik zapytania
     */
    private static void printDataFromQuery(ResultSet r) {
        ResultSetMetaData rsmd;
        try {
            rsmd = r.getMetaData();
            int numcols = rsmd.getColumnCount(); // pobieranie liczby kolumn
            // wyswietlanie nazw kolumn:
            for (int i = 1; i <= numcols; i++) {
                System.out.print("\t" + rsmd.getColumnLabel(i) + "\t|");
            }
            System.out
                    .print("\n____________________________________________________________________________\n");
            /**
             * r.next() - przejście do kolejnego rekordu (wiersza) otrzymanych wyników
             */
            // wyswietlanie kolejnych rekordow:
            while (r.next()) {
                for (int i = 1; i <= numcols; i++) {
                    Object obj = r.getObject(i);
                    if (obj != null)
                        System.out.print("\t" + obj.toString() + "\t|");
                    else
                        System.out.print("\t");
                }
                System.out.println();
            }
        } catch (SQLException e) {
            System.out.println("Bląd odczytu z bazy! " + e.getMessage() + ": " + e.getErrorCode());
        }
    }

    public static String checkLogin(String username, String password)
    {
        String type="";
        System.out.println("Sprawdzanie loginu...");
        ResultSet r = executeQuery(s, "Select workerType from users where username='"+username+"' AND "+"password='"+password+"';");

        try {
            r.next();
            Object obj = r.getObject(1);
            if (obj != null)
                type=obj.toString();
        }
        catch (SQLException e) {
            System.out.println("Bląd odczytu z bazy! " + e.getMessage() + ": " + e.getErrorCode());
        }
        //closeConnection(connection, s);
        return type;
    }

    public static String[] getManagerInfo(String login)
    {
        String[] managerInfo = new String[3];
        System.out.println("Login: "+login);
        ResultSet r = executeQuery(s, "Select * from menadzer where Login='"+login+"'");

        try {
            if(r.next())
            {
                managerInfo[0] = (String) r.getObject(1);
                managerInfo[1] = (String) r.getObject(2);
                managerInfo[2] = (String) r.getObject(3);
            }
        }
        catch (SQLException e) {
            System.out.println("Bląd odczytu z bazy! " + e.getMessage() + ": " + e.getErrorCode());
        }
        //closeConnection(connection, s);
        return managerInfo;
    }

    public static ArrayList<String> getAvailableWorkers()
    {
        ArrayList<String> availableW = new ArrayList<>();
        ResultSet r = executeQuery(s, "Select * from pracownik_magazynu where Dostepny=1;");

        try {
            while(r.next())
            {
                availableW.add(r.getObject(1).toString()+" "+r.getObject(2).toString()+" "+r.getObject(3).toString());
            }
        }
        catch (SQLException e) {
            System.out.println("Bląd odczytu z bazy! " + e.getMessage() + ": " + e.getErrorCode());
        }
        //closeConnection(connection, s);
        return availableW;
    }

    public static ArrayList<String> getAvailableDrivers()
    {
        ArrayList<String> availableD = new ArrayList<>();
        ResultSet r = executeQuery(s, "Select * from kierowca where Dostepny=1;");

        try {
            while(r.next())
            {
                availableD.add(r.getObject(1).toString()+" "+r.getObject(2).toString()+" "+r.getObject(3).toString());
            }
        }
        catch (SQLException e) {
            System.out.println("Bląd odczytu z bazy! " + e.getMessage() + ": " + e.getErrorCode());
        }
        //closeConnection(connection, s);
        return availableD;
    }

    public static ArrayList<String> getCustomers()
    {
        ArrayList<String> availableC = new ArrayList<>();
        ResultSet r = executeQuery(s, "Select * from klient");

        try {
            while(r.next())
            {
                availableC.add(r.getObject(1).toString()+" "+r.getObject(2).toString() + " "+ r.getObject(3).toString());
            }
        }
        catch (SQLException e) {
            System.out.println("Bląd odczytu z bazy! " + e.getMessage() + ": " + e.getErrorCode());
        }
        //closeConnection(connection, s);
        return availableC;
    }

    public static String[] getWorkerDetails(String type, String pesel)
    {
        String[] data = new String[4];
        ResultSet r = executeQuery(s, "Select * from "+ type +" where Pesel='"+pesel+"'");

        try {
            if(r.next())
            {
                data[0]=(String)r.getObject(1);
                data[1]=(String)r.getObject(2);
                data[2]=(String)r.getObject(3);
                data[3]=(String)r.getObject(4);
            }
        }
        catch (SQLException e) {
            System.out.println("Bląd odczytu z bazy! " + e.getMessage() + ": " + e.getErrorCode());
        }
        //closeConnection(connection, s);
        return data;
    }

    public static void sendTask(Task task)
    {
        try{
        executeUpdate(s,"INSERT INTO `zamowienie`(`Numer_Zamowienia`, `Lista_Towarow`, `Opis`, " +
                "`PESEL_Menadzera`, `PESEL_Klienta`, `Adres`, `peselMagazyniera`, `peselKierowcy`, `Stan`) " +
                "VALUES ('"+task.getOrderNumber()+"','"+task.getItems()+"','"+task.getDescribtion()+"','"+SessionController.getPeselNumber()+
                "','"+ task.getCustomer().getPesel() +"','"+task.getCustomer().getAddress()+"','"+ task.getWorker().getPesel()
                +"','"+ task.getDriver().getPesel()+"','Started')");
        }
        catch (Exception e) {
            System.out.println("Bląd zapisu zadania do bazy! " + e.getMessage());
        }
        finally {
            executeUpdate(s,"UPDATE pracownik_magazynu SET Dostepny=0 WHERE PESEL='"+task.getWorker().getPesel()+"';");
            executeUpdate(s,"UPDATE kierowca SET Dostepny=0 WHERE PESEL='"+task.getDriver().getPesel()+"';");

            String[] items = task.getItems().split(",");
            for(int i=0; i<items.length; i++)
                executeUpdate(s,"UPDATE towar SET Ilosc_Sztuk=Ilosc_Sztuk-1 WHERE ID_Towaru='"+items[i]+"';");
        }
    }

    public static ArrayList<String> getActiveTasks()
    {
        ArrayList<String> data = new ArrayList<>();
        ResultSet r = executeQuery(s, "Select * from zamowienie");

        try {
            while(r.next())
            {
                data.add("#"+r.getObject(1).toString()+"\nCustomer: "+r.getObject(5).toString()+ "\nWarehouseWorker: "+
                        r.getObject(7)+ "\nDriver: "+ r.getObject(8)+ "\nState: "+
                        r.getObject(9));
            }
        }
        catch (SQLException e) {
            System.out.println("Bląd odczytu z bazy! " + e.getMessage() + ": " + e.getErrorCode());
        }

        return data;
    }

    public static String[] getSchedule(String pesel)
    {
        String[] schedule = new String[5];
        ResultSet r = executeQuery(s, "Select * from harmonogram where PESEL='"+pesel+"'");

        try {
            if(r.next())
            {
                schedule[0]=(String)r.getObject(2);
                schedule[1]=(String)r.getObject(3);
                schedule[2]=(String)r.getObject(4);
                schedule[3]=(String)r.getObject(5);
                schedule[4]=(String)r.getObject(6);
            }
        }
        catch (SQLException e) {
            System.out.println("Bląd odczytu z bazy! " + e.getMessage() + ": " + e.getErrorCode());
        }
        //closeConnection(connection, s);
        return schedule;
    }

    public DatabaseHandler() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
        //*** sprawdzanie sterownika
        if (checkDriver("com.mysql.jdbc.Driver"))
            System.out.println(" ... OK");
        else
            System.exit(1);
        //*** ustanowienie polaczenia
        try {
            connection = connectToDatabase("jdbc:mysql://", "limitlessgames.pl", "limitlessgames", "limitlessgames", "toniehaslo765");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (connection != null)
            System.out.print(" polaczenie OK\n");


        s = createStatement(connection);
    }
}
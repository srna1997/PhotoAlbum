package model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Osoba extends Table{

    @Entity(type = "INTEGER", size =11, primary = true)
    int id;
    @Entity(type = "VARCHAR", size = 15 )
    String uloga;
    @Entity(type = "VARCHAR", size = 50, isnull = false)
    String ime;
    @Entity(type = "VARCHAR",size = 50, isnull = false)
    String prezime;
    @Entity(type = "VARCHAR", size = 50, isnull = false)
    String korisnickoIme;
    @Entity(type = "VARCHAR", size = 15, isnull = false)

    String lozinka;

    public String getUloga() {
        return uloga;
    }

    public void setUloga(String uloga) {
        this.uloga = uloga;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }

    public String getKorisnickoIme() {
        return korisnickoIme;
    }

    public void setKorisnickoIme(String korisnickoIme) {
        this.korisnickoIme = korisnickoIme;
    }

    public String getLozinka() {
        return lozinka;
    }

    public void setLozinka(String lozinka) {
        this.lozinka = lozinka;
    }

    public static Osoba login (String korisnickoIme, String lozinka) throws Exception {
            String sql = "SELECT id FROM osoba WHERE korisnickoIme=? AND lozinka=?";
            PreparedStatement query = Database.CONNECTION.prepareStatement(sql);
            query.setString(1, korisnickoIme);
            query.setString(2, lozinka);


        ResultSet rs = query.executeQuery();

        if (rs.next()) {
            return (Osoba) Osoba.get(Osoba.class, rs.getInt(1));
        } else {
            return null;
        }

    }
}

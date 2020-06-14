package model;

import controller.Login;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.sql.rowset.serial.SerialBlob;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Album extends Table {

    @Entity(type = "INTEGER", size = 11, primary = true)
    int id;

    @Entity(type = "VARCHAR", size = 50, isnull = false)
    String nazivAlbuma;

    @Entity(type = "TIMESTAMP", isnull = false)
    Timestamp kreiran;

    @ForeignKey(table = "Osoba", attribute = "id")
    @Entity(type = "INTEGER", size = 11)
    int idOsoba;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNazivAlbuma() {
        return nazivAlbuma;
    }

    public void setNazivAlbuma(String nazivAlbuma) {
        this.nazivAlbuma = nazivAlbuma;
    }

    public Timestamp getKreiran() {
        return kreiran;
    }

    public void setKreiran(Timestamp kreiran) {
        this.kreiran = kreiran;
    }

    public int getIdOsoba() {
        return idOsoba;
    }

    public void setIdOsoba(int idOsoba) {
        this.idOsoba = idOsoba;
    }

}


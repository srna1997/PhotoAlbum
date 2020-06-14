package model;

import controller.Login;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javax.sql.rowset.serial.SerialBlob;
import java.sql.*;
import java.util.List;

public class Slika extends Table {
    @Entity(type = "INTEGER", size = 11, primary = true)
    int id;

    @Entity(type = "VARCHAR", size = 50, isnull = false)
    String nazivSlike;

    @Entity(type = "BLOB", size = 10000000, isnull = false)
    SerialBlob sadrzajSlike;

    @Entity(type = "TIMESTAMP", isnull = false)
    Timestamp dodano;

    @ForeignKey(table = "Album", attribute = "id")
    @Entity(type = "INTEGER", size = 11)
    int idAlbum;

    @ForeignKey(table = "Osoba", attribute = "id")
    @Entity(type = "INTEGER", size = 11)
    int idOsoba;

    public int getIdOsoba() {
        return idOsoba;
    }

    public void setIdOsoba(int idOsoba) {
        this.idOsoba = idOsoba;
    }

    public ImageView getSadrzajSlike() {
        try {
            return new ImageView(new Image(sadrzajSlike.getBinaryStream()));
        } catch (Exception e) {
            return null;
        }
    }

    public void setSadrzajSlike(SerialBlob sadrzajSlike) {
        this.sadrzajSlike = sadrzajSlike;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNazivSlike() {
        return nazivSlike;
    }

    public void setNazivSlike(String nazivSlike) {
        this.nazivSlike = nazivSlike;
    }

    public Timestamp getDodano() {
        return dodano;
    }

    public void setDodano(Timestamp dodano) {
        this.dodano = dodano;
    }

    public int getIdAlbum() {
        return idAlbum;
    }

    public void setIdAlbum(int idAlbum) {
        this.idAlbum = idAlbum;
    }
}

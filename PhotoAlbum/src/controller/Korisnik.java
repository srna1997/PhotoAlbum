package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import main.Main;
import model.Album;
import model.Database;
import model.Osoba;
import model.Slika;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;

public class Korisnik implements Initializable {
    Collection<Album> albumi;
    ObservableList<Album> list = FXCollections.observableArrayList();

    public static Album a;

    @FXML
    Label loggedUserLbl;

    @FXML
    Label greskaLbl;

    @FXML
    TextField nazivAlbumTxt;
    @FXML
    TableView<Album> AlbumView;
    @FXML
    TableColumn<Album, String> nazivAlbumaCol;
    @FXML
    TableColumn<Album, Timestamp> kreiranCol;


    public void otvoriAlbum(ActionEvent ev) throws Exception {
        try {

            a = AlbumView.getSelectionModel().getSelectedItem();
            if(a != null)
            {
                albumi = (Collection<Album>) Album.list(Album.class);
                albumi.removeIf(album -> album.getId() != a.getId());
                Main.showWindow(
                        getClass(),
                        "../view/Album.fxml", "Album", 748, 545
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void createAlbum(ActionEvent e) throws Exception {
        if (!this.nazivAlbumTxt.getText().equals("")) {
            Album a = new Album();

            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            a.setNazivAlbuma(this.nazivAlbumTxt.getText());
            a.setKreiran(timestamp);
            a.setIdOsoba(Login.loggedInOsoba.getId());
            a.save();
            albumi.add(a);
            this.AlbumView.getItems().setAll(albumi);

            this.nazivAlbumTxt.setText("");

            this.AlbumView.getItems().setAll(albumi);

        }
    }

    public void brisiAlbum(ActionEvent evt) throws Exception {
        Album a = AlbumView.getSelectionModel().getSelectedItem();
        if (a != null) {
            String sql = "DELETE FROM slika WHERE idAlbum = ?";
            PreparedStatement query = Database.CONNECTION.prepareStatement(sql);
            query.setInt(1, a.getId());
            query.executeUpdate();

            a.delete();
            albumi.remove(a);
            this.AlbumView.getItems().setAll(albumi);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.loggedUserLbl.setText(
                Login.loggedInOsoba.getIme() + " "
                        + Login.loggedInOsoba.getPrezime()
        );
        dohvatiAlbume();
    }

        private void dohvatiAlbume ()
        {
            this.properties();
            try {
                albumi = (Collection<Album>) Album.list(Album.class);
                albumi.removeIf(album -> album.getIdOsoba() != Login.loggedInOsoba.getId());
                this.AlbumView.getItems().setAll(albumi);
            } catch (Exception e) {
                e.printStackTrace();
            }
            this.AlbumView.setEditable(true);
        }

        private void properties()
        {
            this.nazivAlbumaCol.setCellValueFactory(new PropertyValueFactory<>("nazivAlbuma"));
            this.nazivAlbumaCol.setPrefWidth(80);
            this.kreiranCol.setCellValueFactory(new PropertyValueFactory<>("kreiran"));
            this.kreiranCol.setPrefWidth(80);
            this.nazivAlbumaCol.setEditable(true);
            this.nazivAlbumaCol.setCellFactory(TextFieldTableCell.forTableColumn());
            this.nazivAlbumaCol.setSortType(TableColumn.SortType.DESCENDING);
        }

        @FXML
        public void editNazivAlbumaToDatabase(TableColumn.CellEditEvent<Album, String> evt) throws Exception {
        Album a = evt.getRowValue();
        a.setNazivAlbuma(evt.getNewValue());
        a.update();
        }

        public void openFavorites(ActionEvent e) throws Exception {
        Main.showWindow(getClass(),"../view/Favorites.fxml","Album",764,600);
        }
        @FXML
        public void logout (ActionEvent ev) throws IOException {
            Login.loggedInOsoba = null;
            Main.showWindow(
                    getClass(),
                    "../view/Login.fxml",
                    "Login to system", 600, 215
            );
        }
    }

package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import main.Main;
import model.Database;
import model.Osoba;
import model.Slika;
import sun.rmi.runtime.Log;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ResourceBundle;

public class Favorites implements Initializable {


    ArrayList favorite = new ArrayList();

    @FXML
    TableView<Slika> favoritesView;

    @FXML
    TableColumn<Slika, String> nazivTblCol;

    @FXML
    TableColumn<Slika, Blob> slikaTblCol;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dohvatiOmiljeneSlike();
    }

    private void dohvatiOmiljeneSlike(){
        this.nazivTblCol.setPrefWidth(99);
        this.nazivTblCol.setCellValueFactory(new PropertyValueFactory<>("nazivSlike"));
        this.slikaTblCol.setPrefWidth(80);
        this.slikaTblCol.setCellValueFactory(new PropertyValueFactory<>("sadrzajSlike"));

        try {

            String sql = "SELECT * FROM favorites WHERE idOsoba=?";
            PreparedStatement query = Database.CONNECTION.prepareStatement(sql);
            query.setInt(1,Login.loggedInOsoba.getId());
            ResultSet rs = query.executeQuery();
            while(rs.next()) {

                Slika slika = (Slika) Slika.get(Slika.class, rs.getInt(2));
                favorite.add(slika);

            }

            this.favoritesView.getItems().setAll(favorite);
        }catch (Exception e) {
            System.out.println("Nismo uspjeli dohvatiti podatke");
        }
    }


    public void nazad(ActionEvent ev) throws IOException {
        Main.showWindow(
                getClass(),
                "../view/Korisnik.fxml",
                "Albumi", 785, 513
        );
    }

    public void deleteFavorite(ActionEvent e) throws Exception {
        Slika f = this.favoritesView.getSelectionModel().getSelectedItem();
        if(f != null){
            String sql = "DELETE FROM Favorites WHERE IdSlika=? AND idOsoba=?";
            PreparedStatement stmt = Database.CONNECTION.prepareStatement(sql);
            stmt.setInt(1, f.getId());
            stmt.setInt(2, f.getIdOsoba());
            stmt.executeUpdate();

            favorite.remove(f);

           this.favoritesView.getSelectionModel().clearSelection();
            this.favoritesView.getItems().clear();
            this.favoritesView.getItems().addAll(favorite);

        }
    }

}

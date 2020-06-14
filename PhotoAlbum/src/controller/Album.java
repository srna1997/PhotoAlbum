package controller;


import com.mysql.cj.protocol.Resultset;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import main.Main;
import model.Database;
import model.Favorites;
import model.Osoba;
import model.Slika;

import javax.imageio.ImageIO;
import javax.sql.rowset.serial.SerialBlob;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;

public class Album implements Initializable {

    Collection<Slika> slike;

    static ObservableList<Slika> list = FXCollections.observableArrayList();

    Collection<Favorites> favorit;

    static ObservableList<Favorites> lista = FXCollections.observableArrayList();

    public static model.Album getAlbums;


    @FXML
    TableView<Slika> slikaView;
    @FXML
    TextField nazivTxt;
    @FXML
    TableColumn<Slika, String>nazivTblCol;
    @FXML
    TableColumn<Slika, ImageView> slikaTblCol;

    @FXML
    TableColumn<Slika, Timestamp>datumTblCol;

    @FXML
    ImageView ucitanaSlika;

    BufferedImage buffImage;

    Image initialImage;


    @FXML
    //Problem
    public void dodajSliku(ActionEvent e) throws Exception{

        Album.getAlbums = Korisnik.a;
        if(!this.nazivTxt.getText().equals(""))
        { Slika s = new Slika();

            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            s.setDodano(timestamp);
            s.setNazivSlike(this.nazivTxt.getText());
            s.setIdAlbum(Album.getAlbums.getId());
            s.setIdOsoba(Login.loggedInOsoba.getId());
            SerialBlob image =new SerialBlob(imageToByte(this.buffImage));
            s.setSadrzajSlike(image);
            s.save();
            slike.add(s);
            this.slikaView.getItems().setAll(slike);
            this.nazivTxt.setText("");
            this.ucitanaSlika.setImage(this.initialImage);
        }}
    @FXML
    public void openFileDialog(MouseEvent e) throws Exception {
        FileChooser fc = new FileChooser();
        Node node = (Node) e.getSource();
        File file = fc.showOpenDialog(node.getScene().getWindow());
        this.buffImage = ImageIO.read(file);
        this.initialImage = ucitanaSlika.getImage();
        ucitanaSlika.setImage(SwingFXUtils.toFXImage(buffImage, null));
    }

    public void dodajOmiljenuSliku(ActionEvent e) throws Exception {
        Slika s = this.slikaView.getSelectionModel().getSelectedItem();
        if(s != null)
        {
            favorit = (Collection<Favorites>) Favorites.list(Favorites.class);
            favorit.removeIf(favorites -> favorites.getIdSlika() == s.getId());

            Favorites f = new Favorites();
            f.setIdSlika(s.getId());
            f.setIdOsoba(s.getIdOsoba());

            f.save();
            favorit.add(f);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dohvatiSlike();
    }


    private void dohvatiSlike () {
        this.properties();
        try {

            Album.getAlbums = Korisnik.a;
            slike = (Collection<Slika>) Slika.list(Slika.class, Album.getAlbums.getId());
            slike.removeIf(slika -> slika.getIdAlbum() != getAlbums.getId());
            this.slikaView.getItems().setAll(slike);
        }catch (Exception e) {
            e.printStackTrace();
        }
        this.slikaView.setEditable(true);
    }

    private void properties(){
        this.nazivTblCol.setPrefWidth(99);
        this.nazivTblCol.setCellValueFactory(new PropertyValueFactory<>("nazivSlike"));
        this.slikaTblCol.setPrefWidth(80);
        this.slikaTblCol.setCellValueFactory(new PropertyValueFactory<>("sadrzajSlike"));
        this.datumTblCol.setPrefWidth(80);
        this.datumTblCol.setCellValueFactory(new PropertyValueFactory<>("dodano"));
        this.nazivTblCol.setEditable(true);
        this.nazivTblCol.setCellFactory(TextFieldTableCell.forTableColumn());
    }

    @FXML
    public void editNazivSlikeToDatabase(TableColumn.CellEditEvent<Slika, String> evt) throws Exception {
        Slika s = evt.getRowValue();
        s.setNazivSlike(evt.getNewValue());
        s.update();
    }

    private byte[] imageToByte(BufferedImage bufferimage) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            ImageIO.write(bufferimage, "jpg", output );
        } catch (IOException e) {
            System.out.println("Nastala je greška: " + e.getMessage());
            e.printStackTrace();
        }
        byte [] data = output.toByteArray();
        return data;
    }

    public void openFavorites(ActionEvent e) throws Exception {
        Main.showWindow(getClass(),
                "../view/Favorites.fxml","Album",764,600
        );
    }

    public void vratiNazad(ActionEvent e)throws Exception{
        Main.showWindow(getClass(),
                "../view/Korisnik.fxml","Album",785,513
        );
    }

    public void brisiSLiku(ActionEvent evt) throws Exception {
        Slika s = (Slika) slikaView.getSelectionModel().getSelectedItem();
        if(s != null){
            s.delete();
            slike.remove(s);
            this.slikaView.getItems().setAll(slike);
        }
    }

}

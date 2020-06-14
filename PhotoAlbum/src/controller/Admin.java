package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import main.Main;
import model.Database;
import model.Osoba;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.util.Collection;
import java.util.ResourceBundle;


public class Admin implements Initializable {
    @FXML
    TextField ime;

    @FXML
    TextField prezime;
    @FXML
    TextField korIme;
    @FXML
    TextField uloga;
    @FXML
    PasswordField lozinka;

    @FXML
    Label loggedUserLbl;

    @FXML
    TableView<Osoba> tableView;

    @FXML
    TableColumn<Osoba, String> imeTblCol;

    @FXML
    TableColumn<Osoba, String> prezimeTblCol;
    @FXML
    TableColumn<Osoba, String>ulogaTblCol;
    @FXML
    TableColumn<Osoba, String>korImeTblCol;

    Collection<Osoba> osobe;

    {
        try {
            osobe = (Collection<Osoba>) Osoba.list(Osoba.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void addUserToDatabase (ActionEvent e) throws Exception{
            if(!this.ime.getText().equals("")&&
                    !this.prezime.getText().equals("")&&
                    !this.korIme.getText().equals("")&&
                    !this.uloga.getText().equals("")&&
                    !this.lozinka.getText().equals(""))
            {
                Osoba t = new Osoba();
                t.setIme(this.ime.getText());
                t.setPrezime(this.prezime.getText());
                t.setLozinka(this.lozinka.getText());
                t.setKorisnickoIme(this.korIme.getText());
                t.setUloga(this.uloga.getText());
                t.save();
                osobe.add(t);
                this.populateTableView();

                this.ime.setText("");
                this.prezime.setText("");
                this.lozinka.setText("");
                this.korIme.setText("");
                this.uloga.setText("");

            }


        }

    public void deleteUserFromDatabase(ActionEvent ev) throws Exception {

            Osoba o = tableView.getSelectionModel().getSelectedItem();

            if (o.getId() == Login.loggedInOsoba.getId()) {
                System.out.println("Ne mozete obrisat sami sebe");
            } else {

                String sql = "DELETE FROM slika WHERE idOsoba = ?";
                PreparedStatement query = Database.CONNECTION.prepareStatement(sql);
                query.setInt(1, o.getId());
                query.executeUpdate();

                String sql1 = "DELETE FROM album WHERE idOsoba = ?";
                PreparedStatement query1 = Database.CONNECTION.prepareStatement(sql1);
                query1.setInt(1, o.getId());
                query1.executeUpdate();

                o.delete();
                osobe.remove(o);

                this.populateTableView();
            }
        }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.loggedUserLbl.setText(
                Login.loggedInOsoba.getIme() +
                        " " +
                        Login.loggedInOsoba.getPrezime()
        );

        this.imeTblCol.setCellValueFactory(new PropertyValueFactory<>("ime"));
        this.prezimeTblCol.setCellValueFactory(new PropertyValueFactory<>("prezime"));
        this.ulogaTblCol.setCellValueFactory(new PropertyValueFactory<>("uloga"));
        this.korImeTblCol.setCellValueFactory(new PropertyValueFactory<>("korisnickoIme"));

        this.imeTblCol.setEditable(true);

        /* Evo šta nam je falilo :-) */
        this.imeTblCol.setCellFactory(TextFieldTableCell.forTableColumn());

        this.prezimeTblCol.setEditable(true);

        /* Evo šta nam je falilo :-) */
        this.prezimeTblCol.setCellFactory(TextFieldTableCell.forTableColumn());

        this.populateTableView();
    }

    @FXML
    public void editImeToDatabase(TableColumn.CellEditEvent<Osoba, String> evt) throws Exception {
        Osoba o = evt.getRowValue();
        o.setIme(evt.getNewValue());
        o.update();
    }

    @FXML
    public void editPrezimeToDatabase(TableColumn.CellEditEvent<Osoba, String> evt) throws Exception {
        Osoba o = evt.getRowValue();
        o.setPrezime(evt.getNewValue());
        o.update();
    }

    private void populateTableView(){
    try {
        this.tableView.getItems().setAll((Collection<? extends Osoba>) Osoba.list(Osoba.class));
        this.tableView.setEditable(true);
    } catch (Exception e) {
        System.out.println("Nismo uspjeli dohvatiti podatke");
    }
}

    @FXML
    public void logout(ActionEvent ev) throws IOException {
        Login.loggedInOsoba = null;
        Main.showWindow(
                getClass(),
                "../view/Login.fxml",
                "Login to system", 600, 215
        );
    }
}
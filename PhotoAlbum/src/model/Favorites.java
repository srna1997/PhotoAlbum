package model;


public class Favorites extends Table{
    @Entity(type = "INTEGER", size = 11, primary = true)
    int id;

    @ForeignKey(table = "Slika", attribute = "id")
    @Entity(type = "INTEGER", size = 11)
    int idSlika;

    @ForeignKey(table = "Osoba", attribute = "id")
    @Entity(type = "INTEGER", size = 11)
    int idOsoba;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdSlika() {
        return idSlika;
    }

    public void setIdSlika(int idSlika) {
        this.idSlika = idSlika;
    }

    public int getIdOsoba() {
        return idOsoba;
    }

    public void setIdOsoba(int idOsoba) {
        this.idOsoba = idOsoba;
    }
}

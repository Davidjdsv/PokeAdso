package Modelos;

public class Pokemon {
    int id_pokedex;
    String nombre;
    String tipo;
    int peso;
    int altura;
    String descripcion;
    int ataque;
    int defensa;
    String foto;

    public Pokemon(int id_pokedex, String nombre, String tipo, int peso, int altura, String descripcion, int ataque, int defensa, String foto) {
        this.id_pokedex = id_pokedex;
        this.nombre = nombre;
        this.tipo = tipo;
        this.peso = peso;
        this.altura = altura;
        this.descripcion = descripcion;
        this.ataque = ataque;
        this.defensa = defensa;
        this.foto = foto;
    }

    public int getId_pokedex() {
        return id_pokedex;
    }

    public void setId_pokedex(int id_pokedex) {
        this.id_pokedex = id_pokedex;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getPeso() {
        return peso;
    }

    public void setPeso(int peso) {
        this.peso = peso;
    }

    public int getAltura() {
        return altura;
    }

    public void setAltura(int altura) {
        this.altura = altura;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getAtaque() {
        return ataque;
    }

    public void setAtaque(int ataque) {
        this.ataque = ataque;
    }

    public int getDefensa() {
        return defensa;
    }

    public void setDefensa(int defensa) {
        this.defensa = defensa;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
}

package com.example.andinurnaf.cobatugas5.activity.Entity;

public class MovieItem {
    private String id;
    private String name;

    private String glist[][];

    public MovieItem(String id){
        initMovie();
        this.setId(id);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
        this.findMovie();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private void findMovie(){
        for(int i=0; i <this.glist.length; i++){
            if( this.glist[i][0].equals( this.id ) ){
                this.name = this.glist[i][1];
            }
        }
    }

    private void initMovie(){
        glist = new String[][]  {
                { "28", "Action"},
                { "12", "Adventure"},
                { "16", "Animation"},
                { "35", "Comedy"},
                { "80", "Crime"},
                { "99", "Documentary"},
                { "18", "Drama"},
                { "10751", "Family"},
                { "14", "Fantasy"},
                { "36", "History"},
                { "27", "Horror"},
                { "10402", "Music"},
                { "9648", "Mystery"},
                { "10749", "Romance"},
                { "878", "Science Fiction"},
                { "10770", "TV Movie"},
                { "53", "Thriller"},
                { "10752", "War"},
                { "37", "Western"} } ;
    }

}
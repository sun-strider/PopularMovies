package ch.sunstrider.android.popularmovies.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

// TODO (2) Annotate the class with Entity. Use "movie" for the table name
@Entity(tableName = "movie")
public class MovieEntry {

    // TODO (3) Annotate the id as PrimaryKey.
    @PrimaryKey
    private int id;
    private String title;


    public MovieEntry(int id, String title) {
        this.id = id;
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}

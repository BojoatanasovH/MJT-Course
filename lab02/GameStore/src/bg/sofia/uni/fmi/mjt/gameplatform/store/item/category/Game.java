package bg.sofia.uni.fmi.mjt.gameplatform.store.item.category;

import bg.sofia.uni.fmi.mjt.gameplatform.store.item.StoreItem;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Game extends GameItem implements StoreItem{
    String genre;

    public Game(String title, BigDecimal price, LocalDateTime releaseDate, String genre) {
        super(title, price, releaseDate);
        this.genre = genre;
    }

    public String getGenre(){
        return this.genre;
    }

    public void setGenre(String genre){
        this.genre = genre;
    }



//    @Override
//    public String getTitle(){
//        return title;
//    }
//
//    @Override
//    public BigDecimal getPrice(){
//        return price;
//    }
//
//    @Override
//    public double getRating(){
//        return this.rating;
//    }
//
//    @Override
//    public LocalDateTime getReleaseDate(){
//        return releaseDate;
//    }
//
//    @Override
//    public void setTitle(String title){
//        this.title = title;
//    }
//
//    @Override
//    public void setPrice(BigDecimal price){
//        this.price = price;
//    }
//
//    @Override
//    public void setReleaseDate(LocalDateTime releaseDate){
//        this.releaseDate = releaseDate;
//    }
//
//    @Override
//    public void rate(double rating) {
//        this.rating = (this.rating * this.ratingCount + rating) / (++this.ratingCount);
//    }
}
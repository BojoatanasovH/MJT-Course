package bg.sofia.uni.fmi.mjt.gameplatform.store.item.category;

import bg.sofia.uni.fmi.mjt.gameplatform.store.item.StoreItem;

import java.math.BigDecimal;
import java.time.LocalDateTime;


/**
Parent class for classes in category
*/
public class GameItem implements StoreItem {
    String title;
    BigDecimal price;
    LocalDateTime releaseDate;
    double rating;
    int ratingCount;

    public GameItem(String title, BigDecimal price, LocalDateTime releaseDate) {
        this.title = title;
        this.price = price;
        this.releaseDate = releaseDate;
        this.rating = 0.0;
        this.ratingCount = 0;
    }

    @Override
    public String getTitle(){
        return title;
    }

    @Override
    public BigDecimal getPrice(){
        return price;
    }

    @Override
    public double getRating(){
        return this.rating;
    }

    @Override
    public LocalDateTime getReleaseDate(){
        return releaseDate;
    }

    @Override
    public void setTitle(String title){
        this.title = title;
    }

    @Override
    public void setPrice(BigDecimal price){
        this.price = price;
    }

    @Override
    public void setReleaseDate(LocalDateTime releaseDate){
        this.releaseDate = releaseDate;
    }

    @Override
    public void rate(double rating) {
        this.rating = (this.rating * this.ratingCount + rating) / (++this.ratingCount);
    }
}

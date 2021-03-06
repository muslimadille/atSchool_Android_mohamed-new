package com.atschoolPioneerSchool.data;

import android.app.Application;

import com.atschoolPioneerSchool.model.ItemModel;
import com.atschoolPioneerSchool.model.News;
import com.atschoolPioneerSchool.model.Student;

import java.util.ArrayList;
import java.util.List;

public class GlobalVariable extends Application {
    private List<ItemModel> cart = new ArrayList<>();

    public void addCart(ItemModel model) {
        cart.add(model);
    }

    public void removeCart(ItemModel model) {
        for (int i = 0; i < cart.size(); i++) {
            if (cart.get(i).getId() == model.getId()) {
                cart.remove(i);
                break;
            }
        }
    }

    public void clearCart() {
        cart.clear();
    }

    public List<ItemModel> getCart() {
        return cart;
    }

    public long getCartPriceTotal() {
        long total = 0;
        for (int i = 0; i < cart.size(); i++) {
            total = total + cart.get(i).getSumPrice();
        }
        return total;
    }

    public int getCartItemTotal() {
        int total = 0;
        for (int i = 0; i < cart.size(); i++) {
            total = total + cart.get(i).getTotal();
        }
        return total;
    }

    public int getCartItem() {
        return cart.size();
    }

    public void updateItemTotal(ItemModel model) {
        for (int i = 0; i < cart.size(); i++) {
            if (cart.get(i).getId() == model.getId()) {
                cart.remove(i);
                cart.add(i, model);
                break;
            }
        }
    }


    public boolean isCartExist(ItemModel model) {
        for (int i = 0; i < cart.size(); i++) {
            if (cart.get(i).getId() == model.getId()) {
                return true;
            }
        }
        return false;
    }

    private List<News> allNews = new ArrayList<>();
    private List<News> newsPolitics = new ArrayList<>();
    private List<News> newsEntertainment = new ArrayList<>();
    private List<News> newsScience = new ArrayList<>();
    private List<News> newsSport = new ArrayList<>();
    private List<News> newsBusiness = new ArrayList<>();
    private List<News> newsTechnology = new ArrayList<>();
    private List<News> saved = new ArrayList<>();

    @Override
    public void onCreate() {
        allNews = Constant.getAllNews(this);
        newsPolitics = Constant.getNewsPolitics(this);
        newsEntertainment = Constant.getNewsEntertainment(this);
        newsScience = Constant.getNewsScience(this);
        newsSport = Constant.getNewsSport(this);
        newsBusiness = Constant.getNewsBusiness(this);
        newsTechnology = Constant.getNewsTechnology(this);
        super.onCreate();
    }

    public List<News> getAllNews() {
        return allNews;
    }

    // news by channel

    public List<News> getNewsPolitics() {
        return newsPolitics;
    }

    public List<News> getNewsEntertainment() {
        return newsEntertainment;
    }

    public List<News> getNewsScience() {
        return newsScience;
    }

    public List<News> getNewsSport() {
        return newsSport;
    }

    public List<News> getNewsBusiness() {
        return newsBusiness;
    }

    public List<News> getNewsTechnology() {
        return newsTechnology;
    }

    // news by category

    public List<News> getNewsLatest() {
        return allNews.subList(0, 7);
    }

    public List<News> getNewsTrending() {
        return allNews.subList(7, 12);
    }

    public List<News> getNewsHighlight() {
        return allNews.subList(12, 18);
    }

    public List<News> getNewsPopular() {
        return allNews.subList(18, 24);
    }

    public List<News> getNewsMostview() {
        return allNews.subList(24, 30);
    }

    public List<News> getSaved() {
        return saved;
    }

    public void addSaved(News s) {
        this.saved.add(s);
    }

    public void removeSaved(News s) {
        for (int i = 0; i < saved.size(); i++) {
            if (saved.get(i).getTitle().equalsIgnoreCase(s.getTitle())) {
                this.saved.remove(i);
            }
        }
    }

    public boolean isSaved(News s) {
        for (News a : saved) {
            if (a.getTitle().equalsIgnoreCase(s.getTitle())) {
                return true;
            }
        }
        return false;
    }
}

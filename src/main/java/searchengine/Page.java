package searchengine;

import java.util.ArrayList;
import java.util.List;

public class Page {
    
    // private int dateRegistered;
    // private String author;
    // private char startingLetterOfPage;
    // private int numberOfView;

    private String url;
    private String title;
    private List<String> words;

    public Page (String url, String title, List<String> words){
        this.url = url;
        this.title = title;
        words = new ArrayList<>();
    }

    public String getUrl(){
        return url;
    }

    public String getTitle(){
        return title;
    }

    public List<String> getWords(){
        return words;
    } 

      
}

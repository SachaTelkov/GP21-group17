package searchengine;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class PageDatabase{

private List<Page> allPages;

    public PageDatabase(){
        allPages = new ArrayList<>();
    }

    //Reads and split all pages in the file: 
    public void addPages(String filename){
    List<String> lines;
    try {
        lines = Files.readAllLines(Paths.get(filename));
        var lastIndex = lines.size();
        for (var i = lines.size() - 1; i >= 0; --i) {
            if (lines.get(i).startsWith("*PAGE")) {
            // last index 
            String tempUrl = lines.subList(i, lastIndex).get(0);
            String tempTitle = lines.subList(i, lastIndex).get(1);
            List<String> tepmWord = lines.subList(2, lastIndex);
            Page tempPage = new Page(tempUrl, tempTitle, tepmWord);
            allPages.add(tempPage);
            lastIndex = i;
            }
        }
    } catch (IOException e) {
        e.printStackTrace();
        }
        Collections.reverse(allPages);
    }

    //Search for searchTerm in the list of all pages:
    public ArrayList<Page> search(String searchTerm) {
        ArrayList<Page> resultList = new ArrayList<>();
        for (Page page : allPages) {
          if (page.getTitle().contains(searchTerm)) {
            resultList.add(page);
          }
          if(page.getWords().contains(searchTerm)){
            resultList.add(page);
          }
        }
        return resultList;
      }
}

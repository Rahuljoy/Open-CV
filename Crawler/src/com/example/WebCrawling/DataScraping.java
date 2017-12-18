package com.example.WebCrawling;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class DataScraping {
    public static void main(String[] args) {


        // TODO code application logic here
        try {
            final Document document = Jsoup.connect("http://www.imdb.com/chart/top").get();
            /*for(Element row : document.select("div.popular-list")){
                final String petName = row.select(".pet-item__value").text();
                //System.out.println(document.outerHtml());
                System.out.println(petName);
            }**/



          //imdb//


             for(Element row : document.select("table.chart.full-width tr")){
                final String title = row.select(".titleColumn ").text();
                final String rating = row.select(".imdbRating").text();

                //System.out.println(document.outerHtml());
                System.out.println(title + " -> Rating" + rating);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.lang.reflect.Array;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;


public class Webscraper {

    public String zacksAnalystStatusGetter(String ticker)
    {

        ArrayList<String> ratings = new ArrayList<String>();

        String rating = "";

        try {

            String url = "https://www.zacks.com/stock/quote/" + ticker + "?q=" + ticker;


            final Document document = Jsoup.connect(url).get();

            for(int i = 1; i <= 5; i++)
                ratings.add(document.getElementsByClass("rank_chip rankrect_" + i).text());


        } catch (IOException e) {
            System.out.println("Couldn't get Analyst Rating of: " + ticker);
        }

        for(int j = 0; j < 5; j++)
        {
            if(ratings.size() != 0 && !ratings.get(j).equals(""))
                rating = ratings.get(j);
        }


        return rating;
    }
    public String grabEarningsDate(String ticker) throws IOException {

        try {
            String url = "https://finance.yahoo.com/quote/" + ticker;


            final Document document = Jsoup.connect(url).get();

            String earningsDate = document.getElementsByAttributeValue("data-test", "EARNINGS_DATE-value").text();
            return earningsDate;

        } catch (HttpStatusException | FileNotFoundException e) {
            // TODO Auto-generated catch block
            //e.printStackTrace();
            System.out.println("Couldn't get the link to: " + ticker);

        }

        return null;

    }
    public double grabAnalystTarget(String ticker) throws IOException {
        double returnedTarget = -1;
        try {
            String url = "https://finance.yahoo.com/quote/" + ticker;


            final Document document = Jsoup.connect(url).get();

            String target = document.getElementsByAttributeValue("data-test", "ONE_YEAR_TARGET_PRICE-value").text();
            if(!target.isEmpty())
                try {
                    returnedTarget= Double.parseDouble(target);
                } catch (Exception e) {
                    returnedTarget = -1;
                }
            return (returnedTarget);

        } catch (HttpStatusException | FileNotFoundException e) {
            // TODO Auto-generated catch block
            //e.printStackTrace();
            System.out.println("Couldn't get the link to: " + ticker);

        }

        return returnedTarget;
    }
    public double grabPercentDifference(String ticker) throws IOException {
        double price = grabPrice(ticker);
        double target = grabAnalystTarget(ticker);
        double percentage = ((target-price)/(price))*100;
        return percentage;
    }

    public double grabPrice(String ticker) throws IOException{


        double returnedPrice = -1;
        try {
            String url = "https://www.zacks.com/stock/quote/" + ticker;

            //yahoo finance class for price: e3b14781 f5a023e1
            final Document document = Jsoup.connect(url).get();

            String price = document.getElementsByClass("last_price").text();

            price = price.replace("$", "");
            price = price.replace(" USD", "");
            price = price.replace(",", "");

            //System.out.println(price);
            if(!price.isEmpty())
                try {
                    returnedPrice = Double.parseDouble(price);
                } catch (Exception e) {
                    returnedPrice = -1;
                }

            return (returnedPrice);

        } catch (HttpStatusException | FileNotFoundException e) {
            // TODO Auto-generated catch block
            //e.printStackTrace();
            System.out.println("Couldn't get the link to: " + ticker);

        }

        return returnedPrice;

    }

    public long grabVolume(String ticker) throws IOException {

        try {
            String url = "https://finance.yahoo.com/quote/" + ticker;


            final Document document = Jsoup.connect(url).get();

            String volume = document.getElementsByAttributeValue("data-test", "AVERAGE_VOLUME_3MONTH-value").text();

            volume = volume.replace(",", "");

            //volume class for yahoo finance: e3b14781 e983cf79
            //System.out.println(price);
            if(!volume.isEmpty())
                return Long.parseLong(volume);
            return (-1);

        } catch (HttpStatusException | FileNotFoundException e) {
            // TODO Auto-generated catch block
            //e.printStackTrace();
            System.out.println("Couldn't get the link to: " + ticker);

        }

        return -1;

    }






}


import java.io.*;
import java.util.Objects;
import java.util.Scanner;

/**
 * @author ArunIyer05
 */
//adapted from Zacks Webscraper by Yashidev556
public class Main {

    public static void main (String[] args) throws IOException {



        Webscraper webScraper = new Webscraper();


        Scanner input = new Scanner(System.in);
        String analystDesiredRating = "";
        System.out.println("What do you want to search for? \n Analyst Ratings (1); 1-Year Target (2); Earnings (3)");
        String option = input.nextLine();
        String month = "";
        long percentage = 0;

        switch (option) {
            case "1" -> {
                System.out.println("What rating stocks would you like to search for? (1-5, with 1 being strong buy and 5 being strong sell): ");
                analystDesiredRating = input.nextLine();
            }
            case "2" -> {
                System.out.println("What percentage discrepancy between analyst 1-year target and current price do you want? Enter as a number (positive or negative) with no extra characters.");

                while (!input.hasNextLong()) {
                    System.out.println("Invalid percentage. Please try again.");
                   input.next();
                }
                percentage = input.nextLong();
            }
            case "3" -> {
                System.out.println("What month do you want to check earnings for? Enter your input as the first 3 letters of the month. Ex: Jan.");
                month = input.nextLine();
            }
        }

        System.out.println("Minimum Price? (Enter an Integer value): ");
        int min = -1;
        while (!input.hasNextInt()) {
            System.out.println("Invalid syntax. Please input a number.");
            input.next();
        }
        min = input.nextInt();


        System.out.println("Maximum Price? (Enter an Integer value): ");
        int max = -1;
        while (!input.hasNextInt()) {
            System.out.println("Invalid syntax. Please input a number.");
            input.next();
        }
        max = input.nextInt();

        String answertoVol = "";
        System.out.println("Do You wish to search for Average Volume? (Y/N): ");
        do {
            answertoVol = input.nextLine();
        } while (!answertoVol.equals("Y") && !answertoVol.equals("y") && !answertoVol.equals("N") && !answertoVol.equals("n"));
        long minVol = -1;

        long maxVol = -1;

        if ((answertoVol.equals("Y")) || (answertoVol.equals("y"))) {
            System.out.println("Average Volume Minimum? (Enter an Integer)");
            while (!input.hasNextInt()) {
                System.out.println("Invalid syntax. Please input a number.");
                input.next();
            }
            minVol = input.nextInt();
            System.out.println("Average Volume Maxmimum? (Enter an Integer)");
            while (!input.hasNextInt()) {
                System.out.println("Invalid syntax. Please input a number.");
                input.next();
            }
            maxVol = input.nextInt();


    }








        // Open the file
        FileInputStream fstream = new FileInputStream("StockTickers.txt");
        BufferedReader br = new BufferedReader(new InputStreamReader(fstream));


        StringBuilder loadingAnim = new StringBuilder(".");

        File log = new File("StockApplicationOutput.txt");
        try{
            if(!log.exists()){
                System.out.println("We had to make a new file.");
                log.createNewFile();
            }
        }finally
        {
            ;
        }



        String ticker;

        //Read File Line By Line
        while ((ticker = br.readLine()) != null)   {
            // Print the content on the console

            System.out.print(loadingAnim);


            //System.out.println(ticker); //delete this statement, it's for debugging purposes
            //System.out.println(webScraper.grabPrice(ticker));
            if (minVol == -1) {
                if (((option.equals("1") && webScraper.zacksAnalystStatusGetter(ticker).equals(analystDesiredRating))
                        || (option.equals("2") && ((percentage >= 0 && webScraper.grabPercentDifference(ticker) >= percentage) || (percentage < 0 && webScraper.grabPercentDifference(ticker) <= percentage)))
                        || (option.equals("3")&& webScraper.grabEarningsDate(ticker).contains(month)) )
                        && (webScraper.grabPrice(ticker) >= min) && (webScraper.grabPrice(ticker) <= max)) {
                    System.out.println();
                    System.out.println("This is Valid: " + ticker + " Price: " + webScraper.grabPrice(ticker) + " Average Volume: " + webScraper.grabVolume(ticker) + " Earnings Date: " + webScraper.grabEarningsDate(ticker)
                            + " Analyst 1-Year Target: " + webScraper.grabAnalystTarget(ticker)
                    + " Target vs. Price discrepancy: " + webScraper.grabPercentDifference(ticker)+ "%");
                    try (PrintWriter out = new PrintWriter(new FileWriter("StockApplicationOutput.txt", true))) {
                        out.append(ticker + " Price: " + webScraper.grabPrice(ticker) + " Average Volume: " + webScraper.grabVolume(ticker) + "\n" + " Earnings Date: " + webScraper.grabEarningsDate(ticker)
                                + " Analyst 1-Year Target: " + webScraper.grabAnalystTarget(ticker)
                                + " Target vs. Price discrepancy: " + webScraper.grabPercentDifference(ticker) + "%");
                    }
                }
            } else {
                if (webScraper.zacksAnalystStatusGetter(ticker).equals(analystDesiredRating) && (webScraper.grabPrice(ticker) >= min) && (webScraper.grabPrice(ticker) <= max)
                        && (webScraper.grabVolume(ticker) >= minVol) && (webScraper.grabVolume(ticker) <= maxVol)) {
                    System.out.println();
                    System.out.println("This is Valid: " + ticker + " Price: " + webScraper.grabPrice(ticker) + " Average Volume: " + webScraper.grabVolume(ticker) + " Earnings Date: " + webScraper.grabEarningsDate(ticker)
                            + " Analyst 1-Year Target: " + webScraper.grabAnalystTarget(ticker)
                            + " Target vs. Price discrepancy: " + webScraper.grabPercentDifference(ticker)+ "%");
                    try (PrintWriter out = new PrintWriter(new FileWriter("StockApplicationOutput.txt", true))) {
                        out.append(ticker + " Price: " + webScraper.grabPrice(ticker) + " Average Volume: " + webScraper.grabVolume(ticker) + "\n" + " Earnings Date: " + webScraper.grabEarningsDate(ticker)
                                + " Analyst 1-Year Target: " + webScraper.grabAnalystTarget(ticker)
                                + " Target vs. Price discrepancy: " + webScraper.grabPercentDifference(ticker)+ "%");
                    }
                }
            }



        }


        System.out.println("Press Enter to exit");
        String quitter = input.nextLine();

        //System.setOut(out);

        //Close the input stream
        fstream.close();
    }
}
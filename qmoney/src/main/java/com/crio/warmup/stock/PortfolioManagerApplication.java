
package com.crio.warmup.stock;

import com.crio.warmup.stock.dto.AnnualizedReturn;
import com.crio.warmup.stock.dto.PortfolioTrade;
import com.crio.warmup.stock.dto.TiingoCandle;
import com.crio.warmup.stock.log.UncaughtExceptionHandler;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

import org.apache.logging.log4j.ThreadContext;
import org.springframework.web.client.RestTemplate;

//import org.apache.logging.log4j.ThreadContext;

public class PortfolioManagerApplication {

  public static List<String> mainReadFile(String[] args) throws IOException, URISyntaxException {
    ObjectMapper objM = getObjectMapper();
    File file = resolveFileFromResources(args[0]);
    PortfolioTrade[] pt = objM.readValue(file, PortfolioTrade[].class);
    // System.out.println(pt);
    String set = "";
    List<String> s = new ArrayList<String>();
    for (int i = 0; i < pt.length; i++) {
      set = pt[i].getSymbol();
      s.add(set);
      
    }
    return s;
  }

  public static List<String> symPd(String[] args) throws IOException, URISyntaxException {
    ObjectMapper objM = getObjectMapper();
    File file = resolveFileFromResources(args[0]);
    PortfolioTrade[] pt = objM.readValue(file, PortfolioTrade[].class);
    String set = "";
    List<String> s = new ArrayList<String>();
    for (int i = 0; i < pt.length; i++) {
      set = pt[i].getSymbol() + "," + pt[i].getPurchaseDate();
      s.add(set);
    }
    return s;
  }

  private static void printJsonObject(Object object) throws IOException {
    Logger logger = Logger.getLogger(PortfolioManagerApplication.class.getCanonicalName());
    ObjectMapper mapper = new ObjectMapper();
    logger.info(mapper.writeValueAsString(object));
  }

  private static File resolveFileFromResources(String filename) throws URISyntaxException {
    URI th = Thread.currentThread().getContextClassLoader().getResource(filename).toURI();
    return Paths.get(th).toFile();
  }

  private static ObjectMapper getObjectMapper() {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
    return objectMapper;
  }

  public static List<String> debugOutputs() {

    String valueOfArgument0 = "trades.json";
    String x1 = "/home/crio-user/workspace/saumitrawork-ME_QMONEY/qmoney/";
    String x2 = "bin/main/trades.json";
    String x3 = "src/test/resources/assessments/trades.json";
    String resultOfResolveFilePathArgs0 = x1 + x3;
    String toStringOfObjectMapper = "com.fasterxml.jackson.databind.ObjectMapper@373ebf74";
    String functionNameFromTestFileInStackTrace = "mainReadFile()";
    String lineNumberFromTestFileInStackTrace = "22";
    return Arrays.asList(new String[] { 
      valueOfArgument0, resultOfResolveFilePathArgs0, toStringOfObjectMapper,
        functionNameFromTestFileInStackTrace, lineNumberFromTestFileInStackTrace });
  }

  // TODO: CRIO_TASK_MODULE_REST_API
  // Copy the relavent code from #mainReadFile to parse the Json into
  // PortfolioTrade list.
  // Now That you have the list of PortfolioTrade already populated in module#1
  // For each stock symbol in the portfolio trades,
  // Call Tiingo api
  // (https://api.tiingo.com/tiingo/daily/<ticker>/prices?startDate=&endDate=&token=)
  // with
  // 1. ticker = symbol in portfolio_trade
  // 2. startDate = purchaseDate in portfolio_trade.
  // 3. endDate = args[1]
  // Use RestTemplate#getForObject in order to call the API,
  // and deserialize the results in List<Candle>
  // Note - You may have to register on Tiingo to get the api_token.
  // Please refer the the module documentation for the steps.
  // Find out the closing price of the stock on the end_date and
  // return the list of all symbols in ascending order by its close value on
  // endDate
  // Test the function using gradle commands below
  // ./gradlew run --args="trades.json 2020-01-01"
  // ./gradlew run --args="trades.json 2019-07-01"
  // ./gradlew run --args="trades.json 2019-12-03"
  // And make sure that its printing correct results.

  public static List<String> mainReadQuotes(String[] args) throws IOException, URISyntaxException {

    List<Double> ansPrices = new ArrayList<Double>();
    String date = args[1];
    List<String> s = symPd(args);
    //Collections.sort(s);
    for (int i = 0; i < s.size(); i++) {
      String stockname = s.get(i).substring(0, s.get(i).indexOf(",")).toLowerCase();
      String startdate = s.get(i).substring(s.get(i).indexOf(",") + 1, s.get(i).length());
      String url = "https://api.tiingo.com/tiingo/daily/" + stockname + "/prices?startDate=";
      url = url + startdate + "&endDate=" + date;
      url = url + "&token=3363b903ca0b6da429d1fa9209f385716d4a6359";
      ObjectMapper mapper = new ObjectMapper();
      mapper.registerModule(new JavaTimeModule());
      RestTemplate restTemplate = new RestTemplate();
      String result = restTemplate.getForObject(url, String.class);
      List<TiingoCandle> collec = mapper.readValue(result,
              new TypeReference<ArrayList<TiingoCandle>>() {
            });
      ansPrices.add(collec.get(collec.size() - 1).getClose());
      s.set(i,stockname.toUpperCase());
    }

    for (int i = 0; i < ansPrices.size() - 1; i++) {
      for (int j = 0; j < ansPrices.size() - 1 - i; j++) {
        if (ansPrices.get(j) >= ansPrices.get(j + 1)) {
          double dswp = ansPrices.get(j);
          ansPrices.set(j,ansPrices.get(j + 1));
          ansPrices.set(j + 1,dswp);
          String swap = s.get(j);
          s.set(j,s.get(j + 1));
          s.set(j + 1,swap); 
        }
      }
    }




    //printJsonObject(ansPrices);

    return s;
  }



  // TODO: CRIO_TASK_MODULE_CALCULATIONS
  //  Copy the relevant code from #mainReadQuotes to parse the Json into PortfolioTrade list and
  //  Get the latest quotes from TIingo.
  //  Now That you have the list of PortfolioTrade And their data,
  //  With this data, Calculate annualized returns for the stocks provided in the Json
  //  Below are the values to be considered for calculations.
  //  buy_price = open_price on purchase_date and sell_value = close_price on end_date
  //  startDate and endDate are already calculated in module2
  //  using the function you just wrote #calculateAnnualizedReturns
  //  Return the list of AnnualizedReturns sorted by annualizedReturns in descending order.
  //  use gralde command like below to test your code
  //  ./gradlew run --args="trades.json 2020-01-01"
  //  ./gradlew run --args="trades.json 2019-07-01"
  //  ./gradlew run --args="trades.json 2019-12-03"
  //  where trades.json is your json file

  public static List<AnnualizedReturn> mainCalculateSingleReturn(String[] args)
      throws IOException, URISyntaxException {


    List<Double> sellValue = new ArrayList<Double>();
    List<Double> buyValue = new ArrayList<Double>();

    String date = args[1];
    ObjectMapper objM = getObjectMapper();
    File file = resolveFileFromResources(args[0]);
    PortfolioTrade[] pt = objM.readValue(file, PortfolioTrade[].class);
    String set = "";
    List<String> s = new ArrayList<String>();
    for (int i = 0; i < pt.length; i++) {
      set = pt[i].getSymbol() + "," + pt[i].getPurchaseDate();
      s.add(set);
    }
    for (int i = 0; i < s.size(); i++) {
      String stockname = s.get(i).substring(0, s.get(i).indexOf(",")).toLowerCase();
      String startdate = s.get(i).substring(s.get(i).indexOf(",") + 1, s.get(i).length());
      String url = "https://api.tiingo.com/tiingo/daily/" + stockname + "/prices?startDate=";
      url = url + startdate + "&endDate=" + date;
      url = url + "&token=3363b903ca0b6da429d1fa9209f385716d4a6359";
      ObjectMapper mapper = new ObjectMapper();
      mapper.registerModule(new JavaTimeModule());
      RestTemplate restTemplate = new RestTemplate();
      String result = restTemplate.getForObject(url, String.class);
      List<TiingoCandle> collec = mapper.readValue(result,
              new TypeReference<ArrayList<TiingoCandle>>() {
            });
      sellValue.add(collec.get(collec.size() - 1).getClose());
      buyValue.add(collec.get(0).getOpen());

      //s.set(i,stockname.toUpperCase());
    }
    List<AnnualizedReturn> ar = new ArrayList<AnnualizedReturn>();
    for (int i = 0; i < sellValue.size(); i++) {
      ar.add(calculateAnnualizedReturns(LocalDate.parse(args[1]), 
            pt[i], buyValue.get(i), sellValue.get(i)));
    }
    return ar;
  }

  // TODO: CRIO_TASK_MODULE_CALCULATIONS
  //  annualized returns should be calculated in two steps -
  //  1. Calculate totalReturn = (sell_value - buy_value) / buy_value
  //  Store the same as totalReturns
  //  2. calculate extrapolated annualized returns by scaling the same in years span. The formula is
  //  annualized_returns = (1 + total_returns) ^ (1 / total_num_years) - 1
  //  Store the same as annualized_returns
  //  return the populated list of AnnualizedReturn for all stocks,
  //  Test the same using below specified command. The build should be successful
  //  ./gradlew test --tests PortfolioManagerApplicationTest.testCalculateAnnualizedReturn

  public static AnnualizedReturn calculateAnnualizedReturns(LocalDate endDate,
      PortfolioTrade trade, Double buyPrice, Double sellPrice) {
    double totalReturns = (sellPrice - buyPrice) / buyPrice;
    LocalDate startDate = trade.getPurchaseDate();
    
    long days = ChronoUnit.DAYS.between(startDate, endDate);
    if (days <= 0) {
      days = 1;
    }
    double tny = (double)365 / days;
    double annualizedReturns = (double)Math.pow((1 + totalReturns), tny) - 1;

    return new AnnualizedReturn(trade.getSymbol(), annualizedReturns, totalReturns);
  }











  public static void main(String[] args) throws Exception {
    Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler());
    ThreadContext.put("runId", UUID.randomUUID().toString());

    // printJsonObject(mainReadFile(args));
    //printJsonObject(mainReadQuotes(args));
    printJsonObject(mainCalculateSingleReturn(args));
    //calculateAnnualizedReturns();

  }
}


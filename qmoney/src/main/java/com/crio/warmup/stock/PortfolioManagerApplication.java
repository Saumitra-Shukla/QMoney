
package com.crio.warmup.stock;

import com.crio.warmup.stock.dto.AnnualizedReturn;
import com.crio.warmup.stock.dto.Candle;
import com.crio.warmup.stock.dto.PortfolioTrade;
import com.crio.warmup.stock.dto.TiingoCandle;
import com.crio.warmup.stock.dto.TotalReturnsDto;
import com.crio.warmup.stock.log.UncaughtExceptionHandler;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

//import net.bytebuddy.implementation.bytecode.Size;


//import java.nio.file.Paths;

//import java.util.Arrays;



//import java.util.logging.Logger;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
//import java.util.List;
//import java.util.UUID;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.web.client.RestTemplate;

//import org.apache.logging.log4j.ThreadContext;

public class PortfolioManagerApplication {

  // TODO: CRIO_TASK_MODULE_JSON_PARSING
  // Read the json file provided in the argument[0]. The file will be avilable in
  // the classpath.
  // 1. Use #resolveFileFromResources to get actual file from classpath.
  // 2. parse the json file using ObjectMapper provided with #getObjectMapper,
  // and extract symbols provided in every trade.
  // return the list of all symbols in the same order as provided in json.
  // Test the function using gradle commands below
  // ./gradlew run --args="trades.json"
  // Make sure that it prints below String on the console -
  // ["AAPL","MSFT","GOOGL"]
  // Now, run
  // ./gradlew build and make sure that the build passes successfully
  // There can be few unused imports, you will need to fix them to make the build
  // pass.

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
      /*
       * String stock_name =
       * s.get(i).substring(0,s.get(i).indexOf(",")).toLowerCase(); String start_date
       * = s.get(i).substring(s.get(i).indexOf(",")+1,s.get(i).length());
       * System.out.println(stock_name + " " + start_date);
       */
    }
    /*
     * for (int i = 0; i < pt.length; i++) { System.out.println(s.get(i)); }
     */
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

  // TODO: CRIO_TASK_MODULE_JSON_PARSING
  // Follow the instructions provided in the task documentation and fill up the
  // correct values for
  // the variables provided. First value is provided for your reference.
  // A. Put a breakpoint on the first line inside mainReadFile() which says
  // return Collections.emptyList();
  // B. Then Debug the test #mainReadFile provided in
  // PortfoliomanagerApplicationTest.java
  // following the instructions to run the test.
  // Once you are able to run the test, perform following tasks and record the
  // output as a
  // String in the function below.
  // Use this link to see how to evaluate expressions -
  // https://code.visualstudio.com/docs/editor/debugging#_data-inspection
  // 1. evaluate the value of "args[0]" and set the value
  // to the variable named valueOfArgument0 (This is implemented for your
  // reference.)
  // 2. In the same window, evaluate the value of expression below and set it
  // to resultOfResolveFilePathArgs0
  // expression ==> resolveFileFromResources(args[0])
  // 3. In the same window, evaluate the value of expression below and set it
  // to toStringOfObjectMapper.
  // You might see some garbage numbers in the output. Dont worry, its expected.
  // expression ==> getObjectMapper().toString()
  // 4. Now Go to the debug window and open stack trace. Put the name of the
  // function you see at
  // second place from top to variable functionNameFromTestFileInStackTrace
  // 5. In the same window, you will see the line number of the function in the
  // stack trace window.
  // assign the same to lineNumberFromTestFileInStackTrace
  // Once you are done with above, just run the corresponding test and
  // make sure its working as expected. use below command to do the same.
  // ./gradlew test --tests PortfolioManagerApplicationTest.testDebugValues

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

  public static void main(String[] args) throws Exception {
    Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler());
    ThreadContext.put("runId", UUID.randomUUID().toString());

    // printJsonObject(mainReadFile(args));
    printJsonObject(mainReadQuotes(args));

  }
}

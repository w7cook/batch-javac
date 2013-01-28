package sql.northwind.LINQ101;

import java.sql.SQLException;

import sql.northwind.BaseTest;
import batch.Fun;
import batch.sql.Many;
import batch.util.MemSet;

public class LINQConversion extends BaseTest {
  public static void main(String[] args) throws SQLException {
    new LINQConversion().run();
  }

  /* ToArray
  public void Linq54()
  {
      double[] doubles = { 1.7, 2.3, 1.9, 4.1, 2.9 };
  
      var sortedDoubles =
          from d in doubles
          orderby d descending
          select d;
      var doublesArray = sortedDoubles.ToArray();
  
      Console.WriteLine("Every other double from highest to lowest:");
      for (int d = 0; d < doublesArray.Length; d += 2)
      {
          Console.WriteLine(doublesArray[d]);
      }
  }
  */

  private void Batch54() {
    print("***** Batch54");
    Many<Double> doubles = MemSet.make(1.7, 2.3, 1.9, 4.1, 2.9);
    Fun<Double, Double> idF = new Fun<Double, Double>() {
      public Double apply(Double d) {
        return d;
      }
    };
    int index = 0;
    print("Every other double from highest to lowest:");
    for (Double d : doubles.orderByDescending(idF)) {
      if (index % 2 == 0)
        print("{0}", d);
      index = index + 1;
    }
  }

  /* ToList
   
  public void Linq55()
  {
      string[] words = { "cherry", "apple", "blueberry" };
  
      var sortedWords =
          from w in words
          orderby w
          select w;
      var wordList = sortedWords.ToList();
  
      Console.WriteLine("The sorted word list:");
      foreach (var w in wordList)
      {
          Console.WriteLine(w);
      }
  }
   */

  private void Batch55() {
    print("***** Batch55");

    Many<String> words = MemSet.make("cherry", "apple", "blueberry");
    Fun<String, String> idF = new Fun<String, String>() {
      public String apply(String s) {
        return s;
      }
    };
    print("The sorted word list:");
    for (String s : words.orderBy(idF))
      print("{0}", s);
  }

  /* OfType
  
  public void Linq57()
  {
      object[] numbers = { null, 1.0, "two", 3, "four", 5, "six", 7.0 };
  
      var doubles = numbers.OfType<double>();
  
      Console.WriteLine("Numbers stored as doubles:");
      foreach (var d in doubles)
      {
          Console.WriteLine(d);
      }
  }
   */

  private void Batch57() {
    print("***** Batch57");

    Object[] objects = { null, 1.0, "two", 3, "four", 5, "six", 7.0 };

    print("Numbers stored as doubles:");
    for (Object o : objects)
      if (o instanceof Double)
        print("{0}", o);
  }

  public void test() throws SQLException {
    Batch54();
    Batch55();
    Batch57();
  }

}

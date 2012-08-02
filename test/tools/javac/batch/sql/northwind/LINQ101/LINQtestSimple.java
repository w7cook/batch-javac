/*******************************************************************************
 * The accompanying source code is made available to you under the terms of 
 * the UT Research License (this "UTRL"). By installing or using the code, 
 * you are consenting to be bound by the UTRL. See LICENSE.html for a 
 * full copy of the license.
 * 
 * Copyright © 2009, The University of Texas at Austin. All rights reserved.
 * 
 * UNIVERSITY EXPRESSLY DISCLAIMS ANY AND ALL WARRANTIES CONCERNING THIS 
 * SOFTWARE AND DOCUMENTATION, INCLUDING ANY WARRANTIES OF MERCHANTABILITY, 
 * FITNESS FOR ANY PARTICULAR PURPOSE, NON-INFRINGEMENT AND WARRANTIES 
 * OF PERFORMANCE, AND ANY WARRANTY THAT MIGHT OTHERWISE ARISE FROM COURSE 
 * OF DEALING OR USAGE OF TRADE. NO WARRANTY IS EITHER EXPRESS OR IMPLIED 
 * WITH RESPECT TO THE USE OF THE SOFTWARE OR DOCUMENTATION. Under no circumstances 
 * shall University be liable for incidental, special, indirect, direct 
 * or consequential damages or loss of profits, interruption of business, 
 * or related expenses which may arise from use of Software or Documentation, 
 * including but not limited to those resulting from defects in Software 
 * and/or Documentation, or loss or inaccuracy of data of any kind.
 * 
 * Created by: William R. Cook and Eli Tilevich
 * with: Jose Falcon, Marc Fisher II, Ali Ibrahim, Yang Jiao, Ben Wiedermann
 * University of Texas at Austin and Virginia Tech
 ******************************************************************************/
package sql.northwind.LINQ101;

import java.sql.SQLException;

import sql.northwind.BaseTest;
import batch.Fun;
import batch.util.MemSet;

public class LINQtestSimple extends BaseTest {
public static void main(String[] args) throws SQLException {
  new LINQtestSimple().run();
}

public void Batch1() {
  print("Numbers < 5:");
  int[] numbers = { 5, 4, 1, 3, 9, 8, 6, 7, 2, 0 };
  for (int x : numbers) {
    if (x < 5)
      print("{0}", x);
  }
}

public void Batch5() {
  String[] digits = { "zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine" };

  print("Short digits:");
  int index = 0;
  for (String d : digits) {
    index = index + 1;
    if (d.length() < index)
      print("The word {0} is shorter than its value.", d);
  }
}

public void Batch6() {
  int[] numbers = { 5, 4, 1, 3, 9, 8, 6, 7, 2, 0 };

  print("Numbers + 1:");
  for (int i : numbers)
    print("{0}", i + 1);
}

public void Batch8() {
  int[] numbers = { 5, 4, 1, 3, 9, 8, 6, 7, 2, 0 };
  String[] strings = { "zero", "one", "two", "three", "four", "five", "six",
      "seven", "eight", "nine" };

  print("Number strings:");
  for (int n : numbers)
    print(strings[n]);
}

public void Batch9() {
  String[] words = { "aPPLE", "BlUeBeRrY", "cHeRry" };
  for (String w : words)
    print("Uppercase: {0}, Lowercase: {1}", w.toUpperCase(), w.toLowerCase());
}

public void Batch10() {
  int[] numbers = { 5, 4, 1, 3, 9, 8, 6, 7, 2, 0 };
  String[] strings = { "zero", "one", "two", "three", "four", "five", "six",
      "seven", "eight", "nine" };

  for (int n : numbers)
    print("The digit {0} is {1}.", strings[n], (n % 2 == 0) ? "even" : "odd");
}

public void Batch12() {
  int[] numbers = { 5, 4, 1, 3, 9, 8, 6, 7, 2, 0 };

  print("Number: In-place?");
  int index = 0;
  for (int n : numbers) {
    index = index + 1;
    print("{0}: {1}", n, (n == index));
  }
}

public void Batch13() {
  int[] numbers = { 5, 4, 1, 3, 9, 8, 6, 7, 2, 0 };
  String[] digits = { "zero", "one", "two", "three", "four", "five", "six",
      "seven", "eight", "nine" };

  print("Numbers < 5:");
  for (int n : numbers)
    if (n < 5)
      print(digits[n]);
}

public void Batch14() {
  int[] numbersA = { 0, 2, 4, 5, 6, 8, 9 };
  int[] numbersB = { 1, 3, 5, 7, 8 };
  print("Pairs where a < b:");
  for (int a : numbersA)
    for (int b : numbersB)
      if (a < b)
        print("{0} is less than {1}", a, b);
}

/*
 * Take - Simple
 * 
 * public void Batch20() { int[] numbers = { 5, 4, 1, 3, 9, 8, 6, 7, 2, 0 }; var
 * first3Numbers = numbers.Take(3); Console.WriteLine("First 3 numbers:");
 * foreach (var n in first3Numbers) { Console.WriteLine(n); } }
 */

/*
 * Skip - Simple
 * 
 * public void Batch22() { int[] numbers = { 5, 4, 1, 3, 9, 8, 6, 7, 2, 0 }; var
 * allButFirst4Numbers = numbers.Skip(4);
 * Console.WriteLine("All but first 4 numbers:"); foreach (var n in
 * allButFirst4Numbers) { Console.WriteLine(n); } }
 */

/*
 * OrderBy - Simple 1
 * 
 * public void Batch28() { string[] words = { "cherry", "apple", "blueberry" };
 * 
 * var sortedWords = from w in words orderby w select w;
 * 
 * Console.WriteLine("The sorted list of words:"); foreach (var w in
 * sortedWords) { Console.WriteLine(w); } }
 */

/*
 * OrderBy - Simple 2
 * 
 * public void Batch29() { string[] words = { "cherry", "apple", "blueberry" };
 * 
 * var sortedWords = from w in words orderby w.Length select w;
 * 
 * Console.WriteLine("The sorted list of words (by length):"); foreach (var w in
 * sortedWords) { Console.WriteLine(w); } }
 */

/*
 * OrderBy - Comparer
 * 
 * public void Batch31() { string[] words = { "aPPLE", "AbAcUs", "bRaNcH",
 * "BlUeBeRrY", "ClOvEr", "cHeRry" };
 * 
 * var sortedWords = words.OrderBy(a => a, new CaseInsensitiveComparer());
 * 
 * ObjectDumper.Write(sortedWords); }
 * 
 * public class CaseInsensitiveComparer : IComparer<string> { public int
 * Compare(string x, string y) { return string.Compare(x, y,
 * StringComparison.OrdinalIgnoreCase); } }
 */

/*
 * OrderByDescending - Simple 1
 * 
 * public void Batch32() { double[] doubles = { 1.7, 2.3, 1.9, 4.1, 2.9 };
 * 
 * var sortedDoubles = from d in doubles orderby d descending select d;
 * 
 * Console.WriteLine("The doubles from highest to lowest:"); foreach (var d in
 * sortedDoubles) { Console.WriteLine(d); } }
 */

/*
 * OrderByDescending - Comparer
 * 
 * public void Batch34() { string[] words = { "aPPLE", "AbAcUs", "bRaNcH",
 * "BlUeBeRrY", "ClOvEr", "cHeRry" };
 * 
 * var sortedWords = words.OrderByDescending(a => a, new
 * CaseInsensitiveComparer());
 * 
 * ObjectDumper.Write(sortedWords); }
 * 
 * public class CaseInsensitiveComparer : IComparer<string> { public int
 * Compare(string x, string y) { return string.Compare(x, y,
 * StringComparison.OrdinalIgnoreCase); } }
 */

/*
 * ThenBy - Simple
 * 
 * public void Batch35() { string[] digits = { "zero", "one", "two", "three",
 * "four", "five", "six", "seven", "eight", "nine" };
 * 
 * var sortedDigits = from d in digits orderby d.Length, d select d;
 * 
 * Console.WriteLine("Sorted digits:"); foreach (var d in sortedDigits) {
 * Console.WriteLine(d); } }
 */

/*
 * ThenBy - Comparer
 * 
 * public void Batch36() { string[] words = { "aPPLE", "AbAcUs", "bRaNcH",
 * "BlUeBeRrY", "ClOvEr", "cHeRry" };
 * 
 * var sortedWords = words.OrderBy(a => a.Length) .ThenBy(a => a, new
 * CaseInsensitiveComparer());
 * 
 * ObjectDumper.Write(sortedWords); }
 * 
 * public class CaseInsensitiveComparer : IComparer<string> { public int
 * Compare(string x, string y) { return string.Compare(x, y,
 * StringComparison.OrdinalIgnoreCase); } }
 */

/*
 * ThenByDescending - Comparer
 * 
 * public void Batch38() { string[] words = { "aPPLE", "AbAcUs", "bRaNcH",
 * "BlUeBeRrY", "ClOvEr", "cHeRry" };
 * 
 * var sortedWords = words.OrderBy(a => a.Length) .ThenByDescending(a => a, new
 * CaseInsensitiveComparer());
 * 
 * ObjectDumper.Write(sortedWords); }
 * 
 * public class CaseInsensitiveComparer : IComparer<string> { public int
 * Compare(string x, string y) { return string.Compare(x, y,
 * StringComparison.OrdinalIgnoreCase); } }
 */

/*
 * Reverse
 * 
 * This sample uses Reverse to create a list of all digits in the array whose
 * second letter is 'i' that is reversed from the order in the original array.
 * 
 * public void Batch39() { string[] digits = { "zero", "one", "two", "three",
 * "four", "five", "six", "seven", "eight", "nine" };
 * 
 * var reversedIDigits = ( from d in digits where d[1] == 'i' select d)
 * .Reverse();
 * 
 * Console.WriteLine(
 * "A backwards list of the digits with a second character of 'i':"); foreach
 * (var d in reversedIDigits) { Console.WriteLine(d); } }
 */

/*
 * Count - Simple
 * 
 * public void Batch73() { int[] factorsOf300 = { 2, 2, 3, 5, 5 };
 * 
 * int uniqueFactors = factorsOf300.Distinct().Count();
 * 
 * Console.WriteLine("There are {0} unique factors of 300.", uniqueFactors); }
 */

public void Batch73() {
  print("Count - Simple");
  MemSet<Integer> factorsOf300 = MemSet.make(2, 2, 3, 5, 5);

  int uniqueFactors = factorsOf300.distinct().count();

  print("There are {0} unique factors of 300.", uniqueFactors);
}

/*
 * Count - Conditional
 * 
 * public void Batch74() { int[] numbers = { 5, 4, 1, 3, 9, 8, 6, 7, 2, 0 };
 * 
 * int oddNumbers = numbers.Count(n => n % 2 == 1);
 * 
 * Console.WriteLine("There are {0} odd numbers in the list.", oddNumbers); }
 */

public void Batch74() {
  int[] numbers = { 5, 4, 1, 3, 9, 8, 6, 7, 2, 0 };

  int oddNumbers = 0;
  for (int n : numbers)
    if (n % 2 == 1)
      oddNumbers++;
  print("There are {0} odd numbers in the list.", oddNumbers);
}

/*
 * Count - Indexed
 * 
 * public void Batch75() { int[] numbers = { 5, 4, 1, 3, 9, 8, 6, 7, 2, 0 };
 * 
 * int oddEvenMatches = numbers.Count((n, index) => n % 2 == index % 2);
 * 
 * Console.WriteLine("There are {0} numbers in the list whose odd/even status "
 * + "matches that of their position.", oddEvenMatches); }
 */

public void Batch75() {
  int[] numbers = { 5, 4, 1, 3, 9, 8, 6, 7, 2, 0 };

  int oddEvenMatches = 0;
  int index = 0;
  for (int n : numbers)
    if (n % 2 == index++ % 2)
      oddEvenMatches++;

  print("There are {0} numbers in the list whose odd/even status "
      + "matches that of their position.", oddEvenMatches);
}

/*
 * Sum - Simple
 * 
 * public void Batch78() { int[] numbers = { 5, 4, 1, 3, 9, 8, 6, 7, 2, 0 };
 * 
 * double numSum = numbers.Sum();
 * 
 * Console.WriteLine("The sum of the numbers is {0}.", numSum); }
 */
public void Batch78() {
  int[] numbers = { 5, 4, 1, 3, 9, 8, 6, 7, 2, 0 };

  int numSum = 0;
  for (int x : numbers)
    numSum += x;

  print("The sum of the numbers is {0}.", numSum);
}

/*
 * Sum - Projection
 * 
 * public void Batch79() { string[] words = { "cherry", "apple", "blueberry" };
 * 
 * double totalChars = words.Sum(w => w.Length);
 * 
 * Console.WriteLine("There are a total of {0} characters in these words.",
 * totalChars); }
 */
static Fun<String, Long> StringLength = new Fun<String, Long>() {
  public Long apply(String s) {
    return (long) s.length();
  }
};

public void Batch79() {
  MemSet<String> words = MemSet.make("cherry", "apple", "blueberry");

  long totalChars = words.sum(StringLength);

  print("There are a total of {0} characters in these words.", totalChars);
}

/*
 * Min - Simple
 * 
 * public void Batch81() { int[] numbers = { 5, 4, 1, 3, 9, 8, 6, 7, 2, 0 };
 * 
 * int minNum = numbers.Min();
 * 
 * Console.WriteLine("The minimum number is {0}.", minNum); }
 */
public void Batch81() {
  int[] numbers = { 5, 4, 1, 3, 9, 8, 6, 7, 2, 0 };

  int minNum = numbers[0];
  for (int n : numbers)
    if (minNum < 0 || n < minNum)
      minNum = n;

  print("The minimum number is {0}.", minNum);
}

/*
 * Min - Projection
 * 
 * public void Batch82() { string[] words = { "cherry", "apple", "blueberry" };
 * 
 * int shortestWord = words.Min(w => w.Length);
 * 
 * Console.WriteLine("The shortest word is {0} characters long.", shortestWord);
 * }
 */

public void Batch82() {
  MemSet<String> words = MemSet.make("cherry", "apple", "blueberry");

  long shortestWord = words.min(StringLength);

  print("The shortest word is {0} characters long.", shortestWord);
}

/*
 * Max - Simple
 * 
 * public void Batch85() { int[] numbers = { 5, 4, 1, 3, 9, 8, 6, 7, 2, 0 };
 * 
 * int maxNum = numbers.Max();
 * 
 * Console.WriteLine("The maximum number is {0}.", maxNum); }
 */

/*
 * Max - Projection
 * 
 * This sample uses Max to get the length of the longest word in a string array.
 * 
 * public void Batch86() { string[] words = { "cherry", "apple", "blueberry" };
 * 
 * int longestLength = words.Max(w => w.Length);
 * 
 * Console.WriteLine("The longest word is {0} characters long.", longestLength);
 * }
 */

/*
 * Average - Simple
 * 
 * public void Batch89() { int[] numbers = { 5, 4, 1, 3, 9, 8, 6, 7, 2, 0 };
 * 
 * double averageNum = numbers.Average();
 * 
 * Console.WriteLine("The average number is {0}.", averageNum); }
 */

/*
 * Average - Projection
 * 
 * public void Batch90() { string[] words = { "cherry", "apple", "blueberry" };
 * 
 * double averageLength = words.Average(w => w.Length);
 * 
 * Console.WriteLine("The average word length is {0} characters.",
 * averageLength); }
 */

/*
 * Aggregate - Simple
 * 
 * public void Batch92() { double[] doubles = { 1.7, 2.3, 1.9, 4.1, 2.9 };
 * 
 * double product = doubles.Aggregate((runningProduct, nextFactor) =>
 * runningProduct * nextFactor);
 * 
 * Console.WriteLine("Total product of all numbers: {0}", product); }
 */

/*
 * Aggregate - Seed
 * 
 * 
 * This sample subtracts a sequence of integers from a starting value,
 * simulating withdrawls from an account. While there is still cash left in the
 * account, the withdrawal succeeds. The sample uses Aggregate to pass each
 * withdrawal value in turn to the lambda expression that performs the
 * subtraction.
 * 
 * public void Batch93() { double startBalance = 100.0;
 * 
 * int[] attemptedWithdrawals = { 20, 10, 40, 50, 10, 70, 30 };
 * 
 * double endBalance = attemptedWithdrawals.Aggregate(startBalance, (balance,
 * nextWithdrawal) => ( (nextWithdrawal <= balance) ? (balance - nextWithdrawal)
 * : balance ) );
 * 
 * Console.WriteLine("Ending balance: {0}", endBalance); }
 */

public void test() throws SQLException {
  Batch1();
  Batch5();
  Batch6();
  Batch8();
  Batch9();
  Batch10();
  Batch12();
  Batch13();
  Batch14();
  Batch73();
  Batch74();
  Batch75();
  Batch78();
  Batch79();
  Batch81();
  Batch82();
}

}

package pontis.promotion;

import java.util.ArrayList;
import java.util.List;

import org.civet.Civet;
import org.civet.Civet.Compile;

import pontis.discounter.FixedPrice;
import pontis.discounter.PercentageDiscounter;
import pontis.discounter.StepDiscounterByQuantity;
import pontis.eligibility.AllEligible;
import pontis.eligibility.EligibilityAnd;
import pontis.eligibility.EligibilityByPrice;
import pontis.eligibility.EligibilityByPropertyValue;
import pontis.event.An_Event;
import pontis.event.MovieRentalEvent;

public class PromotionSystem {

  List<A_Promotion> promotions = new ArrayList<A_Promotion>();

  public void addPromotion(A_Promotion promotion) {
    promotions.add(promotion);
  }

  public double calcPromotionalPrice(An_Event theEvent) {
    double result = theEvent.getListPrice();
    List<Double> promotionalPrices = new ArrayList<Double>();
    for (A_Promotion promotion : promotions) {
      double promotionalPrice = promotion.calcPromotionalPrice(theEvent);
      promotionalPrices.add(promotionalPrice);
    }
    if (promotionalPrices.size() > 0) {
      result = findLowestPrice(result, promotionalPrices);
    }
    return result;
  }

  private double findLowestPrice(double basePrice, List<Double> prices) {
    double result = basePrice;
    Integer i;
    for (i = 0; i < prices.size(); i++) {
      Double price = prices.get(i);
      if (price < result) {
        result = price;
      }
    }

    return result;
  }

  private static int getIterations() {
    return 2000000;
  }

  public static An_Event readEvent() {
    MovieRentalEvent movieEvent = new MovieRentalEvent();
    movieEvent.setCategory("Science fiction");
    movieEvent.setTitle("Terminator");
    movieEvent.setDirector("James Cameron");
    movieEvent.setListPrice(20);
    movieEvent.setDuration(108);
    movieEvent.setQuantity(5);
    return movieEvent;
  }

  private void checkPerformance(String label) {
    double sum = 0;
    long start = System.currentTimeMillis();
    for (int i = getIterations(); i > 0; i = i - 1) {
      An_Event movieEvent = readEvent();
      double price = calcPromotionalPrice(movieEvent);
      sum = sum + price;
    }
    long end = System.currentTimeMillis();
    System.out.println(label + " Time: " + (end - start) + "ms; Price: " + sum);
  }

  @Compile
  public static void main(String[] args) {
    System.out.println("Promotion system is up and running");

    PromotionSystem dynPromotionSystem = new PromotionSystem();
    dynPromotionSystem.setup(dynPromotionSystem);

    PromotionSystem staticPromotionSystem = Civet.CT(new PromotionSystem());
    staticPromotionSystem.setup(staticPromotionSystem);

    for (int i = 0; i < Civet.CT(4); i = i + 1) {
      dynPromotionSystem.checkPerformance("Dynamic");
      staticPromotionSystem.checkPerformance("Static ");
    }
    System.out.println("Promotion system is goind down.");
  }

  private void setup(PromotionSystem promotionSys) {
    // With one promo "James Cameron's movies 20% off". Expected result is
    // 16.
    setupPromo1(promotionSys);
    // With another promo "science fiction movies for 8 bucks". Expected
    // result is 8.
    setupPromo2(promotionSys);
    // With another promo
    // "Rent 2 for 20% off, 5 for 70% off, 10 for 80% off". Expected result
    // is 6.
    setupPromo3(promotionSys);
    // With another promo "Drama movies for 2 bucks". Expected result is
    // still 6 (no match).
    setupPromo4(promotionSys);
  }

  private static void setupPromo1(PromotionSystem promotionSystem) {
    /*
     * Equivalent ModelTalk code for defining this object graphs is:
     * 
     * <A_Promotion type="Promotion"> <Name>Promo on James Cameron's movies -
     * 20% off</Name> <Eligibility type="EligibilityByPropertyValue">
     * <PropertyName>Director</PropertyName> <Value>Cameron</Value>
     * </Eligibility> <Discounter type="PercentageDiscounter">
     * <Percent>20</Percent> </Discounter> </A_Promotion>
     */
    Promotion promo = new Promotion();
    promo.setName("Promo on James Cameron's movies - 20% off");
    EligibilityByPropertyValue eligibilityByPropertyValue = new EligibilityByPropertyValue();
    eligibilityByPropertyValue.setPropertyName("Director");
    eligibilityByPropertyValue.setValue("Cameron");
    promo.setEligibility(eligibilityByPropertyValue);
    PercentageDiscounter percentageDiscounter = new PercentageDiscounter();
    percentageDiscounter.setPercent(20);
    promo.setDiscounter(percentageDiscounter);

    promotionSystem.addPromotion(promo);
  }

  private static void setupPromo2(PromotionSystem promotionSystem) {

    Promotion promo = new Promotion();
    promo.setName("Promo on expenssive sience fiction movies - only 8 bucks");
    EligibilityByPropertyValue eligibilityByPropertyValue = new EligibilityByPropertyValue();
    eligibilityByPropertyValue.setPropertyName("Category");
    eligibilityByPropertyValue.setValue("Science fiction");
    EligibilityByPrice eligibilityByPrice = new EligibilityByPrice();
    eligibilityByPrice.setPrice(12); // 12 is expenssive
    EligibilityAnd eligibilityAnd = new EligibilityAnd();
    eligibilityAnd.setEligibility1(eligibilityByPropertyValue);
    eligibilityAnd.setEligibility2(eligibilityByPrice);
    promo.setEligibility(eligibilityAnd);
    FixedPrice fixedPrice = new FixedPrice();
    fixedPrice.setFixedPrice(8);
    promo.setDiscounter(fixedPrice);
    promotionSystem.addPromotion(promo);
  }

  private static void setupPromo3(PromotionSystem promotionSystem) {
    Promotion promo = new Promotion();
    promo
        .setName("Promo on multiple movie rentals - get 2 for 20% off, 5 for 70% off and 10 for 80% off");
    AllEligible eligibility = new AllEligible();
    promo.setEligibility(eligibility);
    StepDiscounterByQuantity stepDiscounter = new StepDiscounterByQuantity();
    stepDiscounter.setQuantity1(2);
    stepDiscounter.setQuantity2(5);
    stepDiscounter.setQuantity3(10);
    PercentageDiscounter percentageDiscounter1 = new PercentageDiscounter();
    percentageDiscounter1.setPercent(20);
    stepDiscounter.setDiscounter1(percentageDiscounter1);
    PercentageDiscounter percentageDiscounter2 = new PercentageDiscounter();
    percentageDiscounter2.setPercent(70);
    stepDiscounter.setDiscounter2(percentageDiscounter2);
    PercentageDiscounter percentageDiscounter3 = new PercentageDiscounter();
    percentageDiscounter3.setPercent(80);
    stepDiscounter.setDiscounter3(percentageDiscounter3);
    promo.setDiscounter(stepDiscounter);
    promotionSystem.addPromotion(promo);
  }

  private static void setupPromo4(PromotionSystem promotionSystem) {
    Promotion promo = new Promotion();
    promo.setName("All drama movies for 2 bucks");
    EligibilityByPropertyValue eligibilityByPropertyValue = new EligibilityByPropertyValue();
    eligibilityByPropertyValue.setPropertyName("Category");
    eligibilityByPropertyValue.setValue("Drama");
    promo.setEligibility(eligibilityByPropertyValue);
    FixedPrice fixedPrice = new FixedPrice();
    fixedPrice.setFixedPrice(2);
    promo.setDiscounter(fixedPrice);
    promotionSystem.addPromotion(promo);

  }
}
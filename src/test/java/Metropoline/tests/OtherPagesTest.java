package Metropoline.tests;

import org.testng.annotations.Test;
import static org.testng.Assert.assertTrue; // import TestNG assert
import Metropoline.HomePage;
import Metropoline.OtherPages;
import com.microsoft.playwright.Locator;

public class OtherPagesTest extends BaseTest {

    private HomePage homePage;
    private OtherPages otherPages;

    @Test
    public void testFullHomepageFlow() {
        // --- 1. INITIALIZATION & NAVIGATION ---
        // Initialize HomePage and OtherPages with the page from BaseTest
        homePage = new HomePage(page);
        otherPages = new OtherPages(page);

        // Open Metropoline homepage
        page.navigate("https://www.metropoline.com");

        // Wait a bit to let popup appear
        page.waitForTimeout(2000);

        // Close popup if it exists
        otherPages.closePopupIfExists();

        // --- 2. TICKET REFORMS & TARIFFS ---
        // Click the "רפורמת צדק תחבורתי - אפריל 2025" link
        //otherPages.openReformatTzedeqUnderPrices();

        // --- 3. RAV-KAV & CUSTOMER SERVICES ---
        //Click the "על הקו" in "רב קו" section
        //otherPages.openAlHakavLink();
        //page.waitForTimeout(2000);
        //otherPages.clickRavKavRandomSectionAndPrint();
        //otherPages.printRavKavSectionAsTable();

        // --- 4. REGIONAL UPDATES & MESSAGES ---
        //otherPages.PrintRandomELadUpdate();
        //otherPages.PrintRandomSharonUpdate();
        //otherPages.printRandomMessagesByCategories("elad","sharon","darom","information and lost stuff");

        // --- 5. INSURANCE & LOST AND FOUND ---
        //otherPages.printInsuranceMessage();
        //otherPages.printRandomLostAndFoundMessage();
    }
}
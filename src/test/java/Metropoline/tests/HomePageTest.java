package Metropoline.tests;

import org.testng.annotations.Test;
import Metropoline.HomePage;

public class HomePageTest extends BaseTest {

    private HomePage homePage;

    @Test
    public void testFullHomepageFlow() {
        // --- 1. INITIALIZATION & SETUP ---
        // Initialize HomePage with the page from BaseTest
        homePage = new HomePage(page);

        // Open Metropoline homepage
        page.navigate("https://www.metropoline.com");

        // Wait a bit to let popup appear
        page.waitForTimeout(2000);

        // Close popup if exists
        homePage.closePopupIfExists();

        // --- 2. ROUTE AND LINE SEARCH FLOW ---
        // Fill start and end inputs
        //homePage.fillStartAndEnd("תל אביב", "חיפה");

        // Click "מצא מסלול" button
        //homePage.clickFindRoute();

        // Click "חיפוש קו" tab and fill a random line number
        //homePage.clickSearchLineTab();

        //Click "חפש" button
        //homePage.clickSearchButton();

        // --- 3. ON TIME & TICKET INFORMATION ---
        //Click "ON TIME" station tab
        //homePage.clickOnTimeTab();
        //homePage.fillOnTimeStation("תל אביב"); // example
        //homePage.clickMySearchesTab();
        //homePage.clickPricesAndTickets();
        //homePage.clickRavKavRandomSectionAndPrint();

        // --- 4. CONTACT FORM & COMPLAINTS ---
        // --- New: Contact page ---
        //String firstName = "Ariel";
        //String lastName = "Levi";
        //String email = "ariel.levi@example.com";
        //String phone = "+972501234567";

        //homePage.fillContactForm(firstName, lastName, email, phone);
        //homePage.fillRandomComplaint();
        //homePage.selectRandomArea();
        //homePage.fillContactLineAndStation();

        // --- 5. DATE & TIME SELECTION ---
        // 2. Select random hour (00–23)
        //homePage.selectRandomDropdownValue("hr", 23);

        // 3. Select random minute (00–59)
        //homePage.selectRandomDropdownValue("min", 59);

        //4. Select random date
        //homePage.selectRandomDate();

        // --- 6. SUBMISSION ---
        //homePage.checkSubmitButton();

        //System.out.println("Contact form filled successfully.");

        // Optional: wait a few seconds to see the results
        //page.waitForTimeout(3000);
        //homePage.verifyEmployeeLoginLink();
        homePage.verifyArabicSiteLink();

    }
}
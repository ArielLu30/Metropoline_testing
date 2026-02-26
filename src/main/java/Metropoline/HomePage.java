package Metropoline;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.WaitForSelectorState;

import java.util.List;
import java.util.Random;
import java.util.ArrayList;

public class HomePage {

    private Page page;
    private Random random = new Random();

    public HomePage(Page page) {
        this.page = page;
    }

    // --- 1. DIALOGS & POPUPS ---
    /**
     * Close popup if it exists
     */
    public void closePopupIfExists() {
        Locator closePopup = page.locator("button#closeDialog");
        if (closePopup.count() > 0) {
            closePopup.click();
            System.out.println("Popup closed successfully.");
        } else {
            System.out.println("No popup appeared.");
        }
    }

    // --- 2. TRIP PLANNING & AUTOCOMPLETE ---
    /**
     * Fill start and end inputs and select a random option
     */
    public void fillStartAndEnd(String startText, String endText) {
        selectRandomFromAutocomplete("#lbl01", startText);
        selectRandomFromAutocomplete("#lbl02", endText);
        System.out.println("Start and End inputs filled with random selections.");
    }

    /**
     * Click "מצא מסלול" button
     */
    public void clickFindRoute() {
        page.locator("button.btn.selectBtn").click();
        System.out.println("\"מצא מסלול\" clicked.");
    }

    /**
     * Helper method to type and pick random from autocomplete
     */
    private void selectRandomFromAutocomplete(String inputSelector, String text) {
        Locator input = page.locator(inputSelector);

        // Click and type
        input.click();
        input.fill(text);

        // Wait for autocomplete options to appear
        Locator options = page.locator("a.forceFocus");
        options.first().waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));

        // Pick a random element
        List<String> optionLocators = options.allTextContents();
        int randomIndex = random.nextInt(optionLocators.size());

        // Click random option
        options.nth(randomIndex).click();
        System.out.println("Selected: " + optionLocators.get(randomIndex));
    }

    // --- 3. LINE SEARCH & TABS ---
    /**
     * Click the "חיפוש קו" tab
     */
    public void clickSearchLineTab() {
        Locator tab = page.locator("li a[title='חיפוש קו']");
        tab.click();
        System.out.println("\"חיפוש קו\" tab clicked.");

        // Generate random line number 1-99
        int randomLine = random.nextInt(99) + 1;
        fillLineNumber(String.valueOf(randomLine));
    }

    /**
     * Fill the line number input
     */
    private void fillLineNumber(String lineNumber) {
        Locator lineInput = page.locator("#lbl03");
        lineInput.click();
        lineInput.fill(lineNumber);
        System.out.println("Line number input filled with: " + lineNumber);

        // Wait for autocomplete options to appear
        Locator options = page.locator("a.forceFocus");
        options.first().waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));

        // Pick a random autocomplete option
        int randomIndex = random.nextInt(options.count());
        String selectedOption = options.nth(randomIndex).innerText();
        options.nth(randomIndex).click();

        System.out.println("Random line selected from autocomplete: " + selectedOption);
    }

    /**
     * Click the "חפש" button
     */
    public void clickSearchButton() {
        page.locator("button.ng-binding[ng-click='searchResultAllRoutData()']").click();
        System.out.println("\"חפש\" button clicked.");
    }

    // --- 4. REAL-TIME & USER HISTORY ---
    /**
     * Click the "ON TIME" tab
     */
    public void clickOnTimeTab() {
        Locator onTimeTab = page.locator("li a[title='ON TIME']");
        onTimeTab.click();
        System.out.println("\"ON TIME\" tab clicked.");
    }

    /**
     * Fill the ON TIME input with a station name and select a random option
     */
    public void fillOnTimeStation(String stationName) {
        Locator input = page.locator("#lbl04");

        // Click and type
        input.click();
        input.fill(stationName);

        // Wait for autocomplete options to appear
        Locator options = page.locator("a.forceFocus");
        options.first().waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));

        // Pick a random option
        List<String> optionTexts = options.allTextContents();
        int randomIndex = new Random().nextInt(optionTexts.size());

        options.nth(randomIndex).click();
        System.out.println("ON TIME station selected: " + optionTexts.get(randomIndex));
    }

    /**
     * Click the "החיפושים שלי" tab and print relevant strong text
     */
    public void clickMySearchesTab() {
        Locator tab = page.locator("li a[title='החיפושים שלי']");
        tab.click();
        System.out.println("\"החיפושים שלי\" tab clicked.");

        // Get all strong elements
        Locator strongElements = page.locator("strong.ng-binding");
        List<String> allTexts = strongElements.allTextContents();

        System.out.println("Relevant strong texts:");
        for (String text : allTexts) {
            // Only print texts that contain numbers (likely lines or stations)
            if (text.matches(".*\\d.*")) {
                System.out.println(text.trim());
            }
        }
    }

    // --- 5. TARIFFS & TICKETING ---
    /**
     * Click "תעריפים וכרטיסים", open random tariff section, print its content
     */
    public void clickPricesAndTickets() {
        // 1. Click main link
        Locator link = page.locator("#main-nav")
                .getByRole(AriaRole.LINK,
                        new Locator.GetByRoleOptions().setName("תעריפים וכרטיסים"));
        link.click();
        System.out.println("\"תעריפים וכרטיסים\" link clicked.");

        // 2. Locate all expandable tariff sections
        Locator openers = page.locator("a.opener.summary");
        openers.first().waitFor();

        int count = openers.count();
        if (count == 0) {
            System.out.println("No tariff sections found.");
            return;
        }

        // 3. Pick random opener
        int randomIndex = random.nextInt(count);
        Locator randomOpener = openers.nth(randomIndex);
        String openerText = randomOpener.textContent().trim();

        // Get the controlled panel ID BEFORE click
        String panelId = randomOpener.getAttribute("aria-controls");

        randomOpener.click();
        System.out.println("Random tariff section clicked: " + openerText);

        // 4. Locate the exact opened panel
        Locator panel = page.locator("#" + panelId);
        panel.waitFor();

        // 5. Print content
        String contentText = panel.innerText().trim();
        System.out.println("Tariff content:\n" + contentText);
    }

    /**
     * Open Rav-Kav page, click a random section, and print its content
     */
    public void clickRavKavRandomSectionAndPrint() {
        // 1. Navigate directly to the Rav-Kav page
        page.navigate("https://www.metropoline.com/Pages/Questions.aspx?p=RavKavHashron");
        System.out.println("Navigated to Rav-Kav page.");

        // 2. Locate all expandable Rav-Kav sections
        Locator openers = page.locator("a.opener.summary");
        openers.first().waitFor();

        int count = openers.count();
        if (count == 0) {
            System.out.println("No Rav-Kav sections found.");
            return;
        }

        // 3. Pick a random section
        int randomIndex = random.nextInt(count);
        Locator randomOpener = openers.nth(randomIndex);
        String openerText = randomOpener.textContent().trim();

        // Get the controlled panel ID BEFORE clicking
        String panelId = randomOpener.getAttribute("aria-controls");

        randomOpener.click();
        System.out.println("Random Rav-Kav section clicked: " + openerText);

        // 4. Locate the exact opened panel
        Locator panel = page.locator("#" + panelId);
        panel.waitFor();

        // 5. Print content
        String contentText = panel.innerText().trim();
        System.out.println("Rav-Kav section content:\n" + contentText);
    }

    // --- 6. CONTACT FORM & COMPLAINTS ---
    /**
     * Fill Contact form on Metropoline Contact page
     */
    public void fillContactForm(String firstName, String lastName, String email, String phone) {
        // 1. Navigate to Contact page
        page.navigate("https://www.metropoline.com/Pages/Contact.aspx?p=Contact");
        System.out.println("Navigated to Contact page.");

        // 2. Fill Personal Details
        page.locator("#lbl-001").fill(firstName);
        page.locator("#lbl-0015").fill(lastName);
        page.locator("#lbl-003").fill(email);
        page.locator("#lbl-002").fill(phone);

        // 1. Click the dropdown
        page.getByText("נושא הפנייה").click();

        // 2. Wait until all <li> elements inside the dropdown are visible
        Locator allOptions = page.locator("div.drop-list ul li");
        allOptions.first().waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE)
                .setTimeout(10000));

        page.waitForTimeout(500);

        // 3. Filter only visible options
        List<Locator> visibleOptions = new ArrayList<>();
        int count = allOptions.count();
        for (int i = 0; i < count; i++) {
            Locator option = allOptions.nth(i);
            if (option.isVisible()) {
                visibleOptions.add(option);
            }
        }

        if (visibleOptions.isEmpty()) {
            System.out.println("No visible options found!");
            return;
        }

        // 4. Pick a random visible option
        Locator randomOption = visibleOptions.get(random.nextInt(visibleOptions.size()));

        // 5. Click the <a> inside the <li>
        randomOption.locator("a").click();

        // 6. Print the text inside the span
        String optionText = randomOption.locator("span.ng-binding").innerText().trim();
        System.out.println("Randomly selected option: " + optionText);
    }

    public void fillRandomComplaint() {
        // 1. Locate the textarea/input after the label
        Locator textArea = page.locator("label[for='lbl-007'] + textarea, label[for='lbl-007'] + input");

        // 2. Example random complaints
        String[] complaints = new String[]{
                "שלום, \nשירות הלקוח לא ענה בזמן ואני מרגיש מתוסכל. \nאנא טיפלו בבקשה בהקדם.",
                "שלום, \nהחבילה שהוזמנה הגיעה פגומה. \nנדרש פתרון מיידי.",
                "שלום, \nהמערכת קרסה במהלך השימוש. \nנדרש טיפול דחוף כדי להמשיך בתהליך."
        };

        // 3. Pick a random complaint
        String randomComplaint = complaints[random.nextInt(complaints.length)];

        // 4. Fill the field
        textArea.fill(randomComplaint);
        System.out.println("Filled complaint: \n" + randomComplaint);
    }

    /**
     * Click "בחר אזור" dropdown and select a random option
     */
    public void selectRandomArea() {
        // 1. Click the dropdown
        page.getByText("בחר אזור").click();

        // 2. Wait until all options in the list are visible
        Locator options = page.locator("div.drop-holder div.drop-list ul[aria-label='בחר אזור'] li");
        options.first().waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE)
                .setTimeout(10000));

        page.waitForTimeout(500);

        // 3. Filter visible options
        List<Locator> visibleOptions = new ArrayList<>();
        int count = options.count();
        for (int i = 0; i < count; i++) {
            Locator option = options.nth(i);
            if (option.isVisible()) visibleOptions.add(option);
        }

        if (visibleOptions.isEmpty()) {
            System.out.println("No visible area options found.");
            return;
        }

        // 4. Pick a random option
        Locator randomOption = visibleOptions.get(random.nextInt(visibleOptions.size()));

        // 5. Scroll into view and click
        randomOption.scrollIntoViewIfNeeded();
        randomOption.locator("a").click();

        // 6. Print the selected option
        String optionText = randomOption.locator("span.ng-binding").innerText().trim();
        System.out.println("Selected area: " + optionText);
    }

    // --- 7. STATION & LINE DATA ENTRY ---
    /**
     * Fill Contact ON TIME fields:
     * - Random line number (1–100)
     * - Random Israeli station
     */
    public void fillContactLineAndStation() {
        // ---------- LINE NUMBER ----------
        Locator lineInput = page.locator("#contact11");
        lineInput.click();

        int randomLine = random.nextInt(100) + 1;
        lineInput.fill(String.valueOf(randomLine));
        System.out.println("Typed line number: " + randomLine);

        Locator lineOptions = page.locator("a.forceFocus");
        lineOptions.first().waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE)
                .setTimeout(10000));

        int lineIndex = random.nextInt(lineOptions.count());
        String selectedLine = lineOptions.nth(lineIndex).innerText();
        lineOptions.nth(lineIndex).click();

        System.out.println("Selected line: " + selectedLine);

        // ---------- STATION ----------
        Locator stationInput = page.locator("#contact12");
        stationInput.click();

        String[] israeliPlaces = {
                "תל אביב", "ירושלים", "חיפה", "באר שבע", "אשדוד",
                "פתח תקווה", "רמת גן", "חולון", "נתניה", "אשקלון"
        };

        String randomPlace = israeliPlaces[random.nextInt(israeliPlaces.length)];
        stationInput.fill(randomPlace);
        System.out.println("Typed station search: " + randomPlace);

        Locator stationOptions = page.locator("a.forceFocus");
        stationOptions.first().waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE)
                .setTimeout(10000));

        int stationIndex = random.nextInt(stationOptions.count());
        String selectedStation = stationOptions.nth(stationIndex).innerText();
        stationOptions.nth(stationIndex).click();

        System.out.println("Selected station: " + selectedStation);
    }

    // --- 8. TIME & DATE SELECTION ---
    /**
     * Click a dropdown by its model ("hr" or "min") and select a random option
     */
    public void selectRandomDropdownValue(String model, int maxValue) {
        // 1. Locate the dropdown container by model
        Locator dropdown = page.locator("div[model='" + model + "']");
        if (dropdown.count() == 0) {
            System.out.println("Dropdown with model " + model + " not found.");
            return;
        }

        // 2. Click the field to open dropdown
        Locator field = dropdown.locator("span.center.ng-binding");
        field.first().click();

        // 3. Generate random value
        int rand = new java.util.Random().nextInt(maxValue + 1);
        String randText = String.format("%02d", rand);

        // 4. Find the option that matches
        Locator options = dropdown.locator("li a span.ng-binding", new Locator.LocatorOptions().setHasText(randText));
        options.first().waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));

        // 5. Click the option
        options.first().click();

        System.out.println("Selected " + model + ": " + randText);
    }

    /**
     * Open Datepicker and select a random day
     */
    public void selectRandomDate() {
        // 1. Open the datepicker
        page.locator("button.ui-datepicker-trigger").click();

        // Optional: randomly click next or prev once
        if (random.nextBoolean()) {
            page.locator("a.ui-datepicker-next").click(); // go to next month
        } else {
            page.locator("a.ui-datepicker-prev").click(); // go to previous month
        }

        page.waitForTimeout(300);

        // 3. Get all available days in current month
        Locator days = page.locator("td[data-handler='selectDay'] a.ui-state-default");
        int count = days.count();
        if (count == 0) {
            System.out.println("No selectable days found.");
            return;
        }

        // 4. Pick and click a random day
        int randomIndex = random.nextInt(count);
        Locator randomDay = days.nth(randomIndex);
        randomDay.click();

        // 6. Print selected date details
        String dayText = randomDay.innerText().trim();
        Locator parentTd = randomDay.locator(".."); // go up to <td>
        String month = parentTd.getAttribute("data-month"); // 0-based month
        String year = parentTd.getAttribute("data-year");

        System.out.println("Selected random date: " + dayText + "/" + (Integer.parseInt(month) + 1) + "/" + year);
    }

    // --- 9. FINAL VERIFICATION ---
    public void checkSubmitButton() {
        // 1. Locate and check the submit button
        Locator submitBtn = page.locator("button.ng-binding[type='submit']");

        if (submitBtn.isVisible()) {
            System.out.println("Submit button is found and visible!");
        } else {
            System.out.println("Submit button not found or not visible.");
        }
    }

    /**
     * Clicks the Employee Login link, switches to the new tab,
     * and verifies the presence of the Football League registration text.
     */
    public void verifyEmployeeLoginLink() {
        System.out.println("Starting Employee Login verification...");

        // 1. Use .first() to avoid the 'strict mode violation' (2 elements found)
        Locator employeeLink = page.locator("a[aria-label*='כניסת עובדים']").first();

        // 2. Catch the new page/tab
        Page newPage = page.context().waitForPage(() -> {
            employeeLink.click();
        });

        // 3. Wait for the Google Form to load
        newPage.waitForLoadState(LoadState.NETWORKIDLE);

        // 4. Verify that the page contains "כניסת עובדים"
        String expectedSubstring = "כניסת עובדים";
        boolean isTextVisible = newPage.locator("text=" + expectedSubstring).first().isVisible();

        if (!isTextVisible) {
            // This stops the test immediately and marks it as a failure in any IDE/Console
            throw new RuntimeException("CRITICAL FAILURE: The words '" + expectedSubstring + "' were NOT found on the page!");
        }

// If it didn't throw the exception, it continues here:
        System.out.println("Success: Found '" + expectedSubstring + "' on the new page.");

        // 5. Cleanup
        newPage.close();
    }
    /**
     * Clicks the Arabic language link, switches to the new tab,
     * and fails the test if "not found" appears or Arabic text is missing.
     */
    public void verifyArabicSiteLink() {
        System.out.println("Starting Arabic Site verification...");

        // 1. Locate the link using the aria-label (contains check)
        // This looks for any link where the aria-label contains the words "באתר בערבית"
        Locator arabicLink = page.locator("a[aria-label*='באתר בערבית']").first();
        // 2. Catch the new page/tab
        Page newPage = page.context().waitForPage(() -> {
            arabicLink.click();
        });

        // 3. Wait for the page to load
        newPage.waitForLoadState(LoadState.NETWORKIDLE);

        // 4. Check for "Not Found" (Case-insensitive check)
        String content = newPage.content().toLowerCase();
        if (content.contains("not found") || content.contains("404")) {
            throw new RuntimeException("CRITICAL FAILURE: Arabic site returned 'Not Found' or 404 error!");
        }

        // 5. Verify Arabic script exists (Arabic characters range: \u0600-\u06FF)
        // We look for the word "מטרופולין" in Arabic or general Arabic characters
        boolean hasArabicScript = newPage.locator("body").innerText().matches(".*[\\u0600-\\u06FF].*");

        if (!hasArabicScript) {
            throw new RuntimeException("CRITICAL FAILURE: New page loaded but no Arabic script was detected!");
        }

        System.out.println("Success: Arabic site loaded correctly with Arabic script.");

        // 6. Cleanup
        newPage.close();
    }
}
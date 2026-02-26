package Metropoline;

import com.microsoft.playwright.Keyboard;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.BoundingBox;
import com.microsoft.playwright.options.WaitForSelectorState;
import com.microsoft.playwright.options.WaitUntilState;

public class AdditionalInfoPages {
    private final Page page;

    public AdditionalInfoPages(Page page) {
        this.page = page;
    }

    // --- UTULARITY & DIALOGS ---
    public void closePopupIfExists() {
        Locator closePopup = page.locator("button#closeDialog");
        if (closePopup.isVisible()) {
            closePopup.click();
            System.out.println("Popup closed.");
        }
    }

    // --- SEAT RESERVATION & ROUTE SELECTION (KOL KASHER) ---
    public void clickRandomRoute() {
        // Find all route elements containing the arrow symbol
        Locator routes = page.locator("div[title*='<->']");

        int count = routes.count();

        if (count > 0) {
            int randomIndex = (int) (Math.random() * count);
            Locator selectedRoute = routes.nth(randomIndex);

            // Print the specific title of the route chosen
            System.out.println("Selected Route: " + selectedRoute.getAttribute("title"));

            selectedRoute.click();
            //  Click the 'Continue' (המשך) button
            page.locator("div.continueBotton:has-text('המשך')").click();
            System.out.println("Clicked: המשך");
        } else {
            System.out.println("No elements found with '<->' in the title.");
        }
    }

    // --- TRIP TYPE HANDLING (ONE WAY) ---
    public void handleOneWaySelection() {
        //  Click "נסיעה אחת"
        page.locator("input#oneWay").click();
        System.out.println("Selected: נסיעה אחת");

        // Grab the specific select element by its classes
        Locator directionSelect = page.locator("select.breakText.cursor-pointer").first();

        //  Click it
        directionSelect.click();

        // Target the select element directly
        Locator dropdown = page.locator("select.breakText").first();

        // Count the <option> tags inside
        int count = dropdown.locator("option").count();

        // Pick a random index (0 or 1 in your example) and select it
        if (count > 0) {
            int randomIndex = (int) (Math.random() * count);
            dropdown.selectOption(new com.microsoft.playwright.options.SelectOption().setIndex(randomIndex));

            // Get the text of the option at that index BEFORE selecting
            String selectedText = dropdown.locator("option").nth(randomIndex).innerText();

            // Select the option
            dropdown.selectOption(new com.microsoft.playwright.options.SelectOption().setIndex(randomIndex));

            // Print the text
            System.out.println("Selected Option: " + selectedText.trim());


            // --- DATE SELECTION ---
            page.waitForTimeout(2000); // Essential for the date options to load

            Locator dateSelect = page.locator("select.breakText.cursor-pointer").nth(2);
            int dateCount = dateSelect.locator("option").count();

            if (dateCount > 0) {
                int randomDateIndex = (int) (Math.random() * dateCount);

                // 1. Perform the selection
                dateSelect.selectOption(new com.microsoft.playwright.options.SelectOption().setIndex(randomDateIndex));

                // 2. Small pause to let the UI update its display text
                page.waitForTimeout(500);

                // 3. Get the text that is actually selected in the browser right now
                String selectedDateText = (String) dateSelect.evaluate("el => el.options[el.selectedIndex].text");

                System.out.println("Selected Date: " + selectedDateText.trim());
            }
        }
        // --- TIME SELECTION DEPARTURE ---
        page.waitForTimeout(2000); // Essential for the hour options to load

        // Target the 5th select element on the page (index 4)
        Locator hourSelect = page.locator("select").nth(4);
        int hourCount = hourSelect.locator("option").count();

        if (hourCount > 0) {
            int randomHourIndex = (int) (Math.random() * hourCount);

            // 1. Perform the selection
            hourSelect.selectOption(new com.microsoft.playwright.options.SelectOption().setIndex(randomHourIndex));

            // 2. Small pause to let the UI update its display text
            page.waitForTimeout(500);

            // 3. Get the text that is actually selected in the browser right now
            String selectedHourText = (String) hourSelect.evaluate("el => el.options[el.selectedIndex].text");

            System.out.println("Selected Travel Hour: " + selectedHourText.trim());
        }
    }

    // --- TRIP TYPE HANDLING (ROUND TRIP) ---
    public void handleRoundTripSelection() {
        // 1. Click "הלוך וחזור"
        page.locator("input#towWay").click();
        System.out.println("Selected: הלוך וחזור");

        // --- DIRECTION 1 (Outbound) ---
        Locator outboundDir = page.locator("select.breakText.cursor-pointer").nth(0);
        int outboundDirIndex = 0; // Store this index

        int count1 = outboundDir.locator("option").count();
        if (count1 > 0) {
            outboundDirIndex = (int) (Math.random() * count1);
            outboundDir.selectOption(new com.microsoft.playwright.options.SelectOption().setIndex(outboundDirIndex));
            String text = (String) outboundDir.evaluate("el => el.options[el.selectedIndex].text");
            System.out.println("Selected Outbound Direction: " + text.trim());
        }

        // --- DIRECTION 2 (Return) ---
        Locator returnDir = page.locator("select.breakText.cursor-pointer").nth(1);
        int count2 = returnDir.locator("option").count();

        if (count2 > 0) {
            // CORRECTION: If outbound was 0, pick 1. If outbound was 1, pick 0.
            int returnDirIndex = (outboundDirIndex == 0) ? 1 : 0;

            // Safety check: if for some reason there's only 1 option, just pick 0
            if (returnDirIndex >= count2) returnDirIndex = 0;

            returnDir.selectOption(new com.microsoft.playwright.options.SelectOption().setIndex(returnDirIndex));
            String text = (String) returnDir.evaluate("el => el.options[el.selectedIndex].text");
            System.out.println("Selected Return Direction (Opposite): " + text.trim());
        }

        // --- DATE 1 (Outbound Date) ---
        page.waitForTimeout(2000);
        Locator outboundDate = page.locator("select.breakText.cursor-pointer").nth(2);
        int count3 = outboundDate.locator("option").count();
        int outboundIndex = 0; // Capture this for the next step

        if (count3 > 0) {
            // Pick any date except the very last one to leave room for a return
            outboundIndex = (int) (Math.random() * (count3 - 1));
            outboundDate.selectOption(new com.microsoft.playwright.options.SelectOption().setIndex(outboundIndex));
            page.waitForTimeout(500);
            String text = (String) outboundDate.evaluate("el => el.options[el.selectedIndex].text");
            System.out.println("Selected Outbound Date: " + text.trim());
        }

        // --- DATE 2 (Return Date) ---
        Locator returnDate = page.locator("select.breakText.cursor-pointer").nth(3);
        int count4 = returnDate.locator("option").count();

        if (count4 > 0) {
            // CORRECTION: Ensure return index is at least outboundIndex + 1
            int minIndex = outboundIndex + 1;
            if (minIndex >= count4) minIndex = count4 - 1; // Safety check

            int randomReturnIndex = minIndex + (int) (Math.random() * (count4 - minIndex));

            returnDate.selectOption(new com.microsoft.playwright.options.SelectOption().setIndex(randomReturnIndex));
            page.waitForTimeout(1000);
            String text = (String) returnDate.evaluate("el => el.options[el.selectedIndex].text");
            System.out.println("Selected Return Date: " + text.trim());

            // --- TIME SELECTION OUTBOUND ---
            Locator hourSelectOut = page.locator("select").nth(4);
            if (hourSelectOut.locator("option").count() > 0) {
                int randomHourIndex = (int) (Math.random() * hourSelectOut.locator("option").count());
                hourSelectOut.selectOption(new com.microsoft.playwright.options.SelectOption().setIndex(randomHourIndex));
                page.waitForTimeout(500);
                String selectedHourText = (String) hourSelectOut.evaluate("el => el.options[el.selectedIndex].text");
                System.out.println("Selected Outbound Hour: " + selectedHourText.trim());
            }

            page.waitForTimeout(1000);

            // --- TIME SELECTION RETURN ---
            Locator hourSelectRet = page.locator("select").nth(5);
            if (hourSelectRet.locator("option").count() > 0) {
                int randomHourIndexRet = (int) (Math.random() * hourSelectRet.locator("option").count());
                hourSelectRet.selectOption(new com.microsoft.playwright.options.SelectOption().setIndex(randomHourIndexRet));
                page.waitForTimeout(500);
                String selectedHourTextRet = (String) hourSelectRet.evaluate("el => el.options[el.selectedIndex].text");
                System.out.println("Selected Return Hour: " + selectedHourTextRet.trim());
                // We target the div with both the class and the specific Hebrew text
                Locator finalContinueBtn = page.locator("div.continueBotton:has-text('המשך')").last();
                finalContinueBtn.click();
                System.out.println("Clicked final המשך - Proceeding to next step.");
            }
        }
    }

    // --- DATA ENTRY & FORM SUBMISSION ---
    public void fillPersonalDetails(String lName, String fName, String address, String city, String phone, String email) {
        // 1. Family Name
        page.locator("input[formcontrolname='LName']").fill(lName);
        System.out.println("Filled Family Name: " + lName);

        // 2. Private Name
        page.locator("input[formcontrolname='FName']").fill(fName);
        System.out.println("Filled Private Name: " + fName);

        // 3. Address
        page.locator("input[formcontrolname='Address']").fill(address);
        System.out.println("Filled Address: " + address);

        // 4. City
        page.locator("input[formcontrolname='City']").fill(city);
        System.out.println("Filled City: " + city);

        // 5. Phone
        page.locator("input[formcontrolname='Tel']").fill(phone);
        System.out.println("Filled Phone: " + phone);

        // 6. Email
        page.locator("input[formcontrolname='Email']").fill(email);
        System.out.println("Filled Email: " + email);

        System.out.println("--- All personal details entered ---");
        // 1. Target the Ticket Quantity dropdown
        Locator quantityDropdowns = page.locator("select.ng-valid");

        // --- FIRST DROPDOWN (1 to 15) ---
        Locator firstDropdown = quantityDropdowns.nth(0);
        int count1 = firstDropdown.locator("option").count();
        if (count1 > 0) {
            int randomIndex = (int) (Math.random() * count1);
            firstDropdown.selectOption(new com.microsoft.playwright.options.SelectOption().setIndex(randomIndex));
            String val = (String) firstDropdown.evaluate("el => el.options[el.selectedIndex].text");
            System.out.println("Selected Quantity 1: " + val.trim());
        }

        // --- SECOND DROPDOWN (0 to 10) ---
        Locator secondDropdown = quantityDropdowns.nth(1);
        int count2 = secondDropdown.locator("option").count();
        if (count2 > 0) {
            int randomIndex = (int) (Math.random() * count2);
            secondDropdown.selectOption(new com.microsoft.playwright.options.SelectOption().setIndex(randomIndex));
            String val = (String) secondDropdown.evaluate("el => el.options[el.selectedIndex].text");
            System.out.println("Selected Quantity 2: " + val.trim());
            // Target the specific button and click it regardless of state
            page.locator("div.continueBotton:has-text('המשך')").last()
                    .click(new com.microsoft.playwright.Locator.ClickOptions().setForce(true));

            System.out.println("Final המשך clicked.");

        }
    }

    // --- SUMMARY & VERIFICATION ---
    public void printOrderSummary() {
        System.out.println("\n--- FINAL ORDER SUMMARY ---");

        // 1. Trip Details (Route, Dates, Hours)
        Locator details = page.locator("#pdfTable .linesDetailsDiv");
        int sections = details.count();

        for (int i = 0; i < sections; i++) {
            String tripInfo = details.nth(i).innerText();
            System.out.println(i == 0 ? "[OUTBOUND TRIP]" : "[RETURN TRIP]");
            System.out.println(tripInfo.trim());
        }

        // 2. Personal Details (From the readonly inputs)
        System.out.println("\n[PASSENGER DETAILS]");
        String[] fields = {"LName", "FName", "Address", "City", "Tel", "Email"};
        String[] labels = {"Family Name", "First Name", "Address", "City", "Phone", "Email"};

        for (int i = 0; i < fields.length; i++) {
            String value = page.locator("input[formcontrolname='" + fields[i] + "']").inputValue();
            System.out.println(labels[i] + ": " + value);
        }

        // 3. Passenger Counts (From the selects)
        System.out.println("\n[TICKET QUANTITIES]");

        // First Select (Total Seats)
        String totalSeats = (String) page.locator("#pdfTable select").nth(0).evaluate("el => el.options[el.selectedIndex].text");
        System.out.println("Total Seats: " + totalSeats.trim());

        // Second Select (Under age 5)
        String underFive = (String) page.locator("#pdfTable select").nth(1).evaluate("el => el.options[el.selectedIndex].text");
        System.out.println("Under Age 5: " + underFive.trim());

        System.out.println("---------------------------\n");
        // Simple: Go to the div, find the orange button, grab the text.
        String text = page.locator(".continueBottonDiv .btn-orange").innerText();
        System.out.println("Button identified: " + text.trim());
    }

    // --- CUSTOMER UPDATES & CONTACT ---
    public void fillContactForm(String lineNum, String fullName, String email, String phone) {
        // 1. Hover and Click "עדכונים למייל"
        Locator updatesSpan = page.locator("span:has-text('עדכונים למייל')");
        updatesSpan.hover();
        updatesSpan.click();
        System.out.println("Hovered and clicked: עדכונים למייל");

        // 2. Line Number Input
        Locator lineInput = page.locator("#lbl03");
        lineInput.click();
        lineInput.fill(lineNum);

        // 3. Select from Typeahead and Print Selection
        Locator suggestion = page.locator("a.forceFocus").first();

        try {
            // Wait for suggestions to load
            suggestion.waitFor(new Locator.WaitForOptions().setTimeout(5000));

            // Capture the full text of the selected line
            String fullLineText = suggestion.innerText().replace("\n", " ").trim();
            System.out.println("Line selected from list: " + fullLineText);

            suggestion.click();
        } catch (Exception e) {
            System.out.println("Suggestion list did not appear for line: " + lineNum);
        }

        // 4. Personal Details
        page.locator("#lbl-001").fill(fullName);
        page.locator("#lbl-003").fill(email);
        page.locator("#lbl-002").fill(phone);

        System.out.println("Details filled for: " + fullName);

        // 5. Identify the Submit Button
        Locator submitBtn = page.locator("button[type='submit']:has-text('שלח')");
        if (submitBtn.isVisible()) {
            System.out.println("Final Button identified: [" + submitBtn.innerText().trim() + "]");
        }
    }

    // --- ACCESSIBILITY & FAQ ---
    public void printAccessibleLinesInfo() {
        // 1. ריחוף ולחיצה על "קווים נגישים"
        Locator accessibleSpan = page.locator("span:has-text('קווים נגישים')");
        accessibleSpan.hover();
        accessibleSpan.click();
        System.out.println("בוצע ריחוף ולחיצה על: קווים נגישים");

        // 2. המתנה לטעינת התוכן (ה-article שכולל את המידע)
        Locator infoArea = page.locator("article.col-left");
        infoArea.waitFor();

        // 3. הדפסת כל הטקסט מתוך אזור העדכונים
        System.out.println("\n--- תוכן עדכוני קווים נגישים ורב-קו ---");

        // שליפת הטקסט הנקי מתוך ה-article
        String content = infoArea.innerText();

        // הדפסה למסוף
        System.out.println(content.trim());

        System.out.println("\n--- סיום הדפסת עדכונים ---");
    }

    public void selectRandomFAQAndPrint() {
        // 1. Hover and Click "שאלות נפוצות"
        Locator faqSpan = page.locator("span:has-text('שאלות נפוצות')");
        faqSpan.hover();
        faqSpan.click();
        System.out.println("FAQ page opened.");

        // 2. Locate all question openers
        Locator openers = page.locator("a.opener.summary");
        try {
            openers.first().waitFor(new Locator.WaitForOptions().setTimeout(5000));
        } catch (Exception e) {
            System.out.println("No FAQ sections found.");
            return;
        }

        int count = openers.count();
        System.out.println("Found " + count + " questions.");

        // 3. Pick a random section
        java.util.Random random = new java.util.Random();
        int randomIndex = random.nextInt(count);
        Locator randomOpener = openers.nth(randomIndex);

        // 🔑 Get the controlled panel ID (aria-controls="tabpanel_X")
        String panelId = randomOpener.getAttribute("aria-controls");
        String openerText = randomOpener.innerText().trim();

        randomOpener.click();
        System.out.println("Randomly selected question: " + openerText);

        // 4. Locate the exact panel by its ID
        Locator panel = page.locator("#" + panelId);

        try {
            // Wait for the panel to be visible to ensure animation is complete
            panel.waitFor(new Locator.WaitForOptions()
                    .setState(WaitForSelectorState.VISIBLE)
                    .setTimeout(5000));

            // 5. Print the content
            String contentText = panel.innerText().trim();

            System.out.println(contentText);
            System.out.println("--------------------------------------\n");

        } catch (Exception e) {
            System.out.println("The answer panel did not become visible or could not be found.");
        }
    }

    // --- BUS RENTAL & INJECTION LOGIC ---
    public void fillFullRentalForm(String name, String phone, String email, String from, String to, String count) {
        // 1. Go to the page and wait for the basic scripts to settle
        page.navigate("https://www.metropoline.com/Pages/RentABus.aspx");
        page.waitForSelector("h1, .page-title", new Page.WaitForSelectorOptions().setTimeout(15000));        page.waitForTimeout(5000); // The "Angular" buffer

        // 2. JAVASCRIPT INJECTION
        page.evaluate("([n, p, e, f, t, c]) => {" +
                "  const fields = {" +
                "    'Name': n," +
                "    'Phone': p," +
                "    'Email': e," +
                "    'From': f," +
                "    'To': t," +
                "    'Count': c" +
                "  };" +
                "  " +
                "  // Find the Angular scope and fill the data directly into the model" +
                "  const formElement = document.querySelector('[name=\"contactForm\"]') || document.body;" +
                "  const inputs = document.querySelectorAll('input');" +
                "  " +
                "  inputs.forEach(input => {" +
                "    const model = input.getAttribute('ng-model');" +
                "    if (model) {" +
                "      if (model.includes('Name')) input.value = n;" +
                "      if (model.includes('Phone')) input.value = p;" +
                "      if (model.includes('Email')) input.value = e;" +
                "      // Trigger events so the site 'wakes up' and sees the text" +
                "      input.dispatchEvent(new Event('input', { bubbles: true }));" +
                "      input.dispatchEvent(new Event('change', { bubbles: true }));" +
                "    }" +
                "  });" +
                "  " +
                "  // Finally, find any button that says 'שלח' and click it" +
                "  const btn = Array.from(document.querySelectorAll('button')).find(b => b.textContent.includes('שלח'));" +
                "  if (btn) btn.click();" +
                "}", java.util.Arrays.asList(name, phone, email, from, to, count));

        System.out.println("✅ Memory injection completed.");
    }

    // --- RECRUITMENT & JOBS ---
    public void handleJobsSection() {
        // 1. Navigation: Hover and Click
        Locator jobsMenu = page.locator("span.ng-binding:has-text('משרות פנויות')");

        jobsMenu.hover();
        jobsMenu.click();

        // Wait for the page/content to transition
        page.locator("a.opener.summary").first().waitFor();
        // 2. Locate all question/job openers
        Locator openers = page.locator("a.opener.summary");

        try {
            openers.first().waitFor(new Locator.WaitForOptions().setTimeout(5000));
        } catch (Exception e) {
            System.out.println("No sections found. Check if the 'Jobs' page loaded correctly.");
            return;
        }

        int count = openers.count();
        System.out.println("Found " + count + " jobs/sections.");

        // 3. Pick a random section
        java.util.Random random = new java.util.Random();
        int randomIndex = random.nextInt(count);
        Locator randomOpener = openers.nth(randomIndex);

        // Get the controlled panel ID and the title text
        String panelId = randomOpener.getAttribute("aria-controls");
        String openerText = randomOpener.innerText().trim();

        // Click to expand
        randomOpener.click();
        System.out.println("Randomly selected section: " + openerText);

        // 4. Locate the exact panel by its ID
        if (panelId == null || panelId.isEmpty()) {
            System.out.println("Could not find aria-controls ID for this section.");
            return;
        }

        Locator panel = page.locator("#" + panelId);

        try {
            // 5. Wait for the panel to be visible (handling the animation)
            panel.waitFor(new Locator.WaitForOptions()
                    .setState(WaitForSelectorState.VISIBLE)
                    .setTimeout(5000));

            // 6. Print the content
            String contentText = panel.innerText().trim();

            System.out.println("\n--- Content for: " + openerText + " ---");
            System.out.println(contentText);
            System.out.println("--------------------------------------\n");

        } catch (Exception e) {
            System.out.println("The content panel (" + panelId + ") did not become visible.");
        }
    }

    // --- CAMPAIGNS & REFORMS ---
    public void printLoadThenRideContent() {
        // 1. Locate the menu item by its text for the hover/click action
        Locator loadThenRideMenu = page.locator("span:has-text('קודם מטעינים, אח\"כ עולים')");

        // 2. Perform Hover
        loadThenRideMenu.hover();
        System.out.println("Hovered over 'Load first, then ride' menu item.");

        // 3. Perform Click
        loadThenRideMenu.click();
        System.out.println("Clicked on the menu item.");

        // 4. Wait for the article container to be visible
        Locator contentArticle = page.locator("article.col-left");
        contentArticle.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));

        // 5. Extract and print the content
        System.out.println("--- PRINTING CONTENT FROM ADDITIONAL INFO ---");

        // innerText() is used to strip the HTML tags and print clean text
        String textContent = contentArticle.innerText().trim();

        if (textContent.isEmpty()) {
            System.out.println("Warning: Content container was found but it is empty.");
        } else {
            System.out.println(textContent);

            Locator mainContent = page.locator("article.col-left");
            mainContent.waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE));

            System.out.println("--- FULL REFORM & LINE DATA ---");
            String fullContent = mainContent.innerText().trim();
            System.out.println(fullContent);
        }
    }

    // --- EXTERNAL PAYMENTS ---
    public void verifyExternalMobilePaymentLink() {
        // 1. Hover over the menu item
        Locator mobilePaymentMenu = page.locator("span:has-text('תשלום באמצעות הנייד')");
        mobilePaymentMenu.hover();

        // 2. Click and capture the new tab (popup)
        Page externalPage = page.waitForPopup(() -> {
            mobilePaymentMenu.click();
        });

        // 3. Wait for the specific address to load
        externalPage.waitForURL("https://pti.org.il/chargeApp/");

        // 4. Verification message
        if (externalPage.url().equals("https://pti.org.il/chargeApp/")) {
            System.out.println("The page: " + externalPage.url() + " was opened successfully.");
        }
    }
}
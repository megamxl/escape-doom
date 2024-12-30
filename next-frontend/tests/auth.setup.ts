import {test as setup} from '@playwright/test';
import {LECTOR_PORTAL_APP_PATHS} from "@/app/constants/paths";
import path from 'path'

const authFile = path.join(__dirname, '../playwright/.auth/user.json')

const LOGIN = { name: 'leon@doom.at', pw: 'escapeDoom' }

setup('authenticate', async ({ page }) => {
    // Perform authentication steps. Replace these actions with your own.
    await page.goto(LECTOR_PORTAL_APP_PATHS.LOGIN)

    // Submits form
    await page.getByLabel('Email Address').fill(LOGIN.name);
    await page.getByLabel('Password').fill(LOGIN.pw)
    await page.getByRole("button", {name: "Log In"}).click()

    // Wait until the page receives the cookies.
    //
    // Sometimes login flow sets cookies in the process of several redirects.
    // Wait for the final URL to ensure that the cookies are actually set.
    await page.waitForURL(LECTOR_PORTAL_APP_PATHS.DASHBOARD);

    // End of authentication steps.
    await page.context().storageState({ path: authFile });
});
import {test} from '@playwright/test';
import {LECTOR_PORTAL_APP_PATHS} from "@/app/constants/paths";


test.beforeEach(async ({page}) => {
    await page.goto(LECTOR_PORTAL_APP_PATHS.DASHBOARD)
})

test.describe('Dashboard', () => {


})

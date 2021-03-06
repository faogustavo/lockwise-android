package mozilla.lockbox.uiTests

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import mozilla.lockbox.R
import mozilla.lockbox.robots.itemDetail
import mozilla.lockbox.robots.itemList
import mozilla.lockbox.robots.kebabMenu
import mozilla.lockbox.robots.deleteCredentialDisclaimer
import mozilla.lockbox.robots.editCredential
import mozilla.lockbox.robots.editCredentialDisclaimer
import mozilla.lockbox.view.RootActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
open class ItemDetailsTest {
    private val navigator = Navigator()

    @Rule
    @JvmField
    val activityRule: ActivityTestRule<RootActivity> = ActivityTestRule(RootActivity::class.java)

    @Test
    fun itemDetailsAreDisplayed() {
        navigator.gotoItemList(false)
        itemList { selectItem() }
        itemDetail { exists() }
    }

    @Test
    fun showToastWhenUsernameCopied() {
        navigator.gotoItemDetail()
        itemDetail { tapCopyUsername() }
        itemDetail { toastIsDisplayed(R.string.toast_username_copied, activityRule) }
    }

    @Test
    fun showToastWhenPassCopied() {
        navigator.gotoItemDetail()
        itemDetail { tapCopyPass() }
        itemDetail { toastIsDisplayed(R.string.toast_password_copied, activityRule) }
    }

    @Test
    fun deleteItem() {
        navigator.gotoItemDetailKebabMenu()
        kebabMenu { tapDeleteButton() }
        // First tap on Cancel delete credential
        deleteCredentialDisclaimer { tapCancelButton() }
        itemDetail { exists() }
        // Now delete the credential
        itemDetail { tapKebabMenu() }
        kebabMenu { tapDeleteButton() }
        deleteCredentialDisclaimer { tapDeleteButton() }
        // Check that Item List is shown after removing the credential
        itemList { exists() }
    }

    @Test
    fun editItem() {
        navigator.gotoItemDetailKebabMenu()
        kebabMenu { tapEditButton() }
        editCredential {
            exists()
            tapOnHostname()
            editHostname("HostnameChanged")
            tapOnUserName()
            editUserName("UsernameChanged")
            saveChanges() }
        itemList {
            exists()
            editedCredentialHostnameExists("HostnameChanged")
            editedCredentialUsernameExists("UsernameChanged")
            openCredential("HostnameChanged")
        }

        // Remove this entry
        navigator.gotoItemDetailKebabMenu()
        kebabMenu { tapEditButton() }
        editCredential { deleteEntryFromEdit() }
        // There will be a disclaimer menu here see issue #904
        itemList {
            exists()
            credentialRemovedDoesNotExist("HostnameChanged")
        }
    }

    @Test
    fun cancelEditCredential() {
        // Tap on Cancel edit credential
        navigator.gotoItemDetailKebabMenu()
        kebabMenu { tapEditButton() }
        editCredential {
            closeEditChanges() }
        editCredentialDisclaimer { tapCancelButton() }
        // User is taken to ItemDetail View
        // Now Tap on Discard edit credential
        itemDetail { exists() }

        editCredential { closeEditChanges() }
        editCredentialDisclaimer { tapDiscardButton() }
        // User is taken to ItemList View
        // No changes are applied
        itemList { exists() }
    }
}
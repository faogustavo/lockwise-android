/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package mozilla.lockbox.robots

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.matcher.ViewMatchers.withId
import br.com.concretesolutions.kappuccino.actions.ClickActions
import br.com.concretesolutions.kappuccino.assertions.VisibilityAssertions
import mozilla.lockbox.R

class EditCredentialRobot : BaseTestRobot {

    override fun exists() = VisibilityAssertions.displayed { id(R.id.editLoginTitle) }

    fun saveChanges() = ClickActions.click { id(R.id.saveEntryButton) }
    fun closeEditChanges() = ClickActions.click { contentDescription("Back") }
    fun deleteEntryFromEdit() = ClickActions.click { id(R.id.deleteEntryButton) }

    fun tapOnHostname() = onView(withId(R.id.inputHostname)).perform(click())
    fun editHostname(text: String) = onView(withId(R.id.inputHostname)).perform(replaceText(text))
    fun tapOnUserName() = onView(withId(R.id.inputUsername)).perform(click())
    fun editUserName(text: String) = onView(withId(R.id.inputUsername)).perform(replaceText(text))

    fun editPassword(text: String) = onView(withId(R.id.inputPassword)).perform(replaceText(text))
}

fun editCredential(f: EditCredentialRobot.() -> Unit) = EditCredentialRobot().apply(f)

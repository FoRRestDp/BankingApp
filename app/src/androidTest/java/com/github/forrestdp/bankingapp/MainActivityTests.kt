package com.github.forrestdp.bankingapp

import androidx.core.view.isGone
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.forrestdp.bankingapp.activity.MainActivity
import com.github.forrestdp.bankingapp.repo.model.bankinginfo.BankingInfo
import com.github.forrestdp.bankingapp.repo.network.bankinginfo.BankingInfoApiService
import com.github.forrestdp.bankingapp.utils.BaseRobot
import kotlinx.coroutines.runBlocking

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule

/**
 * Instrumented test, which will execute on an Android device.
 */
@RunWith(AndroidJUnit4::class)
class MainActivityTests {
    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)
    private lateinit var retrofit: BankingInfoApiService
    private lateinit var bankingInfo: BankingInfo

    @Before
    fun setup() = runBlocking {
//        retrofit = CardHoldersApi.retrofitService
//        bankingInfo = retrofit.getBankingInfo()
    }

    @Test
    fun verifyCardViewIsGoneWhileLoading() {
        BaseRobot().assertOnView(withId(R.id.cardView), ViewAssertion { view, _ -> view.isGone })
    }

    @Test
    fun verifyThatListOfCardsOpens() {
//        BaseRobot().waitForView(withId(R.id.cardView))
        BaseRobot().doOnView(withId(R.id.cardView), click())
        val firstCardNumber = bankingInfo.users[0].cardNumber
        onView(withText(firstCardNumber)).check(matches(isDisplayed()))
    }
}
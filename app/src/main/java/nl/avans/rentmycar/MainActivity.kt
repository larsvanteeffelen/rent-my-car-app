package nl.avans.rentmycar

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.auth0.android.Auth0
import com.auth0.android.authentication.AuthenticationException
import com.auth0.android.provider.WebAuthProvider
import com.auth0.android.result.Credentials
import com.auth0.android.result.UserProfile
import nl.avans.rentmycar.components.navigation.AppNavigation
import androidx.core.view.isVisible
import com.auth0.android.authentication.AuthenticationAPIClient
import com.auth0.android.callback.Callback
import com.auth0.android.management.ManagementException
import com.auth0.android.management.UsersAPIClient
import com.google.android.material.snackbar.Snackbar


class MainActivity : ComponentActivity() {

    // Login/logout-related properties
    private lateinit var account: Auth0
    private var cachedCredentials: Credentials? = null
    private var cachedUserProfile: UserProfile? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        account = Auth0(
            getString(R.string.com_auth0_client_id),
            getString(R.string.com_auth0_domain)
        )

        setContent {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (cachedCredentials == null) {
                    LoginScreen()
                } else {
                    AppNavigation()
                }
            }
        }
    }

    //    Login Screen with buttons
    @Preview
    @Composable
    fun LoginScreen() {
        Column(
            modifier = with(Modifier) {
                fillMaxSize()
                    .paint(
                        painterResource(id = R.drawable.background_road),
                        contentScale = ContentScale.FillBounds,
                    )},
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally
                ) {

                Text(
                    text = "RENT MY CAR",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                    color = androidx.compose.ui.graphics.Color.White,
                )
                Text(
                    text = "Let's share a ride",
                    style = MaterialTheme.typography.headlineSmall,
                    color = androidx.compose.ui.graphics.Color.White,
                    modifier = Modifier.padding(bottom = 32.dp)
                )

                Button(onClick = { login() }
                , modifier = with(Modifier) {
                    width(200.dp)
                    padding(bottom = 16.dp)
                    }) {
                    Text(text = "Login")
                }

                if (cachedUserProfile != null) {
                    Text(text = "User: ${cachedUserProfile?.name}")
                }
            }
            }

                    private fun showUserProfile() {
                // Guard against showing the profile when no user is logged in
                if (cachedCredentials == null) {
                    return
                }

                val client = AuthenticationAPIClient(account)
                client
                    .userInfo(cachedCredentials!!.accessToken!!)
                    .start(object : Callback<UserProfile, AuthenticationException> {

                        override fun onFailure(exception: AuthenticationException) {
                            showSnackBar(
                                getString(
                                    R.string.general_failure_with_exception_code,
                                    exception.getCode()
                                )
                            )
                        }

                        override fun onSuccess(profile: UserProfile) {
                            cachedUserProfile = profile
                            val userid = profile.getExtraInfo()["sub"].toString()
                            saveUserId(userid)
                            Log.d("UserID", "UserID: ${userid}")

                        }

                    })
            }

                    private fun login() {
                WebAuthProvider
                    .login(account)
                    .withScheme(getString(R.string.com_auth0_scheme))
                    .withScope(getString(R.string.login_scopes))
                    .withAudience(
                        getString(
                            R.string.login_audience,
                            getString(R.string.com_auth0_domain)
                        )
                    )
                    .start(this, object : Callback<Credentials, AuthenticationException> {

                        override fun onFailure(exception: AuthenticationException) {
                            showSnackBar(
                                getString(
                                    R.string.login_failure_message,
                                    exception.getCode()
                                )
                            )
                        }

                        override fun onSuccess(credentials: Credentials) {
                            cachedCredentials = credentials
                            showUserProfile()
                            Log.d(
                                "UserID",
                                "UserID: 2 2 2 ${
                                    getPreferences(MODE_PRIVATE).getString(
                                        "user_id",
                                        null
                                    )
                                }"
                            )
                            setContent {
                                AppNavigation()
                            }
                        }
                    })
            }

                    private fun showSnackBar(text: String) {
                Snackbar.make(findViewById(android.R.id.content), text, Snackbar.LENGTH_SHORT)
                    .show()
            }

                    private fun saveUserId(userId: String) {
                val sharedPreferences = getPreferences(MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putString("user_id", userId)
                editor.apply()
            }


    }

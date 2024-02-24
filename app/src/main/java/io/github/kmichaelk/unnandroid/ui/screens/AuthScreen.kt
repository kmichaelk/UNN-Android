package io.github.kmichaelk.unnandroid.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.AutofillType
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import io.github.kmichaelk.unnandroid.R
import io.github.kmichaelk.unnandroid.ui.composables.OutlinedPasswordField
import io.github.kmichaelk.unnandroid.ui.extensions.autofill
import io.github.kmichaelk.unnandroid.ui.viewmodels.AuthScreenViewModel

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AuthScreen(
    viewModel: AuthScreenViewModel = hiltViewModel(),
    onFinish: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    if (state.finished) {
        LaunchedEffect(Unit) {
            onFinish()
        }
    }

    Scaffold { paddingValues ->
        Box(
            Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .fillMaxHeight()
                    .imePadding()
                    .verticalScroll(state = rememberScrollState())
                    .widthIn(max = 380.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.unn_logo_text),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp)
                )

                Spacer(Modifier.height(16.dp))
                OutlinedTextField(
                    value = state.username,
                    onValueChange = viewModel::onUsernameChange,
                    label = { Text("Логин") },
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    modifier = Modifier
                        .autofill(
                            autofillTypes = listOf(
                                AutofillType.Username,
                                AutofillType.EmailAddress
                            ),
                            onFill = viewModel::onPasswordChange,
                        )
                        .fillMaxWidth(),
                    enabled = !state.isLoading,
                    isError = state.error != null
                )
                OutlinedPasswordField(
                    value = state.password,
                    onValueChange = viewModel::onPasswordChange,
                    label = { Text("Пароль") },
                    leadingIcon = { Icon(Icons.Default.Password, contentDescription = null) },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Go),
                    keyboardActions = KeyboardActions(
                        onGo = { viewModel.onSubmit() }
                    ),
                    modifier = Modifier
                        .autofill(
                            autofillTypes = listOf(AutofillType.Password),
                            onFill = viewModel::onPasswordChange,
                        )
                        .fillMaxWidth(),
                    enabled = !state.isLoading,
                    isError = state.error != null
                )
                Spacer(Modifier.height(4.dp))
                if (state.isLoading) {
                    LinearProgressIndicator(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        strokeCap = StrokeCap.Round
                    )
                } else if (state.error != null) {
                    Text(
                        state.error!!,
                        color = Color.Red,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = viewModel::onSubmit,
                    enabled = !state.isLoading
                ) {
                    Text("Войти")
                }
                Box {
                    val uriHandler = LocalUriHandler.current
                    TextButton(
                        enabled = !state.isLoading,
                        onClick = { uriHandler.openUri("https://login.unn.ru/") },
                    ) {
                        Text("Забыли пароль?")
                    }
                }
            }
            IconButton(
                onClick = onFinish,
                modifier = Modifier
                    .padding(top = 16.dp, end = 16.dp)
                    .align(Alignment.TopEnd)
            ) {
                Icon(Icons.Default.Close, contentDescription = "Закрыть")
            }
        }
    }
}
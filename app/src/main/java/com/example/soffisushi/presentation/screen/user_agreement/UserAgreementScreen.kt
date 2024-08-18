package com.example.soffisushi.presentation.screen.user_agreement

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.soffisushi.R
import com.example.soffisushi.data.remote.firebase.model.UserAgreement
import com.example.soffisushi.presentation.component.CircularProgressBox
import com.example.soffisushi.presentation.component.button.WrapContentButton
import com.example.soffisushi.util.Status
import com.google.firebase.firestore.FirebaseFirestore
import org.koin.compose.koinInject

@Composable
fun UserAgreementScreen(
    modifier: Modifier
) {
    val resultUserAgreement = remember { mutableStateOf<Status<UserAgreement>>(Status.Pending) }
    val firestore: FirebaseFirestore = koinInject()

    val context = LocalContext.current

    when (resultUserAgreement.value) {
        is Status.Pending -> {
            resultUserAgreement.value = Status.Loading
            firestore
                .collection("general")
                .document("UserAgreement")
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val tempPrivacyPolice = task.result.toObject(UserAgreement::class.java)
                        if (tempPrivacyPolice != null) {
                            resultUserAgreement.value = Status.Success(tempPrivacyPolice)
                        } else {
                            resultUserAgreement.value =
                                Status.Error(Exception(context.getString(R.string.empty_data_returned)))
                        }
                    } else {
                        resultUserAgreement.value =
                            Status.Error(Exception(context.getString(R.string.data_acquisition_error)))
                    }
                }
        }

        is Status.Loading -> {
            CircularProgressBox(modifier = modifier)
        }

        is Status.Error -> {
            val exception = (resultUserAgreement.value as Status.Error).exception
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(48.dp))
                Text(
                    text = exception.message.toString(),
                    fontSize = MaterialTheme.typography.titleLarge.fontSize,
                    fontWeight = MaterialTheme.typography.titleLarge.fontWeight,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(48.dp))
                WrapContentButton(
                    text = stringResource(R.string.try_again),
                    onClick = {
                        resultUserAgreement.value = Status.Pending
                    }
                )
            }
        }

        is Status.Success -> {
            val privacyPolicy = (resultUserAgreement.value as Status.Success).data
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(text = privacyPolicy.text)
            }
        }
    }
}
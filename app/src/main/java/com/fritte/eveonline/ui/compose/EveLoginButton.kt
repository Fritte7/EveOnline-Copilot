package com.fritte.eveonline.ui.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fritte.eveonline.R

@Composable
fun EveLoginButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Image(
        painter = painterResource(R.drawable.eve_sso_login_black_large),
        contentDescription = "Log in with EVE Online",
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .clickable { onClick() }
    )
}

@Preview(showBackground = true)
@Composable
fun EveLoginButtonPreview() {
    EveLoginButton(onClick = {})
}